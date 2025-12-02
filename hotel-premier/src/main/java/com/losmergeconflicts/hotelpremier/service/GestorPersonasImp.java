package com.losmergeconflicts.hotelpremier.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.losmergeconflicts.hotelpremier.dao.DireccionDAO;
import com.losmergeconflicts.hotelpremier.dao.HuespedDAO;
import com.losmergeconflicts.hotelpremier.dao.LocalidadDAO;
import com.losmergeconflicts.hotelpremier.dao.NacionalidadDAO;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.LocalidadDTO;
import com.losmergeconflicts.hotelpremier.dto.NacionalidadDTO;
import com.losmergeconflicts.hotelpremier.entity.Direccion;
import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.entity.Localidad;
import com.losmergeconflicts.hotelpremier.entity.Nacionalidad;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;
import com.losmergeconflicts.hotelpremier.mapper.HuespedMapper;
import com.losmergeconflicts.hotelpremier.mapper.LocalidadMapper;
import com.losmergeconflicts.hotelpremier.mapper.NacionalidadMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de personas.
 *
 * Esta clase contiene toda la lógica de negocio relacionada con
 * la gestión de personas.
 *
 * @Slf4j: Genera automáticamente un logger para la clase
 */
@Service
@Slf4j
public class GestorPersonasImp implements GestorPersonas {

    private final HuespedDAO huespedDAO;
    private final HuespedMapper huespedMapper;
    private final DireccionDAO direccionDAO;
    private final LocalidadDAO localidadDAO;
    private final NacionalidadDAO nacionalidadDAO;
    private final LocalidadMapper localidadMapper;
    private final NacionalidadMapper nacionalidadMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param huespedDAO DAO para operaciones con Huesped
     * @param huespedMapper mapper para conversión entre DTOs y entidades
     * @param direccionDAO DAO para operaciones con Direccion
     * @param localidadDAO DAO para operaciones con Localidad
     * @param nacionalidadDAO DAO para operaciones con Nacionalidad
     * @param localidadMapper mapper para conversión de Localidad a DTO
     * @param nacionalidadMapper mapper para conversión de Nacionalidad a DTO
     */
    @Autowired
    public GestorPersonasImp(HuespedDAO huespedDAO,
                             HuespedMapper huespedMapper,
                             DireccionDAO direccionDAO,
                             LocalidadDAO localidadDAO,
                             NacionalidadDAO nacionalidadDAO,
                             LocalidadMapper localidadMapper,
                             NacionalidadMapper nacionalidadMapper) {
        this.huespedDAO = huespedDAO;
        this.huespedMapper = huespedMapper;
        this.direccionDAO = direccionDAO;
        this.localidadDAO = localidadDAO;
        this.nacionalidadDAO = nacionalidadDAO;
        this.localidadMapper = localidadMapper;
        this.nacionalidadMapper = nacionalidadMapper;
    }

    /**
     * Registra un nuevo huésped en el sistema (sin permitir duplicados).
     * Delega al método sobrecargado con permitirDuplicados = false.
     *
     * @param request DTO con los datos del huésped (ya validados por @Valid)
     * @return DTO de respuesta con los datos del huésped registrado
     * @throws IllegalArgumentException si el documento ya existe o si no se encuentran las entidades relacionadas
     */
    @Override
    @Transactional
    public HuespedDTOResponse altaHuesped(HuespedDTORequest request) {
        return altaHuesped(request, false);
    }

