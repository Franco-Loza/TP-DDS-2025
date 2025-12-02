package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.EstadiaDAO;
import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.ReservaDAO;
import com.losmergeconflicts.hotelpremier.dto.*;
import com.losmergeconflicts.hotelpremier.entity.*;
import com.losmergeconflicts.hotelpremier.mapper.HabitacionMapper;
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

@ExtendWith(MockitoExtension.class)
class GestorHabitacionesImpTest {

    @Mock private HabitacionDAO habitacionDAO;
    @Mock private ReservaDAO reservaDAO;
    @Mock private EstadiaDAO estadiaDAO;
    @Mock private HabitacionMapper habitacionMapper;

    @InjectMocks
    private GestorHabitacionesImp gestorHabitaciones;

    @Test
    void testObtenerDetalleReserva_CaminoFeliz() {
        // --- ARRANGE ---
        String nombreHab = "IE1";
        LocalDate fecha = LocalDate.now();

        Huesped huesped = new Huesped();
        huesped.setNombre("Lionel");
        huesped.setApellido("Messi");
        huesped.setTelefono("10101010");

        Reserva reserva = new Reserva();
        reserva.setHuesped(huesped);

        // Simulamos que el DAO devuelve una lista con 1 reserva
        when(reservaDAO.findReservaPorHabitacionYFecha(eq(nombreHab), eq(fecha)))
                .thenReturn(Collections.singletonList(reserva));

        // --- ACT ---
        DetalleReservaDTO detalle = gestorHabitaciones.obtenerDetalleReserva(nombreHab, fecha);

        // --- ASSERT ---
        assertEquals("Messi", detalle.apellido());
        assertEquals("Lionel", detalle.nombre());
        assertEquals("10101010", detalle.telefono());
    }

    /**
     * Caso Borde: Reserva existe pero el objeto Huesped es null.
     * El sistema debe usar los campos de texto plano (nombreHuesped, etc).
     */
    @Test
    void testObtenerDetalleReserva_HuespedNull_UsaDatosPlanos() {
        // --- ARRANGE ---
        Reserva reservaSinHuesped = new Reserva();
        reservaSinHuesped.setHuesped(null);
        reservaSinHuesped.setNombreHuesped("Cristiano");
        reservaSinHuesped.setApellidoHuesped("Ronaldo");
        reservaSinHuesped.setTelefonoHuesped("777777");

        when(reservaDAO.findReservaPorHabitacionYFecha(anyString(), any()))
                .thenReturn(Collections.singletonList(reservaSinHuesped));

        // --- ACT ---
        DetalleReservaDTO detalle = gestorHabitaciones.obtenerDetalleReserva("IE1", LocalDate.now());

        // --- ASSERT ---
        assertEquals("Ronaldo", detalle.apellido());
        assertEquals("Cristiano", detalle.nombre());
    }

    @Test
    void testObtenerDetalleReserva_ErrorSinReservas() {
        // --- ARRANGE ---
        when(reservaDAO.findReservaPorHabitacionYFecha(anyString(), any()))
                .thenReturn(Collections.emptyList());

        // --- ACT & ASSERT ---
        assertThrows(IllegalArgumentException.class, () ->
                gestorHabitaciones.obtenerDetalleReserva("IE1", LocalDate.now())
        );
    }

    @Test
    void testListarHabitacionesPorID_Exito() {
        // --- ARRANGE ---
        List<Long> ids = Arrays.asList(1L, 2L);
        Habitacion h1 = new Habitacion(); h1.setId(1L);
        Habitacion h2 = new Habitacion(); h2.setId(2L);

        when(habitacionDAO.findAllById(ids)).thenReturn(Arrays.asList(h1, h2));

        // CORRECCIÓN: Usamos los Enums en lugar de Strings "SIMPLE"/"LIBRE"
        when(habitacionMapper.toResponseLimited(any())).thenReturn(
                new HabitacionDTOResponse(
                        1L,
                        "H1",
                        100f,
                        TipoHabitacion.INDIVIDUAL_ESTANDAR,
                        TipoEstadoHabitacion.LIBRE
                )
        );

        // --- ACT ---
        List<HabitacionDTOResponse> resultado = gestorHabitaciones.listarHabitacionesPorID(ids);

        // --- ASSERT ---
        assertEquals(2, resultado.size());
        verify(habitacionDAO).findAllById(ids);
    }
}