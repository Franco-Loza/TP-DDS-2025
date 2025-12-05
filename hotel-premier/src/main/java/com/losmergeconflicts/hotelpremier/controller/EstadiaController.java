package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.EstadiaDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.EstadiaDTORequest;
import com.losmergeconflicts.hotelpremier.service.GestorEstadias;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadias")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Gestión de Estadías", description = "Endpoints para check-in y gestión de estadías")
public class EstadiaController {

    private final GestorEstadias gestorEstadias;

    @Operation(summary = "Ocupar Habitación (Check-in)", description = "Registra una nueva estadía asignando responsable y acompañantes.")
    @PostMapping("/ocupar")
    public ResponseEntity<EstadiaDTOResponse> ocuparHabitacion(@Valid @RequestBody EstadiaDTORequest request) {
        EstadiaDTOResponse response = gestorEstadias.registrarOcupacion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
