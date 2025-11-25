package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.repositories.FichaEventoDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.ReservaNoDisponibleException;

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

	/**
     * Verifica que para la habitación dada NO existan FichaEvento solapadas
     * con el rango [inicio, fin]. Si existe solapamiento, lanza
     * ReservaNoDisponibleException.
     */
        public void validarDisponibilidad(Habitacion habitacion,
                                        LocalDateTime inicio,
                                        LocalDateTime fin) {

        // La query ya filtra cancelado = false y solapamiento básico
        List<FichaEvento> eventosSolapados =
                fichaEventoRepository.findByFechaEvento(inicio, fin);

        boolean ocupada = eventosSolapados.stream()
                .anyMatch(fe ->
                        fe.getHabitacion() != null
                        && fe.getHabitacion().getNumero() == habitacion.getNumero()
                );

        if (ocupada) {
                throw new ReservaNoDisponibleException(
                        "La habitación " + habitacion.getNumero()
                                + " no está disponible en el rango solicitado.");
        }
        }


    /**
     * Crea una FichaEvento de tipo "RESERVADA" asociada a la reserva y la
     * habitación dadas, con el rango [inicio, fin] y la descripción indicada.
     *
     * NO persiste: solo devuelve la entidad armada para que el caso de uso
     * la agregue a la Reserva.
     */
    public FichaEvento crearFichaParaReserva(Reserva reserva,
                                             Habitacion habitacion,
                                             LocalDateTime inicio,
                                             LocalDateTime fin,
                                             String descripcion) {
        FichaEvento ficha = new FichaEvento();
        ficha.setHabitacion(habitacion);
        ficha.setReserva(reserva);
        ficha.setFechaInicio(inicio);
        ficha.setFechaFin(fin);
        ficha.setEstado(EstadoHabitacion.RESERVADA);
        ficha.setCancelado(false);
        ficha.setDescripcion(descripcion);

        return ficha;
    }
}
