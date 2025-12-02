package com.losmergeconflicts.hotelpremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.losmergeconflicts.hotelpremier.dao.DireccionDAO;
import com.losmergeconflicts.hotelpremier.dao.HuespedDAO;
import com.losmergeconflicts.hotelpremier.dao.LocalidadDAO;
import com.losmergeconflicts.hotelpremier.dao.NacionalidadDAO;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTORequest;
import com.losmergeconflicts.hotelpremier.dto.HuespedDTOResponse;
import com.losmergeconflicts.hotelpremier.dto.LocalidadDTO;
import com.losmergeconflicts.hotelpremier.dto.NacionalidadDTO;
import com.losmergeconflicts.hotelpremier.dto.PaisDTO;
import com.losmergeconflicts.hotelpremier.dto.ProvinciaDTO;
import com.losmergeconflicts.hotelpremier.entity.Direccion;
import com.losmergeconflicts.hotelpremier.entity.Huesped;
import com.losmergeconflicts.hotelpremier.entity.Localidad;
import com.losmergeconflicts.hotelpremier.entity.Nacionalidad;
import com.losmergeconflicts.hotelpremier.entity.Pais;
import com.losmergeconflicts.hotelpremier.entity.PosicionFrenteAlIVA;
import com.losmergeconflicts.hotelpremier.entity.Provincia;
import com.losmergeconflicts.hotelpremier.entity.TipoDocumento;
import com.losmergeconflicts.hotelpremier.mapper.HuespedMapper;
import com.losmergeconflicts.hotelpremier.mapper.LocalidadMapper;
import com.losmergeconflicts.hotelpremier.mapper.NacionalidadMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Tests unitarios para GestorPersonasImp.
 * 
 * Probamos la lógica de negocio de gestión de personas en aislamiento,
 * usando Mockito para simular las dependencias (DAOs, Mappers).
 */
@ExtendWith(MockitoExtension.class)
public class GestorPersonasImpTest {

    @Mock
    private HuespedDAO huespedDAO;

    @Mock
    private HuespedMapper huespedMapper;

    @Mock
    private DireccionDAO direccionDAO;

    @Mock
    private LocalidadDAO localidadDAO;

    @Mock
    private NacionalidadDAO nacionalidadDAO;

    @Mock
    private LocalidadMapper localidadMapper;

    @Mock
    private NacionalidadMapper nacionalidadMapper;

    @InjectMocks
    private GestorPersonasImp gestorPersonas;

    private HuespedDTORequest validRequest;
    private Huesped huespedEntity;
    private HuespedDTOResponse expectedResponse;
    private Nacionalidad nacionalidadEntity;
    private Localidad localidadEntity;
    private Direccion direccionEntity;
    private Pais paisEntity;
    private Provincia provinciaEntity;

