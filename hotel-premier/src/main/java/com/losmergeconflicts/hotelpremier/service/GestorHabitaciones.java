package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dto.*;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;

import java.time.LocalDate;
import java.util.List;

public interface GestorHabitaciones {

    HabitacionDTOResponse modificarHabitacion(Long id, HabitacionDTORequest request);

    HabitacionDTOResponse buscarHabitacion(Long id);

    List<HabitacionDTOResponse> listarHabitaciones();

    GrillaDisponibilidadDTO obtenerEstados(LocalDate desde, LocalDate hasta, TipoHabitacion tipo);

    DetalleReservaDTO obtenerDetalleReserva(String nombreHabitacion, LocalDate fecha);

    List<HabitacionDTOResponse> listarHabitacionesPorID(List<Long> idsHabitaciones);
}