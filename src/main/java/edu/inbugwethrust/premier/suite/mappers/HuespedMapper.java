package edu.inbugwethrust.premier.suite.mappers;

import org.mapstruct.Mapper;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;

@Mapper(
		componentModel = "spring",
		uses = { DireccionMapper.class }
)
public interface HuespedMapper {
	
	public HuespedDTO toDTO(Huesped entity);
	public Huesped toEntity(HuespedDTO dto);

}
