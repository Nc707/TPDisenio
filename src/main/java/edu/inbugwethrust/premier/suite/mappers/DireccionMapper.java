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
		dto.setNumero(entity.getNumero());
		dto.setPiso(entity.getPiso());
		dto.setDepartamento(entity.getDepartamento());
		dto.setCodigoPostal(entity.getCodigoPostal());
		dto.setLocalidad(entity.getLocalidad());
		dto.setProvincia(entity.getProvincia());
		dto.setPais(entity.getPais());
		return dto;
		
	}

	@Override
	public Direccion toEntity(DireccionDTO dto) {
		if(dto == null) {
			return null;
		}
		Direccion entity = new Direccion();
		entity.setCalle(dto.getCalle());
		entity.setNumero(dto.getNumero());
		entity.setPiso(dto.getPiso());
		entity.setDepartamento(dto.getDepartamento());
		entity.setCodigoPostal(dto.getCodigoPostal());
		entity.setLocalidad(dto.getLocalidad());
		entity.setProvincia(dto.getProvincia());
		entity.setPais(dto.getPais());
		return entity;
	}

}
