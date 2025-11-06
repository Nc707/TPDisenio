package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.model.CategoriaFiscal;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;
import edu.inbugwethrust.premier.suite.repositories.HuespedDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.DatosObligatoriosException;

@Service
public class Gestor_huespedes implements GestorHuespedes {

    private final HuespedDAO huespedDAO;
    private final HuespedValidator validator;

    public Gestor_huespedes(HuespedDAO huespedDAO, HuespedValidator validator) {
        this.huespedDAO = huespedDAO;
        this.validator = validator;
    }

    @Override
    @Transactional
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
        Huesped nuevo = mapearADominio(dto);

        // 5) guardar
        return huespedDAO.save(nuevo);
    }

    @Override
    @Transactional
    public Huesped dar_alta_huesped_forzar(HuespedDTO dto) {
        // misma validación de obligatorios
        validator.validarDatosObligatorios(dto);

        // acá NO rechazamos si existe; simplemente guardamos
        if (dto.getCategoriaFiscal() == null) {
            dto.setCategoriaFiscal(CategoriaFiscal.CONSUMIDOR_FINAL);
        }

        Huesped nuevo = mapearADominio(dto);
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
    private Huesped mapearADominio(HuespedDTO dto) {
        Huesped h = new Huesped();
        h.setApellido(dto.getApellido());
        h.setNombres(dto.getNombres());
        h.setTipoDocumento(dto.getTipoDocumento());
        h.setNumeroDocumento(dto.getNumeroDocumento());
        h.setCuit(dto.getCuit());
        h.setCategoriaFiscal(dto.getCategoriaFiscal());
        h.setFechaNacimiento(dto.getFechaNacimiento());
        h.setTelefono(dto.getTelefono());
        h.setEmail(dto.getEmail());
        h.setOcupacion(dto.getOcupacion());
        h.setNacionalidad(dto.getNacionalidad());

        // ver como se mapea la dirección
        if (dto.getDireccion() != null) {
            h.setDireccion(dto.getDireccion().toDireccionDominio());
        }

        return h;
    }
}
