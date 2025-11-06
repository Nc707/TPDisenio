package edu.inbugwethrust.premier.suite.services.exceptions;

import java.util.List;

public class DatosObligatoriosException extends RuntimeException {

    private final List<String> errores;

    public DatosObligatoriosException(List<String> errores) {
        super("Faltan datos obligatorios");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
