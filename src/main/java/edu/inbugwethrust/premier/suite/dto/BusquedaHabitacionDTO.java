package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaHabitacionDTO {
	LocalDate fechaInicio;
	LocalDate fechaFin;
}
