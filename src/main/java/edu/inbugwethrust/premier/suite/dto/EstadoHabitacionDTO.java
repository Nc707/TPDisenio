package edu.inbugwethrust.premier.suite.dto;

import edu.inbugwethrust.premier.suite.model.EstadoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoHabitacionDTO {
  EstadoHabitacion estado;
  String NombresResponsable;
  String ApellidosResponsable;
  int idEstado;
}
