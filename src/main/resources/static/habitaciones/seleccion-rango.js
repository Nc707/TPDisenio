document.addEventListener('DOMContentLoaded', () => {
    let seleccionInicio = null;

    const celdas = document.querySelectorAll('.celda-interactiva');
    const btnLimpiar = document.getElementById('reset-btn');
    const formSeleccion = document.getElementById('form-seleccion');

    // Click fuera cancela selección en curso
    document.addEventListener('click', (e) => {
        const clicEnCelda = e.target.closest('.celda-interactiva');
        if (!clicEnCelda && seleccionInicio) {
            limpiarEstiloInicio();
            seleccionInicio = null;
        }
    });

    // Botón LIMPIAR
    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', () => {
            limpiarTodo();
        });
    }

    // Clic en celdas (selección de rango)
    celdas.forEach(celda => {
        celda.addEventListener('click', () => {
            const contenido = celda.querySelector('.cell-content');
            const estado = contenido.getAttribute('data-estado');
            const habitacion = celda.getAttribute('data-habitacion');
            const fechaStr = celda.getAttribute('data-fecha');

            if (estado !== 'LIBRE') {
                alert("No puedes seleccionar una fecha ocupada.");
                return;
            }

            if (!seleccionInicio) {
                limpiarSeleccionDeHabitacion(habitacion);
                limpiarEstiloInicio();

                seleccionInicio = {
                    habitacion,
                    fecha: new Date(fechaStr),
                    elemento: celda
                };

                celda.classList.add('selection-start');
                return;
            }

            if (habitacion !== seleccionInicio.habitacion) {
                limpiarEstiloInicio();
                limpiarSeleccionDeHabitacion(habitacion);

                seleccionInicio = {
                    habitacion,
                    fecha: new Date(fechaStr),
                    elemento: celda
                };
                celda.classList.add('selection-start');
                return;
            }

            const fechaFin = new Date(fechaStr);
            const fechaInicio = seleccionInicio.fecha;

            const start = fechaInicio < fechaFin ? fechaInicio : fechaFin;
            const end   = fechaInicio < fechaFin ? fechaFin   : fechaInicio;

            if (validarYMarcarRango(habitacion, start, end)) {
                limpiarEstiloInicio();
                seleccionInicio = null;
            } else {
                alert("El rango seleccionado contiene fechas no disponibles.");
                limpiarEstiloInicio();
                seleccionInicio = null;
            }
        });
    });

    // Interceptar submit del form para mostrar el mensaje de confirmación
    if (formSeleccion) {
        let omitirConfirm = false;

        formSeleccion.addEventListener('submit', (e) => {
            if (omitirConfirm) return;

            e.preventDefault();

            const seleccionados = document.querySelectorAll('.hidden-checkbox:checked');
            if (seleccionados.length === 0) {
                alert('Debes seleccionar al menos una habitación antes de continuar.');
                return;
            }

            const reservasPorHabitacion = new Map();

            seleccionados.forEach(cb => {
                const valor = cb.value; // "114_2025-11-04"
                const [habitacion, fechaStr] = valor.split('_');
                const fecha = new Date(fechaStr);

                const celda = cb.closest('.celda-interactiva');
                const tipo = celda ? (celda.getAttribute('data-tipo') || '') : '';

                let info = reservasPorHabitacion.get(habitacion);
                if (!info) {
                    info = { habitacion, tipo, inicio: fecha, fin: fecha };
                    reservasPorHabitacion.set(habitacion, info);
                } else {
                    if (fecha < info.inicio) info.inicio = fecha;
                    if (fecha > info.fin) info.fin = fecha;
                }
            });

            function formatearFechaConHora(fecha, horaStr) {
                const dias = ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'];
                const nombreDia = dias[fecha.getDay()];
                const dd = String(fecha.getDate()).padStart(2, '0');
                const mm = String(fecha.getMonth() + 1).padStart(2, '0');
                const yyyy = fecha.getFullYear();
                return `${nombreDia}, ${dd}/${mm}/${yyyy}, ${horaStr}`;
            }

            let mensaje = '';
            mensaje += 'El sistema pinta las habitaciones entre el día inicial y el final con el color de "RESERVADA".\n\n';
            mensaje += 'El sistema presenta el siguiente listado para que verifiques con el huésped la reserva solicitada:\n\n';

            reservasPorHabitacion.forEach(info => {
                const ingreso = formatearFechaConHora(info.inicio, '12:00hs');
                const egreso  = formatearFechaConHora(info.fin,   '10:00hs');

                mensaje += `Habitación ${info.habitacion}`;
                if (info.tipo) mensaje += ` (${info.tipo})`;
                mensaje += `\n  ✔ Ingreso: ${ingreso}`;
                mensaje += `\n  ✔ Egreso: ${egreso}\n\n`;
            });

            mensaje += '¿Deseas confirmar y continuar?';

            if (confirm(mensaje)) {
                omitirConfirm = true;
                formSeleccion.submit();
            }
        });
    }

    // Auxiliares

    function limpiarTodo() {
        document.querySelectorAll('.hidden-checkbox').forEach(cb => cb.checked = false);
        document.querySelectorAll('.in-range').forEach(el => el.classList.remove('in-range'));
        limpiarEstiloInicio();
        seleccionInicio = null;
    }

    function limpiarSeleccionDeHabitacion(nroHabitacion) {
        const celdasHab = document.querySelectorAll(`.celda-interactiva[data-habitacion="${nroHabitacion}"]`);
        celdasHab.forEach(celda => {
            celda.classList.remove('in-range');
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
        const celdasASeleccionar = [];

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

        if (!esValido) return false;

        celdasASeleccionar.forEach(celda => {
            celda.classList.add('in-range');
            const checkbox = celda.querySelector('.hidden-checkbox');
            if (checkbox) checkbox.checked = true;
        });

        return true;
    }
});