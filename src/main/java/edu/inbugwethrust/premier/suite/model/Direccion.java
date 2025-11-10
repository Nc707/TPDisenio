package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Direccion {
	@Column(nullable = false)
    private String calle;
    @Column(nullable = false)
    private String numero;
    @Column(nullable = false)
    private String piso;
    @Column(nullable = false)
    private String departamento;
    @Column(nullable = false)
    private String codigoPostal;
    @Column(nullable = false)
    private String localidad;
    @Column(nullable = false)
    private String provincia;
    @Column(nullable = false)
    private String pais;
}
