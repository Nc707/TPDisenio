package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "divisa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Divisa  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String numero;
    
    private String cotizacion;

    private String moneda;

    

}
