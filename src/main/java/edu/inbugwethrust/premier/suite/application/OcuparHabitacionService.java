package edu.inbugwethrust.premier.suite.application;

import java.util.ArrayList;
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
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.services.GestorEstadia;
import edu.inbugwethrust.premier.suite.services.GestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.GestorHabitaciones;
import edu.inbugwethrust.premier.suite.services.GestorReservas;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;

@Service
public class OcuparHabitacionService implements IOcuparHabitacionService {

  private final GestorHabitaciones gestorHabitaciones;
  private final GestorFichaEvento gestorFichaEvento;
  private final GestorReservas gestorReservas;
  private final IGestorHuespedes gestorHuespedes;
  private final GestorEstadia gestorEstadia;

  @Autowired
  public OcuparHabitacionService(GestorHabitaciones gestorHabitaciones,
                                 GestorFichaEvento gestorFichaEvento,
                                 GestorReservas gestorReservas,
                                 IGestorHuespedes gestorHuespedes,
                                 GestorEstadia gestorEstadia) {
    this.gestorHabitaciones = gestorHabitaciones;
    this.gestorFichaEvento = gestorFichaEvento;
    this.gestorReservas = gestorReservas;
    this.gestorHuespedes = gestorHuespedes;
    this.gestorEstadia = gestorEstadia;
  }

  /**
   * Implementa la lógica principal del CU15 "Ocupar habitación".
   *
   * Responsabilidades del Service:
   * - Validar mínimamente el request (no nulo, lista no vacía).
   * - Orquestar la carga de habitaciones y huéspedes.
   * - Delegar reglas de negocio de ocupación al GestorEstadia.
   * - Delegar validación de disponibilidad global al GestorFichaEvento.
   * - Delegar creación/persistencia de Reserva y Estadia a sus respectivos gestores.
   */
  @Override
  @Transactional
  public void registrarOcupaciones(RegistrarOcupacionesRequestDTO request) {

    // 1) Defensa mínima: request no nulo y lista con elementos
    Objects.requireNonNull(request, "El request de ocupaciones no puede ser nulo");

    List<OcupacionHabitacionDTO> listaOcupaciones = request.getOcupaciones();
    if (listaOcupaciones == null || listaOcupaciones.isEmpty()) {
      return; // Nada que procesar
    }

    // 2) PRE-CARGA DE HABITACIONES (Map<Integer, Habitacion>)
    Set<Integer> nrosHabitacion = listaOcupaciones.stream()
        .map(OcupacionHabitacionDTO::getNumeroHabitacion)
        .collect(Collectors.toSet());

    Map<Integer, Habitacion> mapaHabitaciones =
        gestorHabitaciones.obtenerMapaPorNumeros(nrosHabitacion);

    // 3) VALIDACIÓN DE REGLAS DE OCUPACIÓN (fechas, capacidad, acompañantes, existencia)
    gestorEstadia.validarOcupaciones(listaOcupaciones, mapaHabitaciones);

    // 4) PRE-CARGA DE HUÉSPEDES (Map<HuespedID, Huesped>)
    List<IdentificacionHuespedDTO> todosLosIdsHuespedes = new ArrayList<>();

    for (OcupacionHabitacionDTO dto : listaOcupaciones) {
      if (dto.getIdsAcompanantes() != null) {
        todosLosIdsHuespedes.addAll(dto.getIdsAcompanantes());
      }
    }

    Map<HuespedID, Huesped> mapaHuespedes =
        gestorHuespedes.obtenerMapaPorIds(todosLosIdsHuespedes);

    // 5) VALIDACIÓN GLOBAL DE DISPONIBILIDAD (contra FichaEvento existentes)
    Reserva reserva =
        gestorFichaEvento.validarDisponibilidadGlobal(listaOcupaciones, mapaHabitaciones);

    // 6) CREAR ESTADÍA a partir de la (posible) reserva y las ocupaciones
    Estadia estadia = gestorEstadia.crearEstadia(reserva, listaOcupaciones,
                                                 mapaHabitaciones, mapaHuespedes);

    // 7) WALK-IN O RESERVA EXISTENTE
    if (reserva == null) {
      // No había reserva previa, se crea una reserva "walk-in"
      reserva = gestorReservas.crearReservaWalkIn(listaOcupaciones,
                                                  mapaHabitaciones,
                                                  mapaHuespedes,
                                                  estadia);
    } else {
      // Había reserva previa, se marca como "en curso"
      gestorReservas.marcarReservaComoEnCurso(reserva, estadia);
    }

    // 8) PERSISTIR ESTADÍA (como agregado raíz)
    gestorEstadia.guardarEstadia(estadia);
  }
}
