package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dao.EstadiaDAO;
import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.ReservaDAO;
import com.losmergeconflicts.hotelpremier.dto.DetalleReservaDTO;
import com.losmergeconflicts.hotelpremier.dto.GrillaDisponibilidadDTO;
import com.losmergeconflicts.hotelpremier.dto.HabitacionDTO;
import com.losmergeconflicts.hotelpremier.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestorHabitacionesImp implements GestorHabitaciones {

    private final HabitacionDAO habitacionDAO;
    private final ReservaDAO reservaDAO;
    private final EstadiaDAO estadiaDAO;

    @Override
    public void modificarHabitacion(HabitacionDTO habitacionDTO) {
    }

    @Override
    public Habitacion buscarHabitacion(int id) {
        return habitacionDAO.findById((long) id).orElse(null);
    }

    @Override
    public List<Habitacion> listarHabitaciones() {
        return habitacionDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GrillaDisponibilidadDTO obtenerEstados(LocalDate fechaDesde, LocalDate fechaHasta, TipoHabitacion tipoHabitacion) {

        if (fechaDesde == null || fechaHasta == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias");
        }
        if (fechaHasta.isBefore(fechaDesde)) {
            throw new IllegalArgumentException("Fecha Hasta inválida (anterior a Fecha Desde)");
        }

        // 1. Obtener Habitaciones: Si tipo es NULL, trae todas.
        List<Habitacion> habitaciones;
        if (tipoHabitacion == null) {
            habitaciones = habitacionDAO.findAllByOrderByNombreAsc();
        } else {
            habitaciones = habitacionDAO.findByTipoHabitacionOrderByNombreAsc(tipoHabitacion);
        }

        List<String> nombresHabitaciones = habitaciones.stream().map(Habitacion::getNombre).toList();

        // 2. Obtener Reservas y Estadías en el rango
        List<Reserva> reservas = reservaDAO.findReservasEnRango(fechaDesde, fechaHasta);
        List<Estadia> estadias = estadiaDAO.findEstadiasEnRango(fechaDesde.atStartOfDay(), fechaHasta.atTime(23, 59));

        // 3. Construir filas (Cada fila es UN DÍA)
        List<GrillaDisponibilidadDTO.FilaFechaDTO> filas = new ArrayList<>();

        LocalDate current = fechaDesde;
        while (!current.isAfter(fechaHasta)) {
            Map<String, String> estadosDia = new HashMap<>();

            for (Habitacion hab : habitaciones) {
                TipoEstadoHabitacion estado = calcularEstado(hab, current, reservas, estadias);
                estadosDia.put(hab.getNombre(), estado.name());
            }

            filas.add(new GrillaDisponibilidadDTO.FilaFechaDTO(current, estadosDia));
            current = current.plusDays(1);
        }

        return new GrillaDisponibilidadDTO(nombresHabitaciones, filas);
    }

    private TipoEstadoHabitacion calcularEstado(Habitacion hab, LocalDate fecha, List<Reserva> reservas, List<Estadia> estadias) {
        // Prioridad 1: OCUPADA (ROJO)
        boolean ocupada = estadias.stream().anyMatch(e ->
                e.getHabitacion().getId().equals(hab.getId()) &&
                        !fecha.isBefore(e.getFechaHoraIngreso().toLocalDate()) &&
                        (e.getFechaHoraEgreso() == null || !fecha.isAfter(e.getFechaHoraEgreso().toLocalDate()))
        );
        if (ocupada) return TipoEstadoHabitacion.OCUPADA;

        // Prioridad 2: RESERVADA (AMARILLO)
        boolean reservada = reservas.stream().anyMatch(r ->
                r.getHabitaciones().stream().anyMatch(h -> h.getId().equals(hab.getId())) &&
                        !fecha.isBefore(r.getFechaIngreso()) &&
                        !fecha.isAfter(r.getFechaEgreso())
        );
        if (reservada) return TipoEstadoHabitacion.RESERVADA;

        // Prioridad 3: LIBRE (BLANCO)
        return TipoEstadoHabitacion.LIBRE;
    }

    @Override
    public DetalleReservaDTO obtenerDetalleReserva(String nombreHabitacion, LocalDate fecha) {
        Reserva reserva = reservaDAO.findReservaPorHabitacionYFecha(nombreHabitacion, fecha)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró reserva para esta fecha y habitación"));

        return new DetalleReservaDTO(
                reserva.getHuesped().getApellido(),
                reserva.getHuesped().getNombre(),
                reserva.getHuesped().getTelefono()
        );
    }
}