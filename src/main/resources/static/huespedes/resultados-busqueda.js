console.log("resultados-busqueda.js cargado");

document.addEventListener('DOMContentLoaded', () => {

    // 1. AJUSTAR TIPO DE INPUT (Radio vs Checkbox) AUTOMÁTICAMENTE
    ajustarTipoDeSeleccion();

    // 2. BOTÓN CANCELAR
    const btnCancelar = document.getElementById('btn-cancelar');
    if (btnCancelar) {
        btnCancelar.addEventListener('click', (e) => {
            e.preventDefault(); 
            sessionStorage.removeItem('colaOcupacion');
            sessionStorage.removeItem('indiceOcupacionActual');
            sessionStorage.removeItem('modoAccion');
            sessionStorage.removeItem('seleccionHabitaciones');
            window.location.href = '/';
        });
    }

    // 3. BOTÓN SIGUIENTE
    const btnSiguiente = document.getElementById('btn-siguiente');
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', (e) => {
            e.preventDefault();

            // A. Detectar Modo
            let accion = 'BUSCAR';
            const inputAccion = document.getElementById('input-accion');
            if (inputAccion && inputAccion.value) {
                accion = inputAccion.value.split(',')[0].trim();
            } else {
                const params = new URLSearchParams(window.location.search);
                if (params.get('accion')) accion = params.get('accion').split(',')[0].trim();
            }

            // B. Obtener seleccionados
            const inputsSeleccionados = document.querySelectorAll('input[name="idSeleccionado"]:checked');

            if (inputsSeleccionados.length === 0) {
                alert("Por favor, seleccione al menos un huésped.");
                return; 
            }

            // C. Lógica según Modo
            if (accion === 'OCUPAR') {
                if (typeof gestorOcupacion !== 'undefined') {
                    
                    const listaHuespedes = Array.from(inputsSeleccionados);

                    listaHuespedes.forEach((cb, index) => {
                        const tipo = cb.dataset.tipoDoc;
                        const nro = cb.dataset.nroDoc;
                        const esUltimo = (index === listaHuespedes.length - 1);
                        // Agregamos con flag para mostrar modal solo al final
                        gestorOcupacion.agregarHuespedSeleccionado(tipo, nro, !esUltimo);
                    });

                } else {
                    alert("Error: Gestor de ocupación no cargado.");
                }

            } else {
                // === MODO NORMAL (EDITAR) ===
                // Aunque sea radio, por seguridad verificamos
                if (inputsSeleccionados.length > 1) {
                    alert("Por favor seleccione solo un huésped para editar.");
                    return;
                }

                const seleccionado = inputsSeleccionados[0];
                const urlEditar = `/huespedes/editar?tipoDocumento=${encodeURIComponent(seleccionado.dataset.tipoDoc)}&numeroDocumento=${encodeURIComponent(seleccionado.dataset.nroDoc)}`;
                window.location.href = urlEditar;
            }
        });
    }
});

/**
 * Determina si los inputs deben verse como RADIO (Selección única)
 * o CHECKBOX (Selección múltiple) basándose en el estado actual.
 */
function ajustarTipoDeSeleccion() {
    // 1. Obtener contexto
    let accion = 'BUSCAR';
    const inputAccion = document.getElementById('input-accion');
    if (inputAccion && inputAccion.value) {
        accion = inputAccion.value.split(',')[0].trim();
    } else {
        const params = new URLSearchParams(window.location.search);
        if (params.get('accion')) accion = params.get('accion').split(',')[0].trim();
    }

    let esSeleccionUnica = true; // Por defecto (Buscar/Editar) es Radio

    // 2. Analizar lógica del Wizard
    if (accion === 'OCUPAR') {
        const colaJson = sessionStorage.getItem('colaOcupacion');
        const idxStr = sessionStorage.getItem('indiceOcupacionActual');

        if (colaJson && idxStr) {
            try {
                const cola = JSON.parse(colaJson);
                const habitacionActual = cola[parseInt(idxStr)];

                if (habitacionActual) {
                    // Si YA tiene responsable asignado, ahora tocan los acompañantes -> CHECKBOX
                    if (habitacionActual.idHuespedResponsable) {
                        esSeleccionUnica = false;
                        console.log("Modo: Selección de Acompañantes (Múltiple)");
                    } else {
                        // Si NO tiene responsable, estamos eligiendo al jefe -> RADIO
                        esSeleccionUnica = true;
                        console.log("Modo: Selección de Responsable (Única)");
                    }
                }
            } catch (e) { console.error(e); }
        }
    } else {
        console.log("Modo: Búsqueda Normal (Única)");
    }

    // 3. Aplicar transformación al DOM
    const inputs = document.querySelectorAll('input[name="idSeleccionado"]');
    inputs.forEach(input => {
        // Cambiamos dinámicamente el tipo de input
        input.type = esSeleccionUnica ? 'radio' : 'checkbox';
    });
}

/**
 * Función para ordenar la tabla HTML por columnas
 */
function ordenarTabla(n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("tabla-resultados");
    switching = true;
    dir = "asc"; 

    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];

            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}