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
@Table(name = "tipo_habitacion")
public class TipoHabitacion {
	@Id
	String nombre;
	Double costoNoche;
	int maximoHuespedes;
	
	@ElementCollection
    @CollectionTable(
	name = "tipo_habitacion_distribuciones",
	joinColumns = @JoinColumn(name = "tipo_habitacion_nombre")
	)
    @Column(name = "distribucion")
	String[] distribucionesPosibles;
}
