package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoValidacionHabitacionDTO {

    private Integer numeroHabitacion;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;

    private boolean seleccionValida;   // true si pasa todas las reglas de disponibilidad

    // Info de reserva previa (si la hay)
    private boolean hayReserva;
    private Long idReserva;
    private String apellidoReserva;
    private String nombreReserva;
    private String telefonoReserva;
}
