package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cheque")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cheque extends Pago {

    private String numero;

    private LocalDate fechaCobro;

    private String banco;

    private String tipo;
}