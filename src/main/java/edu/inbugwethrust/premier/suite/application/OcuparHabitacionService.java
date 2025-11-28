package edu.inbugwethrust.premier.suite.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.inbugwethrust.premier.suite.dto.IdentificacionHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.OcupacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.RegistrarOcupacionesRequestDTO;
import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.services.GestorHabitaciones;
import edu.inbugwethrust.premier.suite.services.GestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.GestorReservas;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;
import edu.inbugwethrust.premier.suite.services.GestorEstadia; // lo crearás después

@Service
public class OcuparHabitacionService implements IOcuparHabitacionService {

  private final GestorHabitaciones gestorHabitaciones;
  private final GestorFichaEvento gestorFichaEvento;
  private final GestorReservas gestorReservas;
  private final IGestorHuespedes gestorHuespedes;
  private final GestorEstadia gestorEstadia;

  @Autowired
  public OcuparHabitacionService(GestorHabitaciones gestorHabitaciones,
      GestorFichaEvento gestorFichaEvento, GestorReservas gestorReservas,
      IGestorHuespedes gestorHuespedes, GestorEstadia gestorEstadia) {
    this.gestorHabitaciones = gestorHabitaciones;
    this.gestorFichaEvento = gestorFichaEvento;
    this.gestorReservas = gestorReservas;
    this.gestorHuespedes = gestorHuespedes;
    this.gestorEstadia = gestorEstadia;
  }

  /**
   * Implementa la lógica principal del CU15 "Ocupar habitación".
   *
   * - Revalida reglas de negocio sobre el request. - Verifica disponibilidad de cada habitación
   * contra las FichaEvento existentes. - Toma o no la reserva asociada según forzarSobreReserva. -
   * Crea una Estadia con sus FichaEvento de OCUPACION y las persiste.
   */
  @Transactional
  public void registrarOcupaciones(RegistrarOcupacionesRequestDTO request) {

    // 1. Validar request
    Objects.requireNonNull(request);
    List<OcupacionHabitacionDTO> listaOcupaciones = request.getOcupaciones();
    if (listaOcupaciones == null || listaOcupaciones.isEmpty())
      return;

    // 2. PRE-CARGA DE HABITACIONES (Map<Integer, Habitacion>)
    Set<Integer> nrosHabitacion = listaOcupaciones.stream()
        .map(OcupacionHabitacionDTO::getNumeroHabitacion).collect(Collectors.toSet());
    Map<Integer, Habitacion> mapaHabitaciones =
        gestorHabitaciones.obtenerMapaPorNumeros(nrosHabitacion);

    // Set para controlar que un mismo huésped no esté en múltiples habitaciones a la vez
    Set<IdentificacionHuespedDTO> acompanantesUsadosGlobal = new HashSet<>();

    for (OcupacionHabitacionDTO dto : listaOcupaciones) {
      // A. Validar fechas (Ingreso vs Egreso)
      validarFechas(dto);

      // B. Obtener la entidad habitación real
      Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

      // Validación extra: verificar que la habitación exista en BDD
      if (habitacion == null) {
        throw new IllegalArgumentException(
            "La habitación número " + dto.getNumeroHabitacion() + " no existe.");
      }

      // C. Validar Capacidad (DTO vs Entidad Habitacion)
      validarCapacidadHabitacion(dto, habitacion);

      // D. Validar unicidad de acompañantes en el request
      aplicarReglasAcompanantes(dto, acompanantesUsadosGlobal);
    }



    // 3. PRE-CARGA DE HUÉSPEDES (Map<Long, Huesped>) --- ¡NUEVO! ---
    List<IdentificacionHuespedDTO> todosLosIdsHuespedes = new ArrayList<>();

    for (OcupacionHabitacionDTO dto : listaOcupaciones) {

      if (dto.getIdsAcompanantes() != null) {
        todosLosIdsHuespedes.addAll(dto.getIdsAcompanantes());
      }
    }



    // Obtenemos el mapa (Key: ID, Value: Entidad)
    Map<HuespedID, Huesped> mapaHuespedes = gestorHuespedes.obtenerMapaPorIds(todosLosIdsHuespedes);


    // 4. VALIDACIÓN GLOBAL (Pasa lista y mapa de habs)
    Reserva reserva =
        gestorFichaEvento.validarDisponibilidadGlobal(listaOcupaciones, mapaHabitaciones);
    
    Estadia estadia = gestorEstadia.crearEstadia(reserva, listaOcupaciones, mapaHabitaciones, mapaHuespedes);
    // 5. WALK-IN O RESERVA EXISTENTE
    if (reserva == null) {
      // Nota: Aquí también pasamos el mapaHuespedes para no volver a buscar al titular
      reserva =
          gestorReservas.crearReservaWalkIn(listaOcupaciones, mapaHabitaciones, mapaHuespedes, estadia);
    } else {
      gestorReservas.marcarReservaComoEnCurso(reserva, estadia);
    }

    // 6. CREAR ESTADÍA (Aquí pasamos TODO: Reserva, DTOs, Habitaciones y Huéspedes)
    gestorEstadia.guardarEstadia(estadia);
  }

