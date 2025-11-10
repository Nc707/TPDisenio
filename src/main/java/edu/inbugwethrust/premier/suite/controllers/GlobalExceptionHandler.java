package edu.inbugwethrust.premier.suite.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.inbugwethrust.premier.suite.services.exceptions.HuespedDuplicadoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) Faltan datos obligatorios
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacionDeArgumentos(MethodArgumentNotValidException ex) {
        
        // Crea un mapa para guardar los errores (campo -> mensaje)
        Map<String, String> errores = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Obtenemos el nombre del campo que falló
            String nombreCampo = ((FieldError) error).getField();
            // Obtenemos el mensaje de error (el que pusiste en la anotación)
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        // Preparamos el cuerpo de la respuesta
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("detalles", errores); 

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 2) Documento ya existe
    @ExceptionHandler(HuespedDuplicadoException.class)
    public ResponseEntity<?> manejarHuespedDuplicado(HuespedDuplicadoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Huésped duplicado");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 3) Cualquier otra excepción no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepcionGenerica(Exception ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error interno del servidor");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
