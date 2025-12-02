package com.losmergeconflicts.hotelpremier.dao;

import com.losmergeconflicts.hotelpremier.entity.Conserje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO (Data Access Object) para gestionar operaciones de persistencia de la entidad Conserje.
 * 
 * Extiende JpaRepository para obtener automáticamente métodos CRUD:
 * - save(), findById(), findAll(), delete(), etc.
 * 
 * Spring Data JPA implementará automáticamente todos los métodos.
 */
@Repository
public interface ConserjeDAO extends JpaRepository<Conserje, Long> {

    /**
     * Busca un conserje por su nombre de usuario.
     * 
     * Este método es crucial para Spring Security, ya que lo usa
     * para cargar el usuario durante el proceso de autenticación.
     * 
     * Spring Data JPA genera automáticamente la consulta SQL:
     * SELECT * FROM conserjes WHERE username = ?
     * 
     * @param username el nombre de usuario a buscar
     * @return Optional con el Conserje si existe, Optional.empty() si no
     */
    Optional<Conserje> findByUsername(String username);
    
    /**
     * Verifica si existe un conserje con el username dado.
     * 
     * Útil para validar que no haya duplicados al registrar usuarios.
     * 
     * @param username el nombre de usuario a verificar
     * @return true si existe, false si no
     */
    boolean existsByUsername(String username);
}
