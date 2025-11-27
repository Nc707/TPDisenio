package edu.inbugwethrust.premier.suite.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa la ocupación de una habitación en un rango de fechas,
 * indicando el huésped responsable y los acompañantes.
 *
 * Se usa en el CU15 "Ocupar habitación".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OcupacionHabitacionDTO {

    /**
     * Número de habitación (ID de la entidad Habitacion).
     */
    @NotNull(message = "El número de habitación es obligatorio")
    private Integer numeroHabitacion;

    /**
     * Día de ingreso de la ocupación (incluido).
     */
    @NotNull(message = "La fecha de ingreso es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso;

    /**
     * Día de egreso de la ocupación (incluido como día de salida).
     * La lógica del servicio se encargará de usar la hora adecuada (por ej. 10:00 hs).
     */
    @NotNull(message = "La fecha de egreso es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEgreso;

    /**
     * Identificadores de los acompañantes que ocuparán esta habitación.
     * Puede ser lista vacía si no hay acompañantes.
     */
    @Valid
    @NotNull(message = "La lista de identificaciones de acompañantes no puede ser nula")
    private List<IdentificacionHuespedDTO> idsAcompanantes = new ArrayList<>();

    /**
     * Indica si, en caso de existir reservas en el rango, el actor eligió "OCUPAR IGUAL".
     * Por defecto se considera false.
     */
    private boolean forzarSobreReserva = false;
}
