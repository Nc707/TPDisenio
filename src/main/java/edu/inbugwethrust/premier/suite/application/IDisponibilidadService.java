package edu.inbugwethrust.premier.suite.application;

import java.util.List;

import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.DisponibilidadHabitacionDTO;

public interface IDisponibilidadService {
	public List<DisponibilidadHabitacionDTO> consultarHabitaciones(BusquedaHabitacionDTO busquedaHabitacionDTO);
}
