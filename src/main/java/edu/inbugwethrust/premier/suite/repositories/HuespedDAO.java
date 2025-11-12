package edu.inbugwethrust.premier.suite.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;

@Repository
public interface HuespedDAO extends JpaRepository<Huesped, Integer> {

	Optional<Huesped> findByTipoDocumentoAndNumeroDocumento(TipoDni tipo, String numeroDocumento);

	List<Huesped> findByApellidoStartingWithIgnoreCase(String apellido);

    List<Huesped> findByNombresStartingWithIgnoreCase(String nombres);
    
	List<Huesped> findByApellidoStartingWithIgnoreCaseAndNombresStartingWithIgnoreCase(String apellido, String nombres);

	@Query("""
        SELECT h FROM Huesped h
        WHERE (:apellido IS NULL OR UPPER(h.apellido) LIKE CONCAT(:apellido, '%'))
          AND (:nombres IS NULL OR UPPER(h.nombres) LIKE CONCAT(:nombres, '%'))
          AND (:tipoDocumento IS NULL OR h.tipoDocumento = :tipoDocumento)
          AND (:numeroDocumento IS NULL OR h.numeroDocumento = :numeroDocumento)
    """)
    List<Huesped> buscarHuespedes(
            @Param("apellido") String apellido,
            @Param("nombres") String nombres,
            @Param("tipoDocumento") TipoDni tipoDocumento,
            @Param("numeroDocumento") String numeroDocumento
    );
}
