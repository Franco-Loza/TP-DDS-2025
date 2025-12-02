package com.losmergeconflicts.hotelpremier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConserjeDTORequest(
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    String username,
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {

}
