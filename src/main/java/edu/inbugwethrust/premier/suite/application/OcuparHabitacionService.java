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
import edu.inbugwethrust.premier.suite.dto.ResultadoValidacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.SeleccionHabitacionOcupacionDTO;
import edu.inbugwethrust.premier.suite.dto.ValidarOcupacionesRequestDTO;
import edu.inbugwethrust.premier.suite.dto.ValidarOcupacionesResponseDTO;
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
   * CU15 – Paso 3.
   *
   * Recibe solo habitaciones + fechas (sin acompañantes) y:
   * - Reutiliza GestorEstadia.validarOcupaciones para chequear fechas y existencia.
   * - Usa GestorFichaEvento para ver si hay reservas en el rango.
   *
   * NO persiste nada, solo informa al front qué habitaciones son válidas
   * y si tienen una reserva asociada.
   */
  @Transactional(readOnly = true)
  @Override
  public ValidarOcupacionesResponseDTO prevalidarOcupaciones(ValidarOcupacionesRequestDTO request) {

    Objects.requireNonNull(request, "El request de validación de ocupaciones no puede ser nulo");

    // 1) Normalizar lista de selecciones
    List<SeleccionHabitacionOcupacionDTO> selecciones = request.getOcupaciones();

    // 2) Armar set de números de habitación
    Set<Integer> numerosHabitacion = selecciones.stream()
        .map(SeleccionHabitacionOcupacionDTO::getNumeroHabitacion)
        .collect(Collectors.toSet());

    // 3) Obtener mapa de habitaciones (según lo que ya tengas implementado)
    Map<Integer, Habitacion> mapaHabitaciones =
        gestorHabitaciones.obtenerMapaPorNumeros(numerosHabitacion);

    // 4) Adaptar las selecciones "livianas" a OcupacionHabitacionDTO mínimos
    List<OcupacionHabitacionDTO> ocupacionesMinimas = new ArrayList<>();

    for (SeleccionHabitacionOcupacionDTO sel : selecciones) {
      OcupacionHabitacionDTO dto = new OcupacionHabitacionDTO();
      dto.setNumeroHabitacion(sel.getNumeroHabitacion());
      dto.setFechaIngreso(sel.getFechaIngreso());
      dto.setFechaEgreso(sel.getFechaEgreso());
      // En el paso 3 NO hay acompañantes, ni forzarSobreReserva
      dto.setIdsAcompanantes(new ArrayList<>());
      dto.setForzarSobreReserva(false);
      ocupacionesMinimas.add(dto);
    }

    // 5) Reutilizar las validaciones generales de GestorEstadia
    //    (fechas válidas por habitación, existencia, capacidad – aquí 0 acompañantes)
    gestorEstadia.validarOcupaciones(ocupacionesMinimas, mapaHabitaciones);

    // 6) Para cada ocupación mínima, consultar disponibilidad y reserva
    List<ResultadoValidacionHabitacionDTO> resultados = new ArrayList<>();

    for (OcupacionHabitacionDTO dto : ocupacionesMinimas) {

      Habitacion habitacion = mapaHabitaciones.get(dto.getNumeroHabitacion());

      // Este método NO persiste nada, solo lee FichaEvento y
      // devuelve una Reserva si hay días RESERVADA en el rango.
      Reserva reserva = gestorFichaEvento.obtenerReservaParaOcupacion(dto, habitacion);

      ResultadoValidacionHabitacionDTO res = new ResultadoValidacionHabitacionDTO();
      res.setNumeroHabitacion(dto.getNumeroHabitacion());
      res.setFechaIngreso(dto.getFechaIngreso());
      res.setFechaEgreso(dto.getFechaEgreso());
      res.setSeleccionValida(true);

      if (reserva != null) {
        res.setHayReserva(true);
        res.setIdReserva(reserva.getIdReserva());
        res.setApellidoReserva(reserva.getApellidoReserva());
        res.setNombreReserva(reserva.getNombreReserva());
        res.setTelefonoReserva(reserva.getTelefonoReserva());
      } else {
        res.setHayReserva(false);
      }

      resultados.add(res);
    }

    // 7) Armar response global
    ValidarOcupacionesResponseDTO response = new ValidarOcupacionesResponseDTO();
    response.setResultados(resultados);
    return response;
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
    
    Map<Integer, IdentificacionHuespedDTO> mapaIdsResponsables =
        listaOcupaciones.stream().collect(Collectors.toMap(
            OcupacionHabitacionDTO::getNumeroHabitacion, 
            OcupacionHabitacionDTO::getIdHuespedResponsable));

    for (OcupacionHabitacionDTO dto : listaOcupaciones) {
      if (dto.getIdsAcompanantes() != null) {
        todosLosIdsHuespedes.addAll(dto.getIdsAcompanantes());
      }
    }

    Map<HuespedID, Huesped> mapaHuespedes =
        gestorHuespedes.obtenerMapaPorIds(todosLosIdsHuespedes);
    
    Map<Integer, Huesped> mapaHuespedesResponsables = mapaIdsResponsables.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey, // 1. Mantenemos el Número de Habitación como clave
            entry -> gestorHuespedes.obtenerPorId(entry.getValue()) // 2. Buscamos la entidad usando el DTO
        ));

    // 5) VALIDACIÓN GLOBAL DE DISPONIBILIDAD (contra FichaEvento existentes)
    Reserva reserva =
        gestorFichaEvento.validarDisponibilidadGlobal(listaOcupaciones, mapaHabitaciones);

    // 6) CREAR ESTADÍA a partir de la (posible) reserva y las ocupaciones
    Estadia estadia = gestorEstadia.crearEstadia(reserva, listaOcupaciones,
                                                 mapaHabitaciones, mapaHuespedes,
                                                 mapaHuespedesResponsables);

    // 7) WALK-IN O RESERVA EXISTENTE
    if (reserva == null) {
      // No había reserva previa, se crea una reserva "walk-in"
      reserva = gestorReservas.crearReservaWalkIn(listaOcupaciones,
                                                  mapaHabitaciones,
                                                  mapaHuespedesResponsables,
                                                  estadia);
    } else {
      // Había reserva previa, se marca como "en curso"
      gestorReservas.marcarReservaComoEnCurso(reserva, estadia);
    }

    // 8) PERSISTIR ESTADÍA (como agregado raíz)
    gestorEstadia.guardarEstadia(estadia);
  }
}
