package edu.inbugwethrust.premier.suite.application;

import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;

/**
 * Servicio de aplicación para gestionar el caso de uso CU04: Reservar Habitación.
 */
public interface IReservaHabitacionService {

    /**
     * Registra una reserva de una o varias habitaciones a nombre de un eventual huésped.
     *
     * Responsabilidades típicas de la implementación:
     *  - Validar que las habitaciones sigan disponibles para los rangos indicados.
     *  - Crear la entidad Reserva con los datos del huésped.
     *  - Crear las FichaEvento correspondientes (estado RESERVADA) para cada habitación y rango.
     *  - Persistir todo de forma atómica (transacción).
     *
     * @param confirmacion DTO con la selección de habitaciones y los datos del huésped.
     */
    void registrarReserva(ConfirmacionReservaDTO confirmacion);
}
