package edu.inbugwethrust.premier.suite.services.exceptions;

/**
 * Excepción de dominio que indica que, dentro de una misma solicitud de reserva,
 * hay rangos de fechas que se solapan para la misma habitación.
 */
public class HabitacionesSolapadasEnReservaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HabitacionesSolapadasEnReservaException(String message) {
        super(message);
    }
}
