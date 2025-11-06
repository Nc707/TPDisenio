package edu.inbugwethrust.premier.suite.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.inbugwethrust.premier.suite.dto.DireccionDTO;
import edu.inbugwethrust.premier.suite.dto.HuespedDTO;
import edu.inbugwethrust.premier.suite.services.exceptions.DatosObligatoriosException;

@Component
public class HuespedValidator {

    public void validarDatosObligatorios(HuespedDTO dto) {

        List<String> errores = new ArrayList<>();

        // campos del huésped
        if (dto == null) {
            errores.add("No se recibió el huésped a dar de alta.");
            throw new DatosObligatoriosException(errores);
        }

        if (estaVacio(dto.getApellido())) {
            errores.add("El apellido es obligatorio.");
        }

        if (estaVacio(dto.getNombres())) {
            errores.add("El nombre es obligatorio.");
        }

        if (dto.getTipoDocumento() == null) {
            errores.add("Debe seleccionar un tipo de documento.");
        }

        if (estaVacio(dto.getNumeroDocumento())) {
            errores.add("El número de documento es obligatorio.");
        }

        // dirección
        DireccionDTO dir = dto.getDireccion();
        if (dir == null) {
            errores.add("La dirección es obligatoria.");
        } else {
            if (estaVacio(dir.getCalle())) {
                errores.add("La calle es obligatoria.");
            }
            if (estaVacio(dir.getNumero())) {
                errores.add("El número de la dirección es obligatorio.");
            }
            if (estaVacio(dir.getLocalidad())) {
                errores.add("La localidad es obligatoria.");
            }
            if (estaVacio(dir.getProvincia())) {
                errores.add("La provincia es obligatoria.");
            }
            if (estaVacio(dir.getPais())) {
                errores.add("El país es obligatorio.");
            }
        }

        // teléfono también aparece en el CU
        if (estaVacio(dto.getTelefono())) {
            errores.add("El teléfono es obligatorio.");
        }

        // email y cuit son NO obligatorios según el CU → no los validamos

        if (!errores.isEmpty()) {
            throw new DatosObligatoriosException(errores);
        }
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
