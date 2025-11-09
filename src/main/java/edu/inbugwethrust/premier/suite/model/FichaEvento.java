package edu.inbugwethrust.premier.suite.model;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FichaEvento {
	
	@Id
	int idFichaEventos;
	EstadoHabitacion estado;
	LocalDateTime fechaInicio;
	LocalDateTime fechaFin;
	String descripcion;
	boolean cancelado;
	Habitacion habitacion; 
}
