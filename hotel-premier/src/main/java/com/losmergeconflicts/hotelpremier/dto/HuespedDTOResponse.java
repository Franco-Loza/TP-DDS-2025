package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDate;

import com.losmergeconflicts.hotelpremier.entity.PosicionFrenteAlIVA;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;

public record HuespedDTOResponse(
    Long id,
    String cuit,
    String telefono,
    String info,
    String calle,
    String numero,
    String piso,
    String departamento,
    String codigoPostal,
    Long localidadId,
    String nombreLocalidad,
    Long provinciaId,
    String nombreProvincia,
    Long paisId,
    String nombrePais,
    String nombre,
    String apellido,
    TipoDocumento tipoDocumento,
    String documento,
    LocalDate fechaNacimiento,
    String email,
    String ocupacion,
    PosicionFrenteAlIVA posicionFrenteAlIVA,
    Long nacionalidadId,
    String nombreNacionalidad
) {

}
