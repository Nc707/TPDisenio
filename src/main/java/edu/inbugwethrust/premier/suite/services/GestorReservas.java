package edu.inbugwethrust.premier.suite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.EstadoReserva;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.repositories.ReservaDAO;

@Service
public class GestorReservas {

    private final ReservaDAO reservaDAO;

    @Autowired
    public GestorReservas(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    /**
     * Crea una nueva Reserva en estado ACTIVA, seteando los datos del huésped
     * directamente sobre la entidad.
     *
     * NO persiste: solo devuelve la entidad "viva" para que el caso de uso
     * siga completando la información (fichas, etc.).
     */
    public Reserva crearNuevaReserva(String apellido, String nombre, String telefono) {
        Reserva reserva = new Reserva();
        reserva.setEstadoReserva(EstadoReserva.ACTIVA);

        // Normalizamos a mayúsculas para cumplir con el requerimiento de datos literales
        if (apellido != null) {
            reserva.setApellidoReserva(apellido.toUpperCase());
        }
        if (nombre != null) {
            reserva.setNombreReserva(nombre.toUpperCase());
        }
        if (telefono != null) {
            reserva.setTelefonoReserva(telefono);
        }

        return reserva;
    }

    /**
     * Persiste la reserva (agregado raíz). Por cascade en listaFichaEventos se
     * guardan también las FichaEvento asociadas.
     */
    public Reserva guardar(Reserva reserva) {
        return reservaDAO.save(reserva);
    }
}
