package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadHabitacionDTO {
	int numeroHabitacion;
	LocalDate fecha;
	EstadoHabitacion estado;
}
