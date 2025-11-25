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

import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;
import edu.inbugwethrust.premier.suite.application.IReservaHabitacionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final IReservaHabitacionService reservaHabitacionService;

    @Autowired
    public ReservaController(IReservaHabitacionService reservaHabitacionService) {
        this.reservaHabitacionService = reservaHabitacionService;
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservar(
            @Valid @RequestBody ConfirmacionReservaDTO confirmacionReservaDTO) {

        // Si el DTO viene con errores de validación, @Valid dispara
        // MethodArgumentNotValidException y lo maneja tu GlobalExceptionHandler.
        reservaHabitacionService.registrarReserva(confirmacionReservaDTO);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reserva registrada con éxito");

        // Podés agregar datos extra si querés (id, descripción, etc.)
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
