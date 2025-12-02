package com.losmergeconflicts.hotelpremier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.losmergeconflicts.hotelpremier.entity.Direccion;

/**
 * DAO (Data Access Object) para la entidad Direccion.
 * 
 * Extiende JpaRepository que proporciona métodos CRUD básicos.
 * Spring Data JPA genera automáticamente la implementación.
 */
@Repository
public interface DireccionDAO extends JpaRepository<Direccion, Long> {

}
