package edu.inbugwethrust.premier.suite.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.inbugwethrust.premier.suite.application.IOcuparHabitacionService;
import edu.inbugwethrust.premier.suite.dto.RegistrarOcupacionesRequestDTO;
import jakarta.validation.Valid;

/**
 * Controller REST para operaciones relacionadas con la ocupación de habitaciones
 * (check-in / creación de estadías).
 *
 * Implementa el caso de uso CU15 "Ocupar habitación".
 */
@RestController
@RequestMapping("/api/estadias")
public class EstadiaController {

    private final IOcuparHabitacionService ocuparHabitacionService;

    @Autowired
    public EstadiaController(IOcuparHabitacionService ocuparHabitacionService) {
        this.ocuparHabitacionService = ocuparHabitacionService;
    }

    /**
     * Registra la ocupación de una o varias habitaciones para un huésped y sus acompañantes.
     *
     * Espera un JSON con la estructura de RegistrarOcupacionesRequestDTO.
     * Ejemplo (simplificado):
     *
     * {
     *   "ocupaciones": [
     *     {
     *       "numeroHabitacion": 101,
     *       "fechaIngreso": "2025-11-26",
     *       "fechaEgreso": "2025-11-30",
     *       "idHuespedResponsable": 1,
     *       "idsAcompanantes": [2,3],
     *       "forzarSobreReserva": false
     *     }
     *   ]
     * }
     */
    @PostMapping("/ocupar")
    public ResponseEntity<?> registrarOcupaciones(
            @Valid @RequestBody RegistrarOcupacionesRequestDTO request) {

        // Si hay errores de validación en el DTO, @Valid dispara
        // MethodArgumentNotValidException y lo maneja GlobalExceptionHandler.
        ocuparHabitacionService.registrarOcupaciones(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Ocupación registrada con éxito");

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
