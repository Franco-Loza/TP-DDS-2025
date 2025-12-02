package com.losmergeconflicts.hotelpremier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Huesped.
 *
 * Extiende JpaRepository que proporciona métodos CRUD básicos.
 * Spring Data JPA genera automáticamente la implementación.
 */
@Repository
public interface HuespedDAO extends JpaRepository<Huesped, Long> {

    /**
     * Verifica si existe un huésped con el tipo y número de documento especificados.
     *
     * @param tipoDocumento tipo de documento (DNI, PASAPORTE, etc.)
     * @param documento número de documento a verificar
     * @return true si existe un huésped con ese tipo y número de documento, false si no
     */
    boolean existsByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);

    /**
     * Busca huéspedes basado en los criterios de búsqueda.
     * La consulta JPQL maneja campos nulos o vacíos, permitiendo una búsqueda flexible.
     *
     * @param apellido      Apellido a buscar (parcial, ignora mayúsculas/minúsculas)
     * @param nombre        Nombre a buscar (parcial, ignora mayúsculas/minúsculas)
     * @param tipoDocumento Tipo de documento exacto
     * @param documento     Número de documento a buscar (parcial)
     * @return Lista de huéspedes que coinciden con los criterios
     */
    @Query("SELECT h FROM Huesped h WHERE " +
            "(:apellido IS NULL OR :apellido = '' OR lower(h.apellido) LIKE lower(concat('%', :apellido, '%'))) AND " +
            "(:nombre IS NULL OR :nombre = '' OR lower(h.nombre) LIKE lower(concat('%', :nombre, '%'))) AND " +
            "(:tipoDocumento IS NULL OR h.tipoDocumento = :tipoDocumento) AND " +
            "(:documento IS NULL OR :documento = '' OR h.documento LIKE concat('%', :documento, '%'))")
    List<Huesped> buscarHuespedesPorCriterios(
            @Param("apellido") String apellido,
            @Param("nombre") String nombre,
            @Param("tipoDocumento") TipoDocumento tipoDocumento,
            @Param("documento") String documento);
}
