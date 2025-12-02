package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.ReservaDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ReservaDTOResponse;
import com.losmergeconflicts.hotelpremier.service.GestorReservas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Slf4j
@Tag(name = "Gestión de Reservas", description = "Endpoints para gestión de reservas")
public class ReservaController {

    private final GestorReservas gestorReservas;

    @Autowired
    public ReservaController(GestorReservas gestorReservas) {
        this.gestorReservas = gestorReservas;
    }

    /**
     * Registra una reserva simple.
     */
    @Operation(summary = "Alta de una nueva reserva",
            description = "Permite dar de alta una nueva reserva en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Reserva dada de alta correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PostMapping("/registrar")
    public ResponseEntity<ReservaDTOResponse> registrarReserva(@Valid @RequestBody ReservaDTORequest request) {
        ReservaDTOResponse nuevaReserva = gestorReservas.registrarReserva(request);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    /**
     * Registra MÚLTIPLES reservas en una sola operación.
     */
    @Operation(summary = "Alta de múltiples reservas",
            description = "Permite registrar varias habitaciones con rangos de fechas independientes.",
            responses = {
                @ApiResponse(responseCode = "201", description = "Reserva dada de alta correctamente"),
                @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    @PostMapping("/registrar-multiples")
    public ResponseEntity<List<ReservaDTOResponse>> registrarMultiplesReservas(
            @RequestBody List<@Valid ReservaDTORequest> requests) {

        log.info("Iniciando registro masivo de {} reservas", requests.size());

        List<ReservaDTOResponse> respuestas = new ArrayList<>();

        for (ReservaDTORequest req : requests) {
            respuestas.add(gestorReservas.registrarReserva(req));
        }

        return new ResponseEntity<>(respuestas, HttpStatus.CREATED);
    }
}