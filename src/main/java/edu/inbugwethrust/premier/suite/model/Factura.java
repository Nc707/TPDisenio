package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "factura")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    private LocalDate fechaEmision;

    private Double importeTotal;

    // TODO: relación con responsable de pago (PersonaFisica / PersonaJuridica)
    // Por ahora se comenta para no romper el arranque de la app.
    //
    // @OneToOne(optional = false)
    // @JoinColumn(name = "id_responsable", nullable = false, unique = true)
    // private ResponsableDePago responsable;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();
}