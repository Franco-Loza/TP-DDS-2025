package com.losmergeconflicts.hotelpremier.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para Spring Security.
 * 
 * Esta clase configura:
 * - Qué rutas son públicas y cuáles requieren autenticación
 * - El comportamiento del formulario de login
 * - El comportamiento del logout
 * - El algoritmo de encriptación de contraseñas
 * 
 * @Configuration: Indica que esta clase contiene definiciones de beans
 * @EnableWebSecurity: Activa la configuración de seguridad web de Spring
 * @RequiredArgsConstructor: Genera constructor con campos final (inyección de dependencias)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Define el encoder de contraseñas usando BCrypt.
     * 
     * BCrypt es un algoritmo de hash seguro diseñado específicamente para contraseñas:
     * - Incluye un "salt" aleatorio (previene rainbow table attacks)
     * - Es computacionalmente costoso (previene brute force attacks)
     * - El nivel 10 (default) realiza 2^10 = 1024 rondas de hashing
     * 
     * Este bean será inyectado automáticamente donde se necesite hashear/verificar passwords.
     * 
     * @return BCryptPasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * Este es el corazón de la configuración de Spring Security.
     * Define las reglas de acceso y comportamiento de autenticación.
     * 
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización de requests
            .authorizeHttpRequests(authorize -> authorize
                // Recursos estáticos públicos (CSS, JS, imágenes)
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                
                // Endpoints de autenticación públicos
                .requestMatchers("/login", "/registro").permitAll()
                
                // Swagger UI y documentación API pública
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración del formulario de login personalizado
            .formLogin(form -> form
                // Especifica la página personalizada de login
                .loginPage("/login")
                
                // Redirige al menú principal después de login exitoso
                .defaultSuccessUrl("/menu-principal", true)
                
                // Redirige a login con parámetro error si falla
                .failureUrl("/login?error=true")
                
                // La página de login es accesible por todos
                .permitAll()
            )
            
            // Configuración del logout
            .logout(logout -> logout
                // URL a la que redirigir después de logout exitoso
                .logoutSuccessUrl("/login?logout")
                
                // Invalida la sesión HTTP al hacer logout
                .invalidateHttpSession(true)
                
                // Elimina las cookies de autenticación
                .deleteCookies("JSESSIONID")
                
                // El logout es accesible por todos
                .permitAll()
            );

        return http.build();
    }
}
