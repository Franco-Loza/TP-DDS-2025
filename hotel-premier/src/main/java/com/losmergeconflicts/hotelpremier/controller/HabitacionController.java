package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.DetalleReservaDTO;
import com.losmergeconflicts.hotelpremier.dto.GrillaDisponibilidadDTO;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import com.losmergeconflicts.hotelpremier.service.GestorHabitaciones;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Habitaciones", description = "Endpoints del GestorHabitaciones")
public class HabitacionController {

    private final GestorHabitaciones gestorHabitaciones;

    @Operation(summary = "Obtener estados (CU05)", description = "Devuelve la grilla de disponibilidad. Si no se envía 'tipo', devuelve todas.")
    @GetMapping("/estados")
    public ResponseEntity<?> obtenerEstados(
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(value = "tipo", required = false) TipoHabitacion tipo) {

        log.info("REST: Solicitando estados. Desde: {}, Hasta: {}, Tipo: {}", desde, hasta, (tipo != null ? tipo : "TODAS"));

        try {
            if (desde == null) {
                return ResponseEntity.badRequest().body("La fecha 'Desde' es obligatoria.");
            }
            if (hasta == null) {
                return ResponseEntity.badRequest().body("La fecha 'Hasta' es obligatoria.");
            }

            GrillaDisponibilidadDTO grilla = gestorHabitaciones.obtenerEstados(desde, hasta, tipo);
            return ResponseEntity.ok(grilla);

        } catch (IllegalArgumentException e) {
            log.warn("Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno en el servidor.");
        }
    }

    @GetMapping("/reserva-detalle")
    public ResponseEntity<DetalleReservaDTO> obtenerDetalleReserva(
            @RequestParam("nombre") String nombre,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(gestorHabitaciones.obtenerDetalleReserva(nombre, fecha));
    }
}

