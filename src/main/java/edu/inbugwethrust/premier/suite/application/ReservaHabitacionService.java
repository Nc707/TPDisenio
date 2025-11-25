package edu.inbugwethrust.premier.suite.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;
import edu.inbugwethrust.premier.suite.dto.DatosHuespedReservaDTO;
import edu.inbugwethrust.premier.suite.dto.SeleccionHabitacionDTO;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.application.IReservaHabitacionService;
import edu.inbugwethrust.premier.suite.services.GestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.GestorHabitaciones;
import edu.inbugwethrust.premier.suite.services.GestorReservas;

@Service
public class ReservaHabitacionService implements IReservaHabitacionService {

    private final GestorHabitaciones gestorHabitaciones;
    private final GestorFichaEvento gestorFichaEvento;
    private final GestorReservas gestorReservas;

    @Autowired
    public ReservaHabitacionService(GestorHabitaciones gestorHabitaciones,
                                    GestorFichaEvento gestorFichaEvento,
                                    GestorReservas gestorReservas) {
        this.gestorHabitaciones = gestorHabitaciones;
        this.gestorFichaEvento = gestorFichaEvento;
        this.gestorReservas = gestorReservas;
    }

@Override
@Transactional
public void registrarReserva(ConfirmacionReservaDTO confirmacionReservaDTO) {

    // Defensa mínima: el DTO no puede ser nulo
    Objects.requireNonNull(confirmacionReservaDTO,
            "La confirmación de reserva no puede ser nula");

    // Ya viene validado por Bean Validation en el controller
    var habitacionesSeleccionadas = confirmacionReservaDTO.getHabitacionesSeleccionadas();
// --- NUEVA VALIDACIÓN INTELIGENTE: Chequear solapamiento de fechas para la misma habitación ---
    for (int i = 0; i < habitacionesSeleccionadas.size(); i++) {
        for (int j = i + 1; j < habitacionesSeleccionadas.size(); j++) {
            
            var sel1 = habitacionesSeleccionadas.get(i);
            var sel2 = habitacionesSeleccionadas.get(j);

            // Si es la misma habitación, verificamos si las fechas chocan
            if (sel1.getNumeroHabitacion().equals(sel2.getNumeroHabitacion())) {
                
                // Lógica de intersección de rangos: (InicioA < FinB) y (FinA > InicioB)
                boolean seSolapan = sel1.getFechaIngreso().isBefore(sel2.getFechaEgreso()) && 
                                    sel1.getFechaEgreso().isAfter(sel2.getFechaIngreso());

                if (seSolapan) {
                    throw new IllegalArgumentException(
                        "Error: Se intenta reservar la habitación " + sel1.getNumeroHabitacion() + 
                        " dos veces en fechas superpuestas dentro de la misma solicitud.");
                }
            }
        }
    }
    var datosHuesped = confirmacionReservaDTO.getDatosHuesped();

    // 1) Crear la Reserva con los datos del huésped
    Reserva reserva = gestorReservas.crearNuevaReserva(
            datosHuesped.getApellido(),
            datosHuesped.getNombre(),
            datosHuesped.getTelefono()
    );

    // 2) Recorrer cada selección de habitación
    for (SeleccionHabitacionDTO seleccionHab : habitacionesSeleccionadas) {

        Integer numeroHabitacion = seleccionHab.getNumeroHabitacion();
        LocalDate fechaIngreso   = seleccionHab.getFechaIngreso();
        LocalDate fechaEgreso    = seleccionHab.getFechaEgreso();

        // Regla de negocio: coherencia de fechas (esto sí queda en el servicio)
        if (fechaEgreso.isBefore(fechaIngreso)) {
            throw new IllegalArgumentException(
                    "La fecha de egreso no puede ser anterior a la fecha de ingreso.");
        }

        Habitacion habitacion = gestorHabitaciones.obtenerPorNumero(numeroHabitacion);

        LocalDateTime inicioReserva = fechaIngreso.atTime(12, 0);
        LocalDateTime finReserva    = fechaEgreso.atTime(10, 0);

        gestorFichaEvento.validarDisponibilidad(habitacion, inicioReserva, finReserva);

        String descripcion = generarDescripcionReserva(
                confirmacionReservaDTO, habitacion, fechaIngreso, fechaEgreso);

        FichaEvento ficha = gestorFichaEvento.crearFichaParaReserva(
                reserva, habitacion, inicioReserva, finReserva, descripcion);

        reserva.getListaFichaEventos().add(ficha);
    }

    gestorReservas.guardar(reserva);
}


    /**
     * Genera una descripción legible para la FichaEvento usando datos del huésped,
     * la habitación y el rango de fechas.
     */
    private String generarDescripcionReserva(ConfirmacionReservaDTO dto,
                                             Habitacion habitacion,
                                             LocalDate fechaIngreso,
                                             LocalDate fechaEgreso) {

        StringBuilder sb = new StringBuilder("Reserva de habitación ");
        sb.append(habitacion.getNumero())
          .append(" del ")
          .append(fechaIngreso)
          .append(" al ")
          .append(fechaEgreso);

        if (dto.getDatosHuesped() != null) {
            sb.append(" a nombre de ");
            sb.append(dto.getDatosHuesped().getApellido())
              .append(", ")
              .append(dto.getDatosHuesped().getNombre());

            if (dto.getDatosHuesped().getTelefono() != null) {
                sb.append(" (Tel: ")
                  .append(dto.getDatosHuesped().getTelefono())
                  .append(")");
            }
        }

        return sb.toString();
    }
}