  // ========================
  // Métodos privados helpers
  // ========================

  /**
   * Valida reglas básicas sobre fechas y campos obligatorios del DTO.
   */
  private void validarFechas(OcupacionHabitacionDTO dto) {

    if (dto.getFechaIngreso() == null || dto.getFechaEgreso() == null) {
      throw new IllegalArgumentException("Las fechas de ingreso y egreso son obligatorias.");
    }

    LocalDate ingreso = dto.getFechaIngreso();
    LocalDate egreso = dto.getFechaEgreso();

    if (!egreso.isAfter(ingreso)) {
      throw new IllegalArgumentException(
          "La fecha de egreso debe ser posterior a la fecha de ingreso para la habitación "
              + dto.getNumeroHabitacion());
    }
  }

  /**
   * Reglas del CU15 sobre acompañantes / ocupantes: - Cada huésped puede aparecer como acompañante
   * (ocupante) en UNA sola habitación. - Se permite que el huésped responsable de la reserva figure
   * también como acompañante en UNA habitación (la que realmente ocupa).
   */
  private void aplicarReglasAcompanantes(OcupacionHabitacionDTO dto,
      Set<IdentificacionHuespedDTO> acompanantesUsadosGlobal) {

    // Validamos la lista de acompañantes (ahora es una lista de objetos, no de Longs)
    if (dto.getIdsAcompanantes() == null) {
      return;
    }

    for (IdentificacionHuespedDTO acompDTO : dto.getIdsAcompanantes()) {

      if (acompDTO == null) {
        continue;
      }

      // Regla global: El Set devuelve 'false' si el objeto ya existía adentro.
      // Al tener @EqualsAndHashCode, comparará por tipo y número de documento.
      if (!acompanantesUsadosGlobal.add(acompDTO)) {
        throw new IllegalArgumentException("El huésped con documento "
            + acompDTO.getNumeroDocumento() + " (" + acompDTO.getTipoDocumento() + ") "
            + "ya se encuentra asignado como acompañante en otra habitación de esta misma operación.");
      }
    }
  }


  private void validarCapacidadHabitacion(OcupacionHabitacionDTO dto, Habitacion habitacion) {

    int cantidadOcupantes =
        (dto.getIdsAcompanantes() == null) ? 0 : dto.getIdsAcompanantes().size();

    int maximo = habitacion.getTipoHabitacion().getMaximoHuespedes();

    if (cantidadOcupantes > maximo) {
      throw new IllegalArgumentException(
          "La habitación " + dto.getNumeroHabitacion() + " tiene una capacidad máxima de " + maximo
              + " huéspedes y se están intentando asignar " + cantidadOcupantes + ".");
    }
  }

}
