package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona_juridica")
public class PersonaJuridica extends ResponsableDePago {

    @Id
    @Column(name = "cuit", length = 11, nullable = false, unique = true)
    private String cuit;

    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    @Column(length = 20)
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    private boolean estadoPersonaJuridica;
}