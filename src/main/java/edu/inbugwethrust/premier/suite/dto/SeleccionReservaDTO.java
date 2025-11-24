package edu.inbugwethrust.premier.suite.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Agrupa todas las habitaciones seleccionadas para una reserva.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeleccionReservaDTO {

    @NotEmpty(message = "Debe seleccionar al menos una habitación")
    @Valid
    private List<SeleccionHabitacionDTO> habitacionesSeleccionadas = new ArrayList<>();
}
