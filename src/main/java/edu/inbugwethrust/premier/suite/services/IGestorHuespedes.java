package edu.inbugwethrust.premier.suite.services;

import java.util.List;
import java.util.Map;
import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.IdentificacionHuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.HuespedID;

public interface IGestorHuespedes {

    public HuespedDTO dar_alta_huesped(HuespedDTO dto);

    public HuespedDTO dar_alta_huesped_forzar(HuespedDTO dto);
    
    public HuespedDTO buscarPorId(IdentificacionHuespedDTO id);

    public List<HuespedDTO> buscar_huespedes(BusquedaHuespedDTO busqueda);
    
    public Huesped obtenerPorId(IdentificacionHuespedDTO id);
    
    public Map<HuespedID, Huesped> obtenerMapaPorIds(List<IdentificacionHuespedDTO> idsHuesped);
    
}
