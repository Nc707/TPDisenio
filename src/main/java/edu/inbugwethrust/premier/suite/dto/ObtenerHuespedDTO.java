package edu.inbugwethrust.premier.suite.dto;

import edu.inbugwethrust.premier.suite.model.TipoDni;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObtenerHuespedDTO {
  @NotNull(message = "El tipo de documento es obligatorio")
  private TipoDni tipoDocumento;
  
  @NotBlank(message = "El número de documento es obligatorio")
  @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe consistir en 8 dígitos numéricos.")
  private String numeroDocumento;
}
