package edu.inbugwethrust.premier.suite.model;
import edu.inbugwethrust.premier.suite.model.EstadoReserva;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Estadia;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;


    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<FichaEvento> listaFichaEventos = new ArrayList<>();

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Estadia estadia;

}
