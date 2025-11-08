package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.mappers.HuespedMapper;
import edu.inbugwethrust.premier.suite.model.CategoriaFiscal;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;
import edu.inbugwethrust.premier.suite.repositories.HuespedDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.HuespedDuplicadoException;
import edu.inbugwethrust.premier.suite.validators.HuespedValidator;

@Service
public class GestorHuespedes implements IGestorHuespedes {

    private final HuespedDAO huespedDAO;
    private final HuespedValidator validator;
    private final HuespedMapper huespedMapper;

    @Autowired
    public GestorHuespedes(HuespedDAO huespedDAO, HuespedValidator validator, HuespedMapper mapper) {
        this.huespedDAO = huespedDAO;
        this.validator = validator;
        this.huespedMapper = mapper;
    }

    @Override
    public Huesped dar_alta_huesped(HuespedDTO dto) {
        // 1) validar obligatorios (flujo 2.A)
        validator.validarDatosObligatorios(dto);
        // 2) chequear si ya existe por tipo + número (flujo 2.B)
        Optional<Huesped> existente = buscar_por_doc(dto.getTipoDocumento(), dto.getNumeroDocumento());
        if (existente.isPresent()) {
            // acá en el CU muestran un cartel y dan a elegir.
            // en backend lo representamos con una excepción específica
            throw new HuespedDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema");
        }

        // 3) completar valores por omisión (posición frente al IVA)
        if (dto.getCategoriaFiscal() == null) {
            dto.setCategoriaFiscal(CategoriaFiscal.CONSUMIDOR_FINAL); // según CU: consumidor final por omisión
        }

        // 4) mapear DTO -> entidad
        Huesped nuevo = huespedMapper.toEntity(dto);

        // 5) guardar
        return huespedDAO.save(nuevo);
    }

    @Override
    public Huesped dar_alta_huesped_forzar(HuespedDTO dto) {
        // misma validación de obligatorios
        validator.validarDatosObligatorios(dto);

        // acá NO rechazamos si existe; simplemente guardamos
        if (dto.getCategoriaFiscal() == null) {
            dto.setCategoriaFiscal(CategoriaFiscal.CONSUMIDOR_FINAL);
        }

        Huesped nuevo = huespedMapper.toEntity(dto);
        return huespedDAO.save(nuevo);
    }

    @Override
    public Optional<Huesped> buscar_por_doc(TipoDni tipo, String numeroDocumento) {
        return huespedDAO.findByTipoDocumentoAndNumeroDocumento(tipo, numeroDocumento);
    }

    /* 
    // ----------------------------------------------------
    // Métodos del diagrama NO implementados aún:
    // ----------------------------------------------------
    public void modificar_huesped(HuespedDTO dto) {

    }

    public void dar_baja_huesped(Long id) {

    }
    
    List<Huesped> buscar_huespedes(busquedaHuespedTOD busqueda){ 
    }
    */
    // ----------------------------------------------------
    // Método privado de mapeo DTO -> entidad
    // Esto lo podés sacar a un mapper después
    // ----------------------------------------------------
   
}
