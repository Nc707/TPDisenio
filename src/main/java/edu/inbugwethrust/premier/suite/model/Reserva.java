package edu.inbugwethrust.premier.suite.model;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReserva;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<FichaEvento> listaFichaEventos;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Estadia estadia;

}
