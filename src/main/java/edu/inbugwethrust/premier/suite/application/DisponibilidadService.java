package edu.inbugwethrust.premier.suite.application;

import edu.inbugwethrust.premier.suite.dto.BusquedaHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.CalendarioDisponibilidadDTO;
import edu.inbugwethrust.premier.suite.dto.DisponibilidadHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.HabitacionResumidaDTO;
import edu.inbugwethrust.premier.suite.mappers.HabitacionMapper; // <-- Importante
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.services.IGestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.IGestorHabitaciones;
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

  @Autowired
  public DisponibilidadService(IGestorHabitaciones gestorHabitaciones, 
                               IGestorFichaEvento gestorFichaEvento,
                               HabitacionMapper habitacionMapper) {
      this.gestorHabitaciones = gestorHabitaciones;
      this.gestorFichaEvento = gestorFichaEvento;
      this.habitacionMapper = habitacionMapper;
  }

  /**
   * Construye el modelo completo para la vista de la grilla de disponibilidad.
   * @param busquedaDTO DTO que contiene las fechas de la búsqueda.
   * @return Un DTO que contiene toda la información necesaria para renderizar la grilla.
   */
  public CalendarioDisponibilidadDTO consultarDisponibilidad(BusquedaHabitacionDTO busquedaDTO) {      


    if (busquedaDTO == null || 
        busquedaDTO.getFechaInicio() == null || 
        busquedaDTO.getFechaFin() == null) {
        
        // Si no hay fechas, devolvemos un calendario vacío.
        return new CalendarioDisponibilidadDTO(
            new BusquedaHabitacionDTO(), 
            Collections.emptyList(), 
            Collections.emptyMap(), 
            Collections.emptyMap()
        );
    }

    if (busquedaDTO.getFechaFin().isBefore(busquedaDTO.getFechaInicio())) {   

        return new CalendarioDisponibilidadDTO(
            busquedaDTO, 
            Collections.emptyList(), 
            Collections.emptyMap(), 
            Collections.emptyMap()
        );
    }
    List<LocalDate> fechas = construirFechas(busquedaDTO);
    List<Habitacion> habitaciones = gestorHabitaciones.obtenerHabitaciones();
    List<FichaEvento> fichasEventos = gestorFichaEvento.obtenerFichasEventoPorFechas(
            busquedaDTO.getFechaInicio(), busquedaDTO.getFechaFin());
    
    // Procesamiento de Datos

    // Agrupamos las fichas por habitación UNA SOLA VEZ para un rendimiento óptimo.
    Map<Integer, List<FichaEvento>> fichasPorHabitacion = fichasEventos.stream()
            .collect(Collectors.groupingBy(ficha -> ficha.getHabitacion().getNumero()));

    // Generamos la lista plana de estados, que es un paso intermedio.
    List<DisponibilidadHabitacionDTO> disponibilidadPlana = fechas.stream().flatMap(fecha ->
        habitaciones.stream().map(habitacion -> {
            
            List<FichaEvento> eventosDeLaHabitacion = fichasPorHabitacion.getOrDefault(
                    habitacion.getNumero(), Collections.emptyList());

            EstadoHabitacion estadoDelDia = eventosDeLaHabitacion.stream()
                    .filter(ficha -> { // La única validación necesaria aquí es la de la fecha
                        LocalDate inicioFicha = ficha.getFechaInicio().toLocalDate();
                        LocalDate finFicha = ficha.getFechaFin().toLocalDate();
                        return !fecha.isBefore(inicioFicha) && fecha.isBefore(finFicha);
                    })
                    .map(FichaEvento::getEstado)
                    .max(Comparator.naturalOrder()) // Tu lógica para resolver superposiciones
                    .orElse(EstadoHabitacion.LIBRE);

            return new DisponibilidadHabitacionDTO(habitacion.getNumero(), fecha, estadoDelDia);
        })
    ).toList();

      
      habitaciones.sort(Comparator
          .comparing((Habitacion h) -> h.getTipoHabitacion().getNombre())
          .thenComparing(Habitacion::getNumero)
      );

      // 3b. Mapeamos las entidades a DTOs (la lista sigue ordenada)
      List<HabitacionResumidaDTO> habitacionesResumidas = habitaciones.stream()
              .map(habitacionMapper::toResumidoDTO)
              .collect(Collectors.toList());

      // 3c. ¡NUEVO! Agrupamos la lista ordenada en un LinkedHashMap
      // El LinkedHashMap mantiene el orden de inserción, por eso ordenamos primero.
      Map<String, List<HabitacionResumidaDTO>> mapaAgrupado = habitacionesResumidas.stream()
              .collect(Collectors.groupingBy(
                  HabitacionResumidaDTO::getTipoHabitacion, // Clave = "Doble Superior"
                  LinkedHashMap::new,                       // Queremos un mapa ordenado
                  Collectors.toList()                       // Valor = Lista de Habitaciones
              ));

    // Convertimos la lista plana en el mapa anidado que la vista necesita.
    // Esto es mucho más eficiente para Thymeleaf.
    Map<LocalDate, Map<Integer, EstadoHabitacion>> mapaDisponibilidad = disponibilidadPlana.stream()
            .collect(Collectors.groupingBy(
                DisponibilidadHabitacionDTO::getFecha, // Agrupa por fecha (clave del mapa exterior)
                Collectors.toMap(
                    DisponibilidadHabitacionDTO::getNumeroHabitacion, // Clave del mapa interior
                    DisponibilidadHabitacionDTO::getEstado)          // Valor del mapa interior
            ));
    

      CalendarioDisponibilidadDTO calendarioDTO = new CalendarioDisponibilidadDTO(
          busquedaDTO, 
          fechas, 
          mapaAgrupado,
          mapaDisponibilidad);
      log.debug("Calendario de disponibilidad construido: {}", calendarioDTO);
      return calendarioDTO;
  }

    private List<LocalDate> construirFechas(BusquedaHabitacionDTO busquedaHabitacionDTO) {
        return busquedaHabitacionDTO.getFechaInicio().datesUntil(busquedaHabitacionDTO.getFechaFin().plusDays(1)).toList();
    }
}