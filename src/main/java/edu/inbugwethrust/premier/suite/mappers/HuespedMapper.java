package edu.inbugwethrust.premier.suite.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.IdentificacionHuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;

@Mapper(
		componentModel = "spring",
		uses = { DireccionMapper.class },
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HuespedMapper {
	
	public HuespedDTO toDTO(Huesped entity);
	public Huesped toEntity(HuespedDTO dto);
	
	
	public Huesped toEntityBusqueda(BusquedaHuespedDTO dto);
	
	public HuespedID toId(IdentificacionHuespedDTO dto);
	
	List<HuespedID> toIdList(List<IdentificacionHuespedDTO> dtos);

}
