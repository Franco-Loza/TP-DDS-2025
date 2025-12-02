package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.HuespedDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Huesped Entity y DTOs.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface HuespedMapper {

    /**
     * Convierte HuespedDTORequest a Huesped Entity.
     * 
     * NOTA: Las entidades relacionadas (Direccion, Localidad, Nacionalidad) 
     * deben ser resueltas ANTES de llamar a este mapper y asignadas manualmente.
     * 
     * MapStruct mapea automáticamente todos los campos con nombres coincidentes,
     * incluyendo los heredados de Persona (cuit, telefono, info).
     * 
     * @param request DTO con datos del request
     * @return Entidad Huesped sin ID (para INSERT)
     */
    Huesped toEntity(HuespedDTORequest request);

    /**
     * Convierte Huesped Entity a HuespedDTOResponse.
     * 
     * Mapea todos los datos de la entidad incluyendo las relaciones anidadas
     * (Direccion, Localidad, Provincia, Pais, Nacionalidad).
     * 
     * @param huesped Entidad Huesped
     * @return DTO de respuesta con todos los datos públicos
     */
    @Mapping(target = "calle", source = "direccion.calle")
    @Mapping(target = "numero", source = "direccion.numero")
    @Mapping(target = "piso", source = "direccion.piso")
    @Mapping(target = "departamento", source = "direccion.departamento")
    @Mapping(target = "codigoPostal", source = "direccion.codigoPostal")
    @Mapping(target = "localidadId", source = "direccion.localidad.id")
    @Mapping(target = "nombreLocalidad", source = "direccion.localidad.nombre")
    @Mapping(target = "provinciaId", source = "direccion.localidad.provincia.id")
    @Mapping(target = "nombreProvincia", source = "direccion.localidad.provincia.nombre")
    @Mapping(target = "paisId", source = "direccion.localidad.provincia.pais.id")
    @Mapping(target = "nombrePais", source = "direccion.localidad.provincia.pais.nombre")
    @Mapping(target = "nacionalidadId", source = "nacionalidad.id")
    @Mapping(target = "nombreNacionalidad", source = "nacionalidad.nombre")
    HuespedDTOResponse toResponse(Huesped huesped);

    /**
     * Actualiza entidad existente con datos del request.
     * Utilizado para operaciones de UPDATE.
     * 
     * Las entidades relacionadas (Direccion, Nacionalidad) deben 
     * ser actualizadas manualmente en el servicio.
     * 
     * @param request DTO con nuevos datos
     * @param huesped Entidad existente a actualizar (se modifica in-place)
     */
    void updateEntityFromRequest(HuespedDTORequest request, @org.mapstruct.MappingTarget Huesped huesped);
}
