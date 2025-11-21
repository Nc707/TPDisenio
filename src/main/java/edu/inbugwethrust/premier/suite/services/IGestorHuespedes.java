package edu.inbugwethrust.premier.suite.services;

import java.util.List;

import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.ObtenerHuespedDTO;

public interface IGestorHuespedes {

    public HuespedDTO dar_alta_huesped(HuespedDTO dto);

    public HuespedDTO dar_alta_huesped_forzar(HuespedDTO dto);
    
    public HuespedDTO buscarPorId(ObtenerHuespedDTO id);

    public List<HuespedDTO> buscar_huespedes(BusquedaHuespedDTO busqueda);
    
    // otros métodos del diagrama que no implementamos ahora:
    // void modificar_huesped(...);
    // void baja_huesped(...);
}
