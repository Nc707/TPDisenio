package edu.inbugwethrust.premier.suite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.inbugwethrust.premier.suite.model.ExampleEntity;

@Repository
public interface ExampleEntityRepository extends JpaRepository<ExampleEntity, Integer> {

}
