package edu.inbugwethrust.premier.suite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import edu.inbugwethrust.premier.suite.dto.BusquedaHuespedDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.dto.IdentificacionHuespedDTO;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/huespedes")
public class HuespedController {

    private final IGestorHuespedes gestorHuespedes;
    
    @Autowired
    public HuespedController(IGestorHuespedes gestorHuespedes) {
        this.gestorHuespedes = gestorHuespedes;
    }

    /**
     * Alta normal de huésped.
     * - Valida campos obligatorios
     * - Si el doc existe, lanza HuespedDuplicadoException → lo toma el ControllerAdvice
     * - Si todo OK, guarda y devuelve el huésped
     */
    @GetMapping("formulario-alta")
    public String obtenerFormularioAlta() {
	        return "alta-huesped-page.html";
    }
    @GetMapping("editar")
    public String obtenerFormularioEdicion(@ModelAttribute IdentificacionHuespedDTO idHuesped, Model model) {
      HuespedDTO huesped = gestorHuespedes.buscarPorId(idHuesped);
      model.addAttribute("huesped", huesped);
      return "alta-huesped-page.html";
      
    }
    
     /**
     * Alta normal de huésped (botón SIGUIENTE del CU09, sin "ACEPTAR IGUALMENTE").
     * - Valida campos obligatorios (@Valid)
     * - Si el doc existe, lanza HuespedDuplicadoException → lo maneja un @ControllerAdvice
     */
    @PostMapping("api/alta")
    @ResponseBody
    public ResponseEntity<HuespedDTO> darAlta(@Valid @RequestBody HuespedDTO dto) {
    	// 1. Si la validación falla (ej. apellido en blanco, email inválido)
        //    Spring lanzará una "MethodArgumentNotValidException" AUTOMÁTICAMENTE.
        
        // 2. Esta línea de código NUNCA se ejecutará si la validación falla.
    	
        HuespedDTO creado = gestorHuespedes.dar_alta_huesped(dto);
        // si llegó hasta acá es porque no hubo excepción
        return ResponseEntity.ok(creado);
    }

    /**
     * Alta forzada de huésped.
     * Este endpoint emula el botón "ACEPTAR IGUALMENTE" del CU 9.
     * Vuelve a validar obligatorios, pero no rechaza por documento duplicado.
     */
    @PostMapping("api/alta-forzar")
    @ResponseBody
    public ResponseEntity<HuespedDTO> darAltaForzada(@Valid @RequestBody HuespedDTO dto) {
        HuespedDTO creado = gestorHuespedes.dar_alta_huesped_forzar(dto);
        return ResponseEntity.ok(creado);
    }


    // -------------------------------------------------
    // CU02 – Buscar huésped
    // -------------------------------------------------

    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(
            // Parámetros de control de flujo
            @RequestParam(name = "accion", defaultValue = "BUSCAR") String accion,
            
            // Parámetros de contexto "Ocupar" (pueden venir nulos si es una búsqueda normal)
            @RequestParam(name = "numeroHabitacion", required = false) Integer numeroHabitacion,
            @RequestParam(name = "fechaIngreso", required = false) String fechaIngreso,
            @RequestParam(name = "fechaEgreso", required = false) String fechaEgreso,
            
            Model model) {

        if (!model.containsAttribute("busquedaHuespedDTO")) {
            model.addAttribute("busquedaHuespedDTO", new BusquedaHuespedDTO());
        }
        
        // Pasamos estos datos a la vista para que Thymeleaf los meta en hidden inputs
        model.addAttribute("accion", accion);
        model.addAttribute("numeroHabitacion", numeroHabitacion);
        model.addAttribute("fechaIngreso", fechaIngreso);
        model.addAttribute("fechaEgreso", fechaEgreso);

        return "buscar-huesped-page";
    }
    
    @GetMapping("/resultados")
    public String buscarHuespedes(
            @ModelAttribute("busquedaHuespedDTO") BusquedaHuespedDTO dto,
            
            // Capturamos los mismos params para mantener el contexto
            @RequestParam(name = "accion", defaultValue = "BUSCAR") String accion,
            @RequestParam(name = "numeroHabitacion", required = false) Integer numeroHabitacion,
            @RequestParam(name = "fechaIngreso", required = false) String fechaIngreso,
            @RequestParam(name = "fechaEgreso", required = false) String fechaEgreso,
            
            Model model,
            RedirectAttributes redirectAttributes) {
        
        List<HuespedDTO> listaResultados = gestorHuespedes.buscar_huespedes(dto);
        
        if (listaResultados.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeToast", "No se encontraron resultados.");
            redirectAttributes.addFlashAttribute("tipoToast", "warning");
            
            // IMPORTANTE: Al redirigir, debemos pegar los parámetros de nuevo 
            // para no perder el hilo de la habitación que estamos ocupando.
            redirectAttributes.addAttribute("accion", accion);
            if (numeroHabitacion != null) redirectAttributes.addAttribute("numeroHabitacion", numeroHabitacion);
            if (fechaIngreso != null) redirectAttributes.addAttribute("fechaIngreso", fechaIngreso);
            if (fechaEgreso != null) redirectAttributes.addAttribute("fechaEgreso", fechaEgreso);
            
            return "redirect:/huespedes/buscar"; 
        }
        
        model.addAttribute("huespedes", listaResultados);
        
        // Pasamos de nuevo el contexto a la vista de resultados
        model.addAttribute("accion", accion);
        model.addAttribute("numeroHabitacion", numeroHabitacion);
        model.addAttribute("fechaIngreso", fechaIngreso);
        model.addAttribute("fechaEgreso", fechaEgreso);
        
        return "resultados-huesped-page";
    }
    /**
     * CU02 – Búsqueda de huéspedes.
     * - Si no se envía ningún criterio → devuelve TODOS los huéspedes.
     * - Si se envían criterios y NO hay resultados → 204 No Content
     *   (el front interpreta esto como "ir a CU11 – Dar alta de huésped").
     * - Si hay resultados → 200 OK con la lista.
     */    
    @PostMapping("/api/buscar")
    @ResponseBody
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(
            @RequestBody(required = false) BusquedaHuespedDTO busqueda) {

        List<HuespedDTO> resultados = gestorHuespedes.buscar_huespedes(busqueda);

        if (resultados.isEmpty()) {
            // No hay resultados → no existe ninguna concordancia según CU02 → CU11
            return ResponseEntity.noContent().build(); // 204
        }

        // Hay uno o varios resultados → CU02 paso 4:
        // el front mostrará la grilla y dejará elegir uno para luego ir a CU10.
        return ResponseEntity.ok(resultados);
    }
}
