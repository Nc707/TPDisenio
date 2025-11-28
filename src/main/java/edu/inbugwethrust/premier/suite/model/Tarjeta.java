package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tarjeta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Tarjeta extends MetodoPago{

    @Id
    private String numero;

    private String banco;

    private String tipo;
}