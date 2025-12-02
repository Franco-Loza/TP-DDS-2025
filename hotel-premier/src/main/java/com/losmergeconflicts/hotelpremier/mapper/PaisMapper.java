package com.losmergeconflicts.hotelpremier.mapper;

import com.losmergeconflicts.hotelpremier.dto.PaisDTO;
import com.losmergeconflicts.hotelpremier.entity.Pais;
import com.losmergeconflicts.hotelpremier.mapper.config.MapstructConfig;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre Pais Entity y PaisDTO.
 * 
 * MapStruct genera automáticamente la implementación de este mapper.
 * Utiliza la configuración definida en MapstructConfig.
 */
@Mapper(config = MapstructConfig.class)
public interface PaisMapper {

    /**
     * Convierte Pais Entity a PaisDTO.
     * 
     * @param pais Entidad Pais
     * @return DTO de Pais
     */
    PaisDTO toDTO(Pais pais);
}
