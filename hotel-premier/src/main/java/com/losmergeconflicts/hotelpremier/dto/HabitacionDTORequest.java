package com.losmergeconflicts.hotelpremier.dto;

import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HabitacionDTORequest(
    @Size(max = 5, message = "El nombre no puede exceder los 5 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,
    @NotNull(message = "El precio no puede ser nulo")
    Float precio,
    @NotNull(message = "El tipo de habitación no puede ser nulo")
    TipoHabitacion tipoHabitacion,
    @NotNull(message = "El estado de la habitación no puede ser nulo")
    TipoEstadoHabitacion estadoHabitacion
) {

}
