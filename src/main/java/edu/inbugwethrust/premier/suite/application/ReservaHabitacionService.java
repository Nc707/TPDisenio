package edu.inbugwethrust.premier.suite.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        // 1) Regla de negocio: NO permitir solapamientos internos para la misma habitación
        gestorReservas.validarSolapamientosInternos(habitacionesSeleccionadas);

        // 2) Armar el Set de números de habitación para construir el mapa (evitar idas repetidas a BD)
        Set<Integer> numerosHabitaciones = new HashSet<>();
        for (SeleccionHabitacionDTO seleccion : habitacionesSeleccionadas) {
            numerosHabitaciones.add(seleccion.getNumeroHabitacion());
        }

        // 3) Obtener el mapa de habitaciones reales desde el GestorHabitaciones
        Map<Integer, Habitacion> mapaHabitaciones =
                gestorHabitaciones.obtenerMapaPorNumeros(numerosHabitaciones);

        DatosHuespedReservaDTO datosHuesped = confirmacionReservaDTO.getDatosHuesped();

        // 4) Crear la Reserva con los datos del huésped
        Reserva reserva = gestorReservas.crearNuevaReserva(
                datosHuesped.getApellido(),
                datosHuesped.getNombre(),
                datosHuesped.getTelefono()
        );

        // 5) Recorrer cada selección de habitación y crear la FichaEvento correspondiente
        for (SeleccionHabitacionDTO seleccionHab : habitacionesSeleccionadas) {

            Integer numeroHabitacion = seleccionHab.getNumeroHabitacion();
            LocalDate fechaIngreso   = seleccionHab.getFechaIngreso();
            LocalDate fechaEgreso    = seleccionHab.getFechaEgreso();

            // Regla de negocio: coherencia de fechas (esto lo dejamos en el Service)
            if (!fechaEgreso.isAfter(fechaIngreso)) {
                throw new IllegalArgumentException(
                        "La fecha de egreso debe ser posterior a la fecha de ingreso.");
            }

            Habitacion habitacion = mapaHabitaciones.get(numeroHabitacion);
            if (habitacion == null) {
                // Defensa extra, no debería pasar si el mapa está bien armado
                throw new IllegalArgumentException(
                        "No existe la habitación con número: " + numeroHabitacion);
            }

            LocalDateTime inicioReserva = fechaIngreso.atTime(12, 0);
            LocalDateTime finReserva    = fechaEgreso.atTime(10, 0);

            // Validación contra la disponibilidad real (BD) usando FichaEvento
            gestorFichaEvento.validarDisponibilidad(habitacion, inicioReserva, finReserva);

            String descripcion = generarDescripcionReserva(
                    confirmacionReservaDTO, habitacion, fechaIngreso, fechaEgreso);

            FichaEvento ficha = gestorFichaEvento.crearFichaParaReserva(
                    reserva, habitacion, inicioReserva, finReserva, descripcion);

            reserva.getListaFichaEventos().add(ficha);
        }

        // 6) Persistir la Reserva (con sus fichas) usando el GestorReservas
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
