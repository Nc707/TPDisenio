/**
 * gestor-ocupacion.js
 * Lógica central para el Wizard de Ocupación (CU15).
 */
const gestorOcupacion = {

    // Variable para guardar la acción a ejecutar al cerrar el modal
    _accionAlCerrar: null,

    /**
     * Guarda el huésped seleccionado en la habitación actual.
     */
    agregarHuespedSeleccionado: function(tipoDoc, nroDoc, silenciarModal = false) {
        const colaJson = sessionStorage.getItem('colaOcupacion');
        const idxStr = sessionStorage.getItem('indiceOcupacionActual');

        if (!colaJson || !idxStr) return;

        const cola = JSON.parse(colaJson);
        const idx = parseInt(idxStr);

        if (cola[idx]) {
            const huespedDTO = { tipoDocumento: tipoDoc, numeroDocumento: nroDoc };

            // Lógica Responsable vs Acompañante
            if (!cola[idx].idHuespedResponsable) {
                cola[idx].idHuespedResponsable = huespedDTO;
                console.log("Asignado como Responsable");
            } else {
                if (!cola[idx].idsAcompanantes) cola[idx].idsAcompanantes = [];
                cola[idx].idsAcompanantes.push(huespedDTO);
                console.log("Asignado como Acompañante");
            }

            sessionStorage.setItem('colaOcupacion', JSON.stringify(cola));

            if (!silenciarModal) {
                this.mostrarModalAccion();
            }
        }
    },

    mostrarModalAccion: function() {
        const modal = document.getElementById('modal-accion');
        if (modal) modal.classList.add('show');
    },

    seguirCargando: function() {
        document.getElementById('modal-accion').classList.remove('show');
        // Recargar limpio manteniendo parámetros base
        const params = new URLSearchParams(window.location.search);
        const cleanParams = new URLSearchParams();
        ['accion', 'numeroHabitacion', 'fechaIngreso', 'fechaEgreso'].forEach(key => {
            if(params.has(key)) cleanParams.append(key, params.get(key));
        });
        window.location.href = `/huespedes/buscar?${cleanParams.toString()}`;
    },

    cargarOtraHabitacion: function() {
        const cola = JSON.parse(sessionStorage.getItem('colaOcupacion') || '[]');
        let idx = parseInt(sessionStorage.getItem('indiceOcupacionActual') || '0');

        idx++; 

        if (idx < cola.length) {
            sessionStorage.setItem('indiceOcupacionActual', idx.toString());
            const nextHab = cola[idx];
            const params = new URLSearchParams({
                accion: 'OCUPAR',
                numeroHabitacion: nextHab.numeroHabitacion,
                fechaIngreso: nextHab.fechaIngreso,
                fechaEgreso: nextHab.fechaEgreso
            });
            window.location.href = `/huespedes/buscar?${params.toString()}`;
        } else {
            // Ya no quedan habitaciones, mostramos error genérico o finalizamos
            document.getElementById('modal-accion').classList.remove('show');
            document.getElementById('modal-error').classList.add('show');
        }
    },

    /**
     * Lógica principal de FINALIZAR y enviar al backend.
     */
    finalizarSalir: async function() {
        // Ocultar el modal de "¿Qué desea hacer?"
        document.getElementById('modal-accion').classList.remove('show');

        const cola = JSON.parse(sessionStorage.getItem('colaOcupacion') || '[]');

        if (cola.length === 0) {
            this._mostrarFeedback("Error", "No hay datos para procesar.", true, () => window.location.href='/');
            return;
        }

        // 1. VALIDACIÓN RECUPERABLE (Frontend)
        // Verificamos si alguna habitación quedó sin responsable
        for (let i = 0; i < cola.length; i++) {
            if (!cola[i].idHuespedResponsable) {
                // ERROR RECUPERABLE: No redirigimos (callback null), solo cerramos modal.
                this._mostrarFeedback(
                    "Faltan Datos", 
                    `La habitación ${cola[i].numeroHabitacion} no tiene un Huésped Responsable asignado.<br>Por favor, seleccione uno antes de salir.`, 
                    true, 
                    null // <--- Al ser null, se queda en la página
                );
                return; // Cortamos ejecución aquí
            }
        }

        // 2. INTENTO DE GUARDADO (Backend)
        const payload = { ocupaciones: cola };
        
        try {
            const response = await fetch('/api/estadias/ocupar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const data = await response.json();
                
                // Limpieza
                this._limpiarSesion();

                // ÉXITO: Redirigimos al menú
                this._mostrarFeedback(
                    "¡Éxito!", 
                    data.message || 'Ocupación registrada correctamente.', 
                    false, 
                    () => window.location.href = '/'
                );

            } else {
                const errorData = await response.json();
                
                // ERROR DE BACKEND (No recuperable en este paso): Redirigimos
                this._mostrarFeedback(
                    "Error del Servidor", 
                    errorData.message || 'Ocurrió un error al procesar la ocupación.', 
                    true, 
                    () => window.location.href = '/' // <--- Redirige al menú
                );
            }
        } catch (error) {
            console.error('Error:', error);
            // ERROR DE CONEXIÓN (Fatal): Redirigimos
            this._mostrarFeedback(
                "Error de Conexión", 
                "No se pudo conectar con el servidor. Intente nuevamente más tarde.", 
                true, 
                () => window.location.href = '/'
            );
        }
    },

    _limpiarSesion: function() {
        sessionStorage.removeItem('colaOcupacion');
        sessionStorage.removeItem('indiceOcupacionActual');
        sessionStorage.removeItem('modoAccion');
        sessionStorage.removeItem('seleccionHabitaciones');
    },

    /**
     * Muestra el modal sofisticado.
     * @param {string} titulo - Título del modal.
     * @param {string} mensaje - Cuerpo del mensaje (acepta HTML).
     * @param {boolean} esError - Si es true, usa estilos rojos; si no, azules.
     * @param {function} callback - Función a ejecutar al dar ACEPTAR. Si es null, solo cierra.
     */
    _mostrarFeedback: function(titulo, mensaje, esError, callback) {
        const modal = document.getElementById('modal-feedback');
        const titleContainer = document.getElementById('feedback-header-title');
        const headerBg = document.getElementById('feedback-header-bg');
        const msgContainer = document.getElementById('feedback-message');
        const btnAceptar = document.getElementById('btn-feedback-aceptar');

        if (!modal) {
            alert(mensaje); // Fallback si no está el HTML
            if(callback) callback();
            return;
        }

        // Configurar Estilos
        if (esError) {
            headerBg.style.backgroundColor = "#c0392b"; // Rojo error
            // Icono de error
            titleContainer.innerHTML = `<span style="font-size:1.5em; font-weight:900;">X</span> <span>${titulo}</span>`;
        } else {
            headerBg.style.backgroundColor = "#27ae60"; // Verde éxito (o usa el azul Premier #2c3e50)
            // Icono de check
            titleContainer.innerHTML = `<span style="font-size:1.5em; font-weight:900;">✓</span> <span>${titulo}</span>`;
        }

        // Contenido
        msgContainer.innerHTML = mensaje;

        // Guardar la acción
        this._accionAlCerrar = callback;

        // Limpiar listeners previos para evitar duplicados
        const nuevoBtn = btnAceptar.cloneNode(true);
        btnAceptar.parentNode.replaceChild(nuevoBtn, btnAceptar);

        // Listener del botón Aceptar
        nuevoBtn.addEventListener('click', () => {
            modal.classList.remove('show');
            if (gestorOcupacion._accionAlCerrar) {
                gestorOcupacion._accionAlCerrar();
            }
        });

        // Mostrar
        modal.classList.add('show');
    }
};