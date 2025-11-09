

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