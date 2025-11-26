package edu.inbugwethrust.premier.suite.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.inbugwethrust.premier.suite.dto.OcupacionHabitacionDTO;
import edu.inbugwethrust.premier.suite.dto.RegistrarOcupacionesRequestDTO;
import edu.inbugwethrust.premier.suite.model.Estadia;
import edu.inbugwethrust.premier.suite.model.FichaEvento;
import edu.inbugwethrust.premier.suite.model.Habitacion;
import edu.inbugwethrust.premier.suite.model.Huesped;
import edu.inbugwethrust.premier.suite.model.Reserva;
import edu.inbugwethrust.premier.suite.services.GestorHabitaciones;
import edu.inbugwethrust.premier.suite.services.GestorFichaEvento;
import edu.inbugwethrust.premier.suite.services.GestorReservas;
import edu.inbugwethrust.premier.suite.services.IGestorHuespedes;
import edu.inbugwethrust.premier.suite.services.GestorEstadia; // lo crearás después

@Service
public class OcuparHabitacionService implements IOcuparHabitacionService {

    private final GestorHabitaciones gestorHabitaciones;
    private final GestorFichaEvento gestorFichaEvento;
    private final GestorReservas gestorReservas;
    private final IGestorHuespedes gestorHuespedes;
    private final GestorEstadia gestorEstadia;

    @Autowired
    public OcuparHabitacionService(GestorHabitaciones gestorHabitaciones,
                                   GestorFichaEvento gestorFichaEvento,
                                   GestorReservas gestorReservas,
                                   IGestorHuespedes gestorHuespedes,
                                   GestorEstadia gestorEstadia) {
        this.gestorHabitaciones = gestorHabitaciones;
        this.gestorFichaEvento = gestorFichaEvento;
        this.gestorReservas = gestorReservas;
        this.gestorHuespedes = gestorHuespedes;
        this.gestorEstadia = gestorEstadia;
    }

    /**
     * Implementa la lógica principal del CU15 "Ocupar habitación".
     *
     * - Revalida reglas de negocio sobre el request.
     * - Verifica disponibilidad de cada habitación contra las FichaEvento existentes.
     * - Toma o no la reserva asociada según forzarSobreReserva.
     * - Crea una Estadia con sus FichaEvento de OCUPACION y las persiste.
     */
    @Override
    @Transactional
    public void registrarOcupaciones(RegistrarOcupacionesRequestDTO request) {

        Objects.requireNonNull(request, "El request de ocupaciones no puede ser nulo");
        List<OcupacionHabitacionDTO> ocupacionesDTO = request.getOcupaciones();

        if (ocupacionesDTO == null || ocupacionesDTO.isEmpty()) {
            throw new IllegalArgumentException("Debe especificarse al menos una habitación a ocupar.");
        }

        // Regla CU15: los acompañantes no pueden repetir habitación distinta.
        Set<Long> acompanantesUsadosGlobal = new HashSet<>();

        // Creamos una Estadia "vacía". Luego le vamos agregando las fichas.
        Estadia estadia = new Estadia();

        // Opcional: si detectamos UNA sola reserva asociada, la colgamos a la estadía
        Reserva reservaPrincipal = null;

        for (OcupacionHabitacionDTO dto : ocupacionesDTO) {

            // --- 1) Validaciones básicas de DTO (fechas, ids, etc.) ---
            validarDatosBasicos(dto);

            // --- 2) Conversión de fechas a LocalDateTime (check-in / check-out) ---
            LocalDateTime inicio = construirFechaInicio(dto.getFechaIngreso());
            LocalDateTime fin    = construirFechaFin(dto.getFechaEgreso());

            // --- 3) Reglas de acompañantes a nivel CU15 ---
            aplicarReglasAcompanantes(dto, acompanantesUsadosGlobal);

            // --- 4) Obtener entidades de dominio necesarias ---
            Habitacion habitacion = gestorHabitaciones.obtenerPorNumero(dto.getNumeroHabitacion());
            validarCapacidadHabitacion(dto, habitacion);
            Huesped huespedResponsable = gestorHuespedes.obtenerPorId(dto.getIdHuespedResponsable());
            List<Huesped> acompanantes = List.of();
            if (dto.getIdsAcompanantes() != null && !dto.getIdsAcompanantes().isEmpty()) {
                acompanantes = gestorHuespedes.obtenerPorIds(dto.getIdsAcompanantes());
            }
            // --- 5) Validar disponibilidad y tomar (o no) la reserva asociada ---
            // Esta lógica está en GestorFichaEvento para no sobrecargar el service.
            //
            // La idea es que este método:
            //  - Verifique que en el rango [inicio, fin] solo haya estados DISPONIBLE o RESERVADA.
            //  - Si hay RESERVADA:
            //      * si !forzarSobreReserva => lanza ReservaNoDisponibleException (409)
            //      * si forzarSobreReserva  => devuelve la Reserva asociada.
            //  - Si no hay RESERVADA => devuelve null.
            Reserva reservaAsociada = gestorFichaEvento
                    .validarYObtenerReservaParaOcupacion(habitacion, inicio, fin, dto.isForzarSobreReserva());

            // Nos guardamos una "reserva principal" si hay una sola (opcional, no obligatorio)
            if (reservaAsociada != null) {
                if (reservaPrincipal == null) {
                    reservaPrincipal = reservaAsociada;
                } else if (!reservaPrincipal.equals(reservaAsociada)) {
                    // Si aparecen reservas distintas, preferimos NO colgar la estadía a una sola.
                    reservaPrincipal = null;
                }
            }

            // --- 6) Crear la FichaEvento de OCUPACION para esta habitación ---
            FichaEvento fichaOcupacion = gestorFichaEvento.crearFichaOcupacion(
                    habitacion,
                    reservaAsociada,
                    inicio,
                    fin,
                    huespedResponsable,
                    acompanantes
            );

            // --- 7) Asociar ficha a la estadía (conceptualmente la estadía es el agregado raíz) ---
            estadia.agregarFichaEvento(fichaOcupacion);
        }

        // Si pudimos identificar una única reserva principal, colgamos la estadía de esa reserva
        if (reservaPrincipal != null) {
            estadia.setReserva(reservaPrincipal);

            // Opcional: actualizar estado de la reserva (por ejemplo, EN_CURSO / OCUPADA)
            gestorReservas.marcarReservaComoEnCurso(reservaPrincipal);
        }

        // --- 8) Persistir la estadía (por cascade se guardan las FichaEvento de ocupación) ---
        gestorEstadia.guardar(estadia);
    }

