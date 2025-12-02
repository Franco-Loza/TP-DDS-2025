package com.losmergeconflicts.hotelpremier.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record GrillaDisponibilidadDTO(List<String> nombresHabitaciones, List<FilaFechaDTO> filas) {
    public record FilaFechaDTO(
            LocalDate fecha,
            Map<String, String> estadosPorHabitacion // Mapa: NombreHabitacion -> Estado (String)
    ) {}
}