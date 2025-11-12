package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.mappers.HuespedMapper;
import edu.inbugwethrust.premier.suite.model.CategoriaFiscal;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.TipoDni;
import edu.inbugwethrust.premier.suite.repositories.HuespedDAO;
import edu.inbugwethrust.premier.suite.services.exceptions.CuitVacioException;
import edu.inbugwethrust.premier.suite.services.exceptions.HuespedDuplicadoException;


@Service
public class GestorHuespedes implements IGestorHuespedes {

    private final HuespedDAO huespedDAO;
    private final HuespedMapper huespedMapper;

    @Autowired
    public GestorHuespedes(HuespedDAO huespedDAO, HuespedMapper mapper) {
        this.huespedDAO = huespedDAO;
        this.huespedMapper = mapper;
    }

    @Override
    public Huesped dar_alta_huesped(HuespedDTO dto) {
        verificarDuplicidad(dto);

        verificacionesDeCampos(dto);
            

        // 4) mapear DTO -> entidad
        Huesped nuevo = huespedMapper.toEntity(dto);

        // 5) guardar
        return huespedDAO.save(nuevo);
    }

    private void verificarDuplicidad(HuespedDTO dto) {
      // 2) chequear si ya existe por tipo + número (flujo 2.B)
      Optional<Huesped> existente = buscar_por_doc(dto.getTipoDocumento(), dto.getNumeroDocumento());
      if (existente.isPresent()) {
          // acá en el CU muestran un cartel y dan a elegir.
          // en backend lo representamos con una excepción específica
          throw new HuespedDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema");
      }
    }

    @Override
    public Huesped dar_alta_huesped_forzar(HuespedDTO dto) {
        // acá NO rechazamos si existe; simplemente guardamos
        verificacionesDeCampos(dto);

        Huesped nuevo = huespedMapper.toEntity(dto);
        return huespedDAO.save(nuevo);
    }

    private void verificacionesDeCampos(HuespedDTO dto) {
      if (dto.getCategoriaFiscal() == null) {
          dto.setCategoriaFiscal(CategoriaFiscal.CONSUMIDOR_FINAL);
      }
      if(dto.getCategoriaFiscal() == CategoriaFiscal.RESPONSABLE_INSCRIPTO 
          && (dto.getCuit() == null || dto.getCuit().isBlank())){
          throw new CuitVacioException("Error, el CUIT no puede estar vacio si el huesped es"
              + "responsable inscripto");
      }
    }

    @Override
    public Optional<Huesped> buscar_por_doc(TipoDni tipo, String numeroDocumento) {
        return huespedDAO.findByTipoDocumentoAndNumeroDocumento(tipo, numeroDocumento);
    }
   public List<Huesped> buscar_huespedes(BusquedaHuespedDTO busqueda){ 

        // Convertir a mayúsculas según requerimiento
        String apellido = busqueda.getApellido() != null ?busqueda.getApellido().toUpperCase() : null;
        String nombres = busqueda.getNombres() != null ? busqueda.getNombres().toUpperCase() : null;
        TipoDni tipoDocumento = busqueda.getTipoDocumento();
        String numeroDocumento = busqueda.getNumeroDocumento();

        // Si tiene tipo + número de documento → búsqueda exacta
        if (tipoDocumento != null && numeroDocumento != null && !numeroDocumento.isBlank()) {
            return huespedDAO.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                             .map(List::of)
                             .orElse(List.of());
        }

        // Si tiene apellido + nombre → búsqueda combinada
        if (apellido != null && nombres != null && !apellido.isBlank() && !nombres.isBlank()) {
            return huespedDAO.findByApellidoStartingWithIgnoreCaseAndNombresStartingWithIgnoreCase(apellido, nombres);
        }

        // Solo apellido
        if (apellido != null && !apellido.isBlank()) {
            return huespedDAO.findByApellidoStartingWithIgnoreCase(apellido);
        }

        // Solo nombre
        if (nombres != null && !nombres.isBlank()) {
            return huespedDAO.findByNombresStartingWithIgnoreCase(nombres);
        }

        // Si no puso nada → devuelve todo
        return huespedDAO.findAll();
    }
}

    /* 
    // ----------------------------------------------------
    // Métodos del diagrama NO implementados aún:
    // ----------------------------------------------------
    public void modificar_huesped(HuespedDTO dto) {

    }

    public void dar_baja_huesped(Long id) {

    }
    
    
    */
    // ----------------------------------------------------
    // Método privado de mapeo DTO -> entidad
    // Esto lo podés sacar a un mapper después
    // ----------------------------------------------------
   

