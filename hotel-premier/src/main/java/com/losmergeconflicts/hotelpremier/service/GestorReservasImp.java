package com.losmergeconflicts.hotelpremier.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.losmergeconflicts.hotelpremier.dto.DetalleReservaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.ReservaDAO;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.Reserva;
import com.losmergeconflicts.hotelpremier.mapper.ReservaMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de gestión de reservas.
 * 
 * Esta clase contiene toda la lógica de negocio relacionada con
 * la gestión de reservas.
 *
 * @Slf4j: Genera automáticamente un logger para la clase
 */
@Service
@Slf4j
public class GestorReservasImp implements GestorReservas {
    
    private final ReservaDAO reservaDAO;
    private final HabitacionDAO habitacionDAO;
    private final ReservaMapper reservaMapper;

    /**
     * Constructor con inyección de dependencias.
     * @param reservaDAO
     * @param habitacionDAO
     * @param reservaMapper
     */
    @Autowired
    public GestorReservasImp(ReservaDAO reservaDAO, HabitacionDAO habitacionDAO, ReservaMapper reservaMapper) {
        this.reservaDAO = reservaDAO;
        this.habitacionDAO = habitacionDAO;
        this.reservaMapper = reservaMapper;
    }
    
    /**
     * Registra una nueva reserva en el sistema.
     *
     * Proceso:
     * 1. Valida que el request no sea nulo
     * 2. Valida que la fecha de egreso sea posterior a la de ingreso
     * 3. Busca y valida las habitaciones solicitadas
     * 4. Convierte el DTO a entidad usando el mapper
     * 5. Asigna las habitaciones a la reserva
     * 6. Guarda la reserva en base de datos
     * 7. Convierte la entidad guardada a DTO de respuesta
     *
     * @param request DTO con los datos de la reserva (ya validados por @Valid)
     * @return DTO de respuesta con los datos de la reserva registrada
     * @throws IllegalArgumentException si no se encuentran las entidades relacionadas o si hay conflictos en las reservas
     */
    @Override
    @Transactional
    public ReservaDTOResponse registrarReserva(ReservaDTORequest request) {
        
        // Validar que el request no sea nulo
        if (request == null) {
            log.error("El request de registro de reserva es nulo");
            throw new IllegalArgumentException("Los datos de la reserva no pueden ser nulos");
        }

        log.info("Intentando registrar reserva para {} {} desde {} hasta {}",
                request.nombreHuesped(), request.apellidoHuesped(), 
                request.fechaIngreso(), request.fechaEgreso());

        // Validar que la fecha de egreso sea posterior a la de ingreso
        if (!request.fechaEgreso().isAfter(request.fechaIngreso())) {
            log.warn("Intento de reserva con fecha de egreso no posterior a fecha de ingreso");
            throw new IllegalArgumentException("La fecha de egreso debe ser posterior a la fecha de ingreso");
        }

        // Validar que la lista de habitaciones no esté vacía
        if (request.idsHabitaciones() == null || request.idsHabitaciones().isEmpty()) {
            log.error("Intento de reserva sin habitaciones");
            throw new IllegalArgumentException("Debe seleccionar al menos una habitación");
        }

        try {
            // 1. Buscar y validar las habitaciones solicitadas
            List<Habitacion> habitaciones = request.idsHabitaciones().stream()
                    .map(id -> habitacionDAO.findById(id)
                            .orElseThrow(() -> {
                                log.error("Habitación no encontrada con ID: {}", id);
                                return new IllegalArgumentException("La habitación con ID " + id + " no existe");
                            }))
                    .collect(Collectors.toList());

            log.debug("Se encontraron {} habitaciones válidas", habitaciones.size());

            // 2. Convertir DTO a entidad usando MapStruct
            Reserva nuevaReserva = reservaMapper.toEntity(request);

            // 3. Asignar las habitaciones a la reserva
            nuevaReserva.setHabitaciones(habitaciones);

            // 4. Guardar en base de datos
            Reserva reservaGuardada = reservaDAO.save(nuevaReserva);

            log.info("Reserva registrada exitosamente con ID: {} para {} {}",
                    reservaGuardada.getId(), request.nombreHuesped(), request.apellidoHuesped());

            // 6. Convertir entidad guardada a DTO de respuesta
            return reservaMapper.toResponse(reservaGuardada);

        } catch (Exception e) {
            log.error("Error al registrar reserva para {} {}",
                    request.nombreHuesped(), request.apellidoHuesped(), e);
            throw new RuntimeException("Error al procesar el registro de la reserva", e);
        }
    }

}
