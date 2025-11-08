package edu.inbugwethrust.premier.suite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {
    private String calle;
    private String numero;
    private String piso;
    private String departamento;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private String pais;
}
