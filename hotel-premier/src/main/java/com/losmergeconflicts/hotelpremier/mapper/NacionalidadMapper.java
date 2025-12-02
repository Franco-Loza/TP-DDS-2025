package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.NacionalidadDTO;
import com.losmergeconflicts.hotelpremier.entity.Nacionalidad;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre Nacionalidad Entity y NacionalidadDTO.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface NacionalidadMapper {

    /**
     * Convierte Nacionalidad Entity a NacionalidadDTO.
     * 
     * @param nacionalidad Entidad Nacionalidad
     * @return DTO de Nacionalidad
     */
    NacionalidadDTO toDTO(Nacionalidad nacionalidad);
}
