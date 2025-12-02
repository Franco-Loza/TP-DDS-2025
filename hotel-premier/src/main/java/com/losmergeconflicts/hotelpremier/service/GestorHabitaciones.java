package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dto.DetalleReservaDTO;
import com.losmergeconflicts.hotelpremier.dto.GrillaDisponibilidadDTO;
import com.losmergeconflicts.hotelpremier.dto.HabitacionDTO;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;

import java.time.LocalDate;
import java.util.List;

public interface GestorHabitaciones {

    void modificarHabitacion(HabitacionDTO habitacionDTO);
    Habitacion buscarHabitacion(int id);
    List<Habitacion> listarHabitaciones();

    GrillaDisponibilidadDTO obtenerEstados(LocalDate desde, LocalDate hasta, TipoHabitacion tipo);

    DetalleReservaDTO obtenerDetalleReserva(String nombreHabitacion, LocalDate fecha);
}