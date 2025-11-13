package edu.inbugwethrust.premier.suite.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(HuespedID.class)
public class Huesped {

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String nombres;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDni tipoDocumento;

    @Id
    @Column(length = 8, nullable = false)
    private String numeroDocumento;

    @Column(length = 11)
    private String cuit; // no obligatorio

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaFiscal categoriaFiscal;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Embedded
    private Direccion direccion;

    @Column(nullable = false)
    private String telefono;

    private String email;

    @Column(nullable = false)
    private String ocupacion;

    @Column(nullable = false)
    private String nacionalidad;
}