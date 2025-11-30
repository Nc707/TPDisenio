// --- CONFIGURACIÓN ---
const USAR_VALIDACION_BACKEND = true; // true = API, false = DOM

// Elementos Globales
const modalConflicto = document.getElementById('modal-conflicto-reserva');
const modalEspera = document.getElementById('modal-espera');

// Debug
if (!modalConflicto) console.error("Falta modal-conflicto-reserva");
if (!modalEspera) console.error("Falta modal-espera");

document.addEventListener('DOMContentLoaded', () => {

    // ------------------------------------------------------------------
    // 1. BOTÓN CANCELAR PRINCIPAL (Ir al Home)
    // ------------------------------------------------------------------
    const btnCancelarPrincipal = document.getElementById('open-modal-btn');
    if (btnCancelarPrincipal) {
        btnCancelarPrincipal.addEventListener('click', (e) => {
            e.preventDefault();
            console.log("Cancelando operación...");
            sessionStorage.clear();
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

    // Referencias Flujo Reservar
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

    // Referencias Modales Ocupar
    const listaConflictos = document.getElementById('lista-conflictos');
    const btnConflictoVolver = document.getElementById('btn-conflicto-volver');
    const btnConflictoOcuparIgual = document.getElementById('btn-conflicto-ocupar-igual');
    const leyendaContainer = document.querySelector('.leyenda-container');

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
        seleccionInicio = null;
    }

    function limpiarSeleccionVisualHabitacion(habitacion) {
        const tds = document.querySelectorAll(`.celda-interactiva[data-habitacion="${habitacion}"]`);
        tds.forEach(td => {
            td.classList.remove('in-range', 'selection-start');
            const chk = td.querySelector('input'); if(chk) chk.checked = false;
        });
    }

    /**
     * Valida y pinta el rango.
     * IMPORTANTE: No borra otras habitaciones.
     */
    function intentarAplicarRango(habitacion, f1, f2) {
        const tdsHab = document.querySelectorAll(`.celda-interactiva[data-habitacion="${habitacion}"]`);
        const inicio = f1 < f2 ? f1 : f2; 
        const fin = f1 < f2 ? f2 : f1;

        let rangoValido = true;
        let celdasEnRango = [];

        // 1. Escaneo
        tdsHab.forEach(td => {
            const f = td.dataset.fecha;
            if (f >= inicio && f <= fin) {
                const estado = td.dataset.estado;
                let esCeldaUtil = (estado === 'LIBRE');
                if (modoAccion === 'OCUPAR') esCeldaUtil = (estado === 'LIBRE' || estado === 'RESERVADA'); // Ocupada/FueraServicio rompen

                if (!esCeldaUtil) rangoValido = false;
                celdasEnRango.push(td);
            }
        });

        if (!rangoValido) {
            alert("Rango no válido: hay días no disponibles en el medio.");
            limpiarSeleccionVisualHabitacion(habitacion); // Limpiamos solo esta habitación
            return false;
        }

        // 2. Pintar
        celdasEnRango.forEach(td => {
            td.classList.add('in-range');
            const chk = td.querySelector('input'); if(chk) chk.checked = true;
        });
        
        return true;
    }

    // ------------------------------------------------------------------
    // INTERACCIÓN DE GRILLA (SELECCIÓN MÚLTIPLE)
    // ------------------------------------------------------------------
    celdas.forEach(td => {
        
        // A. DOBLE CLICK: Selecciona 1 día
        td.addEventListener('dblclick', (e) => {
            e.preventDefault();
            const estado = td.dataset.estado;
            const esValido = (estado === 'LIBRE') || (esModoOcupar && estado === 'RESERVADA');
            
            if (!esValido) return;

            const hab = td.dataset.habitacion;
            const fecha = td.dataset.fecha;

            // Limpiamos SOLO esta habitación para empezar de cero en esta fila
            limpiarSeleccionVisualHabitacion(hab);
            
            intentarAplicarRango(hab, fecha, fecha);
            seleccionInicio = null;
        });

        // B. CLICK SIMPLE: Rango
        td.addEventListener('click', () => {
            const estado = td.dataset.estado;
            let esClickValido = (estado === 'LIBRE') || (esModoOcupar && estado === 'RESERVADA');
            if (!esClickValido) return;

            const hab = td.dataset.habitacion;
            const fecha = td.dataset.fecha;

            // CASO 1: Deseleccionar (Si toco algo ya pintado y no estoy arrastrando)
            if (!seleccionInicio && td.classList.contains('in-range')) {
                limpiarSeleccionVisualHabitacion(hab);
                return;
            }

            // CASO 2: Inicio de selección (O cambio de habitación durante selección)
            if (!seleccionInicio || seleccionInicio.habitacion !== hab) {
                // Si cambié de habitación abruptamente, limpio la anterior que quedó a medias?
                // O mejor, limpio la NUEVA habitación para empezar limpio en ella.
                limpiarSeleccionVisualHabitacion(hab); 
                
                seleccionInicio = { habitacion: hab, fecha: fecha };
                
                // Marca visual de inicio
                td.classList.add('selection-start'); 
                td.classList.add('in-range'); 
                const chk = td.querySelector('input'); if(chk) chk.checked = true;
            } 
            
            // CASO 3: Cierre de selección (Misma habitación)
            else {
                intentarAplicarRango(hab, seleccionInicio.fecha, fecha);
                
                seleccionInicio = null;
                // Limpiamos marcas auxiliares
                document.querySelectorAll('.selection-start').forEach(el => el.classList.remove('selection-start'));
            }
        });
    });

    document.addEventListener('click', (e) => {
        // Si hago click fuera, cancelo la selección "a medias", pero no borro lo ya seleccionado
        if (!e.target.closest('.grilla-container') && seleccionInicio) {
            const sel = seleccionInicio;
            limpiarSeleccionVisualHabitacion(sel.habitacion); // Cancela esa fila
            seleccionInicio = null;
        }
    });

    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', () => {
            limpiarSeleccionVisualTotal();
            seleccionInicio = null;
            seleccionDTO = [];
            sessionStorage.clear();
        });
    }

    // ------------------------------------------------------------------
    // DTO BUILDER (+1 DÍA EGRESO)
    // ------------------------------------------------------------------
    function construirSeleccionDTO() {
        const seleccionPorHabitacion = {};
        // Buscamos TODAS las celdas seleccionadas de TODAS las habitaciones
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
                    tipoHabitacion: tipo, idEstado, nombres, apellidos, fechas: []
                };
            }
            seleccionPorHabitacion[hab].fechas.push(fecha);
        });

        const resultado = [];
        Object.keys(seleccionPorHabitacion).forEach(hab => {
            const info = seleccionPorHabitacion[hab];
            const fechasOrdenadas = info.fechas.slice().sort();
            
            // +1 DÍA LOGIC
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
                // Extra data para validación
                idEstado: info.idEstado,
                nombresResponsable: info.nombres,
                apellidosResponsable: info.apellidos,
                
                // BACKEND: Necesitamos info de reserva si validamos por backend?
                // El backend busca por habitación/fecha, pero si necesitamos pasar datos extra:
                hayReservaPrevia: (info.idEstado !== null) // Helper flag
            });
        });
        return resultado;
    }

    // ------------------------------------------------------------------
    // PANTALLA DE ESPERA Y REDIRECCIÓN
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
    // BOTÓN SIGUIENTE (VALIDACIÓN Y FLUJO)
    // ------------------------------------------------------------------
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', async (e) => {
            e.preventDefault();
            console.log(`--> Click Siguiente. Modo: ${modoAccion}`);

            const dto = construirSeleccionDTO();
            if (dto.length === 0) { alert('Debe seleccionar al menos una habitación.'); return; }

            sessionStorage.setItem('modoAccion', modoAccion);

            // --- CASO RESERVAR ---
            if (modoAccion === 'RESERVAR') {
                sessionStorage.setItem('seleccionHabitaciones', JSON.stringify(dto));
                seleccionDTO = dto;
                renderResumen();
                seccionBusqueda.classList.add('hidden');
                seccionResumen.classList.remove('hidden');
            } 
            
            // --- CASO OCUPAR (VALIDACIÓN BACKEND RESTAURADA) ---
            else if (modoAccion === 'OCUPAR') {
                
                if (USAR_VALIDACION_BACKEND) {
                    console.log("[BACKEND] Validando ocupaciones...");
                    try {
                        const payload = { ocupaciones: dto };
                        const response = await fetch('/api/estadias/validar-ocupaciones', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(payload)
                        });

                        if (!response.ok) throw new Error('Error en validación backend');
                        
                        const data = await response.json();
                        // Filtramos las que tienen "hayReserva: true"
                        const conflictosBackend = data.resultados.filter(r => r.hayReserva);

                        if (conflictosBackend.length > 0) {
                            // Mapeamos para el modal
                            const conflictosMapeados = conflictosBackend.map(c => ({
                                numeroHabitacion: c.numeroHabitacion,
                                apellidoReserva: c.apellidoReserva,
                                nombreReserva: c.nombreReserva,
                                fechaIngreso: c.fechaInicioReserva,
                                fechaEgreso: c.fechaFinReserva
                            }));
                            
                            dtoPendienteDeValidacion = dto;
                            mostrarModalConflicto(conflictosMapeados);
                        } else {
                            // Todo limpio -> Pantalla espera
                            mostrarPantallaEsperaYRedirigir(dto, false);
                        }

                    } catch (err) {
                        console.error("Error backend:", err);
                        alert("Error de conexión al validar disponibilidad.");
                    }

                } else {
                    // --- FALLBACK DOM (Viejo) ---
                    const celdasConflictivas = document.querySelectorAll('.in-range[data-estado="RESERVADA"]');
                    if (celdasConflictivas.length > 0) {
                        const conflictos = [];
                        const set = new Set();
                        celdasConflictivas.forEach(td => {
                            if(!set.has(td.dataset.habitacion)){
                                set.add(td.dataset.habitacion);
                                conflictos.push({
                                    numeroHabitacion: td.dataset.habitacion,
                                    apellidoReserva: td.dataset.apellidosResponsable,
                                    nombreReserva: td.dataset.nombresResponsable,
                                    fechaIngreso: td.dataset.fecha, 
                                    fechaEgreso: td.dataset.fecha
                                });
                            }
                        });
                        dtoPendienteDeValidacion = dto;
                        mostrarModalConflicto(conflictos);
                    } else {
                        mostrarPantallaEsperaYRedirigir(dto, false);
                    }
                }
            } 
        });
    }

    // ------------------------------------------------------------------
    // MODALES Y HELPERS DE FORMATO
    // ------------------------------------------------------------------
    function formatearFechaConHora(iso, hora) {
        const f = new Date(iso + 'T00:00:00');
        return `${f.toLocaleDateString('es-AR', {weekday:'long', day:'2-digit', month:'2-digit'})}, ${hora}`;
    }
    
    function formatearFechaCorta(iso) { 
        if (!iso) return '';
        const parts = iso.split('-'); 
        return `${parts[2]}/${parts[1]}/${parts[0].slice(2)}`;
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

    function mostrarModalConflicto(conflictos) {
        if (!listaConflictos || !modalConflicto) return;
        listaConflictos.innerHTML = '';
        conflictos.forEach(c => {
            const li = document.createElement('li');
            li.style.marginBottom = '15px'; li.style.borderBottom = '1px solid #ddd';
            li.innerHTML = `
                <div style="font-size: 0.95em;">Habitación <strong>${c.numeroHabitacion}</strong> ocupada:</div>
                <div style="font-weight: bold; color: #555;">${formatearFechaCorta(c.fechaIngreso)} al ${formatearFechaCorta(c.fechaEgreso)}</div>
                <div style="font-style: italic;">Reserva: ${c.nombreReserva || ''} ${c.apellidoReserva || ''}</div>`;
            listaConflictos.appendChild(li);
        });
        modalConflicto.classList.add('show');
    }

    function cerrarModalConflicto() { if(modalConflicto) modalConflicto.classList.remove('show'); dtoPendienteDeValidacion = null; }
    if (btnConflictoVolver) btnConflictoVolver.addEventListener('click', cerrarModalConflicto);
    
    if (btnConflictoOcuparIgual) {
        btnConflictoOcuparIgual.addEventListener('click', () => {
            if (dtoPendienteDeValidacion) {
                const copia = dtoPendienteDeValidacion; 
                cerrarModalConflicto(); 
                mostrarPantallaEsperaYRedirigir(copia, true); 
            }
        });
    }

    // --- RESTO LISTENERS (RESERVAR) ---
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
            if (!apellido || !nombre || !telefono) { msgErrorHuesped.classList.remove('hidden'); return; }
            msgErrorHuesped.classList.add('hidden');

            const payload = { habitacionesSeleccionadas: seleccionDTO, datosHuesped: { apellido, nombre, telefono } };
            try {
                const res = await fetch('/api/reservas/reservar', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
                if (res.ok) { alert('¡Reserva creada con éxito!'); sessionStorage.clear(); window.location.href = '/'; }
                else { const err = await res.json(); alert('Error: ' + (err.message || 'Error desconocido')); }
            } catch (error) { console.error(error); alert('Error de conexión.'); }
        });
    }
});