    @BeforeEach
    void setUp() {
        // Datos de prueba reutilizables
        
        // 1. Crear entidades relacionadas
        paisEntity = Pais.builder()
                .id(1L)
                .nombre("Argentina")
                .build();

        provinciaEntity = Provincia.builder()
                .id(1L)
                .nombre("Buenos Aires")
                .pais(paisEntity)
                .build();

        localidadEntity = Localidad.builder()
                .id(1L)
                .nombre("La Plata")
                .provincia(provinciaEntity)
                .build();

        nacionalidadEntity = Nacionalidad.builder()
                .id(1L)
                .nombre("Argentina")
                .build();

        direccionEntity = Direccion.builder()
                .id(1L)
                .calle("Calle Falsa")
                .numero("123")
                .piso("1")
                .departamento("A")
                .codigoPostal("1900")
                .localidad(localidadEntity)
                .build();

        // 2. Request de entrada
        validRequest = new HuespedDTORequest(
                "20-12345678-9",    // cuit
                "1234567890",        // telefono
                "Info adicional",    // info
                "Calle Falsa",       // calle
                "123",               // numero
                "1",                 // piso
                "A",                 // departamento
                "1900",              // codigoPostal
                1L,                  // localidadId
                "Juan",              // nombre
                "Pérez",             // apellido
                TipoDocumento.DNI,   // tipoDocumento
                "12345678",          // documento
                LocalDate.of(1990, 1, 1), // fechaNacimiento
                "juan@email.com",    // email
                "Ingeniero",         // ocupacion
                PosicionFrenteAlIVA.RESPONSABLE_INSCRIPTO, // posicionFrenteAlIVA
                1L                   // nacionalidadId
        );

        // 3. Entidad Huesped
        huespedEntity = new Huesped();
        huespedEntity.setId(1L);
        huespedEntity.setNombre("Juan");
        huespedEntity.setApellido("Pérez");
        huespedEntity.setTipoDocumento(TipoDocumento.DNI);
        huespedEntity.setDocumento("12345678");
        huespedEntity.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        huespedEntity.setEmail("juan@email.com");
        huespedEntity.setTelefono("1234567890");
        huespedEntity.setOcupacion("Ingeniero");
        huespedEntity.setPosicionFrenteAlIVA(PosicionFrenteAlIVA.RESPONSABLE_INSCRIPTO);
        huespedEntity.setCuit("20-12345678-9");
        huespedEntity.setInfo("Info adicional");
        huespedEntity.setDireccion(direccionEntity);
        huespedEntity.setNacionalidad(nacionalidadEntity);

        // 4. Response esperado
        expectedResponse = new HuespedDTOResponse(
                1L,                                     // id
                "20-12345678-9",                       // cuit
                "1234567890",                          // telefono
                "Info adicional",                      // info
                "Calle Falsa",                         // calle
                "123",                                 // numero
                "1",                                   // piso
                "A",                                   // departamento
                "1900",                                // codigoPostal
                1L,                                    // localidadId
                "La Plata",                            // nombreLocalidad
                1L,                                    // provinciaId
                "Buenos Aires",                        // nombreProvincia
                1L,                                    // paisId
                "Argentina",                           // nombrePais
                "Juan",                                // nombre
                "Pérez",                               // apellido
                TipoDocumento.DNI,                     // tipoDocumento
                "12345678",                            // documento
                LocalDate.of(1990, 1, 1),             // fechaNacimiento
                "juan@email.com",                      // email
                "Ingeniero",                           // ocupacion
                PosicionFrenteAlIVA.RESPONSABLE_INSCRIPTO, // posicionFrenteAlIVA
                1L,                                    // nacionalidadId
                "Argentina"                            // nombreNacionalidad
        );
    }

    /**
     * CAMINO FELIZ: Alta exitosa de un nuevo huésped.
     * 
     * Escenario:
     * - El tipo y número de documento NO existen en BD
     * - Todas las entidades relacionadas existen (Nacionalidad, Localidad)
     * - Datos válidos
     * 
     * Resultado esperado:
     * - Se crea y guarda la Dirección
     * - Se guarda el Huésped correctamente
     * - Se retorna DTO de respuesta con datos del huésped
     */
    @Test
    void testAltaHuesped_CaminoFeliz() {
        // --- ARRANGE ---
        
        // 1. Simulamos que el documento NO existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(false);
        
        // 2. Simulamos que la Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Simulamos que la Localidad existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.of(localidadEntity));
        
        // 4. Simulamos que la Dirección se guarda correctamente
        when(direccionDAO.save(any(Direccion.class))).thenReturn(direccionEntity);
        
        // 5. Simulamos mapper: DTO -> Entity
        when(huespedMapper.toEntity(validRequest)).thenReturn(huespedEntity);
        
        // 6. Simulamos que el DAO guarda el huésped y retorna la entidad con ID
        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedEntity);
        
