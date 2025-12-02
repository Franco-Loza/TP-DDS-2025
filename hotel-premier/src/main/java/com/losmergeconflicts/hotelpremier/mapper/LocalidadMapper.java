package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.LocalidadDTO;
import com.losmergeconflicts.hotelpremier.entity.Localidad;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre Localidad Entity y LocalidadDTO.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class, uses = {ProvinciaMapper.class})
public interface LocalidadMapper {

    /**
     * Convierte Localidad Entity a LocalidadDTO.
     * 
     * @param localidad Entidad Localidad
     * @return DTO de Localidad con provincia anidada
     */
    LocalidadDTO toDTO(Localidad localidad);
}
