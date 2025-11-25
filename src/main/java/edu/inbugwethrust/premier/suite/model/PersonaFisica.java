package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;



@Entity
@Table(name = "persona_fisica")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PersonaFisica extends ResponsableDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonaFisica;

    private boolean estadoPersonaFisica;
}