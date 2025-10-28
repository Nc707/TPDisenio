package edu.inbugwethrust.premier.suite.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.dto.ExampleDTO;
import edu.inbugwethrust.premier.suite.model.ExampleEntity;
import edu.inbugwethrust.premier.suite.repositories.ExampleEntityRepository;

@Service
public class ExampleEntityService {
	
	final ExampleEntityRepository exampleEntityRepo;
	
	@Autowired
	public ExampleEntityService(ExampleEntityRepository repo){
		this.exampleEntityRepo = repo;
		
	}
	
	
	public int saveExampleEntity(ExampleDTO dto) {
		ExampleEntity entity = new ExampleEntity(dto.getNumber(), dto.getAnotherNumber());
		entity = this.exampleEntityRepo.save(entity);
		return entity.getId();
	}
	
	public ExampleDTO getExampleEntity(int id) throws NoSuchElementException{
		Optional<ExampleEntity> maybeEntity = this.exampleEntityRepo.findById(id);
		ExampleEntity entity = maybeEntity.get();
		return new ExampleDTO(entity.getNumber(), entity.getAnotherNumber());
	}
}
