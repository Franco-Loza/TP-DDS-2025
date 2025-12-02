package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.DetalleReservaDTO;
import com.losmergeconflicts.hotelpremier.dto.GrillaDisponibilidadDTO;
import com.losmergeconflicts.hotelpremier.dto.HabitacionDTOResponse;
import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import com.losmergeconflicts.hotelpremier.service.GestorHabitaciones;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

        GrillaDisponibilidadDTO grilla = gestorHabitaciones.obtenerEstados(desde, hasta, tipo);

        return ResponseEntity.ok(grilla);
    }

    @GetMapping("/reserva-detalle")
    public ResponseEntity<DetalleReservaDTO> obtenerDetalleReserva(
            @RequestParam("nombre") String nombre,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return ResponseEntity.ok(gestorHabitaciones.obtenerDetalleReserva(nombre, fecha));
    }

    /**
     * Listar habitaciones por IDs
     *
     * @param idsHabitaciones
     * @return ResponseEntity con la lista de habitaciones encontradas
     */
    @Operation(summary = "Listar habitaciones por IDs",
            description = "Permite listar habitaciones filtradas por sus IDs.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Habitaciones listadas correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @GetMapping("/listar-por-ids")
    public ResponseEntity<List<HabitacionDTOResponse>> listarHabitacionesPorID(
            @RequestParam("ids") List<Long> idsHabitaciones) {
        List<HabitacionDTOResponse> habitaciones = gestorHabitaciones.listarHabitacionesPorID(idsHabitaciones);
        return ResponseEntity.ok(habitaciones);
    }
}
