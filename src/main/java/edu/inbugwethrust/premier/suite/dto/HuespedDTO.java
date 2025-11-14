package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;

import edu.inbugwethrust.premier.suite.model.TipoDni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import edu.inbugwethrust.premier.suite.model.CategoriaFiscal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedDTO {

	@NotBlank(message = "El apellido es obligatorio")
    private String apellido;
	
	@NotBlank(message = "El nombre es obligatorio")
    private String nombres;
	
	@NotNull(message = "El tipo de documento es obligatorio")
    private TipoDni tipoDocumento;
    
	@NotBlank(message = "El número de documento es obligatorio")
	@Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe consistir en 8 dígitos numéricos.")
	private String numeroDocumento;
	
	@Pattern(regexp = "^[0-9]{11}$", message = "El CUIT debe consistir en 11 dígitos numéricos.")
    private String cuit;                  // no obligatorio
	
    private CategoriaFiscal categoriaFiscal;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;
    
    @NotNull(message = "La dirección es obligatoria")
    @Valid
    private DireccionDTO direccion;
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9()+ -]{7,20}$", message = "El teléfono debe tener un formato válido")
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "La ocupación es obligatoria")
    private String ocupacion;
    
    @NotBlank(message = "La nacionalidad es obligatoria")
    private String nacionalidad;
}
