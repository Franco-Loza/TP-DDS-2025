package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.ReservaDAO;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.Reserva;
import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import com.losmergeconflicts.hotelpremier.mapper.ReservaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para GestorReservasImp.
 * 
 * Probamos la lógica de negocio de gestión de reservas en aislamiento,
 * usando Mockito para simular las dependencias (DAOs, Mappers).
 */
@ExtendWith(MockitoExtension.class)
public class GestorReservasImpTest {

    @Mock
    private ReservaDAO reservaDAO;

    @Mock
    private HabitacionDAO habitacionDAO;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private GestorReservasImp gestorReservas;

    private ReservaDTORequest validRequest;
    private Reserva reservaEntity;
    private ReservaDTOResponse expectedResponse;
    private Habitacion habitacion1;
    private Habitacion habitacion2;
    private List<Long> idsHabitaciones;

    @BeforeEach
    void setUp() {
        // Datos de prueba reutilizables

        // 1. Crear habitaciones de prueba
        habitacion1 = Habitacion.builder()
                .id(1L)
                .nombre("IE1")
                .precio(100.0f)
                .tipoHabitacion(TipoHabitacion.INDIVIDUAL_ESTANDAR)
                .estadoHabitacion(TipoEstadoHabitacion.LIBRE)
                .build();

        habitacion2 = Habitacion.builder()
                .id(2L)
                .nombre("DE1")
                .precio(150.0f)
                .tipoHabitacion(TipoHabitacion.DOBLE_ESTANDAR)
                .estadoHabitacion(TipoEstadoHabitacion.LIBRE)
                .build();

        // 2. IDs de habitaciones
        idsHabitaciones = Arrays.asList(1L, 2L);

        // 3. Request de entrada válido
        validRequest = new ReservaDTORequest(
                LocalDate.now().plusDays(1),    // fechaIngreso: mañana
                LocalDate.now().plusDays(5),    // fechaEgreso: dentro de 5 días
                "Juan",                          // nombreHuesped
                "Pérez",                         // apellidoHuesped
                "123456789",                     // telefonoHuesped
                idsHabitaciones                  // idsHabitaciones
        );

        // 4. Entidad Reserva
        reservaEntity = Reserva.builder()
                .fechaIngreso(validRequest.fechaIngreso())
                .fechaEgreso(validRequest.fechaEgreso())
                .nombreHuesped(validRequest.nombreHuesped())
                .apellidoHuesped(validRequest.apellidoHuesped())
                .telefonoHuesped(validRequest.telefonoHuesped())
                .habitaciones(Arrays.asList(habitacion1, habitacion2))
                .build();

        // 5. Response esperado
        expectedResponse = new ReservaDTOResponse(
                1L,                              // id
                validRequest.fechaIngreso(),     // fechaIngreso
                validRequest.fechaEgreso(),      // fechaEgreso
                validRequest.nombreHuesped(),    // nombreHuesped
                validRequest.apellidoHuesped(),  // apellidoHuesped
                validRequest.telefonoHuesped(),  // telefonoHuesped
                idsHabitaciones                  // idsHabitaciones
        );
    }

