package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
 
@Entity
@Table(name = "cheque")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cheque extends MetodoPago {

    @Id
    private String numero;

    private LocalDate fechaCobro;

    private String banco;

    private float monto;

    @Enumerated(EnumType.STRING)
    private TipoCheque tipo;

    @Enumerated(EnumType.STRING)
    private  EstadoCheque estadoCheque;

    private String plazo;
}