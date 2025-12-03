package edu.inbugwethrust.premier.suite.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarOcupacionesRequestDTO {

    @NotEmpty
    @Valid
    private List<SeleccionHabitacionOcupacionDTO> ocupaciones;
}
