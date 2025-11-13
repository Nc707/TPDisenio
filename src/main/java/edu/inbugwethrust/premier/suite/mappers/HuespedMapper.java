package edu.inbugwethrust.premier.suite.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;

@Mapper(
		componentModel = "spring",
		uses = { DireccionMapper.class },
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HuespedMapper {
	
	public HuespedDTO toDTO(Huesped entity);
	public Huesped toEntity(HuespedDTO dto);
	
	
	public Huesped toEntityBusqueda(BusquedaHuespedDTO dto);

}
