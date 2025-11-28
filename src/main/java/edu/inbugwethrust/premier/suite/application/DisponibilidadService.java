package edu.inbugwethrust.premier.suite.application;

import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.CalendarioDisponibilidadDTO;
import edu.inbugwethrust.premier.suite.dto.DisponibilidadHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.EstadoHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.HabitacionResumidaDTO;
import edu.inbugwethrust.premier.suite.mappers.HabitacionMapper; // <-- Importante
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.services.IGestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.IGestorHabitaciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // <-- Añade @Service

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DisponibilidadService implements IDisponibilidadService {

  private final IGestorHabitaciones gestorHabitaciones;
  private final IGestorFichaEvento gestorFichaEvento;
  private final HabitacionMapper habitacionMapper;

  @AllArgsConstructor
  @Getter
  private static class Celda {
    private Integer nroHabitacion;
    private LocalDate fecha;
    private EstadoHabitacionDTO detalle;
  }

  @Autowired
  public DisponibilidadService(IGestorHabitaciones gestorHabitaciones,
      IGestorFichaEvento gestorFichaEvento, HabitacionMapper habitacionMapper) {
    this.gestorHabitaciones = gestorHabitaciones;
    this.gestorFichaEvento = gestorFichaEvento;
    this.habitacionMapper = habitacionMapper;
  }

  /**
   * Construye el modelo completo para la vista de la grilla de disponibilidad.
   * 
   * @param busquedaDTO DTO que contiene las fechas de la búsqueda.
   * @return Un DTO que contiene toda la información necesaria para renderizar la grilla.
   */
  public CalendarioDisponibilidadDTO consultarDisponibilidad(BusquedaHabitacionDTO busquedaDTO) {


    if (busquedaDTO == null || busquedaDTO.getFechaInicio() == null
        || busquedaDTO.getFechaFin() == null) {

      // Si no hay fechas, devolvemos un calendario vacío.
      return new CalendarioDisponibilidadDTO(new BusquedaHabitacionDTO(), Collections.emptyList(),
          Collections.emptyMap(), Collections.emptyMap());
    }

    if (busquedaDTO.getFechaFin().isBefore(busquedaDTO.getFechaInicio())) {

      return new CalendarioDisponibilidadDTO(busquedaDTO, Collections.emptyList(),
          Collections.emptyMap(), Collections.emptyMap());
    }
    List<LocalDate> fechas = construirFechas(busquedaDTO);
    List<Habitacion> habitaciones = gestorHabitaciones.obtenerHabitaciones();
    List<FichaEvento> fichasEventos = gestorFichaEvento
        .obtenerFichasEventoPorFechas(busquedaDTO.getFechaInicio(), busquedaDTO.getFechaFin());

    // Procesamiento de Datos
    // Agrupamos las fichas por habitación UNA SOLA VEZ para un rendimiento óptimo.
    Map<Integer, List<FichaEvento>> fichasPorHabitacion = fichasEventos.stream()
        .collect(Collectors.groupingBy(ficha -> ficha.getHabitacion().getNumero()));

    // Generamos la lista plana de estados, que es un paso intermedio.
    List<Celda> disponibilidadPlana =
        fechas.stream().flatMap(fecha -> habitaciones.stream().map(habitacion -> {

          List<FichaEvento> eventosDeLaHabitacion =
              fichasPorHabitacion.getOrDefault(habitacion.getNumero(), Collections.emptyList());

          // Calculamos el objeto complejo para este día
          EstadoHabitacionDTO estadoDTO = calcularEstadoDia(eventosDeLaHabitacion, fecha);

          return new Celda(habitacion.getNumero(), fecha, estadoDTO);
        })).toList();

    // Ordenamiento y agrupación de habitaciones (Tu lógica original intacta)
    habitaciones.sort(Comparator.comparing((Habitacion h) -> h.getTipoHabitacion().getNombre())
        .thenComparing(Habitacion::getNumero));

    List<HabitacionResumidaDTO> habitacionesResumidas =
        habitaciones.stream().map(habitacionMapper::toResumidoDTO).collect(Collectors.toList());

    Map<String, List<HabitacionResumidaDTO>> mapaAgrupado =
        habitacionesResumidas.stream().collect(Collectors.groupingBy(
            HabitacionResumidaDTO::getTipoHabitacion, LinkedHashMap::new, Collectors.toList()));

    Map<LocalDate, Map<Integer, EstadoHabitacionDTO>> mapaDisponibilidad =
        disponibilidadPlana.stream().collect(Collectors.groupingBy(Celda::getFecha,
            Collectors.toMap(Celda::getNroHabitacion, Celda::getDetalle)));

    CalendarioDisponibilidadDTO calendarioDTO =
        new CalendarioDisponibilidadDTO(busquedaDTO, fechas, mapaAgrupado, mapaDisponibilidad);
    log.debug("Calendario de disponibilidad construido: {}", calendarioDTO);
    return calendarioDTO;
  }

  /**
   * Método helper que determina cuál es el estado prioritario para una fecha y extrae la
   * información del huésped responsable si corresponde.
   */
  private EstadoHabitacionDTO calcularEstadoDia(List<FichaEvento> eventos, LocalDate fecha) {

    // 1. Encontrar la ficha dominante (Ej: Una OCUPACION tapa a una RESERVA si hubiera conflicto,
    // o simplemente busca si hay algo ese día).
    FichaEvento fichaGanadora = eventos.stream().filter(ficha -> {
      LocalDate inicio = ficha.getFechaInicio().toLocalDate();
      LocalDate fin = ficha.getFechaFin().toLocalDate();
      // "fecha" debe estar dentro del rango [inicio, fin) o [inicio, fin] según tu regla de
      // negocio.
      // Usualmente checkout es a la mañana, así que < finFicha es correcto para pernocte.
      return !fecha.isBefore(inicio) && fecha.isBefore(fin);
    })
        // Comparamos por el estado natural (Ordinal) o tu lógica de prioridad
        .max(Comparator.comparing(FichaEvento::getEstado)).orElse(null);

    // 2. Si no hay ficha, está LIBRE
    if (fichaGanadora == null) {
      // Nota: Asumo que LIBRE tiene un ID o usamos 0. Ajusta según tu Enum.
      return new EstadoHabitacionDTO(EstadoHabitacion.LIBRE, null, null,
          EstadoHabitacion.LIBRE.ordinal());
    }

    // 3. Si hay ficha, extraemos datos
    String nombre = null;
    String apellido = null;

    // Validamos nulos para evitar NullPointerException si es un bloqueo administrativo sin reserva
    if (fichaGanadora.getReserva() != null) {
      nombre = fichaGanadora.getReserva().getNombreReserva();
      apellido = fichaGanadora.getReserva().getApellidoReserva();
      
      //TODO: modificar y poner el responsable cuando se agregue a la fichaEvento
    } else if (fichaGanadora.getAcompanantes().getFirst() != null) {
      Huesped responsable = fichaGanadora.getAcompanantes().getFirst();
      nombre = responsable.getNombres();
      apellido = responsable.getApellido();
    }

    // Retornamos el DTO lleno
    return new EstadoHabitacionDTO(fichaGanadora.getEstado(), nombre, apellido,
        fichaGanadora.getEstado().ordinal() // O fichaGanadora.getEstado().getId() si tu enum lo
                                            // tiene
    );
  }

  private List<LocalDate> construirFechas(BusquedaHabitacionDTO busquedaHabitacionDTO) {
    return busquedaHabitacionDTO.getFechaInicio()
        .datesUntil(busquedaHabitacionDTO.getFechaFin().plusDays(1)).toList();
  }
}
