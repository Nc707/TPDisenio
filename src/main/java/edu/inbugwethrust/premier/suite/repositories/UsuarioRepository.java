package edu.inbugwethrust.premier.suite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.inbugwethrust.premier.suite.model.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

}
