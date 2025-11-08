package edu.inbugwethrust.premier.suite.mappers;

public interface IMapper<D, E> {
	D toDTO(E entity);
	
	E toEntity(D dto);

}
