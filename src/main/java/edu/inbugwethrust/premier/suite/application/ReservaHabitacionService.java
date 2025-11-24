package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;
import edu.inbugwethrust.premier.suite.dto.SeleccionHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.SeleccionReservaDTO;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.EstadoReserva;
import edu.inbugwethrust.premier.suite.repositories.FichaEventoDAO;
import edu.inbugwethrust.premier.suite.repositories.HabitacionDAO;
import edu.inbugwethrust.premier.suite.repositories.ReservaDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.ReservaNoDisponibleException;

@Service
public class ReservaHabitacionService implements IReservaHabitacionService {

    private final ReservaDAO reservaDAO;
    private final HabitacionDAO habitacionDAO;
    private final FichaEventoDAO fichaEventoDAO;

    @Autowired
    public ReservaHabitacionService(ReservaDAO reservaDAO,
                                    HabitacionDAO habitacionDAO,
                                    FichaEventoDAO fichaEventoDAO) {
        this.reservaDAO = reservaDAO;
        this.habitacionDAO = habitacionDAO;
        this.fichaEventoDAO = fichaEventoDAO;
    }

    /**
     * Implementación del paso 10 del CU04 "Reservar Habitación".
     *
     * Responsabilidades:
     *  - Verificar que para cada habitación seleccionada NO existan FichaEvento
     *    solapadas con el rango de fechas de la reserva.
     *  - Si todo está disponible, crear la Reserva y las FichaEvento
     *    correspondientes.
     *  - Guardar todo en una única transacción.
     *
     * Si hay conflicto de disponibilidad, lanza ReservaNoDisponibleException.
     */
    @Override
    @Transactional
    public void registrarReserva(ConfirmacionReservaDTO confirmacionReservaDTO) {

        Objects.requireNonNull(confirmacionReservaDTO,
                "La confirmación de reserva no puede ser nula");

        SeleccionReservaDTO seleccion = confirmacionReservaDTO.getSeleccion();

        if (seleccion == null
                || seleccion.getHabitacionesSeleccionadas() == null
                || seleccion.getHabitacionesSeleccionadas().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos una habitación para registrar la reserva.");
        }

        // 1) VALIDACIÓN DE DISPONIBILIDAD (todas las habitaciones primero)
        for (SeleccionHabitacionDTO seleccionHabitacion
                : seleccion.getHabitacionesSeleccionadas()) {

            Integer numeroHabitacion = seleccionHabitacion.getNumeroHabitacion();
            LocalDate fechaIngreso   = seleccionHabitacion.getFechaIngreso();
            LocalDate fechaEgreso    = seleccionHabitacion.getFechaEgreso();

            if (numeroHabitacion == null || fechaIngreso == null || fechaEgreso == null) {
                // En teoría esto ya está cubierto por Bean Validation,
                // pero lo dejamos como defensa extra.
                throw new IllegalArgumentException(
                        "La selección de habitación contiene datos incompletos.");
            }

            if (fechaEgreso.isBefore(fechaIngreso)) {
                throw new IllegalArgumentException(
                        "La fecha de egreso no puede ser anterior a la fecha de ingreso.");
            }

            Habitacion habitacion = habitacionDAO.findById(numeroHabitacion)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe la habitación con número: " + numeroHabitacion));

            // Rango temporal de la reserva:
            // Ingreso: 12:00 hs del día de ingreso
            // Egreso : 10:00 hs del día de egreso (según CU04)
            LocalDateTime inicioReserva = fechaIngreso.atTime(12, 0);
            LocalDateTime finReserva    = fechaEgreso.atTime(10, 0);

            // La query de FichaEventoDAO:
            //   - ya filtra por cancelado = false
            //   - y por solapamiento de fechas (fechaInicio < fin && fechaFin > inicio)
            List<FichaEvento> eventosSolapados =
                    fichaEventoDAO.findByFechaEvento(inicioReserva, finReserva);

            boolean existeConflicto = eventosSolapados.stream()
                    .anyMatch(fe ->
                            fe.getHabitacion() != null
                                    && fe.getHabitacion().getNumero() == habitacion.getNumero()
                    );

            if (existeConflicto) {
                String mensaje = String.format(
                        "La habitación %d no está disponible entre %s 12:00 y %s 10:00.",
                        numeroHabitacion, fechaIngreso, fechaEgreso);

                // Excepción de negocio que luego manejará GlobalExceptionHandler
                throw new ReservaNoDisponibleException(mensaje);
            }
        }

        // 2) CREACIÓN DE LA RESERVA (ya sabemos que todas las habs están libres)
        Reserva reserva = new Reserva();

        reserva.setEstadoReserva(EstadoReserva.ACTIVA);

        // mapear datos del huésped en la entidad Reserva
        // FALTA AREGAR ESTO EN RESERVA Y AQUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // DatosHuespedReservaDTO datosH = confirmacionReservaDTO.getDatosHuesped();
        // reserva.setApellido(datosH.getApellido());
        // ...

        // 3) CREACIÓN DE LAS FICHAS DE EVENTO por cada habitación seleccionada
        for (SeleccionHabitacionDTO seleccionHabitacion
                : seleccion.getHabitacionesSeleccionadas()) {

            Habitacion habitacion = habitacionDAO.findById(
                    seleccionHabitacion.getNumeroHabitacion()
            ).orElseThrow(() -> new IllegalArgumentException(
                    "No existe la habitación con número: "
                            + seleccionHabitacion.getNumeroHabitacion()));

            LocalDateTime inicioReserva = seleccionHabitacion.getFechaIngreso()
                    .atTime(12, 0);
            LocalDateTime finReserva    = seleccionHabitacion.getFechaEgreso()
                    .atTime(10, 0);

            FichaEvento fichaEvento = new FichaEvento();
            fichaEvento.setHabitacion(habitacion);
            fichaEvento.setReserva(reserva);
            fichaEvento.setCancelado(false);
            fichaEvento.setDescripcion(generarDescripcionReserva(confirmacionReservaDTO, habitacion));
            fichaEvento.setFechaInicio(inicioReserva);
            fichaEvento.setFechaFin(finReserva);
            fichaEvento.setEstado(EstadoHabitacion.RESERVADA);
            

            reserva.getListaFichaEventos().add(fichaEvento);
        }

        // 4) Persistimos la Reserva.
        //    Por cascade = CascadeType.ALL en listaFichaEventos,
        //    también se guardan las FichaEvento.
        reservaDAO.save(reserva);
    }

    /**
     * Genera una descripción legible para la FichaEvento
     * usando datos del huésped y la habitación.
     */
    private String generarDescripcionReserva(ConfirmacionReservaDTO confirmacionReservaDTO,
                                             Habitacion habitacion) {

        StringBuilder sb = new StringBuilder("Reserva de habitación ");
        sb.append(habitacion.getNumero());

        if (confirmacionReservaDTO.getDatosHuesped() != null) {
            sb.append(" a nombre de ");
            sb.append(confirmacionReservaDTO.getDatosHuesped().getApellido())
              .append(", ")
              .append(confirmacionReservaDTO.getDatosHuesped().getNombre());

            if (confirmacionReservaDTO.getDatosHuesped().getTelefono() != null) {
                sb.append(" (Tel: ")
                  .append(confirmacionReservaDTO.getDatosHuesped().getTelefono())
                  .append(")");
            }
        }

        return sb.toString();
    }
}