    // ========================
    // Métodos privados helpers
    // ========================

    /**
     * Valida reglas básicas sobre fechas y campos obligatorios del DTO.
     */
    private void validarDatosBasicos(OcupacionHabitacionDTO dto) {

        if (dto.getFechaIngreso() == null || dto.getFechaEgreso() == null) {
            throw new IllegalArgumentException("Las fechas de ingreso y egreso son obligatorias.");
        }

        LocalDate ingreso = dto.getFechaIngreso();
        LocalDate egreso  = dto.getFechaEgreso();

        if (!egreso.isAfter(ingreso)) {
            throw new IllegalArgumentException(
                    "La fecha de egreso debe ser posterior a la fecha de ingreso para la habitación "
                    + dto.getNumeroHabitacion()
            );
        }

        if (dto.getIdHuespedResponsable() == null) {
            throw new IllegalArgumentException("Debe indicar el huésped responsable de cada habitación.");
        }
    }

    /**
     * Reglas del CU15 sobre acompañantes / ocupantes:
     * - Cada huésped puede aparecer como acompañante (ocupante) en UNA sola habitación.
     * - Se permite que el huésped responsable de la reserva figure también
     *   como acompañante en UNA habitación (la que realmente ocupa).
     */
    private void aplicarReglasAcompanantes(OcupacionHabitacionDTO dto,
                                        Set<Long> acompanantesUsadosGlobal) {

        if (dto.getIdsAcompanantes() == null) {
            return;
        }

        for (Long idAcomp : dto.getIdsAcompanantes()) {
            if (idAcomp == null) {
                continue;
            }

            // Regla global: una persona sólo puede aparecer como acompañante en una habitación
            if (!acompanantesUsadosGlobal.add(idAcomp)) {
                throw new IllegalArgumentException(
                    "El huésped con id " + idAcomp +
                    " ya se encuentra asignado como ocupante en otra habitación en esta operación."
                );
            }
        }
    }


    /**
     * Convierte la fecha de ingreso (LocalDate) a LocalDateTime usando una hora de check-in
     * estándar (por ejemplo 15:00). Si en tu proyecto ya definiste otra convención,
     * podés ajustar este método para reutilizarla.
     */
    private LocalDateTime construirFechaInicio(LocalDate fechaIngreso) {
        return fechaIngreso.atTime(LocalTime.of(14, 0)); // ejemplo: check-in 15:00
    }

    /**
     * Convierte la fecha de egreso (LocalDate) a LocalDateTime usando una hora de check-out
     * estándar (por ejemplo 10:00).
     */
    private LocalDateTime construirFechaFin(LocalDate fechaEgreso) {
        return fechaEgreso.atTime(LocalTime.of(10, 0)); // ejemplo: check-out 10:00
    }

    private void validarCapacidadHabitacion(OcupacionHabitacionDTO dto, Habitacion habitacion) {

    int cantidadOcupantes = (dto.getIdsAcompanantes() == null)
            ? 0
            : dto.getIdsAcompanantes().size();

    int maximo = habitacion.getTipoHabitacion().getMaximoHuespedes();

    if (cantidadOcupantes > maximo) {
        throw new IllegalArgumentException(
            "La habitación " + dto.getNumeroHabitacion() +
            " tiene una capacidad máxima de " + maximo +
            " huéspedes y se están intentando asignar " + cantidadOcupantes + "."
        );
    }
}

}
