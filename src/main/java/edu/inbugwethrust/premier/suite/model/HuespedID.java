package edu.inbugwethrust.premier.suite.model;

import java.io.Serializable;
import java.util.Objects;

public class HuespedID implements Serializable {

	private static final long serialVersionUID = 1L;
	private TipoDni tipoDocumento;
    private String numeroDocumento;


    public HuespedID() {}

    public HuespedID(TipoDni tipoDocumento, String numeroDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuespedID that = (HuespedID) o;
        return tipoDocumento == that.tipoDocumento &&
               Objects.equals(numeroDocumento, that.numeroDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoDocumento, numeroDocumento);
    }
}