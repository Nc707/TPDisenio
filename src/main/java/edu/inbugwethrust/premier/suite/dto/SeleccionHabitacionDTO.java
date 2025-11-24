package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa la selección de una habitación en la grilla de disponibilidad,
 * con su fecha de ingreso y egreso.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeleccionHabitacionDTO {

    @NotNull(message = "El número de habitación es obligatorio")
    private Integer numeroHabitacion;

    /**
     * Día de ingreso de la reserva (incluido).
     */
    @NotNull(message = "La fecha de ingreso es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    /**
     * Día de egreso de la reserva (incluido como día de salida).
     * La lógica del servicio se encargará de usar 10:00 hs para este día.
     */
    @NotNull(message = "La fecha de egreso es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEgreso;
}
