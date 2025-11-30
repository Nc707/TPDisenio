// --- CONFIGURACIÓN DE DEPURACIÓN ---
// true = Llama al backend (/api/estadias/validar-ocupaciones)
// false = Valida leyendo el HTML (atributos data-)
const USAR_VALIDACION_BACKEND = true; 

// Elementos del Modal
const modalConflicto = document.getElementById('modal-conflicto-reserva');
const modalEspera = document.getElementById('modal-espera');

// --- DEBUG ---
if (!modalConflicto) console.error("¡ERROR! No se encuentra 'modal-conflicto-reserva'.");
if (!modalEspera) console.error("¡ERROR! No se encuentra 'modal-espera'.");

document.addEventListener('DOMContentLoaded', () => {
    
    // ------------------------------------------------------------------
    // 1. BOTÓN CANCELAR PRINCIPAL (Ir al Home)
    // ------------------------------------------------------------------
    const btnCancelarPrincipal = document.getElementById('open-modal-btn');
    if (btnCancelarPrincipal) {
        btnCancelarPrincipal.addEventListener('click', (e) => {
            e.preventDefault();
            console.log("Cancelando operación...");
            sessionStorage.clear(); // Limpia todo por seguridad
            try { limpiarSeleccionVisualTotal(); } catch(e) {}
            window.location.href = '/';
        });
    }

    // ------------------------------------------------------------------
    // VARIABLES Y REFERENCIAS
    // ------------------------------------------------------------------
    const modoAccion = document.body.dataset.modo || 'RESERVAR';
    const esSoloDisponibilidad = (modoAccion === 'DISPONIBILIDAD');
    const esModoOcupar = (modoAccion === 'OCUPAR');

    const celdas = document.querySelectorAll('.celda-interactiva');
    const btnLimpiar = document.getElementById('reset-btn');
    const formSeleccion = document.getElementById('form-seleccion');
    const btnSiguiente = document.getElementById('btn-siguiente');

    // Referencias a Secciones (Reservar)
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

    // Referencias a Modales (Ocupar)
    const listaConflictos = document.getElementById('lista-conflictos');
    const btnConflictoVolver = document.getElementById('btn-conflicto-volver');
    const btnConflictoOcuparIgual = document.getElementById('btn-conflicto-ocupar-igual');

    let dtoPendienteDeValidacion = null;
    let seleccionInicio = null; 
    let seleccionDTO = [];      

    if (esSoloDisponibilidad || !formSeleccion) return;

    // ------------------------------------------------------------------
    // HELPERS VISUALES
    // ------------------------------------------------------------------
    function limpiarSeleccionVisualTotal() {
        celdas.forEach(td => {
            td.classList.remove('in-range', 'selection-start');
            const chk = td.querySelector('input'); if(chk) chk.checked = false;
        });
    }
    
    function limpiarSeleccionVisualHabitacion(habitacion) {
        document.querySelectorAll(`.celda-interactiva[data-habitacion="${habitacion}"]`)
            .forEach(td => {
                td.classList.remove('in-range', 'selection-start');
                const chk = td.querySelector('input'); if(chk) chk.checked = false;
            });
    }

    function aplicarRangoSeleccion(habitacion, f1, f2) {
        const tdsHab = document.querySelectorAll(`.celda-interactiva[data-habitacion="${habitacion}"]`);
        const inicio = f1 < f2 ? f1 : f2; 
        const fin = f1 < f2 ? f2 : f1;

        tdsHab.forEach(td => {
            const f = td.dataset.fecha;
            const estado = td.dataset.estado;
            const checkbox = td.querySelector('input');

            let esSeleccionable = (estado === 'LIBRE');
            if (esModoOcupar && estado === 'RESERVADA') esSeleccionable = true;

            if (f >= inicio && f <= fin && esSeleccionable) {
                td.classList.add('in-range');
                if (checkbox) checkbox.checked = true;
            } else {
                td.classList.remove('in-range', 'selection-start');
                if (checkbox) checkbox.checked = false;
            }
        });
        
        // Marcar inicio visual
        const celdaInicio = document.querySelector(`.celda-interactiva[data-habitacion="${habitacion}"][data-fecha="${inicio}"]`);
        if (celdaInicio) celdaInicio.classList.add('selection-start');
    }

    // ------------------------------------------------------------------
    // CONSTRUCCIÓN DTO (Con Egreso + 1 día)
    // ------------------------------------------------------------------
    function construirSeleccionDTO() {
        const seleccionPorHabitacion = {};
        const seleccionadas = document.querySelectorAll('.celda-interactiva.in-range');

        seleccionadas.forEach(td => {
            const hab = td.dataset.habitacion;
            const fecha = td.dataset.fecha;
            const tipo = td.dataset.tipo || '';
            const idEstado = td.dataset.idEstado || null;
            // Datos del DOM (solo usados si la validación es frontend)
            const nombres = td.dataset.nombresResponsable || '';
            const apellidos = td.dataset.apellidosResponsable || '';

            if (!seleccionPorHabitacion[hab]) {
                seleccionPorHabitacion[hab] = {
                    tipoHabitacion: tipo, idEstado, nombres, apellidos, fechas: []
                };
            }
            seleccionPorHabitacion[hab].fechas.push(fecha);
        });

        const resultado = [];
        Object.keys(seleccionPorHabitacion).forEach(hab => {
            const info = seleccionPorHabitacion[hab];
            const fechasOrdenadas = info.fechas.slice().sort();
            
            // Calculamos fecha de egreso (Ultima fecha + 1 día)
            const ultimaFechaStr = fechasOrdenadas[fechasOrdenadas.length - 1];
            const fechaObj = new Date(ultimaFechaStr + 'T00:00:00'); 
            fechaObj.setDate(fechaObj.getDate() + 1);
            
            const year = fechaObj.getFullYear();
            const month = String(fechaObj.getMonth() + 1).padStart(2, '0');
            const day = String(fechaObj.getDate()).padStart(2, '0');
            const fechaEgresoCalc = `${year}-${month}-${day}`;

            resultado.push({
                numeroHabitacion: parseInt(hab, 10),
                fechaIngreso: fechasOrdenadas[0],
                fechaEgreso: fechaEgresoCalc,
                tipoHabitacion: info.tipoHabitacion,
                // Datos extra para validación frontend
                idEstado: info.idEstado,
                nombresResponsable: info.nombres,
                apellidosResponsable: info.apellidos
            });
        });
        return resultado;
    }

    // Helpers Formato Fecha
    function formatearFechaConHora(iso, hora) { /* para resumen */
        const f = new Date(iso + 'T00:00:00');
        return `${f.toLocaleDateString('es-AR', {weekday:'long', day:'2-digit', month:'2-digit'})}, ${hora}`;
    }
    
    function formatearFechaCorta(iso) { /* para modal conflicto (dd/mm) */
        if (!iso) return '';
        const parts = iso.split('-'); // yyyy-mm-dd
        return `${parts[2]}/${parts[1]}/${parts[0].slice(2)}`; // dd/mm/yy
    }

    function renderResumen() {
        if (!tbodyResumen) return;
        tbodyResumen.innerHTML = '';
        seleccionDTO.forEach(sel => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${sel.tipoHabitacion}</td>
                            <td>${formatearFechaConHora(sel.fechaIngreso, '12:00hs')}</td>
                            <td>${formatearFechaConHora(sel.fechaEgreso, '10:00hs')}</td>`;
            tbodyResumen.appendChild(tr);
        });
    }

    // ------------------------------------------------------------------
    // PANTALLA DE ESPERA (Intro)
    // ------------------------------------------------------------------
    function mostrarPantallaEsperaYRedirigir(dto, forzar) {
        if (!modalEspera) {
            iniciarFlujoOcupacion(dto, forzar);
            return;
        }
        modalEspera.classList.add('show');
        
        const alPresionarTecla = (e) => {
            document.removeEventListener('keydown', alPresionarTecla);
            document.removeEventListener('click', alPresionarTecla);
            iniciarFlujoOcupacion(dto, forzar);
        };
        setTimeout(() => {
            document.addEventListener('keydown', alPresionarTecla);
            document.addEventListener('click', alPresionarTecla);
        }, 500); 
    }

    // ------------------------------------------------------------------
    // BOTÓN SIGUIENTE (Lógica Central)
    // ------------------------------------------------------------------
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', async (e) => {
            e.preventDefault();
            console.log(`--> Click Siguiente. Modo: ${modoAccion}`);

            const dto = construirSeleccionDTO();
            if (dto.length === 0) { alert('Debe seleccionar al menos una habitación.'); return; }

            sessionStorage.setItem('modoAccion', modoAccion);

            // --- FLUJO RESERVAR ---
            if (modoAccion === 'RESERVAR') {
                sessionStorage.setItem('seleccionHabitaciones', JSON.stringify(dto));
                seleccionDTO = dto;
                renderResumen();
                seccionBusqueda.classList.add('hidden');
                seccionResumen.classList.remove('hidden');
            } 
            
            // --- FLUJO OCUPAR ---
            else if (modoAccion === 'OCUPAR') {
                
                if (USAR_VALIDACION_BACKEND) {
                    // ==========================================
                    // A. VALIDACIÓN VÍA BACKEND (API)
                    // ==========================================
                    console.log("%c [BACKEND] Validando ocupaciones...", "color: cyan; font-weight: bold;");
                    
                    try {
                        const payload = { ocupaciones: dto };
                        console.log("Request:", JSON.stringify(payload, null, 2));

                        const response = await fetch('/api/estadias/validar-ocupaciones', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(payload)
                        });

                        if (!response.ok) throw new Error('Error en validación backend');

                        const data = await response.json();
                        console.log("Response:", data);

                        // Filtramos las que tienen reserva (conflictos)
                        // Según tu DTO: hayReserva = true implica conflicto
                        const conflictosBackend = data.resultados.filter(r => r.hayReserva);

                        if (conflictosBackend.length > 0) {
                            console.warn("Conflictos encontrados (Backend):", conflictosBackend);
                            
                            // Mapeamos al formato que usa el modal
                            const conflictosMapeados = conflictosBackend.map(c => ({
                                numeroHabitacion: c.numeroHabitacion,
                                apellidoReserva: c.apellidoReserva,
                                nombreReserva: c.nombreReserva,
                                fechaIngreso: c.fechaInicioReserva, // Fecha de la reserva que estorba
                                fechaEgreso: c.fechaFinReserva
                            }));

                            dtoPendienteDeValidacion = dto;
                            mostrarModalConflicto(conflictosMapeados);
                        } else {
                            console.log("Validación Exitosa (Sin reservas previas).");
                            mostrarPantallaEsperaYRedirigir(dto, false);
                        }

                    } catch (err) {
                        console.error("Error conectando con backend:", err);
                        alert("Error de conexión al validar disponibilidad.");
                    }

                } else {
                    // ==========================================
                    // B. VALIDACIÓN VÍA FRONTEND (DOM)
                    // ==========================================
                    console.log("%c [FRONTEND] Validando en DOM...", "color: orange; font-weight: bold;");
                    
                    const celdasConflictivas = document.querySelectorAll('.in-range[data-estado="RESERVADA"]');
                    const conflictosDetectados = [];
                    const procesadas = new Set();

                    celdasConflictivas.forEach(td => {
                        const hab = td.dataset.habitacion;
                        if (!procesadas.has(hab)) {
                            procesadas.add(hab);
                            conflictosDetectados.push({
                                numeroHabitacion: hab,
                                apellidoReserva: td.dataset.apellidosResponsable || 'Huésped',
                                nombreReserva: td.dataset.nombresResponsable || 'Desconocido',
                                fechaIngreso: td.dataset.fecha, 
                                fechaEgreso: td.dataset.fecha 
                            });
                        }
                    });

                    if (conflictosDetectados.length > 0) {
                        console.warn("Conflictos encontrados (Frontend):", conflictosDetectados);
                        dtoPendienteDeValidacion = dto;
                        mostrarModalConflicto(conflictosDetectados);
                    } else {
                        console.log("Validación Exitosa.");
                        mostrarPantallaEsperaYRedirigir(dto, false);
                    }
                }
            } 
        });
    }

    // ------------------------------------------------------------------
    // MODAL DE CONFLICTO (Visualización)
    // ------------------------------------------------------------------
    function mostrarModalConflicto(conflictos) {
        if (!listaConflictos || !modalConflicto) return;
        listaConflictos.innerHTML = '';
        
        conflictos.forEach(c => {
            const li = document.createElement('li');
            li.style.marginBottom = '15px';
            li.style.borderBottom = '1px solid #ddd';
            li.style.paddingBottom = '10px';
            
            // Formato parecido a tu imagen
            li.innerHTML = `
                <div style="font-size: 0.95em; color: #333;">
                    Habitación <strong>${c.numeroHabitacion}</strong> ocupada los días:
                </div>
                <div style="margin-top:5px; font-weight: bold; color: #555;">
                    ${formatearFechaCorta(c.fechaIngreso)} al ${formatearFechaCorta(c.fechaEgreso)}
                </div>
                <div style="margin-top:2px; font-style: italic;">
                    Reserva a nombre de: 
                    <span style="text-transform: uppercase;">
                        ${c.nombreReserva || ''} ${c.apellidoReserva || ''}
                    </span>
                </div>
            `;
            listaConflictos.appendChild(li);
        });

        modalConflicto.classList.add('show');
    }

    function cerrarModalConflicto() {
        if (modalConflicto) modalConflicto.classList.remove('show');
        dtoPendienteDeValidacion = null;
    }

    // Botones del Modal Conflicto
    if (btnConflictoVolver) btnConflictoVolver.addEventListener('click', cerrarModalConflicto);
    
    if (btnConflictoOcuparIgual) {
        btnConflictoOcuparIgual.addEventListener('click', () => {
            if (dtoPendienteDeValidacion) {
                const copiaDatos = dtoPendienteDeValidacion; // Evitar null pointer al cerrar
                cerrarModalConflicto();
                mostrarPantallaEsperaYRedirigir(copiaDatos, true);
            }
        });
    }

    // ------------------------------------------------------------------
    // REDIRECCIÓN FINAL (Wizard)
    // ------------------------------------------------------------------
    function iniciarFlujoOcupacion(dto, forzarReserva) {
        const colaOcupacion = dto.map(item => ({
            numeroHabitacion: item.numeroHabitacion,
            fechaIngreso: item.fechaIngreso,
            fechaEgreso: item.fechaEgreso,
            idHuespedResponsable: null,
            idsAcompanantes: [],
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

    // ------------------------------------------------------------------
    // RESTO DE LISTENERS (Interacción Grilla, Reservar Final, etc.)
    // ------------------------------------------------------------------
    celdas.forEach(td => {
        td.addEventListener('click', () => {
            const estado = td.dataset.estado;
            let esClickValido = (estado === 'LIBRE') || (esModoOcupar && estado === 'RESERVADA');
            if (!esClickValido) return;

            const hab = td.dataset.habitacion;
            const fecha = td.dataset.fecha;

            if (!seleccionInicio || seleccionInicio.habitacion !== hab) {
                seleccionInicio = { habitacion: hab, fecha: fecha };
                limpiarSeleccionVisualHabitacion(hab);
                aplicarRangoSeleccion(hab, fecha, fecha);
            } else {
                aplicarRangoSeleccion(hab, seleccionInicio.fecha, fecha);
                seleccionInicio = null;
            }
        });
    });

    document.addEventListener('click', (e) => {
        if (!e.target.closest('.grilla-container') && seleccionInicio) {
            const sel = seleccionInicio;
            const celda = document.querySelector(`.celda-interactiva[data-habitacion="${sel.habitacion}"][data-fecha="${sel.fecha}"]`);
            if(celda) celda.classList.remove('selection-start');
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

    // Botones flujo Reservar
    if (btnResumenCancelar) {
        btnResumenCancelar.addEventListener('click', () => {
            limpiarSeleccionVisualTotal();
            seleccionDTO = [];
            sessionStorage.removeItem('seleccionHabitaciones');
            seccionResumen.classList.add('hidden');
            seccionBusqueda.classList.remove('hidden');
            if(leyendaContainer) leyendaContainer.classList.remove('hidden');
        });
    }
    if (btnResumenAceptar) {
        btnResumenAceptar.addEventListener('click', () => {
            seccionResumen.classList.add('hidden');
            seccionDatos.classList.remove('hidden');
            if(leyendaContainer) leyendaContainer.classList.add('hidden');
            if(inputApellido) inputApellido.focus();
        });
    }
    if (btnFinalCancelar && seccionDatos) {
        btnFinalCancelar.addEventListener('click', () => {
            seccionDatos.classList.add('hidden');
            seccionResumen.classList.remove('hidden');
            if(leyendaContainer) leyendaContainer.classList.remove('hidden');
        });
    }

    if (formReservaFinal) {
        formReservaFinal.addEventListener('submit', async (e) => {
            e.preventDefault(); 
            const apellido = inputApellido.value.trim();
            const nombre = inputNombre.value.trim();
            const telefono = inputTelefono.value.trim();
            if (!apellido || !nombre || !telefono) {
                msgErrorHuesped.classList.remove('hidden');
                return;
            }
            msgErrorHuesped.classList.add('hidden');

            const payload = {
                habitacionesSeleccionadas: seleccionDTO,
                datosHuesped: { apellido, nombre, telefono }
            };

            try {
                const res = await fetch('/api/reservas/reservar', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                if (res.ok) {
                    alert('¡Reserva creada con éxito!');
                    sessionStorage.clear();
                    window.location.href = '/';
                } else {
                    const err = await res.json();
                    alert('Error: ' + (err.message || 'Error desconocido'));
                }
            } catch (error) {
                console.error(error);
                alert('Error de conexión.');
            }
        });
    }
});