package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDate;

import com.losmergeconflicts.hotelpremier.entity.PosicionFrenteAlIVA;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HuespedDTORequest(
    @Size(max = 11, message = "El CUIT no puede exceder los 11 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El CUIT solo puede contener números")
    String cuit,
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9\\s\\-\\+]+$", message = "El teléfono solo puede contener números, espacios, guiones y el signo +")
    String telefono,
    @Size(max = 100, message = "La info no puede exceder los 100 caracteres")
    @NotBlank(message = "La info no puede estar vacío")
    String info,
    @Size(max = 100, message = "La calle no puede exceder los 100 caracteres")
    @NotBlank(message = "La calle no puede estar vacío")
    String calle,
    @Size(max = 5, message = "El número no puede exceder los 5 caracteres")
    @NotBlank(message = "El número no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número solo puede contener dígitos")
    String numero,
    @Size(max = 5, message = "El piso no puede exceder los 5 caracteres")
    String piso,
    @Size(max = 5, message = "El departamento no puede exceder los 5 caracteres")
    String departamento,
    @Size(max = 4, message = "El código postal no puede exceder los 4 caracteres")
    @NotBlank(message = "El código postal no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El código postal solo puede contener dígitos")
    String codigoPostal,
    Long localidadId,
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    String nombre,
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    String apellido,
    @NotNull(message = "El tipo de documento no puede ser nulo")
    TipoDocumento tipoDocumento,
    @Size(max = 8, message = "El documento no puede exceder los 8 caracteres")
    @NotBlank(message = "El documento no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El documento solo puede contener dígitos")
    String documento,
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    LocalDate fechaNacimiento,
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    String email,
    @Size(max = 100, message = "La ocupación no puede exceder los 100 caracteres")
    @NotBlank(message = "La ocupación no puede estar vacío")
    String ocupacion,
    @NotNull(message = "La posición frente al IVA no puede ser nula")
    PosicionFrenteAlIVA posicionFrenteAlIVA,
    Long nacionalidadId
) {
}
