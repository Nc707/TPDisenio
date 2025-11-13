console.log("Script de huespedes cargado");

// -------------------- Helpers de validación --------------------

function esEmailValido(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

function esTelefonoValido(telefono) {
  const regex = /^[0-9+()\-\s]{7,}$/;
  return regex.test(telefono);
}

// -------------------- Submit del formulario --------------------

async function handleSubmit(event) {
  event.preventDefault();

  const form = document.getElementById("form-huesped");
  const modalOk = document.getElementById("modal-confirm");

  // ✅ Validación HTML5 básica (required, pattern, type="email", etc.)
  if (!form.checkValidity()) {
    form.reportValidity();
    return;
  }

  const formData = new FormData(form);
  const data = Object.fromEntries(formData.entries());

  // 🔎 Validación extra de email (si vino cargado)
  if (data.email && !esEmailValido(data.email)) {
    showErrorModal("Email inválido", "El email no tiene un formato válido.");
    return;
  }

  // 🔎 Validación extra de teléfono
  if (!esTelefonoValido(data.telefono)) {
    showErrorModal("Teléfono inválido", "El teléfono no tiene un formato válido.");
    return;
  }

  // 👇 Armar el JSON que espera el backend (HuespedDTO / Huesped)
  const payload = {
    apellido: data.apellido,
    nombres: data.nombre,               // name="nombre"
    tipoDocumento: data.tipo_documento, // Enum TipoDni
    numeroDocumento: data.numeroDocumento, // name="numeroDocumento" en el HTML
    cuit: data.cuit || null,
    categoriaFiscal: data.iva && data.iva.trim() !== "" ? data.iva : null,
    fechaNacimiento: data.fecha_nacimiento,
    direccion: {
      calle: data.calle,
      numero: data.numero,
      departamento: data.dpto || null,
      piso: data.piso || null,
      codigoPostal: data.codigo_postal || null,
      localidad: data.localidad,
      provincia: data.provincia
      // pais: data.pais  // si después agregás el campo
    },
    telefono: data.telefono,
    email: data.email || null,
    ocupacion: data.ocupacion,
    nacionalidad: data.nacionalidad
  };

  console.log("Payload a enviar:", payload);

  try {
    const response = await fetch("/huespedes/api/alta", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    // ❌ Respuestas con error (400, 409, 500, etc.)
    if (!response.ok) {
      let errorData = null;
      try {
        errorData = await response.json();
      } catch (e) {
        console.error("No se pudo parsear JSON de error:", e);
      }

      console.error("Error del servidor:", errorData || response);

      // 409 - HuespedDuplicadoException
      if (response.status === 409 && errorData && errorData.error === "Huésped duplicado") {
        showErrorModal(
          "Huésped duplicado",
          errorData.message || "Ya existe un huésped con ese tipo y número de documento."
        );
        return;
      }

      // 400 - Error de validación (DTO o reglas como CUIT vacío)
      if (response.status === 400 && errorData && errorData.error === "Error de validación") {
        const msg =
          errorData.message ||
          "Hay errores en los datos enviados. Revisá los campos y volvé a intentar.";
        showErrorModal("Error de validación", msg);
        return;
      }

      // 500 - Error de base de datos u otros internos
      const titulo = (errorData && errorData.error) || "Error al registrar el huésped";
      const mensaje =
        (errorData && errorData.message) ||
        "¡CUIDADO! El tipo y número de documento ya existen en el sistema.";
      showErrorModal(titulo, mensaje);
      return;
    }

    // ✅ Si llegó acá, la respuesta es 2xx → éxito
    const popupText = document.getElementById("popup-text");
    popupText.innerHTML = `El huésped <b>${payload.nombres} ${payload.apellido}</b> ha sido<br>
      satisfactoriamente cargado al sistema.<br>¿Desea cargar otro?`;

    modalOk.style.display = "flex";

  } catch (err) {
    // Solo entra acá si NO hay respuesta del servidor (app caída, puerto mal, etc.)
    console.error("Error de red:", err);
    showErrorModal(
      "Error de conexión",
      "No se pudo conectar con el servidor. Verificá que la aplicación esté ejecutándose."
    );
  }
}

// -------------------- Botones del popup de éxito --------------------

function handleNo() {
  document.getElementById("modal-confirm").style.display = "none";
}

function handleYes() {
  const form = document.getElementById("form-huesped");
  form.reset();
  document.getElementById("modal-confirm").style.display = "none";
}

// -------------------- Modal de error --------------------

function showErrorModal(titulo, mensaje) {
  const modal = document.getElementById("modal-error");
  if (!modal) return;

  const headerRight = modal.querySelector(".modal-right");
  const text = document.getElementById("error-text");

  if (headerRight) {
    headerRight.textContent = titulo;
  }
  if (text) {
    text.textContent = mensaje;
  }

  modal.style.display = "flex";
}

function closeErrorModal() {
  const modal = document.getElementById("modal-error");
  if (modal) {
    modal.style.display = "none";
  }
}

// -------------------- Modal de cancelar --------------------

// Mostrar el popup de cancelar
function showCancelModal() {
  const modal = document.getElementById("modal-cancel");
  if (modal) {
    modal.style.display = "flex";
  }
}

// El usuario elige NO -> se cierra el popup y no se pierde nada
function handleCancelNo() {
  const modal = document.getElementById("modal-cancel");
  if (modal) {
    modal.style.display = "none";
  }
}

// El usuario elige SÍ -> continuar con el paso 6 (volver al menú)
function handleCancelYes() {
  const modal = document.getElementById("modal-cancel");
  if (modal) {
    modal.style.display = "none";
  }

  // Redirigir al menú principal
  window.location.href = "/";
}
