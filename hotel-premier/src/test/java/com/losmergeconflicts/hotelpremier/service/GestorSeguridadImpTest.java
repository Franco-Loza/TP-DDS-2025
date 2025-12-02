package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.ConserjeDAO;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Conserje;
import com.losmergeconflicts.hotelpremier.mapper.ConserjeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Tests unitarios para GestorSeguridadImp.
 * 
 * Probamos la lógica de negocio de autenticación en aislamiento,
 * usando Mockito para simular las dependencias (DAO, Mapper, PasswordEncoder).
 */
@ExtendWith(MockitoExtension.class)
class GestorSeguridadImpTest {

    @Mock
    private ConserjeDAO conserjeDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConserjeMapper conserjeMapper;

    @InjectMocks
    private GestorSeguridadImp gestorSeguridad;

    private ConserjeDTORequest validRequest;
    private Conserje conserjeEntity;
    private ConserjeDTOResponse expectedResponse;

    @BeforeEach
    void setUp() {
        // Datos de prueba reutilizables
        validRequest = new ConserjeDTORequest("nuevoUsuario", "password123");
        
        conserjeEntity = Conserje.builder()
                .id(1L)
                .username("nuevoUsuario")
                .password("$2a$10$hashedPassword")
                .role("ROLE_CONSERJE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        expectedResponse = new ConserjeDTOResponse(
                1L,
                "nuevoUsuario",
                "ROLE_CONSERJE",
                conserjeEntity.getCreatedAt().toString(),
                conserjeEntity.getUpdatedAt().toString()
        );
    }

    /**
     * CAMINO FELIZ: Registro exitoso de un nuevo conserje.
     * 
     * Escenario:
     * - Username NO existe en BD
     * - Datos válidos
     * 
     * Resultado esperado:
     * - Conserje se guarda correctamente
     * - Contraseña se hashea con BCrypt
     * - Se retorna DTO de respuesta con datos del conserje
     */
    @Test
    void testRegistrarConserje_CaminoFeliz() {
        // --- ARRANGE ---
        
        // 1. Simulamos que el username NO existe
        when(conserjeDAO.existsByUsername(validRequest.username())).thenReturn(false);
        
        // 2. Simulamos mapper: DTO -> Entity (sin password hasheada aún)
        Conserje conserjePreSave = Conserje.builder()
                .username(validRequest.username())
                .password(validRequest.password()) // Todavía en texto plano
                .role("ROLE_CONSERJE")
                .build();
        when(conserjeMapper.toEntity(validRequest)).thenReturn(conserjePreSave);
        
        // 3. Simulamos el hasheo de contraseña
        when(passwordEncoder.encode(validRequest.password())).thenReturn("$2a$10$hashedPassword");
        
        // 4. Simulamos que el DAO guarda el conserje y retorna la entidad con ID
        when(conserjeDAO.save(any(Conserje.class))).thenReturn(conserjeEntity);
        
        // 5. Simulamos mapper: Entity -> DTO Response
        when(conserjeMapper.toResponse(conserjeEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        ConserjeDTOResponse resultado = gestorSeguridad.registrarConserje(validRequest);
        
        // --- ASSERT ---
        
        // Verificar que el resultado no sea nulo
        assertNotNull(resultado);
        
        // Verificar datos del resultado
        assertEquals(1L, resultado.id());
        assertEquals("nuevoUsuario", resultado.username());
        assertEquals("ROLE_CONSERJE", resultado.role());
        assertNotNull(resultado.createdAt());
        assertNotNull(resultado.updatedAt());
        
        // Verificar interacciones con los mocks
        verify(conserjeDAO, times(1)).existsByUsername(validRequest.username());
        verify(conserjeMapper, times(1)).toEntity(validRequest);
        verify(passwordEncoder, times(1)).encode(validRequest.password());
        verify(conserjeDAO, times(1)).save(any(Conserje.class));
        verify(conserjeMapper, times(1)).toResponse(conserjeEntity);
    }

    /**
     * CASO DE ERROR: Username ya existe.
     * 
     * Escenario:
     * - Username YA existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarConserje_ErrorUsernameYaExiste() {
        // --- ARRANGE ---
        
        // Simulamos que el username SÍ existe
        when(conserjeDAO.existsByUsername(validRequest.username())).thenReturn(true);
        
        // --- ACT & ASSERT ---
        
        // Verificamos que se lanza la excepción esperada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorSeguridad.registrarConserje(validRequest);
        });
        
        // Verificamos el mensaje de error
        assertEquals("El nombre de usuario ya existe", exception.getMessage());
        
        // Verificamos que el método save NUNCA fue llamado
        verify(conserjeDAO, times(1)).existsByUsername(validRequest.username());
        verify(conserjeDAO, never()).save(any(Conserje.class));
        verify(conserjeMapper, never()).toEntity(any());
        verify(passwordEncoder, never()).encode(any());
        verify(conserjeMapper, never()).toResponse(any());
    }

    /**
     * CASO DE ERROR: Excepción durante el guardado.
     * 
     * Escenario:
     * - Username válido (no existe)
     * - Error inesperado al guardar en BD (ej. timeout, constraint violation)
     * 
     * Resultado esperado:
     * - Se lanza RuntimeException envolviendo la excepción original
     * - Mensaje apropiado para el usuario
     */
    @Test
    void testRegistrarConserje_ErrorAlGuardar() {
        // --- ARRANGE ---
        
        // 1. Username NO existe
        when(conserjeDAO.existsByUsername(validRequest.username())).thenReturn(false);
        
        // 2. Mapper funciona correctamente
        Conserje conserjePreSave = Conserje.builder()
                .username(validRequest.username())
                .password(validRequest.password())
                .role("ROLE_CONSERJE")
                .build();
        when(conserjeMapper.toEntity(validRequest)).thenReturn(conserjePreSave);
        
        // 3. PasswordEncoder funciona
        when(passwordEncoder.encode(validRequest.password())).thenReturn("$2a$10$hashedPassword");
        
        // 4. Simulamos una excepción al guardar en BD
        when(conserjeDAO.save(any(Conserje.class)))
                .thenThrow(new RuntimeException("Error de conexión a la BD"));
        
        // --- ACT & ASSERT ---
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorSeguridad.registrarConserje(validRequest);
        });
        
        // Verificamos el mensaje
        assertEquals("Error al procesar el registro", exception.getMessage());
        
        // Verificamos que se intentó guardar
        verify(conserjeDAO, times(1)).save(any(Conserje.class));
        verify(conserjeMapper, never()).toResponse(any());
    }

