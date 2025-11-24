package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCheckin;

    private LocalDateTime fechaCheckout;

    @ManyToOne(optional = true)
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;
}
