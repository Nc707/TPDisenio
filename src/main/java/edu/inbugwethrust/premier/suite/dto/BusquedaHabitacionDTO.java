package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusquedaHabitacionDTO {
	@NotNull(message = "La fecha de inicio no puede estar vacía")
	LocalDate fechaInicio;
	@NotNull(message = "La fecha de fin no puede estar vacía")
	LocalDate fechaFin;
}
