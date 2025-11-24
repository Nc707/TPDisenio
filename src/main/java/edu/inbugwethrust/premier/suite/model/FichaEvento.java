package edu.inbugwethrust.premier.suite.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ficha_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FichaEvento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int idFichaEventos;
	@Enumerated(EnumType.STRING) 
	EstadoHabitacion estado;

	LocalDateTime fechaInicio;
	LocalDateTime fechaFin;
	
	@Column(length = 500)
	String descripcion;
	
	boolean cancelado;
	
	@ManyToOne
    @JoinColumn(name = "habitacion_id")
	Habitacion habitacion; 

	@ManyToOne
	@JoinColumn(name = "reserva_id")
	private Reserva reserva;
}
