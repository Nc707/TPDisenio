document.addEventListener('DOMContentLoaded', () => {
    let seleccionInicio = null; // Guarda el primer clic: { habitacion, fecha, elemento }

    const celdas = document.querySelectorAll('.celda-interactiva');
    const btnLimpiar = document.getElementById('reset-btn');

    // --- 1. Lógica de "Click Fuera" (Cancelar modo selección) ---
    document.addEventListener('click', (e) => {
        // Si el clic NO fue dentro de una celda interactiva Y hay una selección iniciada
        const clicEnCelda = e.target.closest('.celda-interactiva');
        
        if (!clicEnCelda && seleccionInicio) {
            console.log("Clic fuera detectado: Cancelando modo selección.");
            limpiarEstiloInicio();
            seleccionInicio = null;
        }
    });

    // --- 2. Lógica del Botón LIMPIAR (Reset total) ---
    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', () => {
            limpiarTodo();
        });
    }

    // --- 3. Lógica de Clic en Celdas ---
    celdas.forEach(celda => {
        celda.addEventListener('click', (e) => {
            // Detenemos la propagación para que este clic no dispare el listener del document inmediatamente
            // (Aunque la lógica de closest() arriba ya lo maneja, esto es doble seguridad)
            // e.stopPropagation(); 

            const contenido = celda.querySelector('.cell-content');
            const estado = contenido.getAttribute('data-estado');
            const habitacion = celda.getAttribute('data-habitacion');
            const fechaStr = celda.getAttribute('data-fecha');

            // Validación básica: No se puede clicar en ocupado
            if (estado !== 'LIBRE') {
                alert("No puedes seleccionar una fecha ocupada.");
                return;
            }

            // --- A. PRIMER CLIC (Inicio de un rango nuevo) ---
            if (!seleccionInicio) {
                
                // REGLA NUEVA: Si esta habitación ya tenía algo seleccionado, lo borramos.
                // (Porque las reservas deben ser consecutivas, no puede haber 2 rangos en la misma hab)
                limpiarSeleccionDeHabitacion(habitacion);

                // También limpiamos cualquier "inicio" visual huérfano de otra habitación
                limpiarEstiloInicio(); 

                seleccionInicio = {
                    habitacion: habitacion,
                    fecha: new Date(fechaStr),
                    elemento: celda
                };
                
                // Marcar visualmente el inicio (círculo oscuro)
                celda.classList.add('selection-start');
                return;
            }

            // --- B. SEGUNDO CLIC (Fin del rango) ---
            
            // Validar que sea la MISMA habitación
            if (habitacion !== seleccionInicio.habitacion) {
                // Si el usuario cambia de carril (habitación), asumimos que quiere
                // empezar de nuevo en esta nueva habitación.
                
                limpiarEstiloInicio();
                
                // Limpiamos lo que hubiera en ESTA nueva habitación
                limpiarSeleccionDeHabitacion(habitacion);

                // Iniciamos la selección aquí
                seleccionInicio = {
                    habitacion: habitacion,
                    fecha: new Date(fechaStr),
                    elemento: celda
                };
                celda.classList.add('selection-start');
                return;
            }

            // Calcular rango (ordenar fecha inicio y fin)
            const fechaFin = new Date(fechaStr);
            const fechaInicio = seleccionInicio.fecha;

            let start = fechaInicio < fechaFin ? fechaInicio : fechaFin;
            let end = fechaInicio < fechaFin ? fechaFin : fechaInicio;

            // Validar y Marcar
            if (validarYMarcarRango(habitacion, start, end)) {
                // Éxito
                limpiarEstiloInicio();
                seleccionInicio = null; // Reseteamos para permitir seleccionar otra habitación distinta
            } else {
                alert("El rango seleccionado contiene fechas no disponibles.");
                limpiarEstiloInicio();
                seleccionInicio = null;
            }
        });
    });

    // --- Funciones Auxiliares ---

    function limpiarTodo() {
        document.querySelectorAll('.hidden-checkbox').forEach(cb => cb.checked = false);
        document.querySelectorAll('.in-range').forEach(el => el.classList.remove('in-range'));
        limpiarEstiloInicio();
        seleccionInicio = null;
    }

    /**
     * Limpia la selección existente SOLO para una habitación específica.
     * Útil para garantizar que solo haya un rango consecutivo por habitación.
     */
    function limpiarSeleccionDeHabitacion(nroHabitacion) {
        // Buscamos todas las celdas de esta habitación
        const celdasHab = document.querySelectorAll(`.celda-interactiva[data-habitacion="${nroHabitacion}"]`);
        
        celdasHab.forEach(celda => {
            // Quitamos la clase visual
            celda.classList.remove('in-range');
            // Desmarcamos el checkbox
            const checkbox = celda.querySelector('.hidden-checkbox');
            if (checkbox) checkbox.checked = false;
        });
    }

    function limpiarEstiloInicio() {
        document.querySelectorAll('.selection-start').forEach(el => el.classList.remove('selection-start'));
    }

    function validarYMarcarRango(nroHabitacion, dateStart, dateEnd) {
        const celdasDeLaHabitacion = document.querySelectorAll(`.celda-interactiva[data-habitacion="${nroHabitacion}"]`);
        let esValido = true;
        let celdasASeleccionar = [];

        celdasDeLaHabitacion.forEach(celda => {
            const fechaStr = celda.getAttribute('data-fecha');
            const fechaCelda = new Date(fechaStr);
            
            if (fechaCelda >= dateStart && fechaCelda <= dateEnd) {
                const contenido = celda.querySelector('.cell-content');
                const estado = contenido.getAttribute('data-estado');

                if (estado !== 'LIBRE') {
                    esValido = false;
                } else {
                    celdasASeleccionar.push(celda);
                }
            }
        });

        if (esValido) {
            celdasASeleccionar.forEach(celda => {
                celda.classList.add('in-range');
                const checkbox = celda.querySelector('.hidden-checkbox');
                if (checkbox) checkbox.checked = true;
            });
            return true;
        } else {
            return false;
        }
    }
});