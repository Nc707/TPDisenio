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
import edu.inbugwethrust.premier.suite.model.Huesped;
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
    
     /**
     * Alta normal de huésped (botón SIGUIENTE del CU09, sin "ACEPTAR IGUALMENTE").
     * - Valida campos obligatorios (@Valid)
     * - Si el doc existe, lanza HuespedDuplicadoException → lo maneja un @ControllerAdvice
     */
    @PostMapping("api/alta")
    @ResponseBody
    public ResponseEntity<Huesped> darAlta(@Valid @RequestBody HuespedDTO dto) {
    	// 1. Si la validación falla (ej. apellido en blanco, email inválido)
        //    Spring lanzará una "MethodArgumentNotValidException" AUTOMÁTICAMENTE.
        
        // 2. Esta línea de código NUNCA se ejecutará si la validación falla.
    	
        Huesped creado = gestorHuespedes.dar_alta_huesped(dto);
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
    public ResponseEntity<Huesped> darAltaForzada(@Valid @RequestBody HuespedDTO dto) {
        Huesped creado = gestorHuespedes.dar_alta_huesped_forzar(dto);
        return ResponseEntity.ok(creado);
    }


    // -------------------------------------------------
    // CU02 – Buscar huésped
    // -------------------------------------------------

    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model model) {
        if (!model.containsAttribute("busquedaHuespedDTO")) {
            model.addAttribute("busquedaHuespedDTO", new BusquedaHuespedDTO());
        }
        return "buscar-huesped-page";
    }
    
    @GetMapping("/resultados")
    public String buscarHuespedes(
            @ModelAttribute("busquedaHuespedDTO") BusquedaHuespedDTO dto, 
            Model model,
            RedirectAttributes redirectAttributes) {
        
        
        List<Huesped> listaResultados = gestorHuespedes.buscar_huespedes(dto);
        
        if (listaResultados.isEmpty()) {
            // 3. Agregamos el mensaje "Flash" (vive solo una petición)
            redirectAttributes.addFlashAttribute("mensajeToast", "No se encontraron resultados con esos criterios. Intente nuevamente.");
            redirectAttributes.addFlashAttribute("tipoToast", "warning"); // Opcional: para cambiar color
            
            // 4. Redirigimos AL FORMULARIO DE BÚSQUEDA (no a la tabla vacía)
            return "redirect:/huespedes/buscar"; 
        }
        
        // 2. Pasamos la lista a la vista
        model.addAttribute("huespedes", listaResultados);
        
        // 3. Vamos a la nueva vista (que crearemos abajo)
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
    public ResponseEntity<List<Huesped>> buscarHuespedes(
            @RequestBody(required = false) BusquedaHuespedDTO busqueda) {

        List<Huesped> resultados = gestorHuespedes.buscar_huespedes(busqueda);

        if (resultados.isEmpty()) {
            // No hay resultados → no existe ninguna concordancia según CU02 → CU11
            return ResponseEntity.noContent().build(); // 204
        }

        // Hay uno o varios resultados → CU02 paso 4:
        // el front mostrará la grilla y dejará elegir uno para luego ir a CU10.
        return ResponseEntity.ok(resultados);
    }
}
