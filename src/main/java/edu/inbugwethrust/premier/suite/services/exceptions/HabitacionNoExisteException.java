package edu.inbugwethrust.premier.suite.services.exceptions;

public class HabitacionNoExisteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HabitacionNoExisteException(String message) {
        super(message);
    }
}
