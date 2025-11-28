package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicios_adicionales")
public class ServiciosAdicionales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion; 

    private Double precio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estadia", nullable = false)
    private Estadia estadia;
}
