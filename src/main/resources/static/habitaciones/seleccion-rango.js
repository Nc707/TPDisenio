// --- seleccion-rango.js ---
// Elementos del Modal de Conflicto
	const modalConflicto = document.getElementById('modal-conflicto-reserva');
    
    // --- DEBUG ---
    if (!modalConflicto) {
        console.error("¡ERROR CRÍTICO! El JS no encuentra el div 'modal-conflicto-reserva'. Revisa el HTML.");
    } else {
        console.log("Modal de conflicto encontrado correctamente.");
    }

document.addEventListener('DOMContentLoaded', () => {
    const modoAccion = document.body.dataset.modo || 'RESERVAR';
    const esSoloDisponibilidad = (modoAccion === 'DISPONIBILIDAD');
    const esModoOcupar = (modoAccion === 'OCUPAR');

    const celdas = document.querySelectorAll('.celda-interactiva');
    const btnLimpiar = document.getElementById('reset-btn');
    const formSeleccion = document.getElementById('form-seleccion');
    const btnSiguiente = document.getElementById('btn-siguiente');

    // Secciones para el flujo de RESERVAR
    const seccionBusqueda = document.getElementById('seccion-busqueda-grilla');
    const seccionResumen = document.getElementById('seccion-resumen');
    const seccionDatos = document.getElementById('seccion-datos-huesped');

    const tbodyResumen = document.getElementById('tbody-resumen');
    const btnResumenCancelar = document.getElementById('btn-resumen-cancelar');
    const btnResumenAceptar = document.getElementById('btn-resumen-aceptar');

    const formReservaFinal = document.getElementById('form-reserva-final');
    const msgErrorHuesped = document.getElementById('mensaje-error-huesped');
    const inputApellido = document.getElementById('apellido');
    const inputNombre = document.getElementById('nombre');
    const inputTelefono = document.getElementById('telefono');
    const btnFinalCancelar = document.getElementById('btn-final-cancelar');

    // Elementos del Modal de Conflicto (Solo OCUPAR)
    const modalConflicto = document.getElementById('modal-conflicto-reserva');
    const listaConflictos = document.getElementById('lista-conflictos');
    const btnConflictoVolver = document.getElementById('btn-conflicto-volver');
    const btnConflictoOcuparIgual = document.getElementById('btn-conflicto-ocupar-igual');

    // 👉 Leyenda
    const leyendaContainer = document.querySelector('.leyenda-container');
		
		const btnCancelarPrincipal = document.getElementById('open-modal-btn');

				    if (btnCancelarPrincipal) {
				        btnCancelarPrincipal.addEventListener('click', (e) => {
				            e.preventDefault();
				            console.log("Cancelando y volviendo al inicio...");

				            // 1. Limpiar memoria del navegador
				            sessionStorage.removeItem('seleccionHabitaciones');
				            sessionStorage.removeItem('modoAccion');
				            sessionStorage.removeItem('colaOcupacion');
				            sessionStorage.removeItem('indiceOcupacionActual');

				            // 2. Limpiar visualmente la selección (opcional, pero buena práctica)
				            limpiarSeleccionVisualTotal();

				            // 3. Redirigir a la raíz
				            window.location.href = '/';
				        });
				    }

    // Variable temporal para guardar la selección mientras el usuario decide en el modal
    let dtoPendienteDeValidacion = null;

    // Si es solo disponibilidad, no hay selección
    if (esSoloDisponibilidad || !formSeleccion) {
        return;
    }

    // --- Estado selección por rango ---
    let seleccionInicio = null; // { habitacion, fecha }
    let seleccionDTO = [];      // array

    // ------------------------------------------------------------------
    // Helpers visuales
    // ------------------------------------------------------------------
    function limpiarSeleccionVisualTotal() {
        celdas.forEach(td => {
            td.classList.remove('in-range', 'selection-start');
            const checkbox = td.querySelector('input[type="checkbox"]');
            if (checkbox) checkbox.checked = false;
        });
    }

    function limpiarSeleccionVisualHabitacion(habitacion) {
        const tdsHab = document.querySelectorAll(
            '.celda-interactiva[data-habitacion="' + habitacion + '"]'
        );
        tdsHab.forEach(td => {
            td.classList.remove('in-range', 'selection-start');
            const checkbox = td.querySelector('input[type="checkbox"]');
            if (checkbox) checkbox.checked = false;
        });
    }

    function aplicarRangoSeleccion(habitacion, fechaDesde, fechaHasta) {
        const tdsHab = document.querySelectorAll(
            '.celda-interactiva[data-habitacion="' + habitacion + '"]'
        );

        const inicio = fechaDesde < fechaHasta ? fechaDesde : fechaHasta;
        const fin = fechaDesde < fechaHasta ? fechaHasta : fechaDesde;

        tdsHab.forEach(td => {
            const f = td.dataset.fecha;
            const estado = td.dataset.estado;
            const checkbox = td.querySelector('input[type="checkbox"]');

            // LÓGICA DE PERMISOS VISUALES
            let esSeleccionable = (estado === 'LIBRE');
            if (esModoOcupar && estado === 'RESERVADA') {
                esSeleccionable = true;
            }

            if (f >= inicio && f <= fin && esSeleccionable) {
                td.classList.add('in-range');
                if (checkbox) checkbox.checked = true;
            } else {
                td.classList.remove('in-range', 'selection-start');
                if (checkbox) checkbox.checked = false;
            }
        });

        const selectorInicio =
            '.celda-interactiva[data-habitacion="' +
            habitacion +
            '"][data-fecha="' +
            inicio +
            '"]';
        const celdaInicio = document.querySelector(selectorInicio);
        if (celdaInicio) {
            celdaInicio.classList.add('selection-start');
        }
    }

    // Construye DTOs a partir de celdas in-range
    function construirSeleccionDTO() {
        const seleccionPorHabitacion = {};

        const seleccionadas = document.querySelectorAll('.celda-interactiva.in-range');

        seleccionadas.forEach(td => {
            const hab = td.dataset.habitacion;
            const fecha = td.dataset.fecha;
            const tipo = td.dataset.tipo || '';
            const idEstado = td.dataset.idEstado || null;
            const nombres = td.dataset.nombresResponsable || '';
            const apellidos = td.dataset.apellidosResponsable || '';

            if (!seleccionPorHabitacion[hab]) {
                seleccionPorHabitacion[hab] = {
                    tipoHabitacion: tipo,
                    idEstado: idEstado,
                    nombresResponsable: nombres,
                    apellidosResponsable: apellidos,
                    fechas: []
                };
            }
            seleccionPorHabitacion[hab].fechas.push(fecha);
        });

        const resultado = [];
        Object.keys(seleccionPorHabitacion).forEach(hab => {
            const info = seleccionPorHabitacion[hab];
            const fechasOrdenadas = info.fechas.slice().sort(); // yyyy-MM-dd
            
            resultado.push({
                numeroHabitacion: parseInt(hab, 10),
                fechaIngreso: fechasOrdenadas[0],
                fechaEgreso: fechasOrdenadas[fechasOrdenadas.length - 1],
                tipoHabitacion: info.tipoHabitacion,
                // Extra data
                idEstado: info.idEstado,
                nombresResponsable: info.nombresResponsable,
                apellidosResponsable: info.apellidosResponsable
            });
        });

        return resultado;
    }

    // Formato fecha visual
    function formatearFechaConHora(fechaISO, horaTexto) {
        const fecha = new Date(fechaISO + 'T00:00:00');
        const opciones = { weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric' };
        let texto = fecha.toLocaleDateString('es-AR', opciones);
        texto = texto.charAt(0).toUpperCase() + texto.slice(1);
        return `${texto}, ${horaTexto}`;
    }
    
    function formatearFechaCorta(fechaISO) {
        if (!fechaISO) return '';
        const parts = fechaISO.split('-');
        return `${parts[2]}/${parts[1]}`;
    }

    // Render de la tabla de resumen
    function renderResumen() {
        if (!tbodyResumen) return;
        tbodyResumen.innerHTML = '';

        seleccionDTO.forEach(sel => {
            const tr = document.createElement('tr');
            const tdTipo = document.createElement('td'); tdTipo.textContent = sel.tipoHabitacion || '';
            const tdIngreso = document.createElement('td'); tdIngreso.textContent = formatearFechaConHora(sel.fechaIngreso, '12:00hs');
            const tdEgreso = document.createElement('td'); tdEgreso.textContent = formatearFechaConHora(sel.fechaEgreso, '10:00hs');

            tr.appendChild(tdTipo); tr.appendChild(tdIngreso); tr.appendChild(tdEgreso);
            tbodyResumen.appendChild(tr);
        });
    }

    // ------------------------------------------------------------------
    // Listeners de celdas (selección por rango)
    // ------------------------------------------------------------------
    celdas.forEach(td => {
        td.addEventListener('click', () => {
            const estado = td.dataset.estado;

            // VALIDACIÓN DE CLICK:
            // RESERVAR -> Solo LIBRE
            // OCUPAR -> LIBRE o RESERVADA
            let esClickValido = false;
            if (estado === 'LIBRE') esClickValido = true;
            else if (esModoOcupar && estado === 'RESERVADA') esClickValido = true;

            if (!esClickValido) return;

            const habitacion = td.dataset.habitacion;
            const fecha = td.dataset.fecha;

            if (!seleccionInicio) {
                seleccionInicio = { habitacion, fecha };
                limpiarSeleccionVisualHabitacion(habitacion);
                aplicarRangoSeleccion(habitacion, fecha, fecha);
                return;
            }

            if (seleccionInicio.habitacion !== habitacion) {
                seleccionInicio = { habitacion, fecha };
                limpiarSeleccionVisualHabitacion(habitacion);
                aplicarRangoSeleccion(habitacion, fecha, fecha);
                return;
            }

            const fechaInicio = seleccionInicio.fecha;
            aplicarRangoSeleccion(habitacion, fechaInicio, fecha);
            seleccionInicio = null;
        });
    });

    document.addEventListener('click', (e) => {
        const clicEnGrilla = e.target.closest('.grilla-container');
        if (!clicEnGrilla && seleccionInicio) {
            const sel = seleccionInicio;
            const selector = `.celda-interactiva[data-habitacion="${sel.habitacion}"][data-fecha="${sel.fecha}"]`;
            const celdaInicio = document.querySelector(selector);
            if (celdaInicio) celdaInicio.classList.remove('selection-start');
            seleccionInicio = null;
        }
    });

    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', () => {
            limpiarSeleccionVisualTotal();
            seleccionInicio = null;
            seleccionDTO = [];
            sessionStorage.removeItem('seleccionHabitaciones');
            sessionStorage.removeItem('modoAccion');
        });
    }
		
		

		// ------------------------------------------------------------------
			// BOTÓN SIGUIENTE (VALIDACIÓN FRONTEND)
			// ------------------------------------------------------------------
			if (btnSiguiente) {
				btnSiguiente.addEventListener('click', (e) => {
					// Prevenir comportamiento nativo (aunque sea type="button")
					e.preventDefault(); 

					console.log("--> Click en Siguiente. Modo:", modoAccion);

					// 1. Construir DTO General
					const dto = construirSeleccionDTO();
					if (dto.length === 0) {
						alert('Debe seleccionar al menos una habitación.');
						return;
					}

					// 2. Guardar estado común
					sessionStorage.setItem('modoAccion', modoAccion);

					// === CASO 1: RESERVAR ===
					if (modoAccion === 'RESERVAR') {
						sessionStorage.setItem('seleccionHabitaciones', JSON.stringify(dto));
						seleccionDTO = dto;
						renderResumen();
						
						if (seccionBusqueda) seccionBusqueda.classList.add('hidden');
						if (seccionResumen) seccionResumen.classList.remove('hidden');
					} 

					// === CASO 2: OCUPAR (VALIDACIÓN EN FRONTEND) ===
					else if (modoAccion === 'OCUPAR') {
						console.log("--> Validando conflictos localmente...");

						// A. Escaneamos el DOM buscando celdas RESERVADAS dentro de la selección
						const celdasConflictivas = document.querySelectorAll('.celda-interactiva.in-range[data-estado="RESERVADA"]');
						const conflictosDetectados = [];
						const habitacionesProcesadas = new Set();

						celdasConflictivas.forEach(td => {
							const hab = td.dataset.habitacion;
							
							// Evitamos duplicar la misma habitación varias veces
							if (!habitacionesProcesadas.has(hab)) {
								habitacionesProcesadas.add(hab);

								// Recuperamos datos del HTML (inyectados por Thymeleaf)
								// Nota: Usamos valores por defecto si el atributo está vacío
								conflictosDetectados.push({
									numeroHabitacion: hab,
									seleccionValida: false, // Flag para lógica interna
									
									// Datos visuales para el modal
									apellidoReserva: td.dataset.apellidosResponsable || 'Huésped',
									nombreReserva: td.dataset.nombresResponsable || 'Desconocido',
									
									// Fechas (Simplificación: tomamos la fecha de la celda clicada como referencia visual)
									// Opcional: Podrías buscar min/max fecha de este grupo si quisieras exactitud
									fechaIngreso: td.dataset.fecha, 
									fechaEgreso: td.dataset.fecha 
								});
							}
						});

						// B. Decisión
						if (conflictosDetectados.length > 0) {
							console.warn("--> Conflictos encontrados en FRONTEND:", conflictosDetectados);
							
							// Guardamos el DTO original para usarlo si el usuario da "Ocupar Igual"
							dtoPendienteDeValidacion = dto;
							
							// Mostramos el modal SIN ir al backend
							mostrarModalConflicto(conflictosDetectados);
						
						} else {
							console.log("--> Sin conflictos locales. Avanzando...");
							// Si no hay celdas reservadas seleccionadas, avanzamos directo
							iniciarFlujoOcupacion(dto, false);
						}
					} 
				});
			}
    // --- FUNCIONES DE SOPORTE PARA OCUPAR ---

    function mostrarModalConflicto(conflictos) {
        if (!listaConflictos || !modalConflicto) return;
        listaConflictos.innerHTML = '';
        
        conflictos.forEach(c => {
            const li = document.createElement('li');
            li.style.marginBottom = '10px';
            li.style.borderBottom = '1px solid #ddd';
            li.style.paddingBottom = '5px';
            li.innerHTML = `
                <strong>Habitación ${c.numeroHabitacion}</strong>: 
                Reservada por ${c.apellidoReserva || 'Desconocido'}, ${c.nombreReserva || ''} 
                <br>
                <small>(${formatearFechaCorta(c.fechaIngreso)} - ${formatearFechaCorta(c.fechaEgreso)})</small>
            `;
            listaConflictos.appendChild(li);
        });

        modalConflicto.classList.add('show');
    }

    function cerrarModalConflicto() {
        if (modalConflicto) modalConflicto.classList.remove('show');
        dtoPendienteDeValidacion = null;
    }

    function iniciarFlujoOcupacion(dto, forzarReserva) {
        // Transformamos al DTO final que espera el Wizard (OcupacionHabitacionDTO)
        const colaOcupacion = dto.map(item => ({
            numeroHabitacion: item.numeroHabitacion,
            fechaIngreso: item.fechaIngreso,
            fechaEgreso: item.fechaEgreso,
            
            // Responsable inicialmente nulo
            idHuespedResponsable: null,
            // Lista de acompañantes vacía
            idsAcompanantes: [],
            // Flag de sobre-reserva
            forzarSobreReserva: forzarReserva
        }));

        sessionStorage.setItem('colaOcupacion', JSON.stringify(colaOcupacion));
        sessionStorage.setItem('indiceOcupacionActual', '0');

        const primeraHab = colaOcupacion[0];
        const params = new URLSearchParams({
            accion: 'OCUPAR', 
            numeroHabitacion: primeraHab.numeroHabitacion,
            fechaIngreso: primeraHab.fechaIngreso,
            fechaEgreso: primeraHab.fechaEgreso
        });

        window.location.href = `/huespedes/buscar?${params.toString()}`;
    }

    // --- LISTENERS MODAL CONFLICTO ---
    if (btnConflictoVolver) {
        btnConflictoVolver.addEventListener('click', () => {
            cerrarModalConflicto();
        });
    }

    if (btnConflictoOcuparIgual) {
        btnConflictoOcuparIgual.addEventListener('click', () => {
            if (dtoPendienteDeValidacion) {
                iniciarFlujoOcupacion(dtoPendienteDeValidacion, true); // true = forzar
            }
        });
    }

    // ------------------------------------------------------------------
    // RESTO DE LISTENERS (RESERVAR)
    // ------------------------------------------------------------------
    if (btnResumenCancelar) {
        btnResumenCancelar.addEventListener('click', () => {
            limpiarSeleccionVisualTotal();
            seleccionDTO = [];
            sessionStorage.removeItem('seleccionHabitaciones');
            if (seccionResumen) seccionResumen.classList.add('hidden');
            if (seccionBusqueda) seccionBusqueda.classList.remove('hidden');
            if (leyendaContainer) leyendaContainer.classList.remove('hidden');
        });
    }

    if (btnResumenAceptar) {
        btnResumenAceptar.addEventListener('click', () => {
            if (seccionResumen) seccionResumen.classList.add('hidden');
            if (seccionDatos) seccionDatos.classList.remove('hidden');
            if (leyendaContainer) leyendaContainer.classList.add('hidden');
            if (inputApellido) inputApellido.focus();
        });
    }

    if (btnFinalCancelar && seccionDatos && seccionResumen) {
        btnFinalCancelar.addEventListener('click', () => {
            seccionDatos.classList.add('hidden');
            seccionResumen.classList.remove('hidden');
            if (leyendaContainer) leyendaContainer.classList.remove('hidden');
        });
    }

    if (formReservaFinal) {
        formReservaFinal.addEventListener('submit', async (e) => {
            e.preventDefault(); 
            const apellido = inputApellido ? inputApellido.value.trim() : '';
            const nombre = inputNombre ? inputNombre.value.trim() : '';
            const telefono = inputTelefono ? inputTelefono.value.trim() : '';
            const faltantes = [];
            if (!apellido) faltantes.push(inputApellido);
            if (!nombre) faltantes.push(inputNombre);
            if (!telefono) faltantes.push(inputTelefono);

            if (faltantes.length > 0) {
                if (msgErrorHuesped) msgErrorHuesped.classList.remove('hidden');
                if (faltantes[0]) faltantes[0].focus();
                return;
            } else {
                if (msgErrorHuesped) msgErrorHuesped.classList.add('hidden');
            }

            const habitacionesPayload = seleccionDTO.map(sel => ({
                numeroHabitacion: sel.numeroHabitacion,
                fechaIngreso: sel.fechaIngreso,
                fechaEgreso: sel.fechaEgreso
            }));

            const payloadFinal = {
                habitacionesSeleccionadas: habitacionesPayload,
                datosHuesped: { apellido, nombre, telefono }
            };

            try {
                const response = await fetch('/api/reservas/reservar', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                    body: JSON.stringify(payloadFinal)
                });

                if (response.ok) {
                    alert('¡Reserva creada con éxito!');
                    sessionStorage.removeItem('seleccionHabitaciones');
                    sessionStorage.removeItem('modoAccion');
                    window.location.href = '/';
                } else {
                    const errorData = await response.json();
                    alert('Error: ' + (errorData.message || 'Error al procesar reserva.'));
                }
            } catch (error) {
                console.error('Error de red:', error);
                alert('No se pudo conectar con el servidor.');
            }
        });
    }
});