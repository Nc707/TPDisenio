package edu.inbugwethrust.premier.suite.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Request principal del CU15.
 * Agrupa todas las ocupaciones de habitaciones que se quieren registrar
 * en una misma operación de check-in.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrarOcupacionesRequestDTO {

    /**
     * Lista de habitaciones a ocupar en esta operación.
     */
    @NotEmpty(message = "Debe especificarse al menos una ocupación de habitación")
    @Valid
    private List<OcupacionHabitacionDTO> ocupaciones;
}
