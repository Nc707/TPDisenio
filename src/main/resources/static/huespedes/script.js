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

// 👇 OJO: ahora es async
async function handleSubmit(event) {
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

  // 👇 Armar el JSON que espera HuespedDTO
  const payload = {
    apellido: data.apellido,
    nombres: data.nombre,                    // HTML: name="nombre"
    tipoDocumento: data.tipo_documento,      // Enum TipoDni
    numeroDocumento: data.numero_documento,
    cuit: data.cuit || null,
    categoriaFiscal: data.iva || null,       // Enum CategoriaFiscal
    fechaNacimiento: data.fecha_nacimiento,  // LocalDate: "yyyy-MM-dd"
    direccion: {
      calle: data.calle,
      numero: data.numero,
      piso: data.piso || null,
      departamento: data.dpto || null,
     codigoPostal: data.codigo_postal || null,
      localidad: data.localidad,
      provincia: data.provincia,
      //pais: data.nacionalidad

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

    if (!response.ok) {
      const text = await response.text();
      console.error("Error del servidor:", text);
      alert("Error al registrar el huésped.\n" + text);
      return;
    }

    // Si quisieras ver lo que devuelve el backend:
    // const creado = await response.json();
    // console.log("Huesped creado:", creado);

    // 📝 Actualizar texto del popup usando lo que se envió
    const popupText = document.getElementById("popup-text");
    popupText.innerHTML = `El huésped <b>${payload.nombres} ${payload.apellido}</b> ha sido<br>
      satisfactoriamente cargado al sistema.<br>¿Desea cargar otro?`;

    // 👀 Mostrar popup
    modal.style.display = "flex";

  } catch (err) {
    console.error("Error de red:", err);
    alert("No se pudo conectar con el servidor.");
  }
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