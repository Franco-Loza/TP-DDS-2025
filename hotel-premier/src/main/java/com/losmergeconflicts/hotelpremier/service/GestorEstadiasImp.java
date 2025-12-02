package com.losmergeconflicts.hotelpremier.service;

import com.losmergeconflicts.hotelpremier.dao.EstadiaDAO;
import com.losmergeconflicts.hotelpremier.dao.HabitacionDAO;
import com.losmergeconflicts.hotelpremier.dao.HuespedDAO;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTORequest;
import com.losmergeconflicts.hotelpremier.entity.Estadia;
import com.losmergeconflicts.hotelpremier.entity.Habitacion;
import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.entity.TipoEstadoHabitacion;
import com.losmergeconflicts.hotelpremier.mapper.EstadiaMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestorEstadiasImp implements GestorEstadias {

    private final EstadiaDAO estadiaDAO;
    private final HabitacionDAO habitacionDAO;
    private final HuespedDAO huespedDAO;
    private final EstadiaMapper estadiaMapper;

    @Override
    @Transactional
    public EstadiaDTOResponse registrarOcupacion(EstadiaDTORequest request) {

        Habitacion habitacion = habitacionDAO.findById(request.idHabitacion())
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada"));

        Huesped responsable = huespedDAO.findById(request.idResponsable())
                .orElseThrow(() -> new EntityNotFoundException("Huésped responsable no encontrado"));

        List<Huesped> acompanantes = new ArrayList<>();
        if (request.idsAcompaniantes() != null && !request.idsAcompaniantes().isEmpty()) {
            acompanantes = huespedDAO.findAllById(request.idsAcompaniantes());
        }

        validarReglasNegocio(habitacion, responsable, acompanantes);

        LocalDateTime fechaIngreso = request.fechaIngreso().atTime(12, 0);

        Estadia nuevaEstadia = Estadia.builder()
                .fechaHoraIngreso(fechaIngreso)
                .fechaEgresoEsperado(request.fechaEgreso())
                .habitacion(habitacion)
                .huespedAsignado(responsable)
                .itemsConsumo(new ArrayList<>())
                .build();

        Estadia estadiaGuardada = estadiaDAO.save(nuevaEstadia);

        if (!acompanantes.isEmpty()) {
            for (Huesped a : acompanantes) {
                log.info("Registrando acompañante: {}", a.getApellido());
                estadiaDAO.registrarAcompanante(estadiaGuardada.getId(), a.getId());
            }
        }

        habitacionDAO.actualizarEstado(habitacion.getId(), TipoEstadoHabitacion.OCUPADA);

        return estadiaMapper.toResponse(estadiaGuardada);
    }

    private void validarReglasNegocio(Habitacion habitacion, Huesped responsable, List<Huesped> acompanantes) {
        int edadResponsable = Period.between(responsable.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edadResponsable < 18) {
            throw new IllegalArgumentException("El Responsable debe ser mayor de edad.");
        }

        int cantidadPersonas = 1 + acompanantes.size();
        int capacidadMaxima = obtenerCapacidadMaxima(habitacion.getTipoHabitacion());

        if (cantidadPersonas > capacidadMaxima) {
            throw new IllegalArgumentException("Capacidad excedida. Máximo: " + capacidadMaxima);
        }
    }

    private int obtenerCapacidadMaxima(com.losmergeconflicts.hotelpremier.entity.TipoHabitacion tipo) {

        if (tipo == null) return 1;

        return switch (tipo.name()) {
            case "INDIVIDUAL_ESTANDAR" -> 1;
            case "DOBLE_ESTANDAR" -> 2;
            case "DOBLE_SUPERIOR" -> 2;
            case "SUPERIOR_FAMILY_PLAN" -> 5;
            case "SUITE_DOBLE" -> 2;
            default -> 2;
        };
    }
}


