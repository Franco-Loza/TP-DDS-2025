-- ========================================
-- SISTEMA DE GESTIÓN HOTELERA - HOTEL PREMIER
-- Script de Inicialización de Base de Datos
-- ========================================

-- ========================================
-- TABLA: conserjes
-- Descripción: Almacena los usuarios del sistema (conserjes) con sus credenciales
-- ========================================
CREATE TABLE IF NOT EXISTS conserjes (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Almacena password hasheado con BCrypt
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_CONSERJE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para mejorar búsquedas por username
CREATE INDEX idx_conserjes_username ON conserjes(username);

-- Comentarios sobre la tabla:
-- - id: Identificador único autoincrementable
-- - username: Nombre de usuario único para login
-- - password: Contraseña hasheada usando BCrypt (nunca se almacena en texto plano)
-- - role: Rol del usuario para control de acceso (ej: ROLE_CONSERJE, ROLE_ADMIN)
-- - created_at: Fecha de creación del registro
-- - updated_at: Fecha de última actualización

-- ========================================
-- TABLA: paises
-- Descripción: Catálogo de países
-- ========================================
CREATE TABLE IF NOT EXISTS paises (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Índice para mejorar búsquedas por nombre
CREATE INDEX idx_paises_nombre ON paises(nombre);

-- ========================================
-- TABLA: provincias
-- Descripción: Catálogo de provincias/estados
-- ========================================
CREATE TABLE IF NOT EXISTS provincias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais_id BIGINT NOT NULL,
    CONSTRAINT fk_provincias_pais FOREIGN KEY (pais_id) REFERENCES paises(id),
    CONSTRAINT uk_provincias_nombre_pais UNIQUE (nombre, pais_id)
);

-- Índices
CREATE INDEX idx_provincias_nombre ON provincias(nombre);
CREATE INDEX idx_provincias_pais_id ON provincias(pais_id);

-- ========================================
-- TABLA: localidades
-- Descripción: Catálogo de localidades/ciudades
-- ========================================
CREATE TABLE IF NOT EXISTS localidades (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    provincia_id BIGINT NOT NULL,
    CONSTRAINT fk_localidades_provincia FOREIGN KEY (provincia_id) REFERENCES provincias(id),
    CONSTRAINT uk_localidades_nombre_provincia UNIQUE (nombre, provincia_id)
);

-- Índices
CREATE INDEX idx_localidades_nombre ON localidades(nombre);
CREATE INDEX idx_localidades_provincia_id ON localidades(provincia_id);

-- ========================================
-- TABLA: nacionalidades
-- Descripción: Catálogo de nacionalidades
-- ========================================
CREATE TABLE IF NOT EXISTS nacionalidades (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Índice para mejorar búsquedas por nombre
CREATE INDEX idx_nacionalidades_nombre ON nacionalidades(nombre);

-- ========================================
-- TABLA: direcciones
-- Descripción: Direcciones físicas de personas
-- ========================================
CREATE TABLE IF NOT EXISTS direcciones (
    id BIGSERIAL PRIMARY KEY,
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL,
    piso VARCHAR(5),
    departamento VARCHAR(5),
    codigo_postal VARCHAR(4),
    localidad_id BIGINT NOT NULL,
    CONSTRAINT fk_direcciones_localidad FOREIGN KEY (localidad_id) REFERENCES localidades(id)
);

-- Índice para mejorar búsquedas por localidad
CREATE INDEX idx_direcciones_localidad_id ON direcciones(localidad_id);

-- ========================================
-- TABLA: personas (Tabla padre - JOINED inheritance)
-- Descripción: Tabla padre abstracta para huéspedes y responsables de pago
-- ========================================
CREATE TABLE IF NOT EXISTS personas (
    id BIGSERIAL PRIMARY KEY,
    cuit VARCHAR(11),
    telefono VARCHAR(20) NOT NULL,
    info VARCHAR(100),
    direccion_id BIGINT NOT NULL,
    CONSTRAINT fk_personas_direccion FOREIGN KEY (direccion_id) REFERENCES direcciones(id)
);

-- Índices
CREATE INDEX idx_personas_cuit ON personas(cuit);
CREATE INDEX idx_personas_direccion_id ON personas(direccion_id);

-- ========================================
-- TABLA: huespedes (Tabla hija - JOINED inheritance)
-- Descripción: Información específica de los huéspedes del hotel
-- ========================================
CREATE TABLE IF NOT EXISTS huespedes (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20),
    documento VARCHAR(8) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    email VARCHAR(100),
    ocupacion VARCHAR(100) NOT NULL,
    posicion_frente_al_iva VARCHAR(30),
    nacionalidad_id BIGINT NOT NULL,
    CONSTRAINT fk_huespedes_persona FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE,
    CONSTRAINT fk_huespedes_nacionalidad FOREIGN KEY (nacionalidad_id) REFERENCES nacionalidades(id)
);

-- Índices
CREATE INDEX idx_huespedes_documento ON huespedes(documento);
CREATE INDEX idx_huespedes_email ON huespedes(email);
CREATE INDEX idx_huespedes_nacionalidad_id ON huespedes(nacionalidad_id);
CREATE INDEX idx_huespedes_apellido_nombre ON huespedes(apellido, nombre);

-- ========================================
-- TABLA: responsables_de_pago (Tabla hija - JOINED inheritance)
-- Descripción: Información específica de los responsables de pago
-- ========================================
CREATE TABLE IF NOT EXISTS responsables_de_pago (
    id BIGINT PRIMARY KEY,
    razon_social VARCHAR(200) NOT NULL,
    CONSTRAINT fk_responsables_de_pago_persona FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE
);

-- Índice para búsquedas por razón social
CREATE INDEX idx_responsables_de_pago_razon_social ON responsables_de_pago(razon_social);

-- Comentarios sobre la estrategia de herencia JOINED:
-- - La tabla 'personas' contiene los atributos comunes (id, cuit, telefono, info, direccion_id)
-- - La tabla 'huespedes' hereda de 'personas' mediante FK en el id
-- - La tabla 'responsables_de_pago' hereda de 'personas' mediante FK en el id
-- - El id en ambas tablas hijas es PK y FK a la vez
-- - ON DELETE CASCADE asegura que al eliminar una persona se eliminen sus registros hijos
