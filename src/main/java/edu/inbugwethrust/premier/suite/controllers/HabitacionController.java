package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/habitaciones")
public class HabitacionController {
	
	@RequestMapping("formulario-busqueda")
	public String obtenerFormularioBusqueda() {
		return "busqueda-habitacion-page.html";
	}
}
