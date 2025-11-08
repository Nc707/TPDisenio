package edu.inbugwethrust.premier.suite.mappers;

import org.springframework.stereotype.Component;

import edu.inbugwethrust.premier.suite.dto.DireccionDTO;
import edu.inbugwethrust.premier.suite.model.Direccion;

@Component
public class DireccionMapper implements IDirecionMapper {

	@Override
	public DireccionDTO toDTO(Direccion entity) {
		if(entity == null) {
			return null;
		}
		DireccionDTO dto = new DireccionDTO();
		dto.setCalle(entity.getCalle());
		dto.setCodigoPostal(entity.getCodigoPostal());
		dto.setDepartamento(entity.getDepartamento());
		dto.setLocalidad(entity.getLocalidad());
		dto.setNumero(entity.getNumero());
		dto.setPiso(entity.getPiso());
		dto.setProvincia(entity.getProvincia());
		return dto;
		
	}

	@Override
	public Direccion toEntity(DireccionDTO dto) {
		if(dto == null) {
			return null;
		}
		Direccion entity = new Direccion();
		entity.setCalle(dto.getCalle());
		entity.setCodigoPostal(dto.getCodigoPostal());
		entity.setDepartamento(dto.getDepartamento());
		entity.setLocalidad(dto.getLocalidad());
		entity.setNumero(dto.getNumero());
		entity.setPiso(dto.getPiso());
		entity.setProvincia(dto.getProvincia());
		return entity;
	}

}
