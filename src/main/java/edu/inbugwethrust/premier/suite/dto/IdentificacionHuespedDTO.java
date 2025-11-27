package edu.inbugwethrust.premier.suite.dto;

import edu.inbugwethrust.premier.suite.model.TipoDni;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class IdentificacionHuespedDTO {

    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDni tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;
}