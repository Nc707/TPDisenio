console.log("resultados-busqueda.js cargado");

document.addEventListener('DOMContentLoaded', () => {
    
    // =========================================================
    // 1. LÓGICA DEL BOTÓN CANCELAR
    // =========================================================
    const btnCancelar = document.getElementById('btn-cancelar');
    
    if (btnCancelar) {
        btnCancelar.addEventListener('click', (e) => {
            e.preventDefault(); // Por seguridad, aunque sea type="button"

            // Verificamos el modo
            const inputAccion = document.getElementById('input-accion');
            const accion = inputAccion ? inputAccion.value : '';

            // Si estábamos en medio del Wizard (OCUPAR), limpiamos la memoria
            if (accion === 'OCUPAR') {
                console.log("Cancelando proceso de ocupación. Limpiando sessionStorage...");
                sessionStorage.removeItem('colaOcupacion');
                sessionStorage.removeItem('indiceOcupacionActual');
                sessionStorage.removeItem('modoAccion');
                sessionStorage.removeItem('seleccionHabitaciones');
            }

            // En cualquier caso (Ocupar o Buscar normal), volvemos al Home
            window.location.href = '/';
        });
    }

    // =========================================================
    // 2. LÓGICA DEL BOTÓN SIGUIENTE
    // =========================================================
    const btnSiguiente = document.getElementById('btn-siguiente');
    
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', (e) => {
            // SIEMPRE prevenimos el submit porque /huespedes/seleccionar no existe
            e.preventDefault();

            // Contexto
            const inputAccion = document.getElementById('input-accion');
            const accion = inputAccion ? inputAccion.value : '';
            
            // Validar selección
            const seleccionado = document.querySelector('input[name="idSeleccionado"]:checked');

            if (!seleccionado) {
                window.location.href = '/huespedes/formulario-alta';
								return;
            }

            // Datos del huésped
            const tipoDoc = seleccionado.dataset.tipoDoc;
            const nroDoc = seleccionado.dataset.nroDoc;

            if (accion === 'OCUPAR') {
                // === FLUJO 1: WIZARD OCUPAR ===
                gestorOcupacion.agregarHuespedSeleccionado(tipoDoc, nroDoc);

            } else {
                // === FLUJO 2: MODO NORMAL (BÚSQUEDA) ===
                // Redirigir a Editar
                const urlEditar = `/huespedes/editar?tipoDocumento=${encodeURIComponent(tipoDoc)}&numeroDocumento=${encodeURIComponent(nroDoc)}`;
                window.location.href = urlEditar;
            }
        });
    }
});
