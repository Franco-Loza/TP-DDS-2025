package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dao.ConserjeDAO;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Conserje;
import com.losmergeconflicts.hotelpremier.mapper.ConserjeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de seguridad.
 * 
 * Esta clase contiene toda la lógica de negocio relacionada con
 * registro y autenticación de usuarios.
 * 
 * @Slf4j: Genera automáticamente un logger para la clase
 */
@Service
@Slf4j
public class GestorSeguridadImp implements GestorSeguridad {

    private final ConserjeDAO conserjeDAO;
    private final PasswordEncoder passwordEncoder;
    private final ConserjeMapper conserjeMapper;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param conserjeDAO DAO para operaciones con Conserje
     * @param passwordEncoder encoder para hashear contraseñas
     * @param conserjeMapper mapper para conversión entre DTOs y entidades
     */
    @Autowired
    public GestorSeguridadImp(ConserjeDAO conserjeDAO, 
                          PasswordEncoder passwordEncoder,
                          ConserjeMapper conserjeMapper) {
        this.conserjeDAO = conserjeDAO;
        this.passwordEncoder = passwordEncoder;
        this.conserjeMapper = conserjeMapper;
    }

    /**
     * Registra un nuevo conserje en el sistema.
     * 
     * Proceso:
     * 1. Valida que el username no exista
     * 2. Convierte el DTO a entidad usando el mapper
     * 3. Hashea la contraseña con BCrypt
     * 4. Guarda en base de datos
     * 5. Convierte la entidad guardada a DTO de respuesta
     * 
     * @param request DTO con los datos del conserje (ya validados por @Valid)
     * @return DTO de respuesta con los datos del conserje registrado
     * @throws IllegalArgumentException si el username ya existe
     */
    @Override
    @Transactional
    public ConserjeDTOResponse registrarConserje(ConserjeDTORequest request) {
        log.info("Intentando registrar nuevo conserje: {}", request.username());
        
        // Validar que el username no exista
        if (conserjeDAO.existsByUsername(request.username())) {
            log.warn("Intento de registro con username existente: {}", request.username());
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        try {
            // 1. Convertir DTO a entidad usando MapStruct
            Conserje nuevoConserje = conserjeMapper.toEntity(request);
            
            // 2. Hashear la contraseña (el mapper pasó la contraseña en texto plano)
            nuevoConserje.setPassword(passwordEncoder.encode(request.password()));
            
            // 3. Guardar en base de datos
            Conserje conserjeGuardado = conserjeDAO.save(nuevoConserje);
            
            log.info("Conserje registrado exitosamente: {}", request.username());
            
            // 4. Convertir entidad guardada a DTO de respuesta
            return conserjeMapper.toResponse(conserjeGuardado);
            
        } catch (IllegalArgumentException e) {
            // Re-lanzar excepciones de validación
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar conserje: {}", request.username(), e);
            throw new RuntimeException("Error al procesar el registro", e);
        }
    }

    /**
     * Verifica si un username ya está registrado.
     * 
     * @param username nombre de usuario a verificar
     * @return true si el username existe, false si no
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existeUsername(String username) {
        return conserjeDAO.existsByUsername(username);
    }
}
