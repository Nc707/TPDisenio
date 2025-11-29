/**
 * gestor-ocupacion.js
 * Lógica central para el Wizard de Ocupación (CU15).
 */
const gestorOcupacion = {

    /**
     * Guarda el huésped seleccionado en la habitación actual en memoria (sessionStorage).
     * Luego muestra el modal de decisión.
     */
		agregarHuespedSeleccionado: function(tipoDoc, nroDoc) {
		        const colaJson = sessionStorage.getItem('colaOcupacion');
		        const idxStr = sessionStorage.getItem('indiceOcupacionActual');

		        if (!colaJson || !idxStr) return;

		        const cola = JSON.parse(colaJson);
		        const idx = parseInt(idxStr);

		        if (cola[idx]) {
		            // Creamos el DTO de Identificación
		            const huespedDTO = {
		                tipoDocumento: tipoDoc,
		                numeroDocumento: nroDoc
		            };

		            // === CAMBIO AQUÍ ===
		            if (!cola[idx].idHuespedResponsable) {
		                // Si no hay responsable asignado, este ES el responsable
		                cola[idx].idHuespedResponsable = huespedDTO;
		                console.log("Asignado como Responsable");
		            } else {
		                // Si ya hay responsable, este es un acompañante
		                if (!cola[idx].idsAcompanantes) {
		                    cola[idx].idsAcompanantes = [];
		                }
		                cola[idx].idsAcompanantes.push(huespedDTO);
		                console.log("Asignado como Acompañante");
		            }

		            // Guardamos cambios
		            sessionStorage.setItem('colaOcupacion', JSON.stringify(cola));

		            // Mostramos el modal
		            this.mostrarModalAccion();
		        }
		    },

    mostrarModalAccion: function() {
        const modal = document.getElementById('modal-accion');
        if (modal) modal.classList.add('show');
    },

    /**
     * Botón "SEGUIR": Vuelve a buscar para la misma habitación (agregar otro acompañante).
     */
    seguirCargando: function() {
        document.getElementById('modal-accion').classList.remove('show');
        
        // Recargamos la búsqueda con los mismos parámetros de habitación
        // para permitir buscar otra persona para la misma habitación.
        // Limpiamos los campos de texto del form en la URL si queremos una búsqueda limpia,
        // o simplemente volvemos al controller 'buscar'.
        const params = new URLSearchParams(window.location.search);
        
        // Mantenemos: accion, numeroHabitacion, fechas.
        // Quitamos: apellido, nombre, dni (para que busque de nuevo limpio)
        // Nota: Esto depende de cómo quieras la UX, aquí redirecciono limpio:
        const cleanParams = new URLSearchParams();
        ['accion', 'numeroHabitacion', 'fechaIngreso', 'fechaEgreso'].forEach(key => {
            if(params.has(key)) cleanParams.append(key, params.get(key));
        });

        window.location.href = `/huespedes/buscar?${cleanParams.toString()}`;
    },

    /**
     * Botón "CARGAR OTRA HABITACIÓN": Avanza al siguiente ítem de la cola.
     */
    cargarOtraHabitacion: function() {
        const cola = JSON.parse(sessionStorage.getItem('colaOcupacion') || '[]');
        let idx = parseInt(sessionStorage.getItem('indiceOcupacionActual') || '0');

        idx++; // Avanzamos

        if (idx < cola.length) {
            // Hay otra habitación
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
            // No hay más habitaciones (Error visual)
            document.getElementById('modal-accion').classList.remove('show');
            document.getElementById('modal-error').classList.add('show');
        }
    },

    /**
     * Botón "SALIR": Finaliza el proceso y envía al Backend.
     */
		finalizarSalir: async function() {
		        const cola = JSON.parse(sessionStorage.getItem('colaOcupacion') || '[]');

		        if (cola.length === 0) {
		            alert("No hay datos para procesar.");
		            return;
		        }

		        // Validación simple Frontend
		        for (let i = 0; i < cola.length; i++) {
		            if (!cola[i].idHuespedResponsable) {
		                alert(`La habitación ${cola[i].numeroHabitacion} no tiene un Huésped Responsable asignado.`);
		                return;
		            }
		        }

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
                sessionStorage.removeItem('colaOcupacion');
                sessionStorage.removeItem('indiceOcupacionActual');
                sessionStorage.removeItem('modoAccion');
                sessionStorage.removeItem('seleccionHabitaciones');

                alert(data.message || 'Ocupación registrada con éxito');
                window.location.href = '/'; 
            } else {
                const errorData = await response.json();
                alert('Error: ' + (errorData.message || 'Error desconocido'));
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error de conexión con el servidor.');
        }
    }
};