    /**
     * CAMINO FELIZ: Verificar username existente.
     * 
     * Escenario:
     * - Username SÍ existe en BD
     * 
     * Resultado esperado:
     * - Retorna true
     */
    @Test
    void testExisteUsername_UsuarioExiste() {
        // --- ARRANGE ---
        
        String username = "usuarioExistente";
        when(conserjeDAO.existsByUsername(username)).thenReturn(true);
        
        // --- ACT ---
        
        boolean resultado = gestorSeguridad.existeUsername(username);
        
        // --- ASSERT ---
        
        assertTrue(resultado);
        verify(conserjeDAO, times(1)).existsByUsername(username);
    }

    /**
     * CAMINO FELIZ: Verificar username NO existente.
     * 
     * Escenario:
     * - Username NO existe en BD
     * 
     * Resultado esperado:
     * - Retorna false
     */
    @Test
    void testExisteUsername_UsuarioNoExiste() {
        // --- ARRANGE ---
        
        String username = "usuarioNuevo";
        when(conserjeDAO.existsByUsername(username)).thenReturn(false);
        
        // --- ACT ---
        
        boolean resultado = gestorSeguridad.existeUsername(username);
        
        // --- ASSERT ---
        
        assertFalse(resultado);
        verify(conserjeDAO, times(1)).existsByUsername(username);
    }

    /**
     * VERIFICACIÓN: La contraseña se hashea correctamente.
     * 
     * Escenario:
     * - Registro exitoso
     * 
     * Resultado esperado:
     * - La contraseña guardada NO es la contraseña en texto plano
     * - Se usó el PasswordEncoder
     */
    @Test
    void testRegistrarConserje_ContrasenaEsHasheada() {
        // --- ARRANGE ---
        
        when(conserjeDAO.existsByUsername(validRequest.username())).thenReturn(false);
        
        Conserje conserjePreSave = Conserje.builder()
                .username(validRequest.username())
                .password(validRequest.password())
                .role("ROLE_CONSERJE")
                .build();
        when(conserjeMapper.toEntity(validRequest)).thenReturn(conserjePreSave);
        
        String hashedPassword = "$2a$10$xYz789AbcDef";
        when(passwordEncoder.encode(validRequest.password())).thenReturn(hashedPassword);
        
        // Capturamos el objeto que se pasa a save() para verificarlo
        when(conserjeDAO.save(argThat(conserje -> 
            conserje.getPassword().equals(hashedPassword) &&
            !conserje.getPassword().equals(validRequest.password())
        ))).thenReturn(conserjeEntity);
        
        when(conserjeMapper.toResponse(conserjeEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        gestorSeguridad.registrarConserje(validRequest);
        
        // --- ASSERT ---
        
        // Verificar que el encoder fue llamado con la contraseña correcta
        verify(passwordEncoder, times(1)).encode(validRequest.password());
        
        // Verificar que se guardó con la contraseña hasheada
        verify(conserjeDAO, times(1)).save(argThat(conserje ->
            conserje.getPassword().equals(hashedPassword)
        ));
    }
}

