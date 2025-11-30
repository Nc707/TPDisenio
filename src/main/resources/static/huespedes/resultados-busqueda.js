console.log("resultados-busqueda.js cargado");

document.addEventListener('DOMContentLoaded', () => {

    // =========================================================
    // 1. LÓGICA DEL BOTÓN CANCELAR (Global)
    // =========================================================
    const btnCancelar = document.getElementById('btn-cancelar');
    
    if (btnCancelar) {
        btnCancelar.addEventListener('click', (e) => {
            e.preventDefault(); 
            // Limpieza de sesión si es necesario
            sessionStorage.removeItem('colaOcupacion');
            sessionStorage.removeItem('indiceOcupacionActual');
            sessionStorage.removeItem('modoAccion');
            sessionStorage.removeItem('seleccionHabitaciones');
            
            window.location.href = '/';
        });
    }

    // =========================================================
    // 2. LÓGICA DEL BOTÓN SIGUIENTE / ACEPTAR
    // =========================================================
    const btnSiguiente = document.getElementById('btn-siguiente');
    
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', (e) => {
            e.preventDefault();

            // --- A. OBTENER CONTEXTO (CORREGIDO) ---
            let accion = 'BUSCAR';
            const inputAccion = document.getElementById('input-accion');
            
            if (inputAccion && inputAccion.value) {
                // AQUÍ ESTÁ EL ARREGLO:
                // Si viene "OCUPAR,OCUPAR", hacemos split por coma y tomamos el primero.
                accion = inputAccion.value.split(',')[0].trim();
            } else {
                const params = new URLSearchParams(window.location.search);
                if (params.get('accion')) {
                    accion = params.get('accion').split(',')[0].trim();
                }
            }

            console.log("Click en Siguiente. Modo detectado (limpio):", accion);

            // B. Validar selección
            const seleccionado = document.querySelector('input[name="idSeleccionado"]:checked');

            if (!seleccionado) {
                alert("Por favor, seleccione un huésped de la lista.");
                return; 
            }

            const tipoDoc = seleccionado.dataset.tipoDoc;
            const nroDoc = seleccionado.dataset.nroDoc;

            // C. Decisión
            if (accion === 'OCUPAR') {
                // MODO WIZARD
                if (typeof gestorOcupacion !== 'undefined') {
                    gestorOcupacion.agregarHuespedSeleccionado(tipoDoc, nroDoc);
                } else {
                    alert("Error: No se cargó el gestor de ocupación.");
                }

            } else {
                // MODO NORMAL
                const urlEditar = `/huespedes/editar?tipoDocumento=${encodeURIComponent(tipoDoc)}&numeroDocumento=${encodeURIComponent(nroDoc)}`;
                window.location.href = urlEditar;
            }
        });
    }
});