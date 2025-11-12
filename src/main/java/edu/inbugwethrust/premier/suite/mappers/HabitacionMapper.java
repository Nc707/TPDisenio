package edu.inbugwethrust.premier.suite.mappers;

import edu.inbugwethrust.premier.suite.dto.HabitacionResumidaDTO;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Lo hace un Spring Bean
public interface HabitacionMapper {

    /**
     * Convierte la entidad Habitacion a su DTO resumido (para la grilla).
     */
    @Mapping(source = "numero", target = "numeroHabitacion")
    @Mapping(source = "tipoHabitacion.nombre", target = "tipoHabitacion")
    HabitacionResumidaDTO toResumidoDTO(Habitacion habitacion);
    
    /* * Si tuvieras un DTO completo (HabitacionDTO), 
     * los otros métodos de conversión irían AQUÍ MISMO:
     *
     * HabitacionDTO toDTO(Habitacion habitacion);
     * Habitacion toEntity(HabitacionDTO dto);
     */
}