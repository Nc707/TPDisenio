package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.inbugwethrust.premier.suite.dto.ConfirmacionReservaDTO;
import edu.inbugwethrust.premier.suite.dto.DatosHuespedReservaDTO;
import edu.inbugwethrust.premier.suite.dto.OcupacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.SeleccionHabitacionDTO;
import edu.inbugwethrust.premier.suite.mappers.HuespedMapper;
import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.EstadoReserva;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.repositories.HuespedDAO;
import edu.inbugwethrust.premier.suite.repositories.ReservaDAO;

@Service
public class GestorReservas {

  private final ReservaDAO reservaDAO;
  
  private final HuespedDAO huespedDAO;
  
  private final HuespedMapper mapper;

  @Autowired
  public GestorReservas(ReservaDAO reservaDAO,  HuespedDAO huespedDAO, HuespedMapper mapper) {
    this.reservaDAO = reservaDAO;
    this.huespedDAO = huespedDAO;
    this.mapper = mapper;
  }

  /**
   * Crea una nueva Reserva en estado ACTIVA, seteando los datos del huésped directamente sobre la
   * entidad.
   *
   * NO persiste: solo devuelve la entidad "viva" para que el caso de uso siga completando la
   * información (fichas, etc.).
   */
  public Reserva generarReserva(ConfirmacionReservaDTO dto,
      Map<Integer, Habitacion> mapaHabitaciones) {

    DatosHuespedReservaDTO datosH = dto.getDatosHuesped();

    Reserva reserva = new Reserva();
    reserva.setEstadoReserva(EstadoReserva.ACTIVA);
    reserva.setApellidoReserva(datosH.getApellido());
    reserva.setNombreReserva(datosH.getNombre());
    reserva.setTelefonoReserva(datosH.getTelefono());

    for (SeleccionHabitacionDTO sel : dto.getHabitacionesSeleccionadas()) {

      Habitacion habitacion = mapaHabitaciones.get(sel.getNumeroHabitacion());

      LocalDateTime inicio = sel.getFechaIngreso().atTime(12, 0);
      LocalDateTime fin = sel.getFechaEgreso().atTime(10, 0);

      FichaEvento ficha = new FichaEvento();

      ficha.setReserva(reserva);
      ficha.setHabitacion(habitacion);

      ficha.setFechaInicio(inicio);
      ficha.setFechaFin(fin);
      ficha.setEstado(EstadoHabitacion.RESERVADA);
      ficha.setCancelado(false);
      ficha.setDescripcion("Reserva Hab. " + habitacion.getNumero());

      reserva.agregarFichaEvento(ficha);
    }

    return reserva;
  }

  public Reserva crearReservaWalkIn(List<OcupacionHabitacionDTO> dtos,
      Map<Integer, Habitacion> mapaHabitaciones,
      Map<HuespedID, Huesped> mapaHuespedes,
      Estadia estadia) {

    // 1. Obtener datos del titular (del primer DTO)
    OcupacionHabitacionDTO primerDto = dtos.get(0);
    Huesped titular = mapaHuespedes.get(mapper.toId(primerDto.getIdsAcompanantes().getFirst()));

    // 2. Cabecera Reserva
    Reserva reserva = new Reserva();
    reserva.setEstadoReserva(EstadoReserva.EFECTUADA); // O CONFIRMADA
    reserva.setApellidoReserva(titular.getApellido());
    reserva.setNombreReserva(titular.getNombres());
    reserva.setTelefonoReserva(titular.getTelefono());
    reserva.setEstadia(estadia);

    // 3. Generar las Fichas de Reserva (Espejo de la ocupación)
    for (OcupacionHabitacionDTO dto : dtos) {

      Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

      FichaEvento ficha = new FichaEvento();
      ficha.setReserva(reserva);
      ficha.setHabitacion(habitacion);
      ficha.setFechaInicio(dto.getFechaIngreso().atTime(12, 0));
      ficha.setFechaFin(dto.getFechaEgreso().atTime(10, 0));
      ficha.setEstado(EstadoHabitacion.RESERVADA);
      ficha.setCancelado(false);
      ficha.setDescripcion("Walk-in automático");

      reserva.agregarFichaEvento(ficha);
    }

    return reservaDAO.save(reserva);
  }

  public Reserva crearNuevaReserva(String apellido, String nombre, String telefono) {
    Reserva reserva = new Reserva();
    reserva.setEstadoReserva(EstadoReserva.ACTIVA);

    // Normalizamos a mayúsculas para cumplir con el requerimiento de datos literales
    if (apellido != null) {
      reserva.setApellidoReserva(apellido.toUpperCase());
    }
    if (nombre != null) {
      reserva.setNombreReserva(nombre.toUpperCase());
    }
    if (telefono != null) {
      reserva.setTelefonoReserva(telefono);
    }

    return reserva;
  }

  /**
   * Persiste la reserva (agregado raíz). Por cascade en listaFichaEventos se guardan también las
   * FichaEvento asociadas.
   */
  public Reserva guardar(Reserva reserva) {
    return reservaDAO.save(reserva);
  }

  /**
   * Marca la reserva como EN_CURSO (check-in iniciado) y la persiste.
   */
  public void marcarReservaComoEnCurso(Reserva reserva, Estadia estadia) {
    if (reserva == null) {
      throw new IllegalArgumentException("La reserva no puede ser nula");
    }

    reserva.setEstadoReserva(EstadoReserva.EFECTUADA);
    reserva.setEstadia(estadia);
    reservaDAO.save(reserva);
  }
}
