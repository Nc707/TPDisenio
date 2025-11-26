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
        log.info("Modo de acción recibido: {}", accion); // RESERVAR / OCUPAR / DISPONIBILIDAD

        CalendarioDisponibilidadDTO calendario =
                this.disponibilidadService.consultarDisponibilidad(busquedaDTO);

        model.addAttribute("busquedaDTO", busquedaDTO);
        model.addAttribute("calendario", calendario);
        model.addAttribute("modoAccion", accion);

        return "busqueda-habitacion-page";
    }   
}
