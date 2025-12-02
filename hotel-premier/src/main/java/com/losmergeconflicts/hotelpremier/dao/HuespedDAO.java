package com.losmergeconflicts.hotelpremier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;

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

}
