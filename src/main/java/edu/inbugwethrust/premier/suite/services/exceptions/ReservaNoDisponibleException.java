package edu.inbugwethrust.premier.suite.services.exceptions;

/**
 * Excepción de negocio para indicar que alguna habitación no está disponible
 * en el rango de fechas solicitado al intentar registrar una reserva (CU04).
 *
 * La idea es que luego GlobalExceptionHandler la capture y devuelva
 * un mensaje claro al usuario.
 */
public class ReservaNoDisponibleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReservaNoDisponibleException(String message) {
        super(message);
    }

    public ReservaNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}