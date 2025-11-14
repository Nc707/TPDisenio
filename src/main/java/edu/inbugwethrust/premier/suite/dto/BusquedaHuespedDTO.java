package edu.inbugwethrust.premier.suite.dto;

import edu.inbugwethrust.premier.suite.model.TipoDni;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusquedaHuespedDTO {
    private String apellido;
    private String nombres;
    private TipoDni tipoDocumento;
    private String numeroDocumento;
}
