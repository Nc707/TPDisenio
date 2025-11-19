package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.repositories.FichaEventoDAO;

@Service
public class GestorFichaEvento implements IGestorFichaEvento {
	
	private FichaEventoDAO fichaEventoRepository;
	
	@Autowired
	public GestorFichaEvento(FichaEventoDAO fichaEventoRepository) {
		this.fichaEventoRepository = fichaEventoRepository;
	}

	@Override
	public List<FichaEvento> obtenerFichasEventoPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
		LocalDateTime inicioBusqueda = fechaInicio.atStartOfDay();
		LocalDateTime finBusqueda = fechaFin.plusDays(1).atStartOfDay().minusSeconds(1);
		return fichaEventoRepository.findByFechaEvento(inicioBusqueda,finBusqueda);
	}

}
