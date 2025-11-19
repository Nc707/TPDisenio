package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DisponibilidadHabitacionDTO {
	int numeroHabitacion;
	LocalDate fecha;
	EstadoHabitacion estado;
}
