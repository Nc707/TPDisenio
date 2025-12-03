package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "estadia")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor

public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadia; 

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = true, unique = true)
    private Reserva reserva;    

    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiciosAdicionales> serviciosAdicionales = new ArrayList<>();

    /**
     * Fichas de evento de tipo OCUPACION asociadas a esta estadía.
     */
    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichaEvento> listaFichaEventos = new ArrayList<>();

    // Podrías agregar más datos globales de la estadía si los necesitás:
    // fechaInicioEstadia, fechaFinEstadia, estadoEstadia, etc.

    public void agregarFichaEvento(FichaEvento ficha) {
        listaFichaEventos.add(ficha);
        ficha.setEstadia(this);
    }

    public void quitarFichaEvento(FichaEvento ficha) {
        listaFichaEventos.remove(ficha);
        ficha.setEstadia(null);
    }
}