package edu.inbugwethrust.premier.suite.services;

import java.util.Optional;
import java.util.LinkedList;
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

    @Override
    public List<Huesped> buscar_huespedes(BusquedaHuespedDTO busqueda){ 
        BusquedaHuespedDTO datos = normalizarBusqueda(busqueda);

        boolean hayCriterio =
                notBlank(datos.getApellido()) ||
                notBlank(datos.getNombres()) ||
                datos.getTipoDocumento() != null ||
                notBlank(datos.getNumeroDocumento());

        if (!hayCriterio) {
            // Caso "no ingresó nada" → mostrar todos
            return buscarTodos();
        }

        // Si hay criterios, delego todo al repo:
        return huespedDAO.buscarHuespedes(
                datos.getApellido(),
                datos.getNombres(),
                datos.getTipoDocumento(),
                datos.getNumeroDocumento()
        );
    }

    private List<Huesped> buscarTodos() {
        return huespedDAO.findAll();
    }
     private BusquedaHuespedDTO normalizarBusqueda(BusquedaHuespedDTO busqueda) {
        if (busqueda == null) return new BusquedaHuespedDTO();

        BusquedaHuespedDTO dto = new BusquedaHuespedDTO();
        dto.setApellido(toUpper(busqueda.getApellido()));
        dto.setNombres(toUpper(busqueda.getNombres()));
        dto.setTipoDocumento(busqueda.getTipoDocumento());
        dto.setNumeroDocumento(busqueda.getNumeroDocumento());
        return dto;
    }

    private String toUpper(String valor) {
        return valor != null ? valor.toUpperCase() : null;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
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
   

