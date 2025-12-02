package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDateTime;

public record EstadiaDTOResponse(
        Long id,
        LocalDateTime fechaHoraIngreso,
        String nombreHabitacion,
        String apellidoResponsable
) {}

