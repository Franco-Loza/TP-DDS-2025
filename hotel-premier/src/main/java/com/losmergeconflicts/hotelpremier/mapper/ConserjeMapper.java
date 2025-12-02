package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Conserje;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre Conserje Entity y DTOs.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface ConserjeMapper {

    /**
     * Convierte ConserjeDTORequest a Conserje Entity.
     * El ID se ignora automáticamente para nuevas entidades.
     * Los campos createdAt y updatedAt se manejan por @PrePersist/@PreUpdate.
     * 
     * NOTA: La contraseña debe ser hasheada ANTES de llamar a este mapper.
     * Este mapper solo hace la conversión de estructuras de datos.
     * 
     * @param request DTO con datos del request
     * @return Entidad Conserje sin ID (para INSERT)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "ROLE_CONSERJE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Conserje toEntity(ConserjeDTORequest request);

    /**
     * Convierte Conserje Entity a ConserjeDTOResponse.
     * 
     * NOTA: La contraseña NO se incluye en el DTO de respuesta por seguridad.
     * Solo se exponen datos no sensibles del usuario.
     * 
     * @param conserje Entidad Conserje
     * @return DTO de respuesta con datos públicos (sin password)
     */
    @Mapping(target = "createdAt", expression = "java(conserje.getCreatedAt() != null ? conserje.getCreatedAt().toString() : null)")
    @Mapping(target = "updatedAt", expression = "java(conserje.getUpdatedAt() != null ? conserje.getUpdatedAt().toString() : null)")
    ConserjeDTOResponse toResponse(Conserje conserje);

    /**
     * Actualiza entidad existente con datos del request.
     * Utilizado para operaciones de UPDATE.
     * 
     * El ID se preserva (ignore = true).
     * Los timestamps se actualizan automáticamente por @PreUpdate.
     * 
     * @param request DTO con nuevos datos
     * @param conserje Entidad existente a actualizar (se modifica in-place)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ConserjeDTORequest request, @org.mapstruct.MappingTarget Conserje conserje);
}

