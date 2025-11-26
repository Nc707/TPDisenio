package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.ObtenerHuespedDTO;
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
    public HuespedDTO dar_alta_huesped(HuespedDTO dto) {
        verificarDuplicidad(dto);

        verificacionesDeCampos(dto);
            

        // 4) mapear DTO -> entidad
        Huesped nuevo = huespedMapper.toEntity(dto);

        // 5) guardar
        return huespedMapper.toDTO(huespedDAO.save(nuevo));
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
    public HuespedDTO dar_alta_huesped_forzar(HuespedDTO dto) {
        // acá NO rechazamos si existe; simplemente guardamos
        verificacionesDeCampos(dto);

        Huesped nuevo = huespedMapper.toEntity(dto);
        return huespedMapper.toDTO(huespedDAO.save(nuevo));
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

    
    private Optional<Huesped> buscar_por_doc(TipoDni tipo, String numeroDocumento) {
        return huespedDAO.findByTipoDocumentoAndNumeroDocumento(tipo, numeroDocumento);
    }

    @Override
    public List<HuespedDTO> buscar_huespedes(BusquedaHuespedDTO busqueda){ 
        BusquedaHuespedDTO datos = normalizarBusqueda(busqueda);

        boolean hayCriterio =
                notBlank(datos.getApellido()) ||
                notBlank(datos.getNombres()) ||
                //datos.getTipoDocumento() != null ||
                notBlank(datos.getNumeroDocumento());

        if (!hayCriterio) {
            // Caso "no ingresó nada" → mostrar todos
            return huespedDAO.findAll().stream().map(huespedMapper::toDTO).toList();
        }
        

        Huesped huespedProbe = huespedMapper.toEntityBusqueda(datos);
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return huespedDAO.findAll(Example.of(huespedProbe, matcher)).stream()
            .map(huespedMapper::toDTO).toList();
        
    }

    private BusquedaHuespedDTO normalizarBusqueda(BusquedaHuespedDTO busqueda) {
        if (busqueda == null) return new BusquedaHuespedDTO();

        BusquedaHuespedDTO dto = new BusquedaHuespedDTO();
        dto.setApellido(cleanAndUpper(busqueda.getApellido()));
        dto.setNombres(cleanAndUpper(busqueda.getNombres()));
        dto.setTipoDocumento(busqueda.getTipoDocumento());
        dto.setNumeroDocumento(clean(busqueda.getNumeroDocumento()));
        return dto;
    }

    private String cleanAndUpper(String valor) {
        if (valor == null) return null;
        String trimmed = valor.trim();
        return trimmed.isEmpty() ? null : trimmed.toUpperCase();
    }

    private String clean(String valor) {
        if (valor == null) return null;
        String trimmed = valor.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }


    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
    
    public HuespedDTO buscarPorId(ObtenerHuespedDTO id) {
          Huesped huesped = huespedDAO.findByTipoDocumentoAndNumeroDocumento(id.getTipoDocumento(),
           id.getNumeroDocumento()).orElse(null);
          if (huesped == null) {
            return null;
          }
          return huespedMapper.toDTO(huesped);
        }  

    @Override
    public Huesped obtenerPorId(Long idDocumento) {
        if (idDocumento == null) {
            throw new IllegalArgumentException("El id de huésped no puede ser nulo");
        }

        // Interpretamos el Long como número de documento (DNI)
        String numeroDocumento = String.valueOf(idDocumento);

        return buscar_por_doc(TipoDni.DNI, numeroDocumento)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró huésped con DNI " + numeroDocumento));
    }

    @Override
    public List<Huesped> obtenerPorIds(List<Long> idsDocumento) {
        if (idsDocumento == null || idsDocumento.isEmpty()) {
            return List.of();
        }

        List<Huesped> resultado = new java.util.ArrayList<>();
        for (Long id : idsDocumento) {
            if (id != null) {
                resultado.add(obtenerPorId(id));
            }
        }
        return resultado;
    }
}
    
  
