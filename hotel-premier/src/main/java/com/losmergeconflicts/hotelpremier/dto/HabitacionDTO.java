package com.losmergeconflicts.hotelpremier.dto;

import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;

public record HabitacionDTO (
    Integer idHabitacion,
    String nombre,
    Float precio,
    TipoHabitacion tipoHabitacion,
    TipoEstadoHabitacion estadoHabitacion
) {

}