    /**
     * CAMINO FELIZ: Registro exitoso de una nueva reserva.
     * 
     * Escenario:
     * - Todas las habitaciones solicitadas existen en BD
     * - Las fechas son válidas (egreso posterior a ingreso)
     * - Datos válidos del huésped
     * 
     * Resultado esperado:
     * - Se buscan las habitaciones por sus IDs
     * - Se convierte el DTO a entidad usando el mapper
     * - Se asignan las habitaciones a la reserva
     * - Se guarda la reserva correctamente
     * - Se retorna DTO de respuesta con datos de la reserva
     */
    @Test
    void testRegistrarReserva_CaminoFeliz() {
        // --- ARRANGE ---

        // 1. Simulamos que las habitaciones existen
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(habitacionDAO.findById(2L)).thenReturn(Optional.of(habitacion2));

        // 2. Simulamos el mapeo de DTO a entidad
        when(reservaMapper.toEntity(validRequest)).thenReturn(reservaEntity);

        // 3. Simulamos el guardado exitoso
        Reserva reservaGuardada = Reserva.builder()
                .id(1L)
                .fechaIngreso(validRequest.fechaIngreso())
                .fechaEgreso(validRequest.fechaEgreso())
                .nombreHuesped(validRequest.nombreHuesped())
                .apellidoHuesped(validRequest.apellidoHuesped())
                .telefonoHuesped(validRequest.telefonoHuesped())
                .habitaciones(Arrays.asList(habitacion1, habitacion2))
                .build();
        when(reservaDAO.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // 4. Simulamos el mapeo de entidad a response
        when(reservaMapper.toResponse(reservaGuardada)).thenReturn(expectedResponse);

        // --- ACT ---
        ReservaDTOResponse resultado = gestorReservas.registrarReserva(validRequest);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(validRequest.fechaIngreso(), resultado.fechaIngreso());
        assertEquals(validRequest.fechaEgreso(), resultado.fechaEgreso());
        assertEquals(validRequest.nombreHuesped(), resultado.nombreHuesped());
        assertEquals(validRequest.apellidoHuesped(), resultado.apellidoHuesped());
        assertEquals(validRequest.telefonoHuesped(), resultado.telefonoHuesped());
        assertEquals(idsHabitaciones, resultado.idsHabitaciones());

        // Verificar que los mocks fueron llamados correctamente
        verify(habitacionDAO, times(1)).findById(1L);
        verify(habitacionDAO, times(1)).findById(2L);
        verify(reservaMapper, times(1)).toEntity(validRequest);
        verify(reservaDAO, times(1)).save(any(Reserva.class));
        verify(reservaMapper, times(1)).toResponse(reservaGuardada);
    }

    /**
     * CASO DE ERROR: Request nulo.
     * 
     * Escenario:
     * - Se envía un request null
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarReserva_ErrorRequestNulo() {
        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestorReservas.registrarReserva(null)
        );

        assertEquals("Los datos de la reserva no pueden ser nulos", exception.getMessage());

        // Verificar que no se llamó a ningún DAO
        verify(habitacionDAO, never()).findById(anyLong());
        verify(reservaDAO, never()).save(any(Reserva.class));
        verify(reservaMapper, never()).toEntity(any());
        verify(reservaMapper, never()).toResponse(any());
    }

    /**
     * CASO DE ERROR: Fecha de egreso no posterior a fecha de ingreso.
     * 
     * Escenario:
     * - La fecha de egreso es igual a la fecha de ingreso
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarReserva_ErrorFechaEgresoIgualAIngreso() {
        // --- ARRANGE ---
        LocalDate fecha = LocalDate.now().plusDays(1);
        ReservaDTORequest requestConFechasIguales = new ReservaDTORequest(
                fecha,
                fecha,  // misma fecha
                "Juan",
                "Pérez",
                "123456789",
                idsHabitaciones
        );

        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestorReservas.registrarReserva(requestConFechasIguales)
        );

        assertEquals("La fecha de egreso debe ser posterior a la fecha de ingreso", exception.getMessage());

        // Verificar que no se realizaron operaciones de BD
        verify(habitacionDAO, never()).findById(anyLong());
        verify(reservaDAO, never()).save(any(Reserva.class));
    }

    /**
     * CASO DE ERROR: Fecha de egreso anterior a fecha de ingreso.
     * 
     * Escenario:
     * - La fecha de egreso es anterior a la fecha de ingreso
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarReserva_ErrorFechaEgresoAnteriorAIngreso() {
        // --- ARRANGE ---
        ReservaDTORequest requestConFechasInvertidas = new ReservaDTORequest(
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),  // fecha anterior
                "Juan",
                "Pérez",
                "123456789",
                idsHabitaciones
        );

        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestorReservas.registrarReserva(requestConFechasInvertidas)
        );

        assertEquals("La fecha de egreso debe ser posterior a la fecha de ingreso", exception.getMessage());

        // Verificar que no se realizaron operaciones de BD
        verify(habitacionDAO, never()).findById(anyLong());
        verify(reservaDAO, never()).save(any(Reserva.class));
    }

    /**
     * CASO DE ERROR: Lista de IDs de habitaciones vacía.
     * 
     * Escenario:
     * - Se envía una lista vacía de IDs de habitaciones
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarReserva_ErrorListaHabitacionesVacia() {
        // --- ARRANGE ---
        ReservaDTORequest requestSinHabitaciones = new ReservaDTORequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                "Juan",
                "Pérez",
                "123456789",
                Collections.emptyList()  // lista vacía
        );

        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestorReservas.registrarReserva(requestSinHabitaciones)
        );

        assertEquals("Debe seleccionar al menos una habitación", exception.getMessage());

        // Verificar que no se realizaron operaciones de BD
        verify(habitacionDAO, never()).findById(anyLong());
        verify(reservaDAO, never()).save(any(Reserva.class));
    }

    /**
     * CASO DE ERROR: Lista de IDs de habitaciones nula.
     * 
     * Escenario:
     * - Se envía null como lista de IDs de habitaciones
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testRegistrarReserva_ErrorListaHabitacionesNula() {
        // --- ARRANGE ---
        ReservaDTORequest requestConListaNula = new ReservaDTORequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                "Juan",
                "Pérez",
                "123456789",
                null  // lista nula
        );

        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestorReservas.registrarReserva(requestConListaNula)
        );

        assertEquals("Debe seleccionar al menos una habitación", exception.getMessage());

        // Verificar que no se realizaron operaciones de BD
        verify(habitacionDAO, never()).findById(anyLong());
        verify(reservaDAO, never()).save(any(Reserva.class));
    }

    /**
     * CASO DE ERROR: Habitación no encontrada.
     * 
     * Escenario:
     * - Una de las habitaciones solicitadas NO existe en BD
     * 
     * Resultado esperado:
     * - Se lanza RuntimeException (envuelve IllegalArgumentException)
     * - NO se guarda la reserva
     * - Mensaje de error apropiado indicando el ID inexistente en la causa
     */
    @Test
    void testRegistrarReserva_ErrorHabitacionNoExiste() {
        // --- ARRANGE ---

        // Simulamos que la primera habitación existe
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));

