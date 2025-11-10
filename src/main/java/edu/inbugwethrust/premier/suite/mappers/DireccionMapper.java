package edu.inbugwethrust.premier.suite.mappers;

import org.mapstruct.Mapper;

import edu.inbugwethrust.premier.suite.dto.DireccionDTO;
import edu.inbugwethrust.premier.suite.model.Direccion;

@Mapper(componentModel = "spring")
public interface DireccionMapper{

	
	public DireccionDTO toDTO(Direccion entity);
	public Direccion toEntity(DireccionDTO dto);

}
