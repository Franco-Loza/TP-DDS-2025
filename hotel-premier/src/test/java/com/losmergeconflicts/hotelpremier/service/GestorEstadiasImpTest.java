package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.EstadiaDAO;
import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.HuespedDAO;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.*;
import com.losmergeconflicts.hotelpremier.mapper.EstadiaMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GestorEstadiasImpTest {

    @Mock private EstadiaDAO estadiaDAO;
    @Mock private HabitacionDAO habitacionDAO;
    @Mock private HuespedDAO huespedDAO;
    @Mock private EstadiaMapper estadiaMapper;

    @InjectMocks
    private GestorEstadiasImp gestorEstadias;

    private EstadiaDTORequest requestValido;
    private Habitacion habitacionDoble;
    private Huesped responsableAdulto;
    private Huesped acompanante;
    private Estadia estadiaGuardada;

    @BeforeEach
    void setUp() {
        // 1. Habitación Doble (Capacidad 2)
        habitacionDoble = Habitacion.builder()
                .id(1L)
                .nombre("DE1")
                .tipoHabitacion(TipoHabitacion.DOBLE_ESTANDAR)
                .estadoHabitacion(TipoEstadoHabitacion.LIBRE)
                .build();

        // 2. Responsable Mayor de Edad (25 años)
        responsableAdulto = new Huesped();
        responsableAdulto.setId(10L);
        responsableAdulto.setNombre("Juan");
        responsableAdulto.setFechaNacimiento(LocalDate.now().minusYears(25));

        // 3. Acompañante
        acompanante = new Huesped();
        acompanante.setId(11L);
        acompanante.setNombre("Maria");

        // 4. Request
        requestValido = new EstadiaDTORequest(
                1L, // idHabitacion
                LocalDate.now(), // fechaIngreso
                LocalDate.now().plusDays(3), // fechaEgreso
                10L, // idResponsable
                Arrays.asList(11L) // idsAcompaniantes
        );

        // 5. Estadía Guardada (Simulada)
        estadiaGuardada = Estadia.builder()
                .id(100L)
                .habitacion(habitacionDoble)
                .huespedAsignado(responsableAdulto)
                .build();
    }

    /**
     * CAMINO FELIZ: Registrar Ocupación siguiendo el Diagrama de Secuencia.
     * * Escenario:
     * - Responsable mayor de edad.
     * - Capacidad correcta (1 responsable + 1 acompañante <= 2).
     * * Verificaciones Clave (Diagrama de Secuencia):
     * - Se llama a save() para la estadía base.
     * - Se llama a registrarAcompanante() manualmente en un bucle.
     * - Se llama a actualizarEstado() al final.
     */
    @Test
    void testRegistrarOcupacion_CaminoFeliz_SecuenciaCorrecta() {
        // --- ARRANGE ---
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacionDoble));
        when(huespedDAO.findById(10L)).thenReturn(Optional.of(responsableAdulto));
        when(huespedDAO.findAllById(anyList())).thenReturn(Collections.singletonList(acompanante));

        when(estadiaDAO.save(any(Estadia.class))).thenReturn(estadiaGuardada);

        EstadiaDTOResponse responseDTO = new EstadiaDTOResponse(100L, LocalDateTime.now(), "DE1", "Juan");
        when(estadiaMapper.toResponse(estadiaGuardada)).thenReturn(responseDTO);

        // --- ACT ---
        EstadiaDTOResponse resultado = gestorEstadias.registrarOcupacion(requestValido);

        // --- ASSERT ---
        assertNotNull(resultado);

        // 1. Verificar guardado de Estadia Base
        verify(estadiaDAO, times(1)).save(any(Estadia.class));

        // 2. Verificar Bucle de Acompañantes (SQL Nativo)
        verify(estadiaDAO, times(1)).registrarAcompanante(100L, 11L);

        // 3. Verificar Actualización de Estado de Habitación
        verify(habitacionDAO, times(1)).actualizarEstado(1L, TipoEstadoHabitacion.OCUPADA);
    }

    /**
     * REGLA DE NEGOCIO: Responsable Menor de Edad.
     * * Escenario:
     * - El responsable tiene 17 años.
     * * Resultado:
     * - IllegalArgumentException.
     * - No se guarda nada.
     */
    @Test
    void testRegistrarOcupacion_ErrorResponsableMenor() {
        // --- ARRANGE ---
        Huesped menor = new Huesped();
        menor.setId(20L);
        menor.setFechaNacimiento(LocalDate.now().minusYears(17)); // 17 años

        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacionDoble));
        when(huespedDAO.findById(10L)).thenReturn(Optional.of(menor)); // Usamos el ID del request pero devolvemos menor

        // --- ACT & ASSERT ---
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                gestorEstadias.registrarOcupacion(requestValido)
        );

        assertEquals("El Responsable debe ser mayor de edad.", ex.getMessage());

        verify(estadiaDAO, never()).save(any());
    }

    /**
     * REGLA DE NEGOCIO: Exceso de Capacidad.
     * * Escenario:
     * - Habitación Individual (Capacidad 1).
     * - Intentan entrar 2 personas (1 responsable + 1 acompañante).
     */
    @Test
    void testRegistrarOcupacion_ErrorCapacidadExcedida() {
        // --- ARRANGE ---
        Habitacion habitacionIndividual = Habitacion.builder()
                .id(2L)
                .tipoHabitacion(TipoHabitacion.INDIVIDUAL_ESTANDAR) // Capacidad 1
                .build();

        when(habitacionDAO.findById(anyLong())).thenReturn(Optional.of(habitacionIndividual));
        when(huespedDAO.findById(anyLong())).thenReturn(Optional.of(responsableAdulto));
        // Simulamos 1 acompañante
        when(huespedDAO.findAllById(anyList())).thenReturn(Collections.singletonList(acompanante));

        // --- ACT & ASSERT ---
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                gestorEstadias.registrarOcupacion(requestValido)
        );

        assertTrue(ex.getMessage().contains("Capacidad excedida"));
        verify(estadiaDAO, never()).save(any());
    }

    /**
     * CASO DE ERROR: Habitación no encontrada.
     */
    @Test
    void testRegistrarOcupacion_ErrorHabitacionNoExiste() {
        when(habitacionDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                gestorEstadias.registrarOcupacion(requestValido)
        );
    }
}