        // Simulamos que la segunda habitación NO existe
        when(habitacionDAO.findById(2L)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gestorReservas.registrarReserva(validRequest)
        );

        assertEquals("Error al procesar el registro de la reserva", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("La habitación con ID 2 no existe"));

        // Verificar que NO se guardó la reserva
        verify(reservaDAO, never()).save(any(Reserva.class));
        verify(reservaMapper, never()).toEntity(any());
    }

    /**
     * CASO DE ERROR: Múltiples habitaciones no encontradas.
     * 
     * Escenario:
     * - La primera habitación solicitada no existe en BD
     * - La excepción se lanza inmediatamente, sin buscar las demás habitaciones
     * 
     * Resultado esperado:
     * - Se lanza RuntimeException (envuelve IllegalArgumentException para la primera habitación no encontrada)
     * - NO se guarda la reserva
     */
    @Test
    void testRegistrarReserva_ErrorVariasHabitacionesNoExisten() {
        // --- ARRANGE ---

        // Simulamos que la primera habitación no existe
        // (no se necesita configurar la segunda porque nunca se buscará)
        when(habitacionDAO.findById(1L)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gestorReservas.registrarReserva(validRequest)
        );

        assertEquals("Error al procesar el registro de la reserva", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("La habitación con ID 1 no existe"));

        // Verificar que NO se guardó la reserva
        verify(reservaDAO, never()).save(any(Reserva.class));
    }

    /**
     * CASO DE ERROR: Excepción durante el guardado.
     * 
     * Escenario:
     * - Fechas válidas
     * - Todas las habitaciones existen
     * - Error inesperado al guardar en BD (ej. timeout, constraint violation)
     * 
     * Resultado esperado:
     * - Se lanza RuntimeException envolviendo la excepción original
     * - Mensaje apropiado para el usuario
     */
    @Test
    void testRegistrarReserva_ErrorAlGuardar() {
        // --- ARRANGE ---

        // Simulamos que las habitaciones existen
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(habitacionDAO.findById(2L)).thenReturn(Optional.of(habitacion2));

        // Simulamos el mapeo correcto
        when(reservaMapper.toEntity(validRequest)).thenReturn(reservaEntity);

        // Simulamos una excepción al guardar
        when(reservaDAO.save(any(Reserva.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // --- ACT & ASSERT ---
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gestorReservas.registrarReserva(validRequest)
        );

        assertEquals("Error al procesar el registro de la reserva", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Error de base de datos", exception.getCause().getMessage());

        // Verificar que se intentó guardar
        verify(reservaDAO, times(1)).save(any(Reserva.class));
        // Verificar que NO se llamó al mapper de respuesta
        verify(reservaMapper, never()).toResponse(any());
    }

    /**
     * CASO EXITOSO: Reserva con una sola habitación.
     * 
     * Escenario:
     * - Se reserva una única habitación
     * - Fechas válidas
     * 
     * Resultado esperado:
     * - Se guarda correctamente la reserva con una habitación
     * - Se retorna DTO de respuesta correcto
     */
    @Test
    void testRegistrarReserva_UnaSolaHabitacion() {
        // --- ARRANGE ---
        List<Long> unaHabitacion = Collections.singletonList(1L);

        ReservaDTORequest requestUnaHabitacion = new ReservaDTORequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                "María",
                "García",
                "987654321",
                unaHabitacion
        );

        Reserva reservaUnaHabitacion = Reserva.builder()
                .fechaIngreso(requestUnaHabitacion.fechaIngreso())
                .fechaEgreso(requestUnaHabitacion.fechaEgreso())
                .nombreHuesped(requestUnaHabitacion.nombreHuesped())
                .apellidoHuesped(requestUnaHabitacion.apellidoHuesped())
                .telefonoHuesped(requestUnaHabitacion.telefonoHuesped())
                .habitaciones(Collections.singletonList(habitacion1))
                .build();

        Reserva reservaGuardada = Reserva.builder()
                .id(2L)
                .fechaIngreso(requestUnaHabitacion.fechaIngreso())
                .fechaEgreso(requestUnaHabitacion.fechaEgreso())
                .nombreHuesped(requestUnaHabitacion.nombreHuesped())
                .apellidoHuesped(requestUnaHabitacion.apellidoHuesped())
                .telefonoHuesped(requestUnaHabitacion.telefonoHuesped())
                .habitaciones(Collections.singletonList(habitacion1))
                .build();

        ReservaDTOResponse responseEsperado = new ReservaDTOResponse(
                2L,
                requestUnaHabitacion.fechaIngreso(),
                requestUnaHabitacion.fechaEgreso(),
                requestUnaHabitacion.nombreHuesped(),
                requestUnaHabitacion.apellidoHuesped(),
                requestUnaHabitacion.telefonoHuesped(),
                unaHabitacion
        );

        // Configurar mocks
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(reservaMapper.toEntity(requestUnaHabitacion)).thenReturn(reservaUnaHabitacion);
        when(reservaDAO.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toResponse(reservaGuardada)).thenReturn(responseEsperado);

        // --- ACT ---
        ReservaDTOResponse resultado = gestorReservas.registrarReserva(requestUnaHabitacion);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(2L, resultado.id());
        assertEquals(1, resultado.idsHabitaciones().size());
        assertEquals(1L, resultado.idsHabitaciones().get(0));

        verify(habitacionDAO, times(1)).findById(1L);
        verify(reservaDAO, times(1)).save(any(Reserva.class));
    }

    /**
     * CASO EXITOSO: Reserva con múltiples habitaciones (más de 2).
     * 
     * Escenario:
     * - Se reservan tres habitaciones
     * - Fechas válidas
     * 
     * Resultado esperado:
     * - Se guarda correctamente la reserva con todas las habitaciones
     * - Se retorna DTO de respuesta con todas las habitaciones
     */
    @Test
    void testRegistrarReserva_VariasHabitaciones() {
        // --- ARRANGE ---
        Habitacion habitacion3 = Habitacion.builder()
                .id(3L)
                .nombre("DS1")
                .precio(200.0f)
                .tipoHabitacion(TipoHabitacion.DOBLE_SUPERIOR)
                .estadoHabitacion(TipoEstadoHabitacion.LIBRE)
                .build();

        List<Long> tresHabitaciones = Arrays.asList(1L, 2L, 3L);

        ReservaDTORequest requestTresHabitaciones = new ReservaDTORequest(
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(7),
                "Carlos",
                "Rodríguez",
                "555123456",
                tresHabitaciones
        );

        Reserva reservaTresHabitaciones = Reserva.builder()
                .fechaIngreso(requestTresHabitaciones.fechaIngreso())
                .fechaEgreso(requestTresHabitaciones.fechaEgreso())
                .nombreHuesped(requestTresHabitaciones.nombreHuesped())
                .apellidoHuesped(requestTresHabitaciones.apellidoHuesped())
                .telefonoHuesped(requestTresHabitaciones.telefonoHuesped())
                .habitaciones(Arrays.asList(habitacion1, habitacion2, habitacion3))
                .build();

        Reserva reservaGuardada = Reserva.builder()
                .id(3L)
                .fechaIngreso(requestTresHabitaciones.fechaIngreso())
                .fechaEgreso(requestTresHabitaciones.fechaEgreso())
                .nombreHuesped(requestTresHabitaciones.nombreHuesped())
                .apellidoHuesped(requestTresHabitaciones.apellidoHuesped())
                .telefonoHuesped(requestTresHabitaciones.telefonoHuesped())
                .habitaciones(Arrays.asList(habitacion1, habitacion2, habitacion3))
                .build();

        ReservaDTOResponse responseEsperado = new ReservaDTOResponse(
                3L,
                requestTresHabitaciones.fechaIngreso(),
                requestTresHabitaciones.fechaEgreso(),
                requestTresHabitaciones.nombreHuesped(),
                requestTresHabitaciones.apellidoHuesped(),
                requestTresHabitaciones.telefonoHuesped(),
                tresHabitaciones
        );

        // Configurar mocks
        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(habitacionDAO.findById(2L)).thenReturn(Optional.of(habitacion2));
        when(habitacionDAO.findById(3L)).thenReturn(Optional.of(habitacion3));
        when(reservaMapper.toEntity(requestTresHabitaciones)).thenReturn(reservaTresHabitaciones);
        when(reservaDAO.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toResponse(reservaGuardada)).thenReturn(responseEsperado);

        // --- ACT ---
        ReservaDTOResponse resultado = gestorReservas.registrarReserva(requestTresHabitaciones);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(3L, resultado.id());
        assertEquals(3, resultado.idsHabitaciones().size());
        assertTrue(resultado.idsHabitaciones().contains(1L));
        assertTrue(resultado.idsHabitaciones().contains(2L));
        assertTrue(resultado.idsHabitaciones().contains(3L));

        verify(habitacionDAO, times(1)).findById(1L);
        verify(habitacionDAO, times(1)).findById(2L);
        verify(habitacionDAO, times(1)).findById(3L);
        verify(reservaDAO, times(1)).save(any(Reserva.class));
    }

    /**
     * VERIFICACIÓN: Las habitaciones se asignan correctamente a la reserva.
     * 
     * Escenario:
     * - Reserva exitosa con múltiples habitaciones
     * 
     * Resultado esperado:
     * - La entidad Reserva tiene las habitaciones asignadas antes de guardarse
     * - El método setHabitaciones fue invocado con la lista correcta
     */
    @Test
    void testRegistrarReserva_HabitacionesSeAsignanCorrectamente() {
        // --- ARRANGE ---

        when(habitacionDAO.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(habitacionDAO.findById(2L)).thenReturn(Optional.of(habitacion2));
        when(reservaMapper.toEntity(validRequest)).thenReturn(reservaEntity);

        Reserva reservaGuardada = Reserva.builder()
                .id(1L)
                .fechaIngreso(validRequest.fechaIngreso())
                .fechaEgreso(validRequest.fechaEgreso())
                .nombreHuesped(validRequest.nombreHuesped())
                .apellidoHuesped(validRequest.apellidoHuesped())
                .telefonoHuesped(validRequest.telefonoHuesped())
                .habitaciones(Arrays.asList(habitacion1, habitacion2))
                .build();

        when(reservaDAO.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toResponse(reservaGuardada)).thenReturn(expectedResponse);

        // --- ACT ---
        gestorReservas.registrarReserva(validRequest);

        // --- ASSERT ---

        // Verificar que el método save fue llamado con una reserva que tiene habitaciones
        verify(reservaDAO, times(1)).save(argThat(reserva -> {
            assertNotNull(reserva.getHabitaciones());
            assertEquals(2, reserva.getHabitaciones().size());
            assertTrue(reserva.getHabitaciones().contains(habitacion1));
            assertTrue(reserva.getHabitaciones().contains(habitacion2));
            return true;
        }));
    }
}
