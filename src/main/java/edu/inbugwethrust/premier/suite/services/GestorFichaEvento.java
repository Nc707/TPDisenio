package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.repositories.FichaEventoDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.MultiplesReservasEnRangoException;
import edu.inbugwethrust.premier.suite.services.exceptions.ReservaNoDisponibleException;
import edu.inbugwethrust.premier.suite.dto.OcupacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.model.Huesped;

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
    return fichaEventoRepository.findByFechaEvento(inicioBusqueda, finBusqueda);
  }

  /**
   * Verifica que para la habitación dada NO existan FichaEvento solapadas con el rango [inicio,
   * fin]. Si existe solapamiento, lanza ReservaNoDisponibleException.
   */
  public void validarDisponibilidad(Habitacion habitacion, LocalDateTime inicio,
      LocalDateTime fin) {

    // La query ya filtra cancelado = false y solapamiento básico
    List<FichaEvento> eventosSolapados = fichaEventoRepository.findByFechaEvento(inicio, fin);

    boolean ocupada = eventosSolapados.stream().anyMatch(fe -> fe.getHabitacion() != null
        && fe.getHabitacion().getNumero() == habitacion.getNumero());

    if (ocupada) {
      throw new ReservaNoDisponibleException("La habitación " + habitacion.getNumero()
          + " no está disponible en el rango solicitado.");
    }
  }

  public Reserva validarDisponibilidadGlobal(List<OcupacionHabitacionDTO> dtos,
      Map<Integer, Habitacion> mapaHabitaciones) {
    Reserva reservaUnica = null;

    for (OcupacionHabitacionDTO dto : dtos) {

      // Recuperamos entidad del mapa (Rápido)
      Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

      LocalDateTime inicio = dto.getFechaIngreso().atTime(12, 0); // O tu lógica de fecha
      LocalDateTime fin = dto.getFechaEgreso().atTime(10, 0);

      // Reutilizamos tu lógica existente, pasando los datos sueltos
      Reserva reservaEncontrada =
          validarDisponibilidad(habitacion, inicio, fin, dto.isForzarSobreReserva());

      if (reservaEncontrada != null) {
        if (reservaUnica == null) {
          reservaUnica = reservaEncontrada;
        } else if (!reservaUnica.getIdReserva().equals(reservaEncontrada.getIdReserva())) {
          throw new MultiplesReservasEnRangoException(
              "Conflicto: Las habitaciones seleccionadas pertenecen a reservas distintas.");
        }
      }
    }
    return reservaUnica; // Retorna la reserva si existe, o null si es Walk-in
  }

  /**
   * Valida que la habitación pueda ser ocupada en el rango [inicio, fin].
   *
   * Reglas: - Se puede ocupar sobre días DISPONIBLE o RESERVADA. - Si hay algún evento con estado
   * distinto (OCUPADA, FUERA_SERVICIO, etc.), se lanza ReservaNoDisponibleException. - Si hay días
   * RESERVADA: * si forzarSobreReserva == false -> se lanza ReservaNoDisponibleException para que
   * el front muestre el mensaje del punto 3.D del CU15. * si forzarSobreReserva == true -> se
   * permite y se devuelve la Reserva asociada (si hay más de una, se devuelve la primera que se
   * encuentre).
   *
   * @return la Reserva asociada (si existía una) o null si no había reservas en el rango.
   */
  private Reserva validarDisponibilidad(Habitacion habitacion, LocalDateTime inicio,
      LocalDateTime fin, boolean forzarSobreReserva) {

    // Eventos que se solapan con el rango [inicio, fin]
    List<FichaEvento> eventos = fichaEventoRepository
        .findByHabitacionAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(habitacion, inicio,
            fin);

    boolean hayReserva = false;
    Reserva reservaEncontrada = null;

    for (FichaEvento evento : eventos) {

      if (evento.isCancelado()) {
        continue;
      }

      EstadoHabitacion estado = evento.getEstado();

      if (estado == EstadoHabitacion.RESERVADA) {
        hayReserva = true;
        Reserva reservaActual = evento.getReserva();

        if (reservaActual != null) {
          // Caso 1: Es la primera reserva que encontramos en el bucle
          if (reservaEncontrada == null) {
            reservaEncontrada = reservaActual;
          }
          // Caso 2: Ya teníamos una reserva, verificamos si la actual es DIFERENTE
          // (Comparamos por ID para asegurar que no sean eventos distintos de la misma reserva)
          else if (!reservaEncontrada.getIdReserva().equals(reservaActual.getIdReserva())) {
            throw new MultiplesReservasEnRangoException("Conflicto de ocupación: La habitación "
                + habitacion.getNumero() + " tiene múltiples reservas distintas ("
                + reservaEncontrada.getIdReserva() + " y " + reservaActual.getIdReserva()
                + ") en el rango de fechas seleccionado. Debe resolver esto manualmente antes de ocupar.");
          }
        }
      } else {
        // Cualquier estado distinto de DISPONIBLE o RESERVADA (ej: OCUPADA, FUERA_SERVICIO) bloquea
        throw new ReservaNoDisponibleException("La habitación " + habitacion.getNumero()
            + " no está disponible para ocupar en todo el rango seleccionado (Estado: " + estado
            + ").");
      }
    }

    // Validación del flag forzarSobreReserva (Punto 3.D del CU15)
    if (hayReserva && !forzarSobreReserva) {
      throw new ReservaNoDisponibleException("La habitación " + habitacion.getNumero()
          + " tiene una reserva en el rango seleccionado. "
          + "Debe confirmar 'OCUPAR IGUAL' para continuar.");
    }

    return reservaEncontrada;
  }

  /**
   * Crea una FichaEvento de tipo "RESERVADA" asociada a la reserva y la habitación dadas, con el
   * rango [inicio, fin] y la descripción indicada.
   *
   * NO persiste: solo devuelve la entidad armada para que el caso de uso la agregue a la Reserva.
   */
  public FichaEvento crearFichaParaReserva(Reserva reserva, Habitacion habitacion,
      LocalDateTime inicio, LocalDateTime fin, String descripcion) {
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

  /**
   * Crea una FichaEvento de tipo OCUPADA para una habitación dada, asociándola opcionalmente a una
   * reserva y a una estadía.
   *
   * NO guarda en base de datos, solo construye la entidad. La persistencia se hace al guardar la
   * Estadia (por cascade).
   */
  public FichaEvento crearFichaOcupacion(Habitacion habitacion, Reserva reserva,
      LocalDateTime inicio, LocalDateTime fin,
      List<Huesped> acompanantes) {

    FichaEvento ficha = new FichaEvento();
    ficha.setHabitacion(habitacion);
    ficha.setReserva(reserva); // puede ser null si es walk-in
    ficha.setFechaInicio(inicio);
    ficha.setFechaFin(fin);
    ficha.setEstado(EstadoHabitacion.OCUPADA);
    ficha.setCancelado(false);


    if (acompanantes != null && !acompanantes.isEmpty()) {
      ficha.getAcompanantes().addAll(acompanantes); // lista ManyToMany en FichaEvento
    }

    // Descripción simple, podés tunearla si querés
    String descripcion = "Ocupación de la habitación " + habitacion.getNumero() + " del "
        + inicio.toLocalDate() + " al " + fin.toLocalDate();
    ficha.setDescripcion(descripcion);

    return ficha;
  }

}
