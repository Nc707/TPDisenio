package edu.inbugwethrust.premier.suite.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.inbugwethrust.premier.suite.services.exceptions.CuitVacioException;
import edu.inbugwethrust.premier.suite.services.exceptions.HuespedDuplicadoException;
import edu.inbugwethrust.premier.suite.services.exceptions.ReservaNoDisponibleException;


@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) Faltan datos obligatorios / errores de validación en el DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacionDeArgumentos(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("message", "Hay errores en los datos enviados.");
        body.put("detalles", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 2) HuespedDuplicadoException de tu lógica de negocio
    @ExceptionHandler(HuespedDuplicadoException.class)
    public ResponseEntity<?> manejarHuespedDuplicado(HuespedDuplicadoException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Huésped duplicado");
        body.put("message", "CUIDADO! El tipo y número de documento ya existen en el sistema.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 3) Duplicado detectado por la BASE DE DATOS (PK/UNIQUE) – versión Spring
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> manejarDuplicadoBD_Spring(DataIntegrityViolationException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Huésped duplicado");
        body.put("message", "CUIDADO! El tipo y número de documento ya existen en el sistema.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 4) Duplicado detectado por Hibernate (ConstraintViolationException)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> manejarDuplicadoBD_Hibernate(ConstraintViolationException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Huésped duplicado");
        body.put("message", "CUIDADO! El tipo y número de documento ya existen en el sistema.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 5) Otros errores de base de datos (no duplicados)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> manejarErroresDeBaseDeDatos(DataAccessException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error de base de datos");
        body.put("message", "Ocurrió un problema al guardar los datos. Intente nuevamente más tarde.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 6) CUIT vacío (regla de negocio)
    @ExceptionHandler(CuitVacioException.class)
    public ResponseEntity<?> manejarCuitVacio(CuitVacioException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 7) ReservaNoDisponibleException de la lógica de reservas (CU04)
    @ExceptionHandler(ReservaNoDisponibleException.class)
    public ResponseEntity<?> manejarReservaNoDisponible(ReservaNoDisponibleException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Reserva no disponible");
        // Usamos el mensaje que vos armaste en la excepción para que llegue algo claro al front
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 8) Argumentos Ilegales (Validaciones manuales de lógica como fechas o duplicados)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> manejarArgumentoIlegal(IllegalArgumentException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error en la solicitud");
        body.put("message", ex.getMessage()); // Aquí saldrá: "No se puede reservar la misma..."

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 9) Cualquier otra excepción no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarExcepcionGenerica(Exception ex) {

        // Esto te ayuda a debuguear qué tipo de excepción está llegando realmente
        System.err.println(">>> Excepción NO controlada: " + ex.getClass().getName());
        ex.printStackTrace();

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error interno del servidor");
        body.put("message", "Ocurrió un error inesperado. Por favor, intente nuevamente.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }


}