package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.inbugwethrust.premier.suite.dto.IdentificacionHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.OcupacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.mappers.HuespedMapper;
import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.repositories.EstadiaDAO;

/**
 * Gestor de dominio para la entidad Estadia. Se encarga de persistir y recuperar estadías.
 */
@Service
public class GestorEstadia {

  private final EstadiaDAO estadiaDAO;
  
  private final HuespedMapper mapper;


  @Autowired
  public GestorEstadia(EstadiaDAO estadiaDAO, HuespedMapper mapper) {
    this.estadiaDAO = estadiaDAO;
    this.mapper = mapper;
  }

  /**
   * Guarda la estadía como agregado raíz. Por cascade se persisten también las FichaEvento
   * asociadas.
   */
  public Estadia guardar(Estadia estadia) {
    return estadiaDAO.save(estadia);
  }

  /**
   * Obtiene una estadía por id o lanza IllegalArgumentException si no existe.
   */
  public Estadia obtenerPorId(Long id) {
    return estadiaDAO.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No existe la estadía con id: " + id));
  }

  public LocalDateTime obtenerHoraCheckIn(LocalDate date) {
    // Lógica para obtener la hora de check-in
    return date.atStartOfDay().plusHours(14);
  }

  public LocalDateTime obtenerHoraCheckOut(LocalDate date) {
    // Lógica para obtener la hora de check-out
    return date.atStartOfDay().plusHours(10);
  }

  public Estadia crearEstadia(Reserva reserva, List<OcupacionHabitacionDTO> dtos,
      Map<Integer, Habitacion> mapaHabitaciones, Map<HuespedID, Huesped> mapaHuespedes) {

    Estadia estadia = new Estadia();
    estadia.setReserva(reserva);

    for (OcupacionHabitacionDTO dto : dtos) {

      // 1. Obtener Habitacion del Mapa
      Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

      // 2. Crear Ficha de Ocupación
      FichaEvento ficha = new FichaEvento();
      ficha.setHabitacion(habitacion);
      ficha.setReserva(reserva);
      ficha.setFechaInicio(dto.getFechaIngreso().atTime(12, 0));
      ficha.setFechaFin(dto.getFechaEgreso().atTime(10, 0));
      ficha.setEstado(EstadoHabitacion.OCUPADA);
      ficha.setDescripcion("Ocupación - Check-in realizado");
      ficha.setCancelado(false);

      if (dto.getIdsAcompanantes() != null && !dto.getIdsAcompanantes().isEmpty()) {
        List<Huesped> listaAcompanantes = new ArrayList<>();
        for (IdentificacionHuespedDTO idAcom : dto.getIdsAcompanantes()) {
          Huesped acom = mapaHuespedes.get(mapper.toId(idAcom));
          if (acom != null) {
            listaAcompanantes.add(acom);
          }
        }
        ficha.setAcompanantes(listaAcompanantes);
      }

      estadia.agregarFichaEvento(ficha);
    }

    return estadia;
  }
  
  public void guardarEstadia(Estadia estadia) {
    estadiaDAO.save(estadia);
}
}

  
