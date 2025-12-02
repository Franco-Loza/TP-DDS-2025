package com.losmergeconflicts.hotelpremier.service;

import java.util.List;

import com.losmergeconflicts.hotelpremier.dto.HuespedDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.LocalidadDTO;
import com.losmergeconflicts.hotelpremier.dto.NacionalidadDTO;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;

/**
 * Interfaz del servicio de personas.
 *
 * Define los métodos relacionados con la gestión de personas.
 */
public interface GestorPersonas {

    /**
     * Dar de alta un nuevo huésped en el sistema.
     *
     * @param request DTO con los datos del huésped (ya validados por @Valid)
     * @return DTO de respuesta con los datos del huésped dado de alta
     * @throws IllegalArgumentException si el tipo y número de documento ya existen o si no se encuentran las entidades relacionadas
     */
    HuespedDTOResponse altaHuesped(HuespedDTORequest request);

    /**
     * Dar de alta un nuevo huésped en el sistema.
     *
     * @param request DTO con los datos del huésped (ya validados por @Valid)
     * @param permitirDuplicados si es true, permite registrar huéspedes con tipo y documento duplicado
     * @return DTO de respuesta con los datos del huésped dado de alta
     * @throws IllegalArgumentException si no se encuentran las entidades relacionadas
     */
    HuespedDTOResponse altaHuesped(HuespedDTORequest request, boolean permitirDuplicados);

    /**
     * Verifica si un tipo y número de documento ya están registrados.
     *
     * @param tipoDocumento tipo de documento (DNI, PASAPORTE, etc.)
     * @param documento número de documento a verificar
     * @return true si el tipo y documento existen, false si no
     */
    boolean existeDocumento(TipoDocumento tipoDocumento, String documento);

    /**
     * Obtiene todas las localidades disponibles en el sistema.
     *
     * @return lista de DTOs con todas las localidades (incluye provincia y país anidados)
     */
    List<LocalidadDTO> listarLocalidades();

    /**
     * Obtiene todas las nacionalidades disponibles en el sistema.
     *
     * @return lista de DTOs con todas las nacionalidades
     */
    List<NacionalidadDTO> listarNacionalidades();

    /**
     * Busca huéspedes en el sistema según múltiples criterios.
     *
     * @param apellido Criterio de búsqueda por apellido (parcial)
     * @param nombre Criterio de búsqueda por nombre (parcial)
     * @param tipoDoc Criterio de búsqueda por tipo de documento (exacto)
     * @param nroDoc Criterio de búsqueda por número de documento (parcial)
     * @return Una lista de HuespedDTOResponse que coinciden con la búsqueda
     */
    List<HuespedDTOResponse> buscarHuespedes(String apellido, String nombre, TipoDocumento tipoDoc, String nroDoc);

}