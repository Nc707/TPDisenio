package edu.inbugwethrust.premier.suite.services.exceptions;

import java.util.List;

public class DatosObligatoriosException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
    private final List<String> errores;

    public DatosObligatoriosException(List<String> errores) {
        super("Faltan datos obligatorios");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
