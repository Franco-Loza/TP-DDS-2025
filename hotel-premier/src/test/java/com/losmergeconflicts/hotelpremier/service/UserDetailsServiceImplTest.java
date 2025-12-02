package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.ConserjeDAO;
import com.losmergeconflicts.hotelpremier.entity.Conserje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Tests unitarios para UserDetailsServiceImpl.
 * 
 * Esta clase es crucial para Spring Security, ya que carga los usuarios
 * durante el proceso de autenticación. Probamos todos los escenarios posibles.
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private ConserjeDAO conserjeDAO;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Conserje conserjeExistente;

    @BeforeEach
    void setUp() {
        // Creamos un conserje de prueba
        conserjeExistente = Conserje.builder()
                .id(1L)
                .username("conserje123")
                .password("$2a$10$hashedPassword")
                .role("ROLE_CONSERJE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * CAMINO FELIZ: Usuario existe en la BD.
     * 
     * Escenario:
     * - Spring Security intenta autenticar a un usuario
     * - El usuario SÍ existe en la BD
     * 
     * Resultado esperado:
     * - Se retorna UserDetails con los datos del usuario
     * - Los datos coinciden con el usuario en BD
     * - La implementación de UserDetails funciona correctamente
     */
    @Test
    void testLoadUserByUsername_UsuarioExiste() {
        // --- ARRANGE ---
        
        String username = "conserje123";
        
        // Simulamos que el DAO encuentra al usuario
        when(conserjeDAO.findByUsername(username)).thenReturn(Optional.of(conserjeExistente));
        
        // --- ACT ---
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // --- ASSERT ---
        
        // Verificar que no es nulo
        assertNotNull(userDetails);
        
        // Verificar que el username coincide
        assertEquals(username, userDetails.getUsername());
        
        // Verificar que la contraseña hasheada está presente
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
        
        // Verificar que tiene el rol correcto
        assertEquals(1, userDetails.getAuthorities().size());
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_CONSERJE", authority.getAuthority());
        
        // Verificar flags de cuenta (todos deben ser true)
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        
        // Verificar que el DAO fue llamado correctamente
        verify(conserjeDAO, times(1)).findByUsername(username);
    }

    /**
     * CASO DE ERROR: Usuario NO existe en la BD.
     * 
     * Escenario:
     * - Spring Security intenta autenticar a un usuario
     * - El usuario NO existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza UsernameNotFoundException
     * - El mensaje de error es apropiado
     * - Spring Security manejará esta excepción mostrando error de login
     */
    @Test
    void testLoadUserByUsername_UsuarioNoExiste() {
        // --- ARRANGE ---
        
        String usernameInexistente = "usuarioInexistente";
        
        // Simulamos que el DAO NO encuentra al usuario
        when(conserjeDAO.findByUsername(usernameInexistente)).thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        // Verificamos que se lanza la excepción esperada
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(usernameInexistente);
        });
        
        // Verificamos el mensaje de error
        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        assertTrue(exception.getMessage().contains(usernameInexistente));
        
        // Verificamos que el DAO fue llamado
        verify(conserjeDAO, times(1)).findByUsername(usernameInexistente);
    }

    /**
     * VERIFICACIÓN: La entidad Conserje implementa UserDetails correctamente.
     * 
     * Escenario:
     * - Se carga un usuario existente
     * 
     * Resultado esperado:
     * - El objeto retornado ES una instancia de UserDetails
     * - También ES una instancia de Conserje (nuestra entidad)
     * - Se puede hacer cast a ambas interfaces
     */
    @Test
    void testLoadUserByUsername_RetornaUserDetailsYConserje() {
        // --- ARRANGE ---
        
        String username = "conserje123";
        when(conserjeDAO.findByUsername(username)).thenReturn(Optional.of(conserjeExistente));
        
        // --- ACT ---
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // --- ASSERT ---
        
        // Verificar que es UserDetails
        assertInstanceOf(UserDetails.class, userDetails);
        
        // Verificar que también es Conserje
        assertInstanceOf(Conserje.class, userDetails);
        
        // Verificar que podemos hacer cast y acceder a propiedades de Conserje
        Conserje conserje = (Conserje) userDetails;
        assertEquals(1L, conserje.getId());
        assertNotNull(conserje.getCreatedAt());
        assertNotNull(conserje.getUpdatedAt());
    }

    /**
     * VERIFICACIÓN: Username con espacios o caracteres especiales.
     * 
     * Escenario:
     * - Se busca un usuario con username que tiene mayúsculas/minúsculas
     * 
     * Resultado esperado:
     * - La búsqueda es case-sensitive (distingue mayúsculas)
     * - Solo encuentra el usuario si el username coincide exactamente
     */
    @Test
    void testLoadUserByUsername_CaseSensitive() {
        // --- ARRANGE ---
        
        String usernameExacto = "conserje123";
        String usernameConMayusculas = "Conserje123";
        
        // El DAO solo encuentra con el username exacto
        when(conserjeDAO.findByUsername(usernameExacto)).thenReturn(Optional.of(conserjeExistente));
        when(conserjeDAO.findByUsername(usernameConMayusculas)).thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        // Con username exacto funciona
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameExacto);
        assertNotNull(userDetails);
        
        // Con mayúsculas diferentes falla
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(usernameConMayusculas);
        });
        
        verify(conserjeDAO, times(1)).findByUsername(usernameExacto);
        verify(conserjeDAO, times(1)).findByUsername(usernameConMayusculas);
    }

    /**
     * VERIFICACIÓN: El método es transaccional (readOnly).
     * 
     * Escenario:
     * - Se carga un usuario
     * 
     * Resultado esperado:
     * - Solo se hace una lectura (findByUsername)
     * - NO se modifican datos en BD
     * - NO se llama a save() o delete()
     */
    @Test
    void testLoadUserByUsername_SoloLectura() {
        // --- ARRANGE ---
        
        String username = "conserje123";
        when(conserjeDAO.findByUsername(username)).thenReturn(Optional.of(conserjeExistente));
        
        // --- ACT ---
        
        userDetailsService.loadUserByUsername(username);
        
        // --- ASSERT ---
        
        // Verificar que solo se llamó a findByUsername (lectura)
        verify(conserjeDAO, times(1)).findByUsername(username);
        
        // Verificar que NO se llamó a métodos de escritura
        verify(conserjeDAO, never()).save(any());
        verify(conserjeDAO, never()).delete(any());
        verify(conserjeDAO, never()).deleteById(any());
    }

    /**
     * CASO DE ERROR: Excepción de BD al buscar usuario.
     * 
     * Escenario:
     * - Ocurre un error de BD al buscar (timeout, conexión perdida, etc.)
     * 
     * Resultado esperado:
     * - La excepción se propaga (no se captura)
     * - Spring Security manejará el error apropiadamente
     */
    @Test
    void testLoadUserByUsername_ErrorDeBD() {
        // --- ARRANGE ---
        
        String username = "conserje123";
        
        // Simulamos un error de BD
        when(conserjeDAO.findByUsername(username))
                .thenThrow(new RuntimeException("Timeout de conexión a BD"));
        
        // --- ACT & ASSERT ---
        
        // Verificamos que la excepción se propaga
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
        
        assertEquals("Timeout de conexión a BD", exception.getMessage());
        
        verify(conserjeDAO, times(1)).findByUsername(username);
    }

    /**
     * VERIFICACIÓN: Los roles/authorities se cargan correctamente.
     * 
     * Escenario:
     * - Se carga un usuario con rol específico
     * 
     * Resultado esperado:
     * - El UserDetails contiene exactamente UN authority
     * - El authority es "ROLE_CONSERJE"
     */
    @Test
    void testLoadUserByUsername_VerificarRoles() {
        // --- ARRANGE ---
        
        String username = "conserje123";
        when(conserjeDAO.findByUsername(username)).thenReturn(Optional.of(conserjeExistente));
        
        // --- ACT ---
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // --- ASSERT ---
        
        // Verificar cantidad de authorities
        assertEquals(1, userDetails.getAuthorities().size());
        
        // Verificar que contiene ROLE_CONSERJE
        boolean tieneRoleConserje = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CONSERJE"));
        assertTrue(tieneRoleConserje);
        
        // Verificar que NO contiene otros roles
        boolean tieneOtrosRoles = userDetails.getAuthorities().stream()
                .anyMatch(auth -> !auth.getAuthority().equals("ROLE_CONSERJE"));
        assertFalse(tieneOtrosRoles);
    }
}

