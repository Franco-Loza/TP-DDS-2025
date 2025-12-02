package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;

/**
 * Interfaz del servicio de autenticación.
 * 
 * Define los métodos relacionados con autenticación y registro de usuarios.
 */
public interface AuthService {
    
    /**
     * Registra un nuevo conserje en el sistema.
     * 
     * @param request DTO con los datos del conserje (username y password)
     * @return DTO de respuesta con los datos del conserje registrado
     * @throws IllegalArgumentException si el username ya existe o los datos son inválidos
     */
    ConserjeDTOResponse registrarConserje(ConserjeDTORequest request);
    
    /**
     * Verifica si un username ya está registrado.
     * 
     * @param username nombre de usuario a verificar
     * @return true si el username existe, false si no
     */
    boolean existeUsername(String username);
}
