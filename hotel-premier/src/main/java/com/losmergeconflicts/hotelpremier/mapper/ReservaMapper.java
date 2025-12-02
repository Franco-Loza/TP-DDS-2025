package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.ReservaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Reserva;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Reserva Entity y DTOs.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface ReservaMapper {

    /**
     * Convierte ReservaDTORequest a Reserva Entity.
     * 
     * NOTA: Las habitaciones deben ser resueltas ANTES de llamar a este mapper
     * y asignadas manualmente mediante el servicio.
     * 
     * MapStruct mapea automáticamente todos los campos con nombres coincidentes.
     * 
     * @param request DTO con datos del request
     * @return Entidad Reserva sin ID (para INSERT)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "huesped", ignore = true)
    @Mapping(target = "habitaciones", ignore = true)
    Reserva toEntity(ReservaDTORequest request);

    /**
     * Convierte Reserva Entity a ReservaDTOResponse.
     * 
     * Mapea todos los datos de la entidad incluyendo la lista de IDs de habitaciones.
     * 
     * @param reserva Entidad Reserva
     * @return DTO de respuesta con todos los datos públicos
     */
    @Mapping(target = "idsHabitaciones", expression = "java(reserva.getHabitaciones() != null ? reserva.getHabitaciones().stream().map(h -> h.getId()).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList())")
    ReservaDTOResponse toResponse(Reserva reserva);

    /**
     * Actualiza entidad existente con datos del request.
     * Utilizado para operaciones de UPDATE.
     * 
     * Las habitaciones deben ser actualizadas manualmente en el servicio.
     * 
     * @param request DTO con nuevos datos
     * @param reserva Entidad existente a actualizar (se modifica in-place)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "huesped", ignore = true)
    @Mapping(target = "habitaciones", ignore = true)
    void updateEntityFromRequest(ReservaDTORequest request, @org.mapstruct.MappingTarget Reserva reserva);
}
