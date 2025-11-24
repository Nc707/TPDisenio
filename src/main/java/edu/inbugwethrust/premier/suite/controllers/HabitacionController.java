package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.inbugwethrust.premier.suite.application.DisponibilidadService;
import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.CalendarioDisponibilidadDTO;

import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;
import edu.inbugwethrust.premier.suite.application.ReservaHabitacionService;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/habitaciones")
public class HabitacionController {
  
    private DisponibilidadService disponibilidadService;
    private final ReservaHabitacionService reservaHabitacionService;
    
    @Autowired
    public HabitacionController(DisponibilidadService disponibilidadService,
                                ReservaHabitacionService reservaHabitacionService) {
      this.disponibilidadService = disponibilidadService;
      this.reservaHabitacionService = reservaHabitacionService;
    }
    
    @GetMapping("buscar")
    public String buscarHabitaciones(
        @ModelAttribute BusquedaHabitacionDTO busquedaDTO, 
        BindingResult result,
        @RequestParam(name = "accion", defaultValue = "RESERVAR") String accion,
        Model model) {
            
        if (result.hasErrors()) {
            log.error("⚠️ ERROR DE BINDING DETECTADO: ⚠️");
            result.getAllErrors().forEach(error -> log.error(error.toString()));
        }
        log.info("Buscando habitaciones con criterios: {}", busquedaDTO);

        CalendarioDisponibilidadDTO calendario = 
            this.disponibilidadService.consultarDisponibilidad(busquedaDTO);
        
        model.addAttribute("busquedaDTO", busquedaDTO);
        model.addAttribute("calendario", calendario);
        model.addAttribute("modoAccion", accion);
        
        return "busqueda-habitacion-page";
    }

    // ===========================================
    // ============= CU04 – Reservar  ============
    // ===========================================
    
    /**
     * Punto de entrada final del CU04 en el backend:
     * registra definitivamente la reserva (paso 10 del caso de uso).
     *
     * El front arma un ConfirmacionReservaDTO con:
     *  - la selección de habitaciones y rangos de fechas (SeleccionReservaDTO)
     *  - los datos del eventual huésped (DatosHuespedReservaDTO)
     *
     * Si hay errores de validación en los datos del huésped o en la selección,
     * se vuelve a la pantalla de confirmación.
     * Si todo está OK, se delega en el servicio de reservas para:
     *  - verificar que las habitaciones sigan disponibles
     *  - registrar la Reserva y sus FichaEvento asociadas.
     */
    @PostMapping("reservar")
    public String registrarReserva(
            @Valid @ModelAttribute("confirmacionReserva") ConfirmacionReservaDTO confirmacionReserva,
            BindingResult bindingResult,
            Model model) {
        
        log.info("Intentando registrar reserva con datos: {}", confirmacionReserva);
        
        // 1) Validaciones de Bean Validation (campos obligatorios, longitudes, etc.)
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación en la confirmación de reserva");
            bindingResult.getAllErrors()
                         .forEach(error -> log.warn("Error de validación: {}", error));
            
            // Volvemos a la página de confirmación con los datos cargados
            model.addAttribute("confirmacionReserva", confirmacionReserva);
            // Este nombre de vista es un placeholder: adaptalo al HTML real de confirmación
            return "confirmar-reserva-page";
        }
        
        // 2) Lógica de negocio del CU04: registrar la reserva
        //    - validar disponibilidad nuevamente contra FichaEvento
        //    - crear Reserva + FichaEvento + relacionar Habitacion
        //    - persistir todo
        reservaHabitacionService.registrarReserva(confirmacionReserva);
        
        log.info("Reserva registrada correctamente");
        
        // 3) Preparamos datos para la pantalla de resultado / confirmación
        model.addAttribute("mensajeExito", "La reserva se registró correctamente.");
        // Podrías agregar un DTO de respuesta con info de la reserva creada si lo necesitás:
        // model.addAttribute("reserva", reservaCreada);
        
        // Nombre de la vista de resultado: cambialo por el que usen ustedes
        return "resultado-reserva-page";
    }
    
}
