package com.losmergeconflicts.hotelpremier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.losmergeconflicts.hotelpremier.entity.Nacionalidad;

/**
 * DAO (Data Access Object) para la entidad Nacionalidad.
 * 
 * Extiende JpaRepository que proporciona métodos CRUD básicos.
 * Spring Data JPA genera automáticamente la implementación.
 */
@Repository
public interface NacionalidadDAO extends JpaRepository<Nacionalidad, Long> {

}
