package com.losmergeconflicts.hotelpremier.controller;

import com.losmergeconflicts.hotelpremier.dto.ConserjeDTORequest;

import com.losmergeconflicts.hotelpremier.entity.TipoHabitacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para gestionar peticiones HTTP de interfaces de usuario.
 *
 * Este controlador centraliza todos los endpoints que retornan vistas HTML (Thymeleaf).
 * Separa la responsabilidad de renderizar interfaces de usuario de la lógica de negocio.
 *
 * @Controller: Indica que es un controlador MVC (retorna vistas)
 * @Slf4j: Genera logger automático para la clase
 */
@Controller
@Slf4j
@Tag(name = "Interfaces de Usuario", description = "Endpoints para gestión de vistas e interfaces de usuario")
public class UIController {

    /*
     * ===========================================
     * Endpoints de Autenticación y Acceso
     * ===========================================
     */

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

        log.debug("Renderizando formulario de login");
        return "login";
    }

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

    /*
     * ===========================================
     * Endpoints de Gestión de Huéspedes
     * ===========================================
     */

    /**
     * Muestra el formulario para dar de alta un nuevo huésped.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de alta de huésped
     */
    @Operation(summary = "Mostrar formulario de alta de huésped",
            description = "Renderiza la página HTML con el formulario para registrar un nuevo huésped en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Formulario de alta mostrado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado - Redirige al login")
            })
    @GetMapping("/huesped/alta")
    public String mostrarFormularioAltaHuesped(Model model) {
        log.debug("Mostrando formulario de alta de huésped");
        return "registro-huesped";
    }

    /**
     * Muestra la interfaz para buscar huéspedes.
     * Este método corresponde a CU02: Buscar Huésped.
     * La búsqueda real se realiza mediante JavaScript llamando al endpoint REST.
     *
     * @param model Modelo para pasar datos a la vista
     * @return nombre de la vista de búsqueda de huésped
     */
    @Operation(summary = "Mostrar interfaz de búsqueda de huésped",
            description = "Renderiza la página HTML para buscar huéspedes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interfaz de búsqueda mostrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado - Redirige al login")
            })
    @GetMapping("/huesped/buscar")
    public String mostrarBuscarHuesped(Model model) {
        log.debug("Mostrando interfaz de búsqueda de huésped");
        return "huesped-buscar";
    }

    /**
     * Procesa la selección del botón "SIGUIENTE" de la página buscar-huesped.
     * Implementa los casos de uso 5.A y 6.
     *
     * @param huespedId El ID del huésped seleccionado (puede ser nulo si no se seleccionó)
     * @return Redirección a la siguiente pantalla (alta o modificar)
     */
    @PostMapping("/huesped/seleccionar")
    public String seleccionarHuesped(@RequestParam(value = "huespedId", required = false) Long huespedId) {

        if (huespedId == null) {
            log.info("No se seleccionó huésped, redirigiendo a alta de huésped (CU11)");
            return "redirect:/huesped/alta";
        } else {
            log.info("Huésped seleccionado con ID: {}, redirigiendo a modificar (CU10)", huespedId);
            return "redirect:/huesped/modificar/" + huespedId;
        }
    }

    /*
     * ===========================================
     * Endpoints de Gestión de Reservas
     * ===========================================
     */

    /**
     * Muestra el formulario para crear una nueva reserva de habitación.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de reserva de habitación
     */
    @Operation(summary = "Mostrar formulario de reserva de habitación",
            description = "Renderiza la página HTML con el formulario para crear una nueva reserva.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Formulario de reserva mostrado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado - Redirige al login")
            })
    @GetMapping("/reserva/nueva")
    public String mostrarFormularioReserva(Model model) {
        log.debug("Mostrando formulario de nueva reserva");
        return "reserva-nueva";
    }

    /*
     * ===========================================
     * Endpoints de Gestión de Habitaciones
     * ===========================================
     */

    /**
     * Muestra el formulario para ocupar una habitación (check-in).
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de ocupación de habitación
     */
    @Operation(summary = "Mostrar formulario de ocupación de habitación",
            description = "Renderiza la página HTML con el formulario para realizar check-in de un huésped a una habitación.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Formulario de ocupación mostrado exitosamente"),
                    @ApiResponse(responseCode = "401", description = "No autenticado - Redirige al login")
            })
    @GetMapping("/habitacion/ocupar")
    public String mostrarFormularioOcuparHabitacion(Model model) {
        log.debug("Mostrando formulario de ocupación de habitación");
        return "habitacion-ocupar";
    }

    @Operation(summary = "Mostrar estado de habitaciones",
            description = "Muestra la grilla. El botón cambia entre RESERVAR y OCUPAR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vista mostrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            })
    @GetMapping("/habitacion/estado")
    public String mostrarEstadoHabitaciones(
            @RequestParam(name = "contexto", defaultValue = "reservar") String contexto,
            Model model) {

        log.info("UI: Estado Habitaciones. Contexto: {}", contexto);

        model.addAttribute("contexto", contexto); // Pasa 'reservar' u 'ocupar'
        model.addAttribute("tiposHabitacion", TipoHabitacion.values());

        String textoBoton = "reservar".equalsIgnoreCase(contexto) ? "RESERVAR" : "OCUPAR";
        model.addAttribute("textoBoton", textoBoton);

        return "estado-habitaciones";
    }

    /**
     * Muestra la vista de confirmación de reserva.
     *
     * @return nombre de la vista de confirmación de reserva
     */
    @Operation(summary = "Mostrar confirmación de reserva",
            description = "Renderiza la página HTML para confirmar los datos de la reserva antes de completarla.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vista mostrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            })
    @GetMapping("/reserva/confirmar")
    public String confirmarReserva() {
        log.info("UI: Confirmación de Reserva");
        return "confirmar-reserva";
    }

    /**
     * Muestra la vista para ingresar los datos del huésped de la reserva.
     *
     * @return nombre de la vista de datos del huésped
     */
    @Operation(summary = "Mostrar formulario de datos del huésped",
            description = "Renderiza la página HTML para completar los datos del huésped responsable de la reserva.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vista mostrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            })
    @GetMapping("/reserva/datos-huesped")
    public String datosHuespedReserva() {
        log.info("UI: Datos de Huésped para Reserva");
        return "reserva-datos-huesped";
    }

}