package edu.inbugwethrust.premier.suite.application;

import edu.inbugwethrust.premier.suite.dto.RegistrarOcupacionesRequestDTO;
import edu.inbugwethrust.premier.suite.dto.ValidarOcupacionesRequestDTO;
import edu.inbugwethrust.premier.suite.dto.ValidarOcupacionesResponseDTO;

/**
 * Servicio de aplicación para el CU15 "Ocupar habitación".
 *
 * Más adelante su implementación (OcuparHabitacionService) delegará
 * en GestorEstadia, GestorFichaEvento, GestorReservas, etc.
 */
public interface IOcuparHabitacionService {

    /**
     * Registra la ocupación de una o varias habitaciones.
     */
    void registrarOcupaciones(RegistrarOcupacionesRequestDTO request);

    ValidarOcupacionesResponseDTO prevalidarOcupaciones(ValidarOcupacionesRequestDTO request);
}
