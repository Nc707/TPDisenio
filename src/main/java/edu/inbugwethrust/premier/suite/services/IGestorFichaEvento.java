package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.util.List;

import edu.inbugwethrust.premier.suite.model.FichaEvento;

public interface IGestorFichaEvento {
	public List<FichaEvento> obtenerFichasEventoPorFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
