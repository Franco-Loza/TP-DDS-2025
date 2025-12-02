package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.HabitacionDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HabitacionDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Habitacion Entity y DTOs.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface HabitacionMapper {

    /**
     * Convierte HabitacionDTORequest a Habitacion Entity.
     * 
     * MapStruct mapea automáticamente todos los campos con nombres coincidentes.
     * 
     * @param request DTO con datos del request
     * @return Entidad Habitacion sin ID (para INSERT)
     */
    @Mapping(target = "id", ignore = true)
    Habitacion toEntity(HabitacionDTORequest request);

    /**
     * Convierte Habitacion Entity a HabitacionDTOResponse.
     * 
     * Mapea todos los datos de la entidad.
     * 
     * @param habitacion Entidad Habitacion
     * @return DTO de respuesta con todos los datos públicos
     */
    @Mapping(target = "idHabitacion", source = "id")
    HabitacionDTOResponse toResponse(Habitacion habitacion);

    /**
     * Convierte Habitacion Entity a HabitacionDTOResponse con datos limitados.
     * 
     * Solo mapea ID, nombre y tipo de habitación. Los demás campos quedan en null.
     * Útil para listas o resúmenes donde no se necesitan todos los datos.
     * 
     * @param habitacion Entidad Habitacion
     * @return DTO de respuesta con solo ID, nombre y tipo
     */
    @Mapping(target = "idHabitacion", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "tipoHabitacion", source = "tipoHabitacion")
    @Mapping(target = "precio", ignore = true)
    @Mapping(target = "estadoHabitacion", ignore = true)
    HabitacionDTOResponse toResponseLimited(Habitacion habitacion);

    /**
     * Actualiza entidad existente con datos del request.
     * Utilizado para operaciones de UPDATE.
     * 
     * @param request DTO con nuevos datos
     * @param habitacion Entidad existente a actualizar (se modifica in-place)
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(HabitacionDTORequest request, @org.mapstruct.MappingTarget Habitacion habitacion);
}
