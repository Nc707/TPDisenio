package edu.inbugwethrust.premier.suite.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta ocupar una habitación en un rango de fechas
 * donde existen dos o más reservas DISTINTAS solapadas.
 * El sistema no puede determinar automáticamente cuál de ellas se está ejecutando.
 */
@ResponseStatus(HttpStatus.CONFLICT) // Devuelve un 409 Conflict si llega a la API
public class MultiplesReservasEnRangoException extends RuntimeException {

    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    public MultiplesReservasEnRangoException(String message) {
        super(message);
    }
}