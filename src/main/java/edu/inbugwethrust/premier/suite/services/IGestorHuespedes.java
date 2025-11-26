package edu.inbugwethrust.premier.suite.services;

import java.util.List;

import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.ObtenerHuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;

public interface IGestorHuespedes {

    public HuespedDTO dar_alta_huesped(HuespedDTO dto);

    public HuespedDTO dar_alta_huesped_forzar(HuespedDTO dto);
    
    public HuespedDTO buscarPorId(ObtenerHuespedDTO id);

    public List<HuespedDTO> buscar_huespedes(BusquedaHuespedDTO busqueda);
    
    /**
     * Obtiene el huésped a partir de un identificador numérico
     * que interpretamos como su número de documento (DNI).
     */
    Huesped obtenerPorId(Long idDocumento);

    /**
     * Obtiene una lista de huéspedes a partir de sus números de documento (DNI).
     */
    List<Huesped> obtenerPorIds(List<Long> idsDocumento);
}
