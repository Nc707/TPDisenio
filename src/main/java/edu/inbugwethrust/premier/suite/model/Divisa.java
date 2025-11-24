package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;




@Entity
@Table(name = "divisa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Divisa extends Pago {

    private String cotizacion;

    private String moneda;
}
