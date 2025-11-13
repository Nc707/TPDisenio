package edu.inbugwethrust.premier.suite.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;

@Repository
public interface HuespedDAO extends JpaRepository<Huesped, Integer>, QueryByExampleExecutor<Huesped> {

	Optional<Huesped> findByTipoDocumentoAndNumeroDocumento(TipoDni tipo, String numeroDocumento);

	List<Huesped> findByApellidoStartingWithIgnoreCase(String apellido);

    List<Huesped> findByNombresStartingWithIgnoreCase(String nombres);
    
	List<Huesped> findByApellidoStartingWithIgnoreCaseAndNombresStartingWithIgnoreCase(String apellido, String nombres);

}
