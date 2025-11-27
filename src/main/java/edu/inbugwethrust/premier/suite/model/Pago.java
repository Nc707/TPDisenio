package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "pago")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private Double importe;

    private Double vuelto;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;
}