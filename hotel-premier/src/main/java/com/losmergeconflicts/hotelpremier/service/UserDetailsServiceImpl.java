package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dao.ConserjeDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación personalizada de UserDetailsService para Spring Security.
 * 
 * Esta clase es el puente entre Spring Security y nuestra base de datos.
 * Spring Security llamará a loadUserByUsername() durante el proceso de autenticación
 * para obtener los detalles del usuario (credenciales, roles, etc.).
 * 
 * @Slf4j: Genera automáticamente un logger para la clase
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ConserjeDAO conserjeDAO;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param conserjeDAO DAO para operaciones con Conserje
     */
    @Autowired
    public UserDetailsServiceImpl(ConserjeDAO conserjeDAO) {
        this.conserjeDAO = conserjeDAO;
    }

    /**
     * Carga los detalles del usuario por su nombre de usuario.
     * 
     * Este método es invocado por Spring Security cuando un usuario intenta autenticarse.
     * 
     * Proceso:
     * 1. Spring Security recibe username y password del formulario de login
     * 2. Llama a este método con el username
     * 3. Este método busca al usuario en la BD
     * 4. Spring Security compara el password ingresado con el hash almacenado
     * 5. Si coinciden, autentica al usuario y crea la sesión
     * 
     * @param username el nombre de usuario ingresado en el login
     * @return UserDetails con la información del usuario (nuestra entidad Conserje)
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando cargar usuario: {}", username);
        
        return conserjeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException(
                            "Usuario no encontrado: " + username
                    );
                });
    }
}
