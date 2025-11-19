package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalendarioDisponibilidadDTO {
  private BusquedaHabitacionDTO busquedaDTO;
  private List<LocalDate> fechas;
  private Map<String, List<HabitacionResumidaDTO>> habitacionesAgrupadas;
  private Map<LocalDate, Map<Integer, EstadoHabitacion>> mapaDisponibilidad;
}
