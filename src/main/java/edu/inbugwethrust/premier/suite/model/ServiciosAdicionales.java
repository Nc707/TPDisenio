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

    private String descripcion; // Ej: "Cena Romántica", "Lavandería"

    private Double precio;

@ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;
}
