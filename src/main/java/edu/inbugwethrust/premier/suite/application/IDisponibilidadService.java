package edu.inbugwethrust.premier.suite.application;


import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.CalendarioDisponibilidadDTO;

public interface IDisponibilidadService {
  public CalendarioDisponibilidadDTO consultarDisponibilidad(BusquedaHabitacionDTO busquedaDTO);
}
