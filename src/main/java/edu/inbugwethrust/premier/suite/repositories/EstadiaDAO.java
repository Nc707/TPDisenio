package edu.inbugwethrust.premier.suite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.inbugwethrust.premier.suite.model.Estadia;

/**
 * DAO de Estadia basado en Spring Data JPA.
 *
 * Proporciona operaciones CRUD básicas:
 *  - save(estadía)
 *  - findById(id)
 *  - findAll()
 *  - deleteById(id)
 *  - etc.
 */
@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {

}
