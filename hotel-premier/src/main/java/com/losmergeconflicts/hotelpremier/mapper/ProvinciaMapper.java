package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.ProvinciaDTO;
import com.losmergeconflicts.hotelpremier.entity.Provincia;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre Provincia Entity y ProvinciaDTO.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class, uses = {PaisMapper.class})
public interface ProvinciaMapper {

    /**
     * Convierte Provincia Entity a ProvinciaDTO.
     * 
     * @param provincia Entidad Provincia
     * @return DTO de Provincia con país anidado
     */
    ProvinciaDTO toDTO(Provincia provincia);
}