    /**
     * Registra un nuevo huésped en el sistema.
     *
     * Proceso:
     * 1. Valida que el documento no exista (si permitirDuplicados es false)
     * 2. Busca y valida las entidades relacionadas (Localidad, Nacionalidad)
     * 3. Crea y guarda la Direccion
     * 4. Convierte el DTO a entidad usando el mapper
     * 5. Asigna las entidades relacionadas
     * 6. Guarda el huésped en base de datos
     * 7. Convierte la entidad guardada a DTO de respuesta
     *
     * @param request DTO con los datos del huésped (ya validados por @Valid)
     * @param permitirDuplicados si es true, permite registrar huéspedes con tipo y documento duplicado
     * @return DTO de respuesta con los datos del huésped registrado
     * @throws IllegalArgumentException si el documento ya existe (y no se permite duplicados) o si no se encuentran las entidades relacionadas
     */
    @Override
    @Transactional
    public HuespedDTOResponse altaHuesped(HuespedDTORequest request, boolean permitirDuplicados) {

        // Validar que los datos no sean nulos
        if (request == null) {
            log.error("El request de alta de huésped es nulo");
            throw new IllegalArgumentException("Los datos del huésped no pueden ser nulos");
        }

        log.info("Intentando dar de alta nuevo huésped con documento: {} {}",
                request.tipoDocumento(), request.documento());

        // Validar que la combinación de tipo de documento y documento no exista
        if (huespedDAO.existsByTipoDocumentoAndDocumento(request.tipoDocumento(), request.documento()) && !permitirDuplicados) {
            log.warn("Intento de alta con tipo y número de documento existentes: {} {}",
                    request.tipoDocumento(), request.documento());
            throw new IllegalArgumentException(
                    "¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }

        try {
            // 1. Buscar y validar Nacionalidad
            Nacionalidad nacionalidad = nacionalidadDAO.findById(request.nacionalidadId())
                    .orElseThrow(() -> {
                        log.error("Nacionalidad no encontrada con ID: {}", request.nacionalidadId());
                        return new IllegalArgumentException("La nacionalidad especificada no existe");
                    });

            // 2. Buscar y validar Localidad
            Localidad localidad = localidadDAO.findById(request.localidadId())
                    .orElseThrow(() -> {
                        log.error("Localidad no encontrada con ID: {}", request.localidadId());
                        return new IllegalArgumentException("La localidad especificada no existe");
                    });

            // 3. Crear y guardar Direccion
            Direccion direccion = Direccion.builder()
                    .calle(request.calle())
                    .numero(request.numero())
                    .piso(request.piso())
                    .departamento(request.departamento())
                    .codigoPostal(request.codigoPostal())
                    .localidad(localidad)
                    .build();

            Direccion direccionGuardada = direccionDAO.save(direccion);
            log.debug("Dirección guardada con ID: {}", direccionGuardada.getId());

            // 4. Convertir DTO a entidad usando MapStruct
            Huesped nuevoHuesped = huespedMapper.toEntity(request);

            // 5. Asignar las entidades relacionadas
            nuevoHuesped.setDireccion(direccionGuardada);
            nuevoHuesped.setNacionalidad(nacionalidad);

            // 6. Guardar en base de datos
            Huesped huespedGuardado = huespedDAO.save(nuevoHuesped);

            log.info("Huésped dado de alta exitosamente con ID: {} - Documento: {}",
                    huespedGuardado.getId(), request.documento());

            // 7. Convertir entidad guardada a DTO de respuesta
            return huespedMapper.toResponse(huespedGuardado);

        } catch (IllegalArgumentException e) {
            // Re-lanzar excepciones de validación
            throw e;
        } catch (Exception e) {
            log.error("Error al dar de alta huésped con documento: {}", request.documento(), e);
            throw new RuntimeException("Error al procesar el alta del huésped", e);
        }
    }

    /**
     * Verifica si un tipo y número de documento ya están registrados.
     *
     * @param tipoDocumento tipo de documento (DNI, PASAPORTE, etc.)
     * @param documento número de documento a verificar
     * @return true si el tipo y documento existen, false si no
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existeDocumento(TipoDocumento tipoDocumento, String documento) {
        return huespedDAO.existsByTipoDocumentoAndDocumento(tipoDocumento, documento);
    }

    /**
     * Obtiene todas las localidades disponibles en el sistema.
     *
     * @return lista de DTOs con todas las localidades (incluye provincia y país anidados)
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocalidadDTO> listarLocalidades() {
        log.info("Listando todas las localidades");

        List<Localidad> localidades = localidadDAO.findAll();

        log.debug("Se encontraron {} localidades", localidades.size());

        return localidades.stream()
                .map(localidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las nacionalidades disponibles en el sistema.
     *
     * @return lista de DTOs con todas las nacionalidades
     */
    @Override
    @Transactional(readOnly = true)
    public List<NacionalidadDTO> listarNacionalidades() {
        log.info("Listando todas las nacionalidades");

        List<Nacionalidad> nacionalidades = nacionalidadDAO.findAll();

        log.debug("Se encontraron {} nacionalidades", nacionalidades.size());

        return nacionalidades.stream()
                .map(nacionalidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca huéspedes en el sistema según múltiples criterios.
     *
     * Proceso (mapeado al Diagrama de Secuencia):
     * 1. Llama al HuespedDAO con los criterios (llamada a HuespedDAOImp)
     * 2. Recibe la lista de entidades Huesped
     * 3. Mapea la lista de Huesped a HuespedDTOResponse (el "loop" del diagrama)
     * 4. Retorna la lista de DTOs
     *
     * @param apellido Criterio de búsqueda por apellido
     * @param nombre Criterio de búsqueda por nombre
     * @param tipoDoc Criterio de búsqueda por tipo de documento
     * @param nroDoc Criterio de búsqueda por número de documento
     * @return Lista de DTOs de huéspedes encontrados
     */
    @Override
    @Transactional(readOnly = true)
    public List<HuespedDTOResponse> buscarHuespedes(String apellido, String nombre, TipoDocumento tipoDoc, String nroDoc) {

        log.info("Iniciando búsqueda de huéspedes con criterios: Apellido [{}], Nombre [{}], TipoDoc [{}], NroDoc [{}]",
                apellido, nombre, tipoDoc, nroDoc);

        List<Huesped> huespedesEncontrados = huespedDAO.buscarHuespedesPorCriterios(apellido, nombre, tipoDoc, nroDoc);

        log.debug("Se encontraron {} huéspedes", huespedesEncontrados.size());

        return huespedesEncontrados.stream()
                .map(huespedMapper::toResponse)
                .collect(Collectors.toList());
    }

}
