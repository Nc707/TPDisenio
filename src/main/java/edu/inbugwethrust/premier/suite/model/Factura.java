package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;


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

    @OneToOne(optional = false)
    @JoinColumn(name = "id_responsable", nullable = false, unique = true)
    private ResponsableDePago responsable;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();
}