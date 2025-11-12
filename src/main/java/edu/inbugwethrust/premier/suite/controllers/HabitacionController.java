package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.inbugwethrust.premier.suite.application.DisponibilidadService;
import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.CalendarioDisponibilidadDTO;

@Controller
@RequestMapping("/habitaciones")
public class HabitacionController {
  
    private DisponibilidadService disponibilidadService;
    
    @Autowired
    public HabitacionController(DisponibilidadService disponibilidadService) {
      this.disponibilidadService = disponibilidadService;
    }
    
	
	@RequestMapping("formulario-busqueda")
	public String obtenerFormularioBusqueda(Model model) {
	  model.addAttribute("busquedaHabitacionDTO", new BusquedaHabitacionDTO());
		return "busqueda-habitacion-page";
	}
	
	@RequestMapping("buscar")
	public String buscarHabitaciones(
        @ModelAttribute BusquedaHabitacionDTO busquedaDTO, 
        Model model) {
    
    // 1. Llama a tu servicio para buscar
    CalendarioDisponibilidadDTO calendario = this.disponibilidadService.consultarDisponibilidad(busquedaDTO);
    
    // 2. Pasa los resultados Y el DTO de búsqueda a la vista
    model.addAttribute("resultados", calendario);
    model.addAttribute("busquedaDTO", busquedaDTO); // Para rellenar el formulario
    
    // 3. Devuelve LA MISMA vista
    return "busqueda-habitacion-page";
}
}
