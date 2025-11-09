package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/huespedes")
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
    @GetMapping("formulario-alta")
    public String obtenerFormularioAlta() {
    	        return "alta-huesped-page.html";
    }
    
    @PostMapping("api/alta")
    @ResponseBody
    public ResponseEntity<Huesped> darAlta(@Valid @RequestBody HuespedDTO dto) {
    	// 1. Si la validación falla (ej. apellido en blanco, email inválido)
        //    Spring lanzará una "MethodArgumentNotValidException" AUTOMÁTICAMENTE.
        
        // 2. Esta línea de código NUNCA se ejecutará si la validación falla.
    	
        Huesped creado = gestorHuespedes.dar_alta_huesped(dto);
        // si llegó hasta acá es porque no hubo excepción
        return ResponseEntity.ok(creado);
    }

    /**
     * Alta forzada de huésped.
     * Este endpoint emula el botón "ACEPTAR IGUALMENTE" del CU 9.
     * Vuelve a validar obligatorios, pero no rechaza por documento duplicado.
     */
    @PostMapping("api/alta-forzar")
    @ResponseBody
    public ResponseEntity<Huesped> darAltaForzada(@Valid @RequestBody HuespedDTO dto) {
        Huesped creado = gestorHuespedes.dar_alta_huesped_forzar(dto);
        return ResponseEntity.ok(creado);
    }

}
