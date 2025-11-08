package edu.inbugwethrust.premier.suite.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Huesped {
    private String apellido;
    private String nombres;
    private TipoDni tipoDocumento;
    private String numeroDocumento;
    private String cuit;                  // no obligatorio
    private CategoriaFiscal categoriaFiscal;
    private LocalDate fechaNacimiento;
    private Direccion direccion;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
}
