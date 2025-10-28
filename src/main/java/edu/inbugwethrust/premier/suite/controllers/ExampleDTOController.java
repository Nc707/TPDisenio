package edu.inbugwethrust.premier.suite.controllers;


import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.inbugwethrust.premier.suite.dto.ExampleDTO;
import edu.inbugwethrust.premier.suite.services.ExampleEntityService;

@Controller
@RequestMapping("/examples")
public class ExampleDTOController {
	
	private final ExampleEntityService service;
	
	public ExampleDTOController(ExampleEntityService service) {
		this.service = service;
	}
	
	@PostMapping("/add")
	public ResponseEntity<Integer> addExample(@RequestBody ExampleDTO dto){
		try {	
			int id = this.service.saveExampleEntity(dto);
			return ResponseEntity.ok(id);
		}catch(Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	@GetMapping("/get")
	public ResponseEntity<ExampleDTO> getExample(@RequestParam int id){
		try {
			return ResponseEntity.ok(this.service.getExampleEntity(id));
		}catch(NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}catch(Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	
}
