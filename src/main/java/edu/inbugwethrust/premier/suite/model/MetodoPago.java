package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "metodo_pago")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

}