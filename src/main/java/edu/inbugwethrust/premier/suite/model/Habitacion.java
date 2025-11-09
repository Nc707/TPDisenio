package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Habitacion {
	
	@Id
	int numero;
	int piso;
	EstadoHabitacion estado;
	TipoHabitacion tipoHabitacion;
}
