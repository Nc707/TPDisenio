package edu.inbugwethrust.premier.suite.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;

@Component
public class HuespedMapper implements IHuespedMapper {
	
	private DireccionMapper direccionMapper;
	
	@Autowired
	public HuespedMapper(DireccionMapper direccionMapper) {
		this.direccionMapper = direccionMapper;
	}

	@Override
	public HuespedDTO toDTO(Huesped entity) {
		if (entity == null) {
			return null;
		}
		HuespedDTO dto = new HuespedDTO();
		dto.setApellido(entity.getApellido());
		dto.setNombres(entity.getNombres());
		dto.setTipoDocumento(entity.getTipoDocumento());
		dto.setNumeroDocumento(entity.getNumeroDocumento());
		dto.setCuit(entity.getCuit());
		dto.setCategoriaFiscal(entity.getCategoriaFiscal());
		dto.setFechaNacimiento(entity.getFechaNacimiento());
		dto.setDireccion(direccionMapper.toDTO(entity.getDireccion()));
		dto.setTelefono(entity.getTelefono());
		dto.setEmail(entity.getEmail());
		dto.setOcupacion(entity.getOcupacion());
		dto.setNacionalidad(entity.getNacionalidad());
		return dto;
	}

	@Override
	public Huesped toEntity(HuespedDTO dto) {
		if (dto == null) {
			return null;
		}
		Huesped entity = new Huesped();
		entity.setApellido(dto.getApellido());
		entity.setNombres(dto.getNombres());
		entity.setTipoDocumento(dto.getTipoDocumento());
		entity.setNumeroDocumento(dto.getNumeroDocumento());
		entity.setCuit(dto.getCuit());
		entity.setCategoriaFiscal(dto.getCategoriaFiscal());
		entity.setFechaNacimiento(dto.getFechaNacimiento());
		entity.setDireccion(direccionMapper.toEntity(dto.getDireccion()));
		entity.setTelefono(dto.getTelefono());
		entity.setEmail(dto.getEmail());
		entity.setOcupacion(dto.getOcupacion());
		entity.setNacionalidad(dto.getNacionalidad());
		return entity;
	}

}
