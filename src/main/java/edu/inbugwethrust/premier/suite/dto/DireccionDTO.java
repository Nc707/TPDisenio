package edu.inbugwethrust.premier.suite.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {

    @NotBlank(message = "La calle es obligatoria")
    private String calle;
    
    @NotBlank(message = "El número es obligatorio")
    private String numero;
    private String piso;
    private String departamento;
    
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    
    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;
    
    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;
    
    private String pais;
}
