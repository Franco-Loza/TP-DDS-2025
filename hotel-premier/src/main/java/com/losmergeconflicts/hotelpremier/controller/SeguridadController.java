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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar peticiones HTTP de autenticación y registro.
 * 
 * Este controlador maneja la lógica de negocio de autenticación.
 * Para las vistas/interfaces, ver UIController.
 * 
 * @Controller: Indica que es un controlador MVC (retorna vistas)
 * @Slf4j: Genera logger automático para la clase
 */
@Controller
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class SeguridadController {

    private final GestorSeguridad gestorSeguridad;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param gestorSeguridad servicio que contiene la lógica de autenticación
     */
    @Autowired
    public SeguridadController(GestorSeguridad gestorSeguridad) {
        this.gestorSeguridad = gestorSeguridad;
    }

    /**
     * Procesa el registro de un nuevo conserje.
     * 
     * @param request DTO con los datos validados del formulario
     * @param bindingResult resultados de validación de Spring
     * @param redirectAttributes para pasar mensajes flash a la redirección
     * @return redirección al login si éxito, o al registro si error
     */
    @Operation(summary = "Registrar nuevo usuario",
                description = "Procesa el formulario de registro, valida los datos y crea un nuevo usuario en el sistema.",
                responses = {
                    @ApiResponse(responseCode = "302", description = "Redirección al login tras registro exitoso"),
                    @ApiResponse(responseCode = "400", description = "Error de validación (usuario duplicado, contraseña inválida)"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                })
    @PostMapping("/registro")
    public String registrarConserje(
            @Valid @ModelAttribute ConserjeDTORequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        // Verificar si hay errores de validación
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación en registro: {}", bindingResult.getAllErrors());
            var fieldError = bindingResult.getFieldError();
            String errorMessage = (fieldError != null && fieldError.getDefaultMessage() != null)
                    ? fieldError.getDefaultMessage() 
                    : "Datos inválidos";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/registro";
        }
        
        try {
            log.debug("Procesando petición de registro para: {}", request.username());
            
            // Delegar al servicio (validaciones y mapeo dentro del servicio)
            ConserjeDTOResponse response = gestorSeguridad.registrarConserje(request);
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("success", 
                "Registro exitoso. Por favor, inicia sesión.");
            
            log.info("Usuario registrado exitosamente: {}", response.username());
            
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            // Errores de validación (username duplicado, etc.)
            log.warn("Error de validación en registro: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
            
        } catch (Exception e) {
            // Errores inesperados
            log.error("Error inesperado al procesar registro", e);
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar el registro. Intente nuevamente.");
            return "redirect:/registro";
        }
    }

}
