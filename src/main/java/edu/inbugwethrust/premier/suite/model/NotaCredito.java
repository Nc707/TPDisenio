package edu.inbugwethrust.premier.suite.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nota_credito")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NotaCredito {
 @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idNotaCredito;

private float IVA;

private float importeTotal;

@Enumerated(EnumType.STRING)
private EstadoNotaCredito estadoNotaCredito;

 @ManyToOne
 @JoinColumn(name = "id_factura", nullable = false)
 private Factura factura;

}
