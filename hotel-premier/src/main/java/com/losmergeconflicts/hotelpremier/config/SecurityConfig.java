package com.losmergeconflicts.hotelpremier.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. ACTIVAR CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Deshabilitar CSRF
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                .authorizeHttpRequests(authorize -> authorize
                        // Recursos estáticos
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                        // Auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/login", "/registro").permitAll()
                        // API pública
                        .requestMatchers("/api/**").permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu-principal", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * CONFIGURACIÓN GLOBAL DE CORS
     * Este método le dice a Spring Security:
     * "Deja pasar todo lo que venga de localhost:3000"
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir el origen de React (Frontend)
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, OPTIONS)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitir todos los headers (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir enviar credenciales (cookies, headers de auth) si fuera necesario
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}