// --- seleccion-rango.js ---

document.addEventListener('DOMContentLoaded', () => {
	const modoAccion = document.body.dataset.modo || 'RESERVAR';
	const esSoloDisponibilidad = (modoAccion === 'DISPONIBILIDAD');

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

	// 👉 Leyenda (para ocultarla en la pantalla final)
	const leyendaContainer = document.querySelector('.leyenda-container');

	// Si es solo disponibilidad, no hay selección
	if (esSoloDisponibilidad || !formSeleccion) {
		return;
	}

	// --- Estado selección por rango ---
	let seleccionInicio = null; // { habitacion, fecha }
	let seleccionDTO = [];      // array enriquecido con datos de EstadoHabitacionDTO

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
			// Nota: Asumimos que data-estado mapea al Enum EstadoHabitacion (ej: "LIBRE")
			const estado = td.dataset.estado; 
			const checkbox = td.querySelector('input[type="checkbox"]');
			
			if (f >= inicio && f <= fin && estado === 'LIBRE') {
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
	// AHORA: Soporta la estructura de EstadoHabitacionDTO (idEstado, responsables)
	function construirSeleccionDTO() {
		const seleccionPorHabitacion = {}; // { hab: { tipo, idEstado, responsable..., fechas[] } }

		const seleccionadas = document.querySelectorAll('.celda-interactiva.in-range');
		
		seleccionadas.forEach(td => {
			const hab = td.dataset.habitacion;
			const fecha = td.dataset.fecha;
			const tipo = td.dataset.tipo || '';
			
			// Nuevos datos provenientes de EstadoHabitacionDTO
			// Se leen del dataset HTML (ej: data-id-estado="1", data-nombres-responsable="Juan")
			const idEstado = td.dataset.idEstado || null;
			const nombres = td.dataset.nombresResponsable || '';
			const apellidos = td.dataset.apellidosResponsable || '';

			if (!seleccionPorHabitacion[hab]) {
				seleccionPorHabitacion[hab] = {
					tipoHabitacion: tipo,
					// Guardamos estos datos extra por si se necesitan en la UI futura
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
			const fechaIngreso = fechasOrdenadas[0];
			const fechaEgreso = fechasOrdenadas[fechasOrdenadas.length - 1];

			resultado.push({
				numeroHabitacion: parseInt(hab, 10),
				fechaIngreso: fechaIngreso,
				fechaEgreso: fechaEgreso,
				tipoHabitacion: info.tipoHabitacion,
				// Campos extra del DTO (para uso interno del front o futura funcionalidad)
				idEstado: info.idEstado,
				nombresResponsable: info.nombresResponsable,
				apellidosResponsable: info.apellidosResponsable
			});
		});

		return resultado;
	}

	// Formato "Día, dd/mm/aaaa, hh:mmhs"
	function formatearFechaConHora(fechaISO, horaTexto) {
		const fecha = new Date(fechaISO + 'T00:00:00');
		const opciones = {
			weekday: 'long',
			day: '2-digit',
			month: '2-digit',
			year: 'numeric'
		};
		let texto = fecha.toLocaleDateString('es-AR', opciones);
		texto = texto.charAt(0).toUpperCase() + texto.slice(1); // capitalizar
		return `${texto}, ${horaTexto}`;
	}

	// Render de la tabla de resumen
	function renderResumen() {
		if (!tbodyResumen) return;
		tbodyResumen.innerHTML = '';

		seleccionDTO.forEach(sel => {
			const tr = document.createElement('tr');

			const tdTipo = document.createElement('td');
			tdTipo.textContent = sel.tipoHabitacion || '';

			const tdIngreso = document.createElement('td');
			tdIngreso.textContent = formatearFechaConHora(sel.fechaIngreso, '12:00hs');

			const tdEgreso = document.createElement('td');
			tdEgreso.textContent = formatearFechaConHora(sel.fechaEgreso, '10:00hs');

			tr.appendChild(tdTipo);
			tr.appendChild(tdIngreso);
			tr.appendChild(tdEgreso);

			tbodyResumen.appendChild(tr);
		});
	}

	// ------------------------------------------------------------------
	// Listeners de celdas (selección por rango)
	// ------------------------------------------------------------------
	celdas.forEach(td => {
		td.addEventListener('click', () => {
			// Adaptado: Verifica el string del Enum (EstadoHabitacion.LIBRE)
			const estado = td.dataset.estado; 
			
			// Mantenemos la lógica de solo seleccionar celdas LIBRES
			if (estado !== 'LIBRE') return;

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

	// Click fuera de la grilla: cancela "inicio" pero deja el rango
	document.addEventListener('click', (e) => {
		const clicEnGrilla = e.target.closest('.grilla-container');
		if (!clicEnGrilla && seleccionInicio) {
			const sel = seleccionInicio;
			const selector =
				'.celda-interactiva[data-habitacion="' +
				sel.habitacion +
				'"][data-fecha="' +
				sel.fecha +
				'"]';
			const celdaInicio = document.querySelector(selector);
			if (celdaInicio) celdaInicio.classList.remove('selection-start');
			seleccionInicio = null;
		}
	});

	// ------------------------------------------------------------------
	// Botón LIMPIAR
	// ------------------------------------------------------------------
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
	// Submit del formulario de selección (SIGUIENTE)
	// ------------------------------------------------------------------
	if (formSeleccion && btnSiguiente) {
		formSeleccion.addEventListener('submit', (e) => {
			const dto = construirSeleccionDTO();

			if (dto.length === 0) {
				alert('Debe seleccionar al menos una habitación para continuar.');
				e.preventDefault();
				return;
			}

			// Guardamos en sessionStorage para consistencia
			sessionStorage.setItem('modoAccion', modoAccion);
			sessionStorage.setItem('seleccionHabitaciones', JSON.stringify(dto));

			if (modoAccion === 'RESERVAR') {
				// Interceptamos: mostramos RESUMEN
				e.preventDefault();
				seleccionDTO = dto;
				renderResumen();

				if (seccionBusqueda) seccionBusqueda.classList.add('hidden');
				if (seccionResumen) seccionResumen.classList.remove('hidden');
			} 			else if (modoAccion === 'OCUPAR') {
			    e.preventDefault();

			    // 1. TRANSFORMAR EL DTO: Adaptado al nuevo OcupacionHabitacionDTO
			    const colaOcupacion = dto.map(item => ({
			        numeroHabitacion: item.numeroHabitacion,
			        fechaIngreso: item.fechaIngreso,
			        fechaEgreso: item.fechaEgreso,
			        
			        // NUEVO CAMPO: Inicialmente nulo, se llenará con el primer seleccionado
			        idHuespedResponsable: null, 
			        
			        // Lista para los siguientes (acompañantes)
			        idsAcompanantes: [], 
			        
			        forzarSobreReserva: false
			    }));

			    // 2. GUARDAR EN SESSION STORAGE
			    sessionStorage.setItem('colaOcupacion', JSON.stringify(colaOcupacion));
			    sessionStorage.setItem('indiceOcupacionActual', '0'); // Empezamos por la primera hab

			    // 3. REDIRECCIONAR
			    const primeraHab = colaOcupacion[0];
			    const params = new URLSearchParams({
			        accion: 'OCUPAR', 
			        numeroHabitacion: primeraHab.numeroHabitacion,
			        fechaIngreso: primeraHab.fechaIngreso,
			        fechaEgreso: primeraHab.fechaEgreso
			    });

			    window.location.href = `/huespedes/buscar?${params.toString()}`;
			}
		});
	}

	// ------------------------------------------------------------------
	// RESUMEN: RECHAZAR (CU 7.A)
	// ------------------------------------------------------------------
	if (btnResumenCancelar) {
		btnResumenCancelar.addEventListener('click', () => {
			// Se deshacen operaciones sobre la grilla
			limpiarSeleccionVisualTotal();
			seleccionDTO = [];
			sessionStorage.removeItem('seleccionHabitaciones');

			if (seccionResumen) seccionResumen.classList.add('hidden');
			if (seccionBusqueda) seccionBusqueda.classList.remove('hidden');
			if (leyendaContainer) leyendaContainer.classList.remove('hidden');
		});
	}

	// ------------------------------------------------------------------
	// RESUMEN: ACEPTAR → mostrar datos huésped (CU 8)
	// ------------------------------------------------------------------
	if (btnResumenAceptar) {
		btnResumenAceptar.addEventListener('click', () => {
			if (seccionResumen) seccionResumen.classList.add('hidden');
			if (seccionDatos) seccionDatos.classList.remove('hidden');
			if (leyendaContainer) leyendaContainer.classList.add('hidden'); // ocultar leyenda
			if (inputApellido) inputApellido.focus();
		});
	}

	// ------------------------------------------------------------------
	// FORM HUÉSPED: cancelar (volver al resumen)
	// ------------------------------------------------------------------
	if (btnFinalCancelar && seccionDatos && seccionResumen) {
		btnFinalCancelar.addEventListener('click', () => {
			seccionDatos.classList.add('hidden');
			seccionResumen.classList.remove('hidden');
			if (leyendaContainer) leyendaContainer.classList.remove('hidden'); // mostrar leyenda otra vez
		});
	}

	// ------------------------------------------------------------------
	// FORM HUÉSPED: Integración con Backend REST (JSON)
	// ------------------------------------------------------------------
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

			// Construcción del JSON para ConfirmacionReservaDTO
			// IMPORTANTE: Mapeamos explícitamente solo lo que el backend de RESERVA espera.
			// Ignoramos idEstado/Responsables aquí porque ConfirmacionReservaDTO no los usa.
			const habitacionesPayload = seleccionDTO.map(sel => ({
				numeroHabitacion: sel.numeroHabitacion,
				fechaIngreso: sel.fechaIngreso,
				fechaEgreso: sel.fechaEgreso
			}));

			const datosHuespedPayload = {
				apellido: apellido,
				nombre: nombre,
				telefono: telefono
			};

			const payloadFinal = {
				habitacionesSeleccionadas: habitacionesPayload,
				datosHuesped: datosHuespedPayload
			};

			const headers = {
				'Content-Type': 'application/json',
				'Accept': 'application/json'
			};

			try {
				const response = await fetch('/api/reservas/reservar', {
					method: 'POST',
					headers: headers,
					body: JSON.stringify(payloadFinal)
				});

				if (response.ok) {
					const data = await response.json();
					alert('¡Reserva creada con éxito!');

					sessionStorage.removeItem('seleccionHabitaciones');
					sessionStorage.removeItem('modoAccion');

					window.location.href = '/';
				} else {
					const errorData = await response.json();
					const msg = errorData.message || 'Ocurrió un error al procesar la reserva.';
					alert('Error: ' + msg);
					console.error('Error backend:', errorData);
				}

			} catch (error) {
				console.error('Error de red:', error);
				alert('No se pudo conectar con el servidor.');
			}
		});
	}

});