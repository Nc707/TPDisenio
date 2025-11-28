package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "habitacion")
public class Habitacion {
	
	@Id
	int numero;
	int piso;

	@ManyToOne
    @JoinColumn(name = "tipo_habitacion_id")
	TipoHabitacion tipoHabitacion;
}
