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
  let seleccionDTO = [];      // array de SeleccionHabitacionDTO extendido (con tipoHabitacion)

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
      const checkbox = td.querySelector('input[type="checkbox"]');
      if (f >= inicio && f <= fin && td.dataset.estado === 'LIBRE') {
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
  function construirSeleccionDTO() {
    const seleccionPorHabitacion = {}; // { hab: { tipoHabitacion, fechas[] } }

    const seleccionadas = document.querySelectorAll('.celda-interactiva.in-range');
    seleccionadas.forEach(td => {
      const hab = td.dataset.habitacion;
      const fecha = td.dataset.fecha;
      const tipo = td.dataset.tipo || '';
      if (!seleccionPorHabitacion[hab]) {
        seleccionPorHabitacion[hab] = {
          tipoHabitacion: tipo,
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
        tipoHabitacion: info.tipoHabitacion
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
      const estado = td.dataset.estado;
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
      } else {
        // OCUPAR: se deja enviar el form normalmente
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
  // FORM HUÉSPED: validar y enviar (CU 9 y 9.A)
  // ------------------------------------------------------------------
  if (formReservaFinal) {
    formReservaFinal.addEventListener('submit', (e) => {
      const apellido = inputApellido ? inputApellido.value.trim() : '';
      const nombre = inputNombre ? inputNombre.value.trim() : '';
      const telefono = inputTelefono ? inputTelefono.value.trim() : '';

      const faltantes = [];
      if (!apellido) faltantes.push(inputApellido);
      if (!nombre) faltantes.push(inputNombre);
      if (!telefono) faltantes.push(inputTelefono);

      if (faltantes.length > 0) {
        e.preventDefault();
        if (msgErrorHuesped) msgErrorHuesped.classList.remove('hidden');
        if (faltantes[0]) faltantes[0].focus(); // primer campo faltante
        return;
      }

      // Agregamos los DTO como inputs hidden para el backend (SeleccionHabitacionDTO)
      seleccionDTO.forEach((sel, index) => {
        const base = `selecciones[${index}]`;

        const inputHab = document.createElement('input');
        inputHab.type = 'hidden';
        inputHab.name = `${base}.numeroHabitacion`;
        inputHab.value = sel.numeroHabitacion;
        formReservaFinal.appendChild(inputHab);

        const inputIng = document.createElement('input');
        inputIng.type = 'hidden';
        inputIng.name = `${base}.fechaIngreso`;
        inputIng.value = sel.fechaIngreso;
        formReservaFinal.appendChild(inputIng);

        const inputEgr = document.createElement('input');
        inputEgr.type = 'hidden';
        inputEgr.name = `${base}.fechaEgreso`;
        inputEgr.value = sel.fechaEgreso;
        formReservaFinal.appendChild(inputEgr);
      });

      // Limpiamos storage
      sessionStorage.removeItem('seleccionHabitaciones');
      sessionStorage.removeItem('modoAccion');
      // Se deja enviar el form a /reservas/confirmar
    });
  }

});
