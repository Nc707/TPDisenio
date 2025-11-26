package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "estadia")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor

public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadia; 

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;    

    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiciosAdicionales> serviciosAdicionales = new ArrayList<>();
}