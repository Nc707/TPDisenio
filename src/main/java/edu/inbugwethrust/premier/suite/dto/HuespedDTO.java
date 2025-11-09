package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import edu.inbugwethrust.premier.suite.model.TipoDni;
import edu.inbugwethrust.premier.suite.model.CategoriaFiscal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedDTO {

    private String apellido;
    private String nombres;
    private TipoDni tipoDocumento;
    private String numeroDocumento;
    private String cuit;                  // no obligatorio
    private CategoriaFiscal categoriaFiscal;
    private LocalDate fechaNacimiento;
    private DireccionDTO direccion;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
}
