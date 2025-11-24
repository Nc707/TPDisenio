package edu.inbugwethrust.premier.suite.repositories;
import edu.inbugwethrust.premier.suite.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Long> {
}
