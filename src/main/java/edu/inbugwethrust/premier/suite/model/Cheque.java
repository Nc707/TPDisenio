package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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