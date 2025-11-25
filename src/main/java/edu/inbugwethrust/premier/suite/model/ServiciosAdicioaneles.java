package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicios_adicionales")
public class ServiciosAdicioaneles {

    @Id
    @Column(name = "numero_habitacion", length = 11, nullable = false, unique = true)
    private int numeroHabitacion;
    
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estadoHabitacion;

    
    private int numeroPiso;


    
}
