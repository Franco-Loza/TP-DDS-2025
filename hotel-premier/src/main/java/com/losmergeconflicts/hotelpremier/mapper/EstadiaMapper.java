package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.EstadiaDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Estadia;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface EstadiaMapper {

    @Mapping(target = "nombreHabitacion", source = "habitacion.nombre")
    @Mapping(target = "apellidoResponsable", source = "huespedAsignado.apellido")
    EstadiaDTOResponse toResponse(Estadia estadia);
}