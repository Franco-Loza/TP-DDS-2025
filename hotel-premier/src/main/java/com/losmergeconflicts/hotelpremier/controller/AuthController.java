package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;
import com.losmergeconflicts.hotelpremier.dto.ConserjeDTOResponse;
import com.losmergeconflicts.hotelpremier.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar peticiones HTTP de autenticación y registro.
 * 
 * @Controller: Indica que es un controlador MVC (retorna vistas)
 * @Slf4j: Genera logger automático para la clase
 */
@Controller
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para registro, login y autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param authService servicio que contiene la lógica de autenticación
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
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
            ConserjeDTOResponse response = authService.registrarConserje(request);
            
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

    /*
     * ===========================================
     * Endpoints para mostrar formularios y vistas
     * ===========================================
     */

    /**
     * Muestra el formulario de registro.
     * 
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de registro
     */
    @Operation(summary = "Mostrar formulario de registro",
                description = "Renderiza la página HTML con el formulario de registro de nuevos usuarios.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Formulario de registro mostrado exitosamente")
                })
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        log.debug("Mostrando formulario de registro");
        // Agregar un DTO vacío al modelo para el binding del formulario
        model.addAttribute("conserjeDTORequest", new ConserjeDTORequest("", ""));
        return "registro";
    }

    /**
     * Endpoint para mostrar el menú principal después del login.
     * Este es el destino configurado en SecurityConfig después de un login exitoso.
     * 
     * @return nombre de la vista del menú principal
     */
    @Operation(summary = "Mostrar menú principal",
                description = "Renderiza el dashboard principal del sistema. Requiere autenticación.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Menú principal mostrado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado - Redirige al login")
                })
    @GetMapping("/menu-principal")
    public String menuPrincipal() {
        log.debug("Usuario accedió al menú principal");
        return "menu-principal";
    }
    
    /**
     * Muestra el formulario de login personalizado.
     * 
     * @param error indica si hubo un error en el login anterior
     * @param logout indica si el usuario acaba de hacer logout
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de login
     */
    @Operation(summary = "Mostrar formulario de login",
                description = "Renderiza la página HTML con el formulario de inicio de sesión.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Formulario de login mostrado exitosamente")
                })
    @GetMapping("/login")
    public String mostrarFormularioLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            log.debug("Mostrando login con mensaje de error");
        }
        
        if (logout != null) {
            model.addAttribute("success", "Has cerrado sesión correctamente");
            log.debug("Mostrando login con mensaje de logout");
        }
        
        return "login";
    }
}
