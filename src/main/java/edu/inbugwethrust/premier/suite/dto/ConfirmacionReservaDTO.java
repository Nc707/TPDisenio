package edu.inbugwethrust.premier.suite.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que encapsula toda la información necesaria para registrar una reserva:
 * - habitaciones y rangos de fechas seleccionados
 * - datos del eventual huésped / responsable de pago
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfirmacionReservaDTO {

    @NotNull(message = "La selección de habitaciones es obligatoria")
    @Valid
    private SeleccionReservaDTO seleccion;

    @NotNull(message = "Los datos del huésped son obligatorios")
    @Valid
    private DatosHuespedReservaDTO datosHuesped;
}
