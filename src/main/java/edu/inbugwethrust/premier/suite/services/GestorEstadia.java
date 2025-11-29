package edu.inbugwethrust.premier.suite.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.inbugwethrust.premier.suite.services.exceptions.CapacidadMaximaExcedidaException;
import edu.inbugwethrust.premier.suite.services.exceptions.HabitacionNoExisteException;
import edu.inbugwethrust.premier.suite.services.exceptions.HuespedDuplicadoEnOcupacionException;
import edu.inbugwethrust.premier.suite.services.exceptions.OcupacionFechasInvalidasException;

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

  // =========================================================
  // NUEVA LÓGICA DE VALIDACIÓN DE OCUPACIONES (CU15)
  // =========================================================

  /**
   * Valida las reglas de negocio del CU15 "Ocupar habitación" sobre la lista de ocupaciones.
   *
   * Reglas:
   * - Las fechas de ingreso y egreso deben ser válidas por habitación.
   * - La habitación debe existir en el mapa precargado.
   * - No se puede superar la capacidad máxima de huéspedes por habitación.
   * - Un mismo huésped acompañante no puede aparecer en más de una habitación
   *   dentro de la misma operación.
   */
  public void validarOcupaciones(List<OcupacionHabitacionDTO> listaOcupaciones,
                                 Map<Integer, Habitacion> mapaHabitaciones) {

    if (listaOcupaciones == null || listaOcupaciones.isEmpty()) {
      return;
    }

    // Set global para controlar que un mismo huésped no esté en múltiples habitaciones a la vez
    Set<IdentificacionHuespedDTO> acompanantesUsadosGlobal = new HashSet<>();

    for (OcupacionHabitacionDTO dto : listaOcupaciones) {

      // 1) Validar fechas por habitación
      validarFechas(dto);

      // 2) Obtener la entidad habitación (y validar existencia)
      Habitacion habitacion = obtenerHabitacionDesdeMapa(dto, mapaHabitaciones);

      // 3) Validar capacidad máxima de la habitación
      validarCapacidadHabitacion(dto, habitacion);

      // 4) Validar unicidad de acompañantes en toda la operación
      aplicarReglasAcompanantes(dto, acompanantesUsadosGlobal);
    }
  }

  private void validarFechas(OcupacionHabitacionDTO dto) {

    if (dto.getFechaIngreso() == null || dto.getFechaEgreso() == null) {
      throw new OcupacionFechasInvalidasException(
          "Las fechas de ingreso y egreso son obligatorias para la habitación "
              + dto.getNumeroHabitacion());
    }

    LocalDate ingreso = dto.getFechaIngreso();
    LocalDate egreso = dto.getFechaEgreso();

    if (!egreso.isAfter(ingreso)) {
      throw new OcupacionFechasInvalidasException(
          "La fecha de egreso debe ser posterior a la fecha de ingreso para la habitación "
              + dto.getNumeroHabitacion());
    }
  }

  private Habitacion obtenerHabitacionDesdeMapa(OcupacionHabitacionDTO dto,
                                                Map<Integer, Habitacion> mapaHabitaciones) {

    Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

    if (habitacion == null) {
      throw new HabitacionNoExisteException(
          "La habitación número " + dto.getNumeroHabitacion() + " no existe.");
    }

    return habitacion;
  }

  private void validarCapacidadHabitacion(OcupacionHabitacionDTO dto, Habitacion habitacion) {

    int cantidadOcupantes =
        (dto.getIdsAcompanantes() == null) ? 0 : dto.getIdsAcompanantes().size();

    int maximo = habitacion.getTipoHabitacion().getMaximoHuespedes();

    if (cantidadOcupantes > maximo) {
      throw new CapacidadMaximaExcedidaException(
          "La habitación " + dto.getNumeroHabitacion()
              + " tiene una capacidad máxima de " + maximo
              + " huéspedes y se están intentando asignar " + cantidadOcupantes + ".");
    }
  }

  /**
   * Reglas del CU15 sobre acompañantes / ocupantes:
   * - Cada huésped puede aparecer como acompañante (ocupante) en UNA sola habitación.
   * - Se permite que el huésped responsable de la reserva figure también como acompañante
   *   en UNA habitación (la que realmente ocupa), siempre que el DTO lo indique así.
   *
   * El control se hace mediante un Set global de IdentificacionHuespedDTO,
   * que asume que dicho DTO implementa equals/hashCode por tipo y número de documento.
   */
  private void aplicarReglasAcompanantes(OcupacionHabitacionDTO dto,
                                         Set<IdentificacionHuespedDTO> acompanantesUsadosGlobal) {

    if (dto.getIdsAcompanantes() == null) {
      return;
    }

    for (IdentificacionHuespedDTO acompDTO : dto.getIdsAcompanantes()) {

      if (acompDTO == null) {
        continue;
      }

      // El Set devuelve false si el objeto ya existía dentro del conjunto
      if (!acompanantesUsadosGlobal.add(acompDTO)) {
        throw new HuespedDuplicadoEnOcupacionException(
            "El huésped con documento "
                + acompDTO.getNumeroDocumento() + " (" + acompDTO.getTipoDocumento() + ") "
                + "ya se encuentra asignado como acompañante en otra habitación de esta misma operación.");
      }
    }
  }

  // =========================================================
  // LÓGICA EXISTENTE PARA CREAR Y GUARDAR ESTADÍA
  // =========================================================

  public Estadia crearEstadia(Reserva reserva, List<OcupacionHabitacionDTO> dtos,
      Map<Integer, Habitacion> mapaHabitaciones, Map<HuespedID, Huesped> mapaHuespedes,
      Map<Integer, Huesped> mapaHuespedesResponsables) {

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
      // 3. Asignar Huesped Responsable
      ficha.setResponsable(mapaHuespedesResponsables.get(dto.getNumeroHabitacion()));

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
