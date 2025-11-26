package edu.inbugwethrust.premier.suite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.repositories.EstadiaDAO;

/**
 * Gestor de dominio para la entidad Estadia.
 * Se encarga de persistir y recuperar estadías.
 */
@Service
public class GestorEstadia {

    private final EstadiaDAO estadiaDAO;

    @Autowired
    public GestorEstadia(EstadiaDAO estadiaDAO) {
        this.estadiaDAO = estadiaDAO;
    }

    /**
     * Guarda la estadía como agregado raíz.
     * Por cascade se persisten también las FichaEvento asociadas.
     */
    public Estadia guardar(Estadia estadia) {
        return estadiaDAO.save(estadia);
    }

    /**
     * Obtiene una estadía por id o lanza IllegalArgumentException si no existe.
     */
    public Estadia obtenerPorId(Long id) {
        return estadiaDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la estadía con id: " + id));
    }
}
