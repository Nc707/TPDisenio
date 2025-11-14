package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class MenuController {

	@RequestMapping("")
	public String mostrarMenuPrincipal() {
		return "menu.html";
	}
}
