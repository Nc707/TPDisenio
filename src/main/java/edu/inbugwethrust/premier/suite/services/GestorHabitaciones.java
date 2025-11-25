package edu.inbugwethrust.premier.suite.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.repositories.HabitacionDAO;

/*TODO: Ver si la lógica de obtencion de fichas de evento
 * y filtrado va aquí o en otro lado
 * Gemini me tiró la idea de hacer un gestor del tipo FACADE
 * de capa de Aplicación que maneje la lógica de construccion
 * de la tabla de habitaciones
 * */
@Service
public class GestorHabitaciones implements IGestorHabitaciones {

	private HabitacionDAO habitacionRepository;
	
	@Autowired
	public GestorHabitaciones(HabitacionDAO habitacionRepository) {
		this.habitacionRepository = habitacionRepository;
	}
	@Override
	public List<Habitacion> obtenerHabitaciones() {
		return habitacionRepository.findAll();
	}

	/**
     * Devuelve la habitación por su número (id) o lanza IllegalArgumentException
     * si no existe.
     */
    public Habitacion obtenerPorNumero(Integer numeroHabitacion) {
        return habitacionRepository.findById(numeroHabitacion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la habitación con número: " + numeroHabitacion));
    }
	
	
}
