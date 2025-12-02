package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dto.ReservaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTOResponse;

/**
 * Interfaz del servicio de gestión de reservas.
 *
 * Define los métodos relacionados con la gestión de reservas.
 */
public interface GestorReservas {
    /**
     * Registra una nueva reserva en el sistema.
     *
     * @param request DTO con los datos de la reserva (ya validados por @Valid)
     * @return DTO de respuesta con los datos de la reserva registrada
     * @throws IllegalArgumentException si no se encuentran las entidades relacionadas o si hay conflictos en las reservas
     */
    public ReservaDTOResponse registrarReserva(ReservaDTORequest request);
}
