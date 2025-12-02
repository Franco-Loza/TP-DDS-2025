package com.losmergeconflicts.hotelpremier.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record EstadiaDTORequest(
        @NotNull(message = "El ID de la habitación es obligatorio")
        Long idHabitacion,

        @NotNull(message = "La fecha de ingreso es obligatoria")
        LocalDate fechaIngreso,

        @NotNull(message = "La fecha de egreso es obligatoria")
        LocalDate fechaEgreso,

        @NotNull(message = "Debe seleccionar un huésped responsable")
        Long idResponsable,

        List<Long> idsAcompaniantes
) {}