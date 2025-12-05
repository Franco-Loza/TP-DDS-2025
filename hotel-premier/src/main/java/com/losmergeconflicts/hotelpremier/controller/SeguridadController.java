package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;
import com.losmergeconflicts.hotelpremier.service.GestorSeguridad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class SeguridadController {

    private final GestorSeguridad gestorSeguridad;

    @Autowired
    public SeguridadController(GestorSeguridad gestorSeguridad) {
        this.gestorSeguridad = gestorSeguridad;
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/registro")
    public ResponseEntity<?> registrarConserje(@Valid @RequestBody ConserjeDTORequest request) {
        try {
            log.debug("API: Registro para: {}", request.username());
            ConserjeDTOResponse response = gestorSeguridad.registrarConserje(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("API: Error validación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", e.getMessage()));

        } catch (Exception e) {
            log.error("API: Error inesperado", e);
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("message", "Error interno del servidor"));
        }
    }

    @Operation(summary = "Iniciar Sesión")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ConserjeDTORequest request) {
        try {
            ConserjeDTOResponse usuario = gestorSeguridad.autenticarConserje(request.username(), request.password());
            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            log.warn("Login fallido: {}", request.username());
            // AQUÍ TAMBIÉN: Devolvemos JSON
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Usuario o contraseña incorrectos"));
        }
    }
}