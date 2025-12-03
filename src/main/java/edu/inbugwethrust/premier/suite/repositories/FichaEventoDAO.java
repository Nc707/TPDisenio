package edu.inbugwethrust.premier.suite.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;

@Repository
public interface FichaEventoDAO extends JpaRepository<FichaEvento, Integer> {
	
	/*
	 * * Consulta para obtener las fichas de eventos que no esten canceladas y que
	 * tengan fechas entre las fechas proporcionadas
	 * @Param fechaInicio Fecha de inicio del rango
	 * @Param fechaFin Fecha de fin del rango
	 * @return Lista de fichas de eventos que cumplen con los criterios
	 */
	@Query("SELECT f FROM FichaEvento f WHERE "
			+ " f.cancelado = false AND "
			+ " f.fechaInicio < :fechaFin AND"
			+ " f.fechaFin > :fechaInicio")
	List<FichaEvento> findByFechaEvento(
			@Param("fechaInicio")LocalDateTime fechaInicio,
			@Param("fechaFin")LocalDateTime fechaFin);
	
	/**
     * Devuelve todos los eventos que se solapan con el rango [inicio, fin]
     * para una habitación dada.
     */
    List<FichaEvento> findByHabitacionAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
            Habitacion habitacion,
            LocalDateTime inicio,
            LocalDateTime fin
    );
}