        // 7. Simulamos mapper: Entity -> DTO Response
        when(huespedMapper.toResponse(huespedEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        HuespedDTOResponse resultado = gestorPersonas.altaHuesped(validRequest);
        
        // --- ASSERT ---
        
        // Verificar que el resultado no sea nulo
        assertNotNull(resultado);
        
        // Verificar datos del resultado
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.nombre());
        assertEquals("Pérez", resultado.apellido());
        assertEquals(TipoDocumento.DNI, resultado.tipoDocumento());
        assertEquals("12345678", resultado.documento());
        assertEquals("juan@email.com", resultado.email());
        
        // Verificar interacciones con los mocks
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento());
        verify(nacionalidadDAO, times(1)).findById(validRequest.nacionalidadId());
        verify(localidadDAO, times(1)).findById(validRequest.localidadId());
        verify(direccionDAO, times(1)).save(any(Direccion.class));
        verify(huespedMapper, times(1)).toEntity(validRequest);
        verify(huespedDAO, times(1)).save(any(Huesped.class));
        verify(huespedMapper, times(1)).toResponse(huespedEntity);
    }

    /**
     * CASO DE ERROR: Request nulo.
     * 
     * Escenario:
     * - Se envía un request null
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuesped_ErrorRequestNulo() {
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(null);
        });
        
        // Verificamos el mensaje de error
        assertEquals("Los datos del huésped no pueden ser nulos", exception.getMessage());
        
        // Verificamos que NINGÚN método fue llamado
        verify(huespedDAO, never()).existsByTipoDocumentoAndDocumento(any(), any());
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Tipo y número de documento ya existen.
     * 
     * Escenario:
     * - El tipo y número de documento YA existen en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuesped_ErrorDocumentoYaExiste() {
        // --- ARRANGE ---
        
        // Simulamos que el documento SÍ existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(true);
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest);
        });
        
        // Verificamos el mensaje de error
        assertTrue(exception.getMessage().contains("tipo y número de documento ya existen"));
        
        // Verificamos que el método save NUNCA fue llamado
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento());
        verify(nacionalidadDAO, never()).findById(any());
        verify(localidadDAO, never()).findById(any());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Nacionalidad no encontrada.
     * 
     * Escenario:
     * - El documento es válido (no existe)
     * - La nacionalidad especificada NO existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuesped_ErrorNacionalidadNoExiste() {
        // --- ARRANGE ---
        
        // 1. Documento NO existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(false);
        
        // 2. Nacionalidad NO existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest);
        });
        
        // Verificamos el mensaje de error
        assertEquals("La nacionalidad especificada no existe", exception.getMessage());
        
        // Verificamos que save NUNCA fue llamado
        verify(nacionalidadDAO, times(1)).findById(validRequest.nacionalidadId());
        verify(localidadDAO, never()).findById(any());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Localidad no encontrada.
     * 
     * Escenario:
     * - El documento es válido (no existe)
     * - La nacionalidad existe
     * - La localidad especificada NO existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuesped_ErrorLocalidadNoExiste() {
        // --- ARRANGE ---
        
        // 1. Documento NO existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(false);
        
        // 2. Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Localidad NO existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest);
        });
        
        // Verificamos el mensaje de error
        assertEquals("La localidad especificada no existe", exception.getMessage());
        
        // Verificamos que save NUNCA fue llamado
        verify(localidadDAO, times(1)).findById(validRequest.localidadId());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Excepción durante el guardado.
     * 
     * Escenario:
     * - Documento válido (no existe)
     * - Todas las entidades relacionadas existen
     * - Error inesperado al guardar en BD (ej. timeout, constraint violation)
     * 
     * Resultado esperado:
     * - Se lanza RuntimeException envolviendo la excepción original
     * - Mensaje apropiado para el usuario
     */
    @Test
    void testAltaHuesped_ErrorAlGuardar() {
        // --- ARRANGE ---
        
        // 1. Documento NO existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(false);
        
        // 2. Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Localidad existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.of(localidadEntity));
        
        // 4. Dirección se guarda correctamente
        when(direccionDAO.save(any(Direccion.class))).thenReturn(direccionEntity);
        
        // 5. Mapper funciona
        when(huespedMapper.toEntity(validRequest)).thenReturn(huespedEntity);
        
        // 6. Simulamos una excepción al guardar el huésped
        when(huespedDAO.save(any(Huesped.class)))
                .thenThrow(new RuntimeException("Error de conexión a la BD"));
        
        // --- ACT & ASSERT ---
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorPersonas.altaHuesped(validRequest);
        });
        
        // Verificamos el mensaje
        assertEquals("Error al procesar el alta del huésped", exception.getMessage());
        
        // Verificamos que se intentó guardar
        verify(huespedDAO, times(1)).save(any(Huesped.class));
        verify(huespedMapper, never()).toResponse(any());
    }

    /**
     * CAMINO FELIZ: Verificar que tipo y número de documento existen.
     * 
     * Escenario:
     * - El tipo y número de documento SÍ existen en BD
     * 
     * Resultado esperado:
     * - Retorna true
     */
    @Test
    void testExisteDocumento_DocumentoExiste() {
        // --- ARRANGE ---
        
        TipoDocumento tipo = TipoDocumento.DNI;
        String documento = "12345678";
        
        when(huespedDAO.existsByTipoDocumentoAndDocumento(tipo, documento)).thenReturn(true);
        
        // --- ACT ---
        
        boolean resultado = gestorPersonas.existeDocumento(tipo, documento);
        
        // --- ASSERT ---
        
        assertTrue(resultado);
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(tipo, documento);
    }

    /**
     * CAMINO FELIZ: Verificar que tipo y número de documento NO existen.
     * 
     * Escenario:
     * - El tipo y número de documento NO existen en BD
     * 
     * Resultado esperado:
     * - Retorna false
     */
    @Test
    void testExisteDocumento_DocumentoNoExiste() {
        // --- ARRANGE ---
        
        TipoDocumento tipo = TipoDocumento.DNI;
        String documento = "99999999";
        
        when(huespedDAO.existsByTipoDocumentoAndDocumento(tipo, documento)).thenReturn(false);
        
        // --- ACT ---
        
        boolean resultado = gestorPersonas.existeDocumento(tipo, documento);
        
        // --- ASSERT ---
        
        assertFalse(resultado);
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(tipo, documento);
    }

    /**
     * CAMINO FELIZ: Listar todas las localidades.
     * 
     * Escenario:
     * - Existen localidades en la BD
     * 
     * Resultado esperado:
     * - Retorna lista de DTOs con todas las localidades
     * - Cada DTO contiene provincia y país anidados
     */
    @Test
    void testListarLocalidades_CaminoFeliz() {
        // --- ARRANGE ---
        
        // Crear lista de localidades
        List<Localidad> localidades = Arrays.asList(
                localidadEntity,
                Localidad.builder().id(2L).nombre("Mar del Plata").provincia(provinciaEntity).build()
        );
        
        // Crear DTOs esperados
        LocalidadDTO dto1 = new LocalidadDTO(
                1L, 
                "La Plata",
                new ProvinciaDTO(1L, "Buenos Aires", new PaisDTO(1L, "Argentina"))
        );
        LocalidadDTO dto2 = new LocalidadDTO(
                2L, 
                "Mar del Plata",
                new ProvinciaDTO(1L, "Buenos Aires", new PaisDTO(1L, "Argentina"))
        );
        
        when(localidadDAO.findAll()).thenReturn(localidades);
        when(localidadMapper.toDTO(localidades.get(0))).thenReturn(dto1);
        when(localidadMapper.toDTO(localidades.get(1))).thenReturn(dto2);
        
        // --- ACT ---
        
        List<LocalidadDTO> resultado = gestorPersonas.listarLocalidades();
        
        // --- ASSERT ---
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("La Plata", resultado.get(0).nombre());
        assertEquals("Mar del Plata", resultado.get(1).nombre());
        
        verify(localidadDAO, times(1)).findAll();
        verify(localidadMapper, times(2)).toDTO(any(Localidad.class));
    }

    /**
     * CAMINO FELIZ: Listar todas las nacionalidades.
     * 
     * Escenario:
     * - Existen nacionalidades en la BD
     * 
     * Resultado esperado:
     * - Retorna lista de DTOs con todas las nacionalidades
     */
    @Test
    void testListarNacionalidades_CaminoFeliz() {
        // --- ARRANGE ---
        
        // Crear lista de nacionalidades
        List<Nacionalidad> nacionalidades = Arrays.asList(
                nacionalidadEntity,
                Nacionalidad.builder().id(2L).nombre("Brasileña").build()
        );
        
        // Crear DTOs esperados
        NacionalidadDTO dto1 = new NacionalidadDTO(1L, "Argentina");
        NacionalidadDTO dto2 = new NacionalidadDTO(2L, "Brasileña");
        
        when(nacionalidadDAO.findAll()).thenReturn(nacionalidades);
        when(nacionalidadMapper.toDTO(nacionalidades.get(0))).thenReturn(dto1);
        when(nacionalidadMapper.toDTO(nacionalidades.get(1))).thenReturn(dto2);
        
        // --- ACT ---
        
        List<NacionalidadDTO> resultado = gestorPersonas.listarNacionalidades();
        
        // --- ASSERT ---
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Argentina", resultado.get(0).nombre());
        assertEquals("Brasileña", resultado.get(1).nombre());
        
        verify(nacionalidadDAO, times(1)).findAll();
        verify(nacionalidadMapper, times(2)).toDTO(any(Nacionalidad.class));
    }

    /**
     * VERIFICACIÓN: La dirección se crea y guarda correctamente.
     * 
     * Escenario:
     * - Alta exitosa de huésped
     * 
     * Resultado esperado:
     * - Se crea un objeto Dirección con los datos del request
     * - Se guarda la dirección antes del huésped
     * - La dirección guardada se asigna al huésped
     */
    @Test
    void testAltaHuesped_DireccionSeCreaCorrectamente() {
        // --- ARRANGE ---
        
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento()
        )).thenReturn(false);
        
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.of(localidadEntity));
        
        // Capturamos el objeto Dirección que se pasa a save()
        when(direccionDAO.save(argThat(direccion ->
                direccion.getCalle().equals(validRequest.calle()) &&
                direccion.getNumero().equals(validRequest.numero()) &&
                direccion.getPiso().equals(validRequest.piso()) &&
                direccion.getDepartamento().equals(validRequest.departamento()) &&
                direccion.getCodigoPostal().equals(validRequest.codigoPostal()) &&
                direccion.getLocalidad().equals(localidadEntity)
        ))).thenReturn(direccionEntity);
        
        when(huespedMapper.toEntity(validRequest)).thenReturn(huespedEntity);
        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedEntity);
        when(huespedMapper.toResponse(huespedEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        gestorPersonas.altaHuesped(validRequest);
        
        // --- ASSERT ---
        
        // Verificar que se guardó la dirección con los datos correctos
        verify(direccionDAO, times(1)).save(argThat(direccion ->
                direccion.getCalle().equals("Calle Falsa") &&
                direccion.getNumero().equals("123") &&
                direccion.getCodigoPostal().equals("1900")
        ));
        
        // Verificar que se guardó el huésped después
        verify(huespedDAO, times(1)).save(any(Huesped.class));
    }

    // ============================================================================
    // TESTS PARA altaHuesped(HuespedDTORequest request, boolean permitirDuplicados)
    // ============================================================================

    /**
     * CAMINO FELIZ: Alta exitosa con permitirDuplicados = true y documento existente.
     * 
     * Escenario:
     * - El tipo y número de documento YA existen en BD
     * - Se llama al método con permitirDuplicados = true
     * - Todas las entidades relacionadas existen (Nacionalidad, Localidad)
     * - Datos válidos
     * 
     * Resultado esperado:
     * - NO se lanza excepción por documento duplicado
     * - Se crea y guarda la Dirección
     * - Se guarda el Huésped correctamente
     * - Se retorna DTO de respuesta con datos del huésped
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_DocumentoExistente_CaminoFeliz() {
        // --- ARRANGE ---
        
        // 1. Simulamos que el documento SÍ existe (esto normalmente causaría error)
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(true);
        
        // 2. Simulamos que la Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Simulamos que la Localidad existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.of(localidadEntity));
        
        // 4. Simulamos que la Dirección se guarda correctamente
        when(direccionDAO.save(any(Direccion.class))).thenReturn(direccionEntity);
        
        // 5. Simulamos mapper: DTO -> Entity
        when(huespedMapper.toEntity(validRequest)).thenReturn(huespedEntity);
        
        // 6. Simulamos que el DAO guarda el huésped y retorna la entidad con ID
        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedEntity);
        
        // 7. Simulamos mapper: Entity -> DTO Response
        when(huespedMapper.toResponse(huespedEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        // CLAVE: Llamamos con permitirDuplicados = true
        HuespedDTOResponse resultado = gestorPersonas.altaHuesped(validRequest, true);
        
        // --- ASSERT ---
        
        // Verificar que el resultado no sea nulo
        assertNotNull(resultado);
        
        // Verificar datos del resultado
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.nombre());
        assertEquals("Pérez", resultado.apellido());
        assertEquals(TipoDocumento.DNI, resultado.tipoDocumento());
        assertEquals("12345678", resultado.documento());
        assertEquals("juan@email.com", resultado.email());
        
        // Verificar que se verificó la existencia del documento
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento());
        
        // Verificar que se continuó con el proceso a pesar de que el documento existe
        verify(nacionalidadDAO, times(1)).findById(validRequest.nacionalidadId());
        verify(localidadDAO, times(1)).findById(validRequest.localidadId());
        verify(direccionDAO, times(1)).save(any(Direccion.class));
        verify(huespedMapper, times(1)).toEntity(validRequest);
        verify(huespedDAO, times(1)).save(any(Huesped.class));
        verify(huespedMapper, times(1)).toResponse(huespedEntity);
    }

    /**
     * CAMINO FELIZ: Alta exitosa con permitirDuplicados = true y documento nuevo.
     * 
     * Escenario:
     * - El tipo y número de documento NO existen en BD
     * - Se llama al método con permitirDuplicados = true
     * - Todas las entidades relacionadas existen (Nacionalidad, Localidad)
     * - Datos válidos
     * 
     * Resultado esperado:
     * - Se crea y guarda la Dirección
     * - Se guarda el Huésped correctamente
     * - Se retorna DTO de respuesta con datos del huésped
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_DocumentoNuevo_CaminoFeliz() {
        // --- ARRANGE ---
        
        // 1. Simulamos que el documento NO existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(false);
        
        // 2. Simulamos que la Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Simulamos que la Localidad existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.of(localidadEntity));
        
        // 4. Simulamos que la Dirección se guarda correctamente
        when(direccionDAO.save(any(Direccion.class))).thenReturn(direccionEntity);
        
        // 5. Simulamos mapper: DTO -> Entity
        when(huespedMapper.toEntity(validRequest)).thenReturn(huespedEntity);
        
        // 6. Simulamos que el DAO guarda el huésped y retorna la entidad con ID
        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedEntity);
        
        // 7. Simulamos mapper: Entity -> DTO Response
        when(huespedMapper.toResponse(huespedEntity)).thenReturn(expectedResponse);
        
        // --- ACT ---
        
        // Llamamos con permitirDuplicados = true (pero el documento no existe)
        HuespedDTOResponse resultado = gestorPersonas.altaHuesped(validRequest, true);
        
        // --- ASSERT ---
        
        // Verificar que el resultado no sea nulo
        assertNotNull(resultado);
        
        // Verificar datos del resultado
        assertEquals(1L, resultado.id());
        assertEquals("Juan", resultado.nombre());
        assertEquals("Pérez", resultado.apellido());
        assertEquals(TipoDocumento.DNI, resultado.tipoDocumento());
        assertEquals("12345678", resultado.documento());
        
        // Verificar interacciones con los mocks
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento());
        verify(nacionalidadDAO, times(1)).findById(validRequest.nacionalidadId());
        verify(localidadDAO, times(1)).findById(validRequest.localidadId());
        verify(direccionDAO, times(1)).save(any(Direccion.class));
        verify(huespedMapper, times(1)).toEntity(validRequest);
        verify(huespedDAO, times(1)).save(any(Huesped.class));
        verify(huespedMapper, times(1)).toResponse(huespedEntity);
    }

    /**
     * CASO DE ERROR: Alta con permitirDuplicados = false y documento existente.
     * 
     * Escenario:
     * - El tipo y número de documento YA existen en BD
     * - Se llama al método con permitirDuplicados = false
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_False_DocumentoExistente_Error() {
        // --- ARRANGE ---
        
        // Simulamos que el documento SÍ existe
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(true);
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest, false);
        });
        
        // Verificamos el mensaje de error
        assertTrue(exception.getMessage().contains("tipo y número de documento ya existen"));
        
        // Verificamos que el método save NUNCA fue llamado
        verify(huespedDAO, times(1)).existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), validRequest.documento());
        verify(nacionalidadDAO, never()).findById(any());
        verify(localidadDAO, never()).findById(any());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Alta con permitirDuplicados = true pero nacionalidad no existe.
     * 
     * Escenario:
     * - El documento puede estar duplicado (permitirDuplicados = true)
     * - La nacionalidad especificada NO existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado sobre la nacionalidad
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_NacionalidadNoExiste_Error() {
        // --- ARRANGE ---
        
        // 1. Documento puede existir o no, no importa
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(true);
        
        // 2. Nacionalidad NO existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest, true);
        });
        
        // Verificamos el mensaje de error
        assertEquals("La nacionalidad especificada no existe", exception.getMessage());
        
        // Verificamos que save NUNCA fue llamado
        verify(nacionalidadDAO, times(1)).findById(validRequest.nacionalidadId());
        verify(localidadDAO, never()).findById(any());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Alta con permitirDuplicados = true pero localidad no existe.
     * 
     * Escenario:
     * - El documento puede estar duplicado (permitirDuplicados = true)
     * - La nacionalidad existe
     * - La localidad especificada NO existe en la BD
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado sobre la localidad
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_LocalidadNoExiste_Error() {
        // --- ARRANGE ---
        
        // 1. Documento puede existir o no, no importa
        when(huespedDAO.existsByTipoDocumentoAndDocumento(
                validRequest.tipoDocumento(), 
                validRequest.documento()
        )).thenReturn(true);
        
        // 2. Nacionalidad existe
        when(nacionalidadDAO.findById(validRequest.nacionalidadId()))
                .thenReturn(Optional.of(nacionalidadEntity));
        
        // 3. Localidad NO existe
        when(localidadDAO.findById(validRequest.localidadId()))
                .thenReturn(Optional.empty());
        
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(validRequest, true);
        });
        
        // Verificamos el mensaje de error
        assertEquals("La localidad especificada no existe", exception.getMessage());
        
        // Verificamos que save NUNCA fue llamado
        verify(localidadDAO, times(1)).findById(validRequest.localidadId());
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
    }

    /**
     * CASO DE ERROR: Alta con permitirDuplicados = true pero request nulo.
     * 
     * Escenario:
     * - Se envía un request null con permitirDuplicados = true
     * 
     * Resultado esperado:
     * - Se lanza IllegalArgumentException
     * - NO se guarda nada en la BD
     * - Mensaje de error apropiado
     */
    @Test
    void testAltaHuespedConPermitirDuplicados_RequestNulo_Error() {
        // --- ACT & ASSERT ---
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPersonas.altaHuesped(null, true);
        });
        
        // Verificamos el mensaje de error
        assertEquals("Los datos del huésped no pueden ser nulos", exception.getMessage());
        
        // Verificamos que NINGÚN método fue llamado
        verify(huespedDAO, never()).existsByTipoDocumentoAndDocumento(any(), any());
        verify(huespedDAO, never()).save(any(Huesped.class));
    }
}
