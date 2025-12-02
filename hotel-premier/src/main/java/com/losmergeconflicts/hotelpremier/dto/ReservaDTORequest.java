package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReservaDTORequest(
    @FutureOrPresent(message = "La fecha de ingreso debe ser hoy o una fecha futura")
    @NotNull(message = "La fecha de ingreso no puede ser nula")
    LocalDate fechaIngreso,
    @FutureOrPresent(message = "La fecha de egreso debe ser hoy o una fecha futura")
    @NotNull(message = "La fecha de egreso no puede ser nula")
    LocalDate fechaEgreso,
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    String nombreHuesped,
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    String apellidoHuesped,
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9\\s\\-\\+]+$", message = "El teléfono solo puede contener números, espacios, guiones y el signo +")
    String telefonoHuesped,
    @NotNull(message = "La lista de IDs de habitaciones no puede ser nula")
    List<Long> idsHabitaciones
) {

}
