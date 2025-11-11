console.log("Script de huespedes cargado");

// -------------------- Helpers de validación --------------------

function esEmailValido(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

function esTelefonoValido(telefono) {
  const regex = /^[0-9+()\s-]{7,}$/;
  return regex.test(telefono);
}

// -------------------- Submit del formulario --------------------

function handleSubmit(event) {
  event.preventDefault();

  const form = document.getElementById("form-huesped");
  const modal = document.getElementById("modal-confirm");

  // ✅ Validación HTML5 básica (required, pattern, type="email", etc.)
  if (!form.checkValidity()) {
    form.reportValidity();
    return;
  }

  const formData = new FormData(form);
  const data = Object.fromEntries(formData.entries());

  // 🔎 Validación extra de email (si vino cargado)
  if (data.email && !esEmailValido(data.email)) {
    alert("El email no tiene un formato válido.");
    return;
  }

  // 🔎 Validación extra de teléfono
  if (!esTelefonoValido(data.telefono)) {
    alert("El teléfono no tiene un formato válido.");
    return;
  }

  // 📝 Actualizar texto del popup
  const popupText = document.getElementById("popup-text");
  popupText.innerHTML = `El huésped <b>${data.nombre} ${data.apellido}</b> ha sido<br>
    satisfactoriamente cargado al sistema.<br>¿Desea cargar otro?`;

  // 👀 Mostrar popup
  modal.style.display = "flex"; // o "block" según tu CSS
}

// -------------------- Botones del popup --------------------

function handleNo() {
  document.getElementById("modal-confirm").style.display = "none";
}

function handleYes() {
  const form = document.getElementById("form-huesped");
  form.reset();
  document.getElementById("modal-confirm").style.display = "none";
}