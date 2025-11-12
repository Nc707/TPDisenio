package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;
import java.util.List;

import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;

public interface IGestorHuespedes {

    Huesped dar_alta_huesped(HuespedDTO dto);

    Huesped dar_alta_huesped_forzar(HuespedDTO dto);

    Optional<Huesped> buscar_por_doc(TipoDni tipo, String numeroDocumento);

    List <Huesped> buscar_huespedes(BusquedaHuespedDTO busqueda);

    // otros métodos del diagrama que no implementamos ahora:
    // void modificar_huesped(...);
    // void baja_huesped(...);
}
