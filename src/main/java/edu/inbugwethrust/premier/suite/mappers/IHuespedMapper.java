package edu.inbugwethrust.premier.suite.mappers;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;

public interface IHuespedMapper extends IMapper<HuespedDTO, Huesped> {

		HuespedDTO toDTO(Huesped entity);
		
		Huesped toEntity(HuespedDTO dto);
}
