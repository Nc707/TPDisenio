
console.log("resultados-busqueda.js cargado");
document.addEventListener('DOMContentLoaded', () => {
    const btnSiguiente = document.getElementById('btn-siguiente');
    
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', (e) => {
            
            // 1. Verificamos si estamos en modo OCUPAR
            const inputAccion = document.getElementById('input-accion');
            const accion = inputAccion ? inputAccion.value : '';

            if (accion === 'OCUPAR') {
                // 2. Detenemos el envío normal del formulario
                e.preventDefault();

                // 3. Obtenemos el radio seleccionado
                const seleccionado = document.querySelector('input[name="idSeleccionado"]:checked');
                
                if (seleccionado) {
                    // 4. Leemos los atributos data- que pusimos en el HTML
                    const tipoDoc = seleccionado.dataset.tipoDoc;
                    const nroDoc = seleccionado.dataset.nroDoc;

                    // 5. Delegamos al Gestor
                    gestorOcupacion.agregarHuespedSeleccionado(tipoDoc, nroDoc);
                } else {
                    alert("Por favor, seleccione un huésped.");
                }
            }
            // Si NO es OCUPAR (es decir, flujo normal), dejamos que el form se envíe (submit)
        });
    }
});