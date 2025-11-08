package edu.inbugwethrust.premier.suite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;

public interface HuespedDAO extends JpaRepository<Huesped, Integer> {

	Optional<Huesped> findByTipoDocumentoAndNumeroDocumento(TipoDni tipo, String numeroDocumento);


}
