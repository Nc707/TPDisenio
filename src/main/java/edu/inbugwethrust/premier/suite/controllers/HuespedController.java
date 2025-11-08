package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;

@RestController
@RequestMapping("/api/huespedes")
public class HuespedController {

    private final IGestorHuespedes gestorHuespedes;
    
    @Autowired
    public HuespedController(IGestorHuespedes gestorHuespedes) {
        this.gestorHuespedes = gestorHuespedes;
    }

    /**
     * Alta normal de huésped.
     * - Valida campos obligatorios
     * - Si el doc existe, lanza HuespedDuplicadoException → lo toma el ControllerAdvice
     * - Si todo OK, guarda y devuelve el huésped
     */
    @PostMapping("/alta")
    public ResponseEntity<Huesped> darAlta(@RequestBody HuespedDTO dto) {
        Huesped creado = gestorHuespedes.dar_alta_huesped(dto);
        // si llegó hasta acá es porque no hubo excepción
        return ResponseEntity.ok(creado);
    }

    /**
     * Alta forzada de huésped.
     * Este endpoint emula el botón "ACEPTAR IGUALMENTE" del CU 9.
     * Vuelve a validar obligatorios, pero no rechaza por documento duplicado.
     */
    @PostMapping("/forzar")
    public ResponseEntity<Huesped> darAltaForzada(@RequestBody HuespedDTO dto) {
        Huesped creado = gestorHuespedes.dar_alta_huesped_forzar(dto);
        return ResponseEntity.ok(creado);
    }

}
