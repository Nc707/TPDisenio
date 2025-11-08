package edu.inbugwethrust.premier.suite.mappers;

import edu.inbugwethrust.premier.suite.dto.DireccionDTO;
import edu.inbugwethrust.premier.suite.model.Direccion;

public interface IDirecionMapper extends IMapper<DireccionDTO, Direccion> {
	
	DireccionDTO toDTO(Direccion entity);
	
	Direccion toEntity(DireccionDTO dto);
}
