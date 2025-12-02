package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dao.EstadiaDAO;
import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.ReservaDAO;
import com.losmergeconflicts.hotelpremier.dto.*;
import com.losmergeconflicts.hotelpremier.entity.*;
import com.losmergeconflicts.hotelpremier.mapper.HabitacionMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestorHabitacionesImp implements GestorHabitaciones {

    private final HabitacionDAO habitacionDAO;
    private final ReservaDAO reservaDAO;
    private final EstadiaDAO estadiaDAO;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional
    public HabitacionDTOResponse modificarHabitacion(Long id, HabitacionDTORequest request) {
        log.info("Intentando modificar habitación con ID: {}", id);

        Habitacion habitacion = habitacionDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la habitación con ID: " + id));

        habitacionMapper.updateEntityFromRequest(request, habitacion);

        Habitacion habitacionGuardada = habitacionDAO.save(habitacion);
        log.info("Habitación actualizada correctamente: {}", habitacionGuardada.getNombre());

        return habitacionMapper.toResponse(habitacionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionDTOResponse buscarHabitacion(Long id) {
        log.debug("Buscando habitación con ID: {}", id);
        return habitacionDAO.findById(id)
                .map(habitacionMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la habitación con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionDTOResponse> listarHabitaciones() {
        log.debug("Listando todas las habitaciones");
        return habitacionDAO.findAll().stream()
                .map(habitacionMapper::toResponse)
                .collect(Collectors.toList());
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

        // Crear mapa de nombre -> ID
        Map<String, Long> idsHabitaciones = habitaciones.stream()
                .collect(java.util.stream.Collectors.toMap(Habitacion::getNombre, Habitacion::getId));

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

        return new GrillaDisponibilidadDTO(nombresHabitaciones, idsHabitaciones, filas);
    }

    private TipoEstadoHabitacion calcularEstado(Habitacion hab, LocalDate fecha, List<Reserva> reservas, List<Estadia> estadias) {
        // Prioridad 1: OCUPADA
        boolean ocupada = estadias.stream().anyMatch(e ->
                e.getHabitacion().getId().equals(hab.getId()) &&
                        !fecha.isBefore(e.getFechaHoraIngreso().toLocalDate()) &&
                        (e.getFechaHoraEgreso() == null || !fecha.isAfter(e.getFechaHoraEgreso().toLocalDate()))
        );
        if (ocupada) return TipoEstadoHabitacion.OCUPADA;

        // Prioridad 2: RESERVADA
        boolean reservada = reservas.stream().anyMatch(r ->
                r.getHabitaciones().stream().anyMatch(h -> h.getId().equals(hab.getId())) &&
                        !fecha.isBefore(r.getFechaIngreso()) &&
                        !fecha.isAfter(r.getFechaEgreso())
        );
        if (reservada) return TipoEstadoHabitacion.RESERVADA;

        // Prioridad 3: LIBRE
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

    /**
     * Lista las habitaciones filtradas por sus IDs.
     * 
     * Busca las habitaciones en la base de datos usando los IDs proporcionados
     * y las convierte a DTOs de respuesta usando el mapper.
     * 
     * @param idsHabitaciones Lista de IDs de habitaciones a buscar
     * @return Lista de DTOs de las habitaciones encontradas (se retorna solo los datos limitados que se requieren para la vista)
     * @throws IllegalArgumentException si la lista de IDs es nula o vacía
     */
    @Override
    @Transactional(readOnly = true)
    public List<HabitacionDTOResponse> listarHabitacionesPorID(List<Long> idsHabitaciones) {
        
        if (idsHabitaciones == null || idsHabitaciones.isEmpty()) {
            log.warn("Intento de listar habitaciones con lista de IDs vacía o nula");
            throw new IllegalArgumentException("La lista de IDs de habitaciones no puede ser nula o vacía");
        }

        log.info("Listando habitaciones por IDs: {}", idsHabitaciones);

        // Buscar habitaciones por IDs
        List<Habitacion> habitaciones = habitacionDAO.findAllById(idsHabitaciones);

        if (habitaciones.isEmpty()) {
            log.warn("No se encontraron habitaciones con los IDs proporcionados: {}", idsHabitaciones);
            return new ArrayList<>();
        }

        log.debug("Se encontraron {} habitaciones", habitaciones.size());

        // Convertir entidades a DTOs usando el mapper
        return habitaciones.stream()
                .map(habitacionMapper::toResponseLimited)
                .collect(Collectors.toList());
    }
}