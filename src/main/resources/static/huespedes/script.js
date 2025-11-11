

console.log("Script de huespedes cargado");
function handleSubmit(event) {
    event.preventDefault(); // evita que se envíe el formulario

    const form = document.getElementById("form-huesped");
    const requiredFields = form.querySelectorAll("[required]");

    let valid = true;

    // limpiar mensajes previos
    form.querySelectorAll(".error-msg").forEach(e => e.remove());
    form.querySelectorAll("input, select").forEach(el => el.style.borderColor = "");

    // verificar campos vacíos
    requiredFields.forEach(field => {
      if (field.value.trim() === "") {
        valid = false;
        field.style.borderColor = "red";
        const msg = document.createElement("small");
        msg.classList.add("error-msg");
        msg.style.color = "red";
        msg.innerText = "El campo no puede quedar vacío";
        field.insertAdjacentElement("afterend", msg);
      }
    });

    if (!valid) return; // si hay vacíos, no continua

    // obtener nombre y apellido del formulario
    const nombre = form.querySelector("[name='nombre']").value.trim();
    const apellido = form.querySelector("[name='apellido']").value.trim();

    // actualizar texto del popup
    const popupText = document.getElementById("popup-text");
    popupText.innerHTML = `El huésped <b>${nombre} ${apellido}</b> ha sido<br>
      satisfactoriamente cargado al sistema.<br>¿Desea cargar otro?`;

    // mostrar popup
    document.getElementById("modal-confirm").style.display = "flex";
  }

  function handleNo() {
    // cerrar popup
    document.getElementById("modal-confirm").style.display = "none";
  }

  function handleYes() {
    // limpiar formulario
    const form = document.getElementById("form-huesped");
    form.reset();
    document.getElementById("modal-confirm").style.display = "none";
  }

  // Validación extra de email (más allá del type="email")
  function esEmailValido(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  // Validación de teléfono con regex
  function esTelefonoValido(telefono) {
    const regex = /^[0-9+()\s-]{7,}$/;
    return regex.test(telefono);
  }

  function handleSubmit(event) {
    event.preventDefault();

    const form = document.getElementById("form-huesped");
    const modal = document.getElementById("modal-confirm");

    // ✅ Validación HTML5 (required, pattern, type="email", etc.)
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

    // 📲 Llamada a la API (cambiá la URL si tu endpoint es otro)
    fetch("/huespedes/api/alta", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (!response.ok) {
          // si quisieras usar el alta forzada por algún motivo (doc duplicado, etc.)
          // acá podrías chequear el status y decidir
          throw new Error("Error al guardar el huésped");
        }
        return response.json().catch(() => ({}));
      })
      .then(() => {
        const modal = document.getElementById("modal-confirm");
        if (modal) modal.style.display = "flex";
      })
      .catch((error) => {
        console.error(error);
        alert("Ocurrió un error al guardar el huésped.");
      });