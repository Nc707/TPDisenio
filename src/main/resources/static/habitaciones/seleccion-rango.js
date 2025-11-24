// --- seleccion-rango.js ---

document.addEventListener("DOMContentLoaded", () => {
  const modoAccion = document.body.dataset.modo || "RESERVAR";
  const esSoloDisponibilidad = (modoAccion === "DISPONIBILIDAD");

  const celdas = document.querySelectorAll(".celda-interactiva");
  const btnLimpiar = document.getElementById("reset-btn");
  const formSeleccion = document.getElementById("form-seleccion");

  // Si es solo disponibilidad, no configuramos selección ni storage
  if (esSoloDisponibilidad) {
    return;
  }

  // --- Estado interno para seleccionar rangos ---
  let seleccionInicio = null; // { habitacion, fecha }

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  function limpiarSeleccionVisualTotal() {
    celdas.forEach((td) => {
      td.classList.remove("in-range", "selection-start");
      const checkbox = td.querySelector('input[type="checkbox"]');
      if (checkbox) {
        checkbox.checked = false;
      }
    });
  }

  function limpiarSeleccionVisualHabitacion(habitacion) {
    const tdsHab = document.querySelectorAll(
      '.celda-interactiva[data-habitacion="' + habitacion + '"]'
    );
    tdsHab.forEach((td) => {
      td.classList.remove("in-range", "selection-start");
      const checkbox = td.querySelector('input[type="checkbox"]');
      if (checkbox) {
        checkbox.checked = false;
      }
    });
  }

  // Aplica el rango [fechaDesde, fechaHasta] (incluidos) para una habitación
  function aplicarRangoSeleccion(habitacion, fechaDesde, fechaHasta) {
    const tdsHab = document.querySelectorAll(
      '.celda-interactiva[data-habitacion="' + habitacion + '"]'
    );

    const inicio = fechaDesde < fechaHasta ? fechaDesde : fechaHasta;
    const fin = fechaDesde < fechaHasta ? fechaHasta : fechaDesde;

    tdsHab.forEach((td) => {
      const f = td.dataset.fecha;
      const checkbox = td.querySelector('input[type="checkbox"]');
      if (f >= inicio && f <= fin && td.dataset.estado === "LIBRE") {
        td.classList.add("in-range");
        if (checkbox) checkbox.checked = true;
      } else {
        td.classList.remove("in-range", "selection-start");
        if (checkbox) checkbox.checked = false;
      }
    });

    // Marca la celda de inicio del rango
    const selectorInicio =
      '.celda-interactiva[data-habitacion="' +
      habitacion +
      '"][data-fecha="' +
      inicio +
      '"]';
    const celdaInicio = document.querySelector(selectorInicio);
    if (celdaInicio) {
      celdaInicio.classList.add("selection-start");
    }
  }

  // Construye el array de SeleccionHabitacionDTO a partir de las celdas .in-range
  function construirSeleccionDTO() {
    const seleccionPorHabitacion = {}; // { hab: [fechas...] }

    const seleccionadas = document.querySelectorAll(".celda-interactiva.in-range");

    seleccionadas.forEach((td) => {
      const hab = td.dataset.habitacion;
      const fecha = td.dataset.fecha;
      if (!seleccionPorHabitacion[hab]) {
        seleccionPorHabitacion[hab] = [];
      }
      seleccionPorHabitacion[hab].push(fecha);
    });

    const resultado = [];

    Object.keys(seleccionPorHabitacion).forEach((hab) => {
      const fechas = seleccionPorHabitacion[hab].slice().sort(); // yyyy-MM-dd -> sort lexicográfico ok
      const fechaIngreso = fechas[0];
      const fechaEgreso = fechas[fechas.length - 1];

      resultado.push({
        numeroHabitacion: parseInt(hab, 10),
        fechaIngreso: fechaIngreso,
        fechaEgreso: fechaEgreso
      });
    });

    return resultado;
  }

  // ------------------------------------------------------------------
  // Listeners de celdas (selección por rango)
  // ------------------------------------------------------------------

  celdas.forEach((td) => {
    td.addEventListener("click", (e) => {
      const estado = td.dataset.estado;
      if (estado !== "LIBRE") {
        // No se puede seleccionar celdas que no estén libres
        return;
      }

      const habitacion = td.dataset.habitacion;
      const fecha = td.dataset.fecha;

      // Si no había inicio, este click define el inicio
      if (!seleccionInicio) {
        seleccionInicio = { habitacion, fecha };
        limpiarSeleccionVisualHabitacion(habitacion);
        aplicarRangoSeleccion(habitacion, fecha, fecha);
        return;
      }

      // Si había un inicio pero de otra habitación, empezamos nuevo rango para la nueva habitación
      if (seleccionInicio.habitacion !== habitacion) {
        seleccionInicio = { habitacion, fecha };
        limpiarSeleccionVisualHabitacion(habitacion);
        aplicarRangoSeleccion(habitacion, fecha, fecha);
        return;
      }

      // Si es la misma habitación: cerramos el rango entre inicio y este click
      const fechaInicio = seleccionInicio.fecha;
      aplicarRangoSeleccion(habitacion, fechaInicio, fecha);
      seleccionInicio = null;
    });
  });

  // ------------------------------------------------------------------
  // Click fuera de la grilla -> cancelar modo "rango en curso"
  // ------------------------------------------------------------------
  document.addEventListener("click", (e) => {
    const clicEnGrilla = e.target.closest(".grilla-container");
    if (!clicEnGrilla && seleccionInicio) {
      // Quita solo la marca de inicio, deja lo que ya estaba seleccionado como rango
      const sel = seleccionInicio;
      const selector =
        '.celda-interactiva[data-habitacion="' +
        sel.habitacion +
        '"][data-fecha="' +
        sel.fecha +
        '"]';
      const celdaInicio = document.querySelector(selector);
      if (celdaInicio) {
        celdaInicio.classList.remove("selection-start");
      }
      seleccionInicio = null;
    }
  });

  // ------------------------------------------------------------------
  // Botón LIMPIAR
  // ------------------------------------------------------------------
  if (btnLimpiar) {
    btnLimpiar.addEventListener("click", () => {
      limpiarSeleccionVisualTotal();
      seleccionInicio = null;
      sessionStorage.removeItem("seleccionHabitaciones");
      sessionStorage.removeItem("modoAccion");
    });
  }

  // ------------------------------------------------------------------
  // Submit del formulario de selección
  // ------------------------------------------------------------------
  if (formSeleccion) {
    formSeleccion.addEventListener("submit", (e) => {
      const dto = construirSeleccionDTO();

      if (dto.length === 0) {
        alert("Debe seleccionar al menos una habitación para continuar.");
        e.preventDefault();
        return;
      }

      // Guardar en sessionStorage con el formato que espera el backend:
      // List<SeleccionHabitacionDTO> (numeroHabitacion, fechaIngreso, fechaEgreso)
      sessionStorage.setItem("modoAccion", modoAccion);
      sessionStorage.setItem("seleccionHabitaciones", JSON.stringify(dto));
      // Dejar que el form se envíe normalmente al endpoint correspondiente
    });
  }
});