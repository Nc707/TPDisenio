package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private float importe;

    private float vuelto;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private  EstadoPago estadoPago;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private  Factura factura;
    
    @ManyToOne 
    @JoinColumn(name = "divisa_numero") 
    private Divisa divisa;

    @ManyToOne
    @JoinColumn(name = "numero_tarjeta") 
    private Tarjeta tarjeta;

    @OneToOne
    @JoinColumn(name = "numero_cheque")
    private Cheque cheque;
    
    @Transient
    private MetodoDePago metodoDePago; 
}





