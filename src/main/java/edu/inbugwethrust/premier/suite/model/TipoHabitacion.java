package edu.inbugwethrust.premier.suite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoHabitacion {
	String nombre;
	Double costoNoche;
	int maximoHuespedes;
	String[] distribucionesPosibles;
}
