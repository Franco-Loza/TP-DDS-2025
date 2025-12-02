package com.losmergeconflicts.hotelpremier.dto;

import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;

public record HabitacionDTOResponse(
    Long idHabitacion,
    String nombre,
    Float precio,
    TipoHabitacion tipoHabitacion,
    TipoEstadoHabitacion estadoHabitacion
) {

}
