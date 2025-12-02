package com.losmergeconflicts.hotelpremier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.losmergeconflicts.hotelpremier.dto.HuespedDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.LocalidadDTO;
import com.losmergeconflicts.hotelpremier.dto.NacionalidadDTO;
import com.losmergeconflicts.hotelpremier.service.GestorPersonas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar peticiones HTTP de gestión de personas.
 * 
 * @RestController: Indica que es un controlador REST (retorna JSON)
 * @Slf4j: Genera logger automático para la clase
 */
@RestController
@RequestMapping("/api/personas")
@Slf4j
@Tag(name = "Gestión de Personas", description = "Endpoints para gestión de personas")
public class PersonaController {

    private final GestorPersonas gestorPersonas;

    @Autowired
    public PersonaController(GestorPersonas gestorPersonas) {
        this.gestorPersonas = gestorPersonas;
    }

    /**
     * Registra un nuevo huésped en el sistema.
     * 
     * @param request DTO con los datos del huésped
     * @return ResponseEntity con el huésped registrado
     */
    @Operation(summary = "Alta de un nuevo huésped",
                description = "Permite dar de alta un nuevo huésped en el sistema.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Huésped dado de alta correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/huesped/alta")
    public ResponseEntity<HuespedDTOResponse> altaHuesped(@Valid @RequestBody HuespedDTORequest request) {
        HuespedDTOResponse nuevoHuesped = gestorPersonas.altaHuesped(request);
        return new ResponseEntity<>(nuevoHuesped, HttpStatus.CREATED);
    }

    /**
     * Registra un nuevo huésped en el sistema permitiendo duplicados.
     * Este endpoint se utiliza cuando el usuario confirma que desea registrar
     * un huésped con un documento ya existente (flujo 2.B del CU09).
     * 
     * @param request DTO con los datos del huésped
     * @return ResponseEntity con el huésped registrado
     */
    @Operation(summary = "Alta de un nuevo huésped permitiendo duplicados",
                description = "Permite dar de alta un nuevo huésped en el sistema, incluso si el tipo y número de documento ya existen.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Huésped dado de alta correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/huesped/alta-duplicado")
    public ResponseEntity<HuespedDTOResponse> altaHuespedPermitirDuplicado(@Valid @RequestBody HuespedDTORequest request) {
        log.info("POST /api/personas/huesped/alta-duplicado - Permitiendo registro con documento duplicado: {} {}", 
                request.tipoDocumento(), request.documento());
        HuespedDTOResponse nuevoHuesped = gestorPersonas.altaHuesped(request, true);
        return new ResponseEntity<>(nuevoHuesped, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las localidades disponibles en el sistema.
     * 
     * @return ResponseEntity con la lista de localidades (incluye provincia y país)
     */
    @Operation(summary = "Listar todas las localidades",
                description = "Obtiene la lista completa de localidades disponibles con sus provincias y países.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de localidades obtenida correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @GetMapping("/localidades")
    public ResponseEntity<List<LocalidadDTO>> listarLocalidades() {
        log.info("GET /api/personas/localidades - Solicitando lista de localidades");
        List<LocalidadDTO> localidades = gestorPersonas.listarLocalidades();
        return new ResponseEntity<>(localidades, HttpStatus.OK);
    }

    /**
     * Obtiene todas las nacionalidades disponibles en el sistema.
     * 
     * @return ResponseEntity con la lista de nacionalidades
     */
    @Operation(summary = "Listar todas las nacionalidades",
                description = "Obtiene la lista completa de nacionalidades disponibles en el sistema.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de nacionalidades obtenida correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @GetMapping("/nacionalidades")
    public ResponseEntity<List<NacionalidadDTO>> listarNacionalidades() {
        log.info("GET /api/personas/nacionalidades - Solicitando lista de nacionalidades");
        List<NacionalidadDTO> nacionalidades = gestorPersonas.listarNacionalidades();
        return new ResponseEntity<>(nacionalidades, HttpStatus.OK);
    }

}
