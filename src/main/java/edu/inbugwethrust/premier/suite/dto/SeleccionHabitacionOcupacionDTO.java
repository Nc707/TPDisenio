package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeleccionHabitacionOcupacionDTO {

    @NotNull
    @Min(1)
    private Integer numeroHabitacion;

    @NotNull
    private LocalDate fechaIngreso;

    @NotNull
    private LocalDate fechaEgreso;
}
