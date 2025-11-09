package edu.inbugwethrust.premier.suite.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.DisponibilidadHabitacionDTO;
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.services.IGestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.IGestorHabitaciones;

public class DisponibilidadService implements IDisponibilidadService {

	private IGestorHabitaciones gestorHabitaciones;
	
	private IGestorFichaEvento gestorFichaEvento;
	
	@Autowired
	public DisponibilidadService(IGestorHabitaciones gestorHabitaciones, IGestorFichaEvento gestorFichaEvento) {
		this.gestorHabitaciones = gestorHabitaciones;
		this.gestorFichaEvento = gestorFichaEvento;
	}
	
	/*
	 * * Consulta la disponibilidad de las habitaciones en un rango de fechas.
	 * * @param busquedaHabitacionDTO DTO que contiene la fecha de inicio y fin de la busqueda.
	 * * @return Lista de DisponibilidadHabitacionDTO que contiene el numero de habitacion, fecha y estado.
	 * */
	public List<DisponibilidadHabitacionDTO> consultarHabitaciones(BusquedaHabitacionDTO busquedaHabitacionDTO){
		// Si la fecha de fin es anterior a la de inicio, retornamos lista vacia
		if (busquedaHabitacionDTO.getFechaFin().isBefore(busquedaHabitacionDTO.getFechaInicio())) {
			return List.of();
		}
		var fechas = construirFechas(busquedaHabitacionDTO);
		var habitaciones = gestorHabitaciones.obtenerHabitaciones();
		var fichasEventos = gestorFichaEvento.obtenerFichasEventoPorFechas(busquedaHabitacionDTO.getFechaInicio(), busquedaHabitacionDTO.getFechaFin());
		
		List<DisponibilidadHabitacionDTO> disponibilidad = fechas.stream().flatMap(fecha -> habitaciones.stream().map(
				// Para cada habitacion y fecha, buscamos si hay una ficha de evento que coincida
		        habitacion -> {
		            EstadoHabitacion estadoDelDia = fichasEventos.stream()
		            	// Filtramos las fichas que corresponden a la habitacion y fecha actual
		                .filter(ficha -> ficha.getHabitacion().getNumero() == habitacion.getNumero())
		                .filter(ficha -> {
		                    LocalDate inicioFicha = ficha.getFechaInicio().toLocalDate();
		                    LocalDate finFicha = ficha.getFechaFin().toLocalDate();
		                    return !fecha.isBefore(inicioFicha) && fecha.isBefore(finFicha);
		                })
		                .findFirst()
		                // Si encontramos una ficha, obtenemos su estado; si no, la habitacion esta libre
		                .map(ficha -> ficha.getEstado()) 
		                .orElse(EstadoHabitacion.LIBRE);
		            return new DisponibilidadHabitacionDTO(
		                habitacion.getNumero(),
		                fecha,
		                estadoDelDia
		            );
		        })).toList();
		return disponibilidad;
	}

	private List<LocalDate> construirFechas(BusquedaHabitacionDTO busquedaHabitacionDTO) {
		// Construye una lista de fechas entre la fecha de inicio y fin incluidas
		return busquedaHabitacionDTO.getFechaInicio().datesUntil(busquedaHabitacionDTO.getFechaFin().plusDays(1)).toList();
	}
	
	
}
