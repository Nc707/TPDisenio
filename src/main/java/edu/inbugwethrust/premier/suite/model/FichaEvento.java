package edu.inbugwethrust.premier.suite.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

	/**
     * Estadía asociada a este evento (para eventos de tipo OCUPACION).
     */
	@ManyToOne
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;

    /**
     * Huésped responsable de esta habitación en este evento (para OCUPACION).
     */
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "huesped_resp_tipo_doc", referencedColumnName = "tipoDocumento"),
        @JoinColumn(name = "huesped_resp_num_doc",  referencedColumnName = "numeroDocumento")
    })
    private Huesped huespedResponsable;

    /**
     * Lista de acompañantes de la habitación durante este evento (OCUPACION).
     */
    @ManyToMany
    @JoinTable(
        name = "ficha_evento_acompanante",
        joinColumns = @JoinColumn(name = "ficha_evento_id"),  // FK a FichaEvento (su PK es simple)
        inverseJoinColumns = {
            @JoinColumn(name = "huesped_tipo_doc", referencedColumnName = "tipoDocumento"),
            @JoinColumn(name = "huesped_num_doc",  referencedColumnName = "numeroDocumento")
        }
    )
    private List<Huesped> acompanantes = new ArrayList<>();
}
