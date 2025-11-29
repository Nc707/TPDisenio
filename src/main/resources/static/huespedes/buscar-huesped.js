document.addEventListener('DOMContentLoaded', () => {
    gestionarToast();
    actualizarTextoRolOcupacion();
});
document.addEventListener('DOMContentLoaded', () => {
    // ... tu código existente (gestionarToast, etc.) ...

    // Lógica del botón Cancelar
    const btnCancelar = document.getElementById('btn-cancelar');
    if (btnCancelar) {
        btnCancelar.addEventListener('click', (e) => {
            e.preventDefault(); // Buena práctica, aunque sea type="button"
            
            // Opción A: Recargar la página tal cual está (mantiene parámetros de URL si los hay)
            window.location.reload();

            // Opción B (Alternativa): Si quisieras "limpiar" la búsqueda y recargar limpio:
            // window.location.href = window.location.pathname; 
        });
    }
});

/**
 * Maneja la visualización y ocultamiento del mensaje Toast
 */
function gestionarToast() {
    const toast = document.getElementById("toast-notification");
    if (toast) {
        setTimeout(function(){ 
            toast.classList.remove("show");
            toast.style.display = 'none';
        }, 4500);
    }
}

/**
 * Verifica si estamos en modo "OCUPAR" y actualiza el texto 
 * para indicar si buscamos "Responsable" o "Acompañante"
 * basándose en el sessionStorage.
 */
function actualizarTextoRolOcupacion() {
    // 1. Obtener parámetros de URL para saber si estamos en 'OCUPAR'
    const params = new URLSearchParams(window.location.search);
    const accion = params.get('accion');

    // 2. Elemento del DOM donde escribiremos el rol
    const spanRol = document.getElementById('texto-rol-huesped');

    // Si no es OCUPAR o no existe el span, no hacemos nada
    if (accion !== 'OCUPAR' || !spanRol) return;

    // 3. Leer datos del Wizard desde sessionStorage
		const colaJson = sessionStorage.getItem('colaOcupacion');
		    const indiceStr = sessionStorage.getItem('indiceOcupacionActual');

		    if (colaJson && indiceStr) {
		        try {
		            const cola = JSON.parse(colaJson);
		            const indice = parseInt(indiceStr);
		            const habitacionActual = cola[indice];

		            if (habitacionActual) {
		                // === CAMBIO AQUÍ ===
		                // Verificamos si el campo OBJETO 'idHuespedResponsable' es nulo
		                if (!habitacionActual.idHuespedResponsable) {
		                    spanRol.textContent = "Huésped Responsable";
		                    spanRol.style.color = "#d35400"; // Naranja
		                } else {
		                    spanRol.textContent = "Acompañante";
		                    spanRol.style.color = "#27ae60"; // Verde
		                }
		            }
		        } catch (e) { console.error(e); }
    }
}