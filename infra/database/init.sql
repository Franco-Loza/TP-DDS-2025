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

-- ========================================
-- TABLA: bancos
-- Descripción: Catálogo de bancos emisores
-- ========================================
CREATE TABLE IF NOT EXISTS bancos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Índice para mejorar búsquedas por nombre
CREATE INDEX idx_bancos_nombre ON bancos(nombre);

-- ========================================
-- TABLA: items_consumo
-- Descripción: Catálogo de items consumibles (servicios adicionales del hotel)
-- ========================================
CREATE TABLE IF NOT EXISTS items_consumo (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio REAL NOT NULL
);

-- Índice para mejorar búsquedas por nombre
CREATE INDEX idx_items_consumo_nombre ON items_consumo(nombre);

-- ========================================
-- TABLA: habitaciones
-- Descripción: Habitaciones del hotel
-- ========================================
CREATE TABLE IF NOT EXISTS habitaciones (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(5) NOT NULL,
    precio REAL NOT NULL,
    tipo_habitacion VARCHAR(20) NOT NULL,
    estado_habitacion VARCHAR(20) NOT NULL
);

-- Índice para mejorar búsquedas por nombre y estado
CREATE INDEX idx_habitaciones_nombre ON habitaciones(nombre);
CREATE INDEX idx_habitaciones_estado ON habitaciones(estado_habitacion);

-- Comentarios sobre tipos enumerados:
-- tipo_habitacion: SIMPLE, DOBLE, TRIPLE, SUITE
-- estado_habitacion: DISPONIBLE, OCUPADA, MANTENIMIENTO, LIMPIEZA

-- ========================================
-- TABLA: habitaciones_fuera_servicio
-- Descripción: Registro de períodos en que una habitación está fuera de servicio
-- ========================================
CREATE TABLE IF NOT EXISTS habitaciones_fuera_servicio (
    id BIGSERIAL PRIMARY KEY,
    motivo VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    habitacion_id BIGINT NOT NULL,
    CONSTRAINT fk_habitaciones_fuera_servicio_habitacion FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id)
);

-- Índices
CREATE INDEX idx_habitaciones_fuera_servicio_habitacion_id ON habitaciones_fuera_servicio(habitacion_id);
CREATE INDEX idx_habitaciones_fuera_servicio_fechas ON habitaciones_fuera_servicio(fecha_inicio, fecha_fin);

-- ========================================
-- TABLA: reservas
-- Descripción: Reservas de habitaciones realizadas por huéspedes
-- ========================================
CREATE TABLE IF NOT EXISTS reservas (
    id BIGSERIAL PRIMARY KEY,
    fecha_ingreso DATE NOT NULL,
    fecha_egreso DATE NOT NULL,
    nombre_huesped VARCHAR(100) NOT NULL,
    apellido_huesped VARCHAR(100) NOT NULL,
    telefono_huesped VARCHAR(20) NOT NULL,
    huesped_id BIGINT,
    CONSTRAINT fk_reservas_huesped FOREIGN KEY (huesped_id) REFERENCES huespedes(id)
);

-- Índices
CREATE INDEX idx_reservas_huesped_id ON reservas(huesped_id);
CREATE INDEX idx_reservas_fechas ON reservas(fecha_ingreso, fecha_egreso);
CREATE INDEX idx_reservas_apellido_nombre ON reservas(apellido_huesped, nombre_huesped);

-- ========================================
-- TABLA: reserva_habitaciones
-- Descripción: Tabla de relación many-to-many entre reservas y habitaciones
-- ========================================
CREATE TABLE IF NOT EXISTS reserva_habitaciones (
    reserva_id BIGINT NOT NULL,
    habitacion_id BIGINT NOT NULL,
    PRIMARY KEY (reserva_id, habitacion_id),
    CONSTRAINT fk_reserva_habitaciones_reserva FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_habitaciones_habitacion FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id)
);

-- Índices
CREATE INDEX idx_reserva_habitaciones_reserva_id ON reserva_habitaciones(reserva_id);
CREATE INDEX idx_reserva_habitaciones_habitacion_id ON reserva_habitaciones(habitacion_id);

-- ========================================
-- TABLA: estadias
-- Descripción: Estadías de huéspedes en el hotel
-- ========================================
CREATE TABLE IF NOT EXISTS estadias (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora_ingreso TIMESTAMP NOT NULL,
    fecha_hora_egreso TIMESTAMP,
    fecha_egreso_esperado DATE,
    huesped_id BIGINT NOT NULL,
    habitacion_id BIGINT NOT NULL,
    reserva_id BIGINT,
    CONSTRAINT fk_estadias_huesped FOREIGN KEY (huesped_id) REFERENCES huespedes(id),
    CONSTRAINT fk_estadias_habitacion FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id),
    CONSTRAINT fk_estadias_reserva FOREIGN KEY (reserva_id) REFERENCES reservas(id)
);

-- Índices
CREATE INDEX idx_estadias_huesped_id ON estadias(huesped_id);
CREATE INDEX idx_estadias_habitacion_id ON estadias(habitacion_id);
CREATE INDEX idx_estadias_reserva_id ON estadias(reserva_id);
CREATE INDEX idx_estadias_fecha_ingreso ON estadias(fecha_hora_ingreso);

-- ========================================
-- TABLA: estadia_huespedes_acompaniantes
-- Descripción: Tabla de relación one-to-many entre estadías y huéspedes acompañantes
-- ========================================
CREATE TABLE IF NOT EXISTS estadia_huespedes_acompaniantes (
    estadia_id BIGINT NOT NULL,
    huesped_id BIGINT NOT NULL,
    PRIMARY KEY (estadia_id, huesped_id),
    CONSTRAINT fk_estadia_acompaniantes_estadia FOREIGN KEY (estadia_id) REFERENCES estadias(id) ON DELETE CASCADE,
    CONSTRAINT fk_estadia_acompaniantes_huesped FOREIGN KEY (huesped_id) REFERENCES huespedes(id)
);

-- Índices
CREATE INDEX idx_estadia_acompaniantes_estadia_id ON estadia_huespedes_acompaniantes(estadia_id);
CREATE INDEX idx_estadia_acompaniantes_huesped_id ON estadia_huespedes_acompaniantes(huesped_id);

-- ========================================
-- TABLA: estadias_items_consumo
-- Descripción: Tabla de relación many-to-many entre estadías e items de consumo
-- ========================================
CREATE TABLE IF NOT EXISTS estadias_items_consumo (
    estadia_id BIGINT NOT NULL,
    item_consumo_id BIGINT NOT NULL,
    PRIMARY KEY (estadia_id, item_consumo_id),
    CONSTRAINT fk_estadias_items_estadia FOREIGN KEY (estadia_id) REFERENCES estadias(id) ON DELETE CASCADE,
    CONSTRAINT fk_estadias_items_item FOREIGN KEY (item_consumo_id) REFERENCES items_consumo(id)
);

-- Índices
CREATE INDEX idx_estadias_items_estadia_id ON estadias_items_consumo(estadia_id);
CREATE INDEX idx_estadias_items_item_id ON estadias_items_consumo(item_consumo_id);

-- ========================================
-- TABLA: facturas
-- Descripción: Facturas emitidas por estadías
-- ========================================
CREATE TABLE IF NOT EXISTS facturas (
    id BIGSERIAL PRIMARY KEY,
    estado_factura VARCHAR(10) NOT NULL,
    numero_habitacion VARCHAR(5),
    fecha_emision DATE NOT NULL,
    fecha_hora_salida TIMESTAMP NOT NULL,
    tipo_factura CHAR(1) NOT NULL,
    valor_estadia REAL NOT NULL,
    monto_total REAL NOT NULL,
    id_responsable_de_pago BIGINT NOT NULL,
    id_estadia BIGINT NOT NULL,
    CONSTRAINT fk_facturas_responsable FOREIGN KEY (id_responsable_de_pago) REFERENCES responsables_de_pago(id),
    CONSTRAINT fk_facturas_estadia FOREIGN KEY (id_estadia) REFERENCES estadias(id)
);

-- Índices
CREATE INDEX idx_facturas_responsable_id ON facturas(id_responsable_de_pago);
CREATE INDEX idx_facturas_estadia_id ON facturas(id_estadia);
CREATE INDEX idx_facturas_fecha_emision ON facturas(fecha_emision);
CREATE INDEX idx_facturas_estado ON facturas(estado_factura);

-- Comentarios sobre tipos enumerados:
-- estado_factura: PAGADA, PENDIENTE, CANCELADA
-- tipo_factura: A, B

-- ========================================
-- TABLA: factura_detalles
-- Descripción: Líneas de detalle de las facturas
-- ========================================
CREATE TABLE IF NOT EXISTS factura_detalles (
    id BIGSERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    cantidad INTEGER NOT NULL,
    unidad_medida VARCHAR(10) NOT NULL,
    precio_unitario REAL NOT NULL,
    subtotal REAL NOT NULL,
    factura_id BIGINT NOT NULL,
    CONSTRAINT fk_factura_detalles_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);

-- Índice
CREATE INDEX idx_factura_detalles_factura_id ON factura_detalles(factura_id);

-- ========================================
-- TABLA: medios_de_pago (Tabla padre - JOINED inheritance)
-- Descripción: Tabla padre abstracta para medios de pago (tarjetas y cheques)
-- ========================================
CREATE TABLE IF NOT EXISTS medios_de_pago (
    id BIGSERIAL PRIMARY KEY,
    dtype VARCHAR(31) NOT NULL -- Hibernate usa esto automáticamente para diferenciar (Tarjeta, Cheque, Efectivo)
);

-- Índice
CREATE INDEX idx_medios_de_pago_tipo ON medios_de_pago(dtype);

-- ========================================
-- TABLA: efectivo (Tabla hija - JOINED inheritance)
-- FALTABA ESTA TABLA
-- ========================================
CREATE TABLE IF NOT EXISTS efectivo (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_efectivo_medio_de_pago FOREIGN KEY (id) REFERENCES medios_de_pago(id) ON DELETE CASCADE
);

-- ========================================
-- TABLA: cheques (Tabla hija - JOINED inheritance)
-- Descripción: Información específica de cheques como medio de pago
-- ========================================
CREATE TABLE IF NOT EXISTS cheques (
    id BIGINT PRIMARY KEY,
    numero VARCHAR(50) NOT NULL,
    plaza VARCHAR(100) NOT NULL,
    tipo_cheque VARCHAR(20) NOT NULL,
    banco_id BIGINT NOT NULL,
    CONSTRAINT fk_cheques_medio_de_pago FOREIGN KEY (id) REFERENCES medios_de_pago(id) ON DELETE CASCADE,
    CONSTRAINT fk_cheques_banco FOREIGN KEY (banco_id) REFERENCES bancos(id)
    );

-- Índices
CREATE INDEX idx_cheques_numero ON cheques(numero);
CREATE INDEX idx_cheques_banco_id ON cheques(banco_id);

-- Comentarios sobre tipos enumerados:
-- tipo_cheque: PROPIO, DE_TERCERO

-- ========================================
-- TABLA: tarjetas (Tabla hija - JOINED inheritance)
-- Descripción: Información específica de tarjetas como medio de pago
-- ========================================
CREATE TABLE IF NOT EXISTS tarjetas (
    id BIGINT PRIMARY KEY,
    red_de_pago VARCHAR(20) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    banco_id BIGINT NOT NULL,
    CONSTRAINT fk_tarjetas_medio_de_pago FOREIGN KEY (id) REFERENCES medios_de_pago(id) ON DELETE CASCADE,
    CONSTRAINT fk_tarjetas_banco FOREIGN KEY (banco_id) REFERENCES bancos(id)
    );

-- Índices
CREATE INDEX idx_tarjetas_red_de_pago ON tarjetas(red_de_pago);
CREATE INDEX idx_tarjetas_banco_id ON tarjetas(banco_id);

-- Comentarios sobre tipos enumerados:
-- red_de_pago: VISA, MASTERCARD

-- Comentarios sobre la estrategia de herencia JOINED para medios de pago:
-- - La tabla 'medios_de_pago' contiene los atributos comunes (id, tipo)
-- - La tabla 'cheques' hereda de 'medios_de_pago' mediante FK en el id
-- - La tabla 'tarjetas' hereda de 'medios_de_pago' mediante FK en el id
-- - El id en ambas tablas hijas es PK y FK a la vez
-- - ON DELETE CASCADE asegura que al eliminar un medio de pago se eliminen sus registros hijos

-- ========================================
-- TABLA: pagos
-- Descripción: Pagos realizados de facturas
-- ========================================
CREATE TABLE IF NOT EXISTS pagos (
    id BIGSERIAL PRIMARY KEY,
    fecha_cobro DATE NOT NULL,
    moneda VARCHAR(10) NOT NULL,
    importe REAL NOT NULL,
    cotizacion REAL,
    factura_id BIGINT NOT NULL,
    medio_de_pago_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_pagos_factura FOREIGN KEY (factura_id) REFERENCES facturas(id),
    CONSTRAINT fk_pagos_medio_de_pago FOREIGN KEY (medio_de_pago_id) REFERENCES medios_de_pago(id)
    );

-- Índices
CREATE INDEX idx_pagos_factura_id ON pagos(factura_id);
CREATE INDEX idx_pagos_medio_de_pago_id ON pagos(medio_de_pago_id);
CREATE INDEX idx_pagos_fecha_cobro ON pagos(fecha_cobro);

-- Comentarios sobre tipos enumerados:
-- moneda: PESOS, DOLARES, EUROS

-- ========================================
-- TABLA: notas_de_credito
-- Descripción: Notas de crédito emitidas para cancelar facturas
-- ========================================
CREATE TABLE IF NOT EXISTS notas_de_credito (
    id BIGSERIAL PRIMARY KEY,
    fecha_emision DATE NOT NULL,
    monto_total REAL NOT NULL
);

-- Índice
CREATE INDEX idx_notas_de_credito_fecha_emision ON notas_de_credito(fecha_emision);

-- ========================================
-- TABLA: notas_de_credito_facturas_canceladas
-- Descripción: Tabla de relación one-to-many entre notas de crédito y facturas canceladas
-- ========================================
CREATE TABLE IF NOT EXISTS notas_de_credito_facturas_canceladas (
    nota_de_credito_id BIGINT NOT NULL,
    factura_id BIGINT NOT NULL,
    PRIMARY KEY (nota_de_credito_id, factura_id),
    CONSTRAINT fk_notas_facturas_nota FOREIGN KEY (nota_de_credito_id) REFERENCES notas_de_credito(id) ON DELETE CASCADE,
    CONSTRAINT fk_notas_facturas_factura FOREIGN KEY (factura_id) REFERENCES facturas(id)
);

-- Índices
CREATE INDEX idx_notas_facturas_nota_id ON notas_de_credito_facturas_canceladas(nota_de_credito_id);
CREATE INDEX idx_notas_facturas_factura_id ON notas_de_credito_facturas_canceladas(factura_id);

-- ========================================
-- DATOS INICIALES - ARGENTINA
-- Script de población de datos maestros
-- ========================================

-- ========================================
-- PAÍSES
-- ========================================
INSERT INTO paises (nombre) VALUES ('Argentina') ON CONFLICT DO NOTHING;

-- ========================================
-- PROVINCIAS DE ARGENTINA
-- ========================================
INSERT INTO provincias (nombre, pais_id) VALUES 
    ('Buenos Aires', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Ciudad Autónoma de Buenos Aires', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Catamarca', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Chaco', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Chubut', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Córdoba', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Corrientes', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Entre Ríos', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Formosa', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Jujuy', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('La Pampa', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('La Rioja', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Mendoza', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Misiones', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Neuquén', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Río Negro', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Salta', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('San Juan', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('San Luis', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Santa Cruz', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Santa Fe', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Santiago del Estero', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Tierra del Fuego', (SELECT id FROM paises WHERE nombre = 'Argentina')),
    ('Tucumán', (SELECT id FROM paises WHERE nombre = 'Argentina'))
ON CONFLICT DO NOTHING;

-- ========================================
-- LOCALIDADES PRINCIPALES DE ARGENTINA
-- ========================================

-- Buenos Aires
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('La Plata', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Mar del Plata', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Bahía Blanca', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('San Nicolás', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Tandil', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Pergamino', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Junín', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Olavarría', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Azul', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Necochea', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Pilar', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('San Isidro', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Vicente López', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Lomas de Zamora', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Quilmes', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Avellaneda', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Lanús', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('San Martín', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Morón', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires')),
    ('Tres de Febrero', (SELECT id FROM provincias WHERE nombre = 'Buenos Aires'))
ON CONFLICT DO NOTHING;

-- Ciudad Autónoma de Buenos Aires
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Palermo', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Recoleta', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Belgrano', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Caballito', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Almagro', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Villa Crespo', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('San Telmo', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Puerto Madero', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Núñez', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires')),
    ('Villa Urquiza', (SELECT id FROM provincias WHERE nombre = 'Ciudad Autónoma de Buenos Aires'))
ON CONFLICT DO NOTHING;

-- Córdoba
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Córdoba', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Villa Carlos Paz', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Río Cuarto', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Villa María', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('San Francisco', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Alta Gracia', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Bell Ville', (SELECT id FROM provincias WHERE nombre = 'Córdoba')),
    ('Jesús María', (SELECT id FROM provincias WHERE nombre = 'Córdoba'))
ON CONFLICT DO NOTHING;

-- Santa Fe
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Rosario', (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
    ('Santa Fe', (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
    ('Rafaela', (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
    ('Venado Tuerto', (SELECT id FROM provincias WHERE nombre = 'Santa Fe')),
    ('Reconquista', (SELECT id FROM provincias WHERE nombre = 'Santa Fe'))
ON CONFLICT DO NOTHING;

-- Mendoza
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Mendoza', (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
    ('San Rafael', (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
    ('Godoy Cruz', (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
    ('Maipú', (SELECT id FROM provincias WHERE nombre = 'Mendoza')),
    ('Luján de Cuyo', (SELECT id FROM provincias WHERE nombre = 'Mendoza'))
ON CONFLICT DO NOTHING;

-- Tucumán
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('San Miguel de Tucumán', (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
    ('Yerba Buena', (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
    ('Tafí Viejo', (SELECT id FROM provincias WHERE nombre = 'Tucumán')),
    ('Concepción', (SELECT id FROM provincias WHERE nombre = 'Tucumán'))
ON CONFLICT DO NOTHING;

-- Salta
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Salta', (SELECT id FROM provincias WHERE nombre = 'Salta')),
    ('San Ramón de la Nueva Orán', (SELECT id FROM provincias WHERE nombre = 'Salta')),
    ('Tartagal', (SELECT id FROM provincias WHERE nombre = 'Salta')),
    ('Cafayate', (SELECT id FROM provincias WHERE nombre = 'Salta'))
ON CONFLICT DO NOTHING;

-- Entre Ríos
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Paraná', (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
    ('Concordia', (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
    ('Gualeguaychú', (SELECT id FROM provincias WHERE nombre = 'Entre Ríos')),
    ('Concepción del Uruguay', (SELECT id FROM provincias WHERE nombre = 'Entre Ríos'))
ON CONFLICT DO NOTHING;

-- Misiones
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Posadas', (SELECT id FROM provincias WHERE nombre = 'Misiones')),
    ('Oberá', (SELECT id FROM provincias WHERE nombre = 'Misiones')),
    ('Eldorado', (SELECT id FROM provincias WHERE nombre = 'Misiones')),
    ('Puerto Iguazú', (SELECT id FROM provincias WHERE nombre = 'Misiones'))
ON CONFLICT DO NOTHING;

-- Chaco
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Resistencia', (SELECT id FROM provincias WHERE nombre = 'Chaco')),
    ('Presidencia Roque Sáenz Peña', (SELECT id FROM provincias WHERE nombre = 'Chaco')),
    ('Villa Ángela', (SELECT id FROM provincias WHERE nombre = 'Chaco')),
    ('Barranqueras', (SELECT id FROM provincias WHERE nombre = 'Chaco'))
ON CONFLICT DO NOTHING;

-- Corrientes
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Corrientes', (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
    ('Goya', (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
    ('Paso de los Libres', (SELECT id FROM provincias WHERE nombre = 'Corrientes')),
    ('Curuzú Cuatiá', (SELECT id FROM provincias WHERE nombre = 'Corrientes'))
ON CONFLICT DO NOTHING;

-- Santiago del Estero
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Santiago del Estero', (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
    ('La Banda', (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
    ('Termas de Río Hondo', (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero')),
    ('Frías', (SELECT id FROM provincias WHERE nombre = 'Santiago del Estero'))
ON CONFLICT DO NOTHING;

-- Jujuy
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('San Salvador de Jujuy', (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
    ('Palpalá', (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
    ('San Pedro', (SELECT id FROM provincias WHERE nombre = 'Jujuy')),
    ('Libertador General San Martín', (SELECT id FROM provincias WHERE nombre = 'Jujuy'))
ON CONFLICT DO NOTHING;

-- Catamarca
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('San Fernando del Valle de Catamarca', (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
    ('Andalgalá', (SELECT id FROM provincias WHERE nombre = 'Catamarca')),
    ('Belén', (SELECT id FROM provincias WHERE nombre = 'Catamarca'))
ON CONFLICT DO NOTHING;

-- La Rioja
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('La Rioja', (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
    ('Chilecito', (SELECT id FROM provincias WHERE nombre = 'La Rioja')),
    ('Aimogasta', (SELECT id FROM provincias WHERE nombre = 'La Rioja'))
ON CONFLICT DO NOTHING;

-- San Juan
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('San Juan', (SELECT id FROM provincias WHERE nombre = 'San Juan')),
    ('Rawson', (SELECT id FROM provincias WHERE nombre = 'San Juan')),
    ('Chimbas', (SELECT id FROM provincias WHERE nombre = 'San Juan')),
    ('Pocito', (SELECT id FROM provincias WHERE nombre = 'San Juan'))
ON CONFLICT DO NOTHING;

-- San Luis
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('San Luis', (SELECT id FROM provincias WHERE nombre = 'San Luis')),
    ('Villa Mercedes', (SELECT id FROM provincias WHERE nombre = 'San Luis')),
    ('Merlo', (SELECT id FROM provincias WHERE nombre = 'San Luis'))
ON CONFLICT DO NOTHING;

-- La Pampa
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Santa Rosa', (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
    ('General Pico', (SELECT id FROM provincias WHERE nombre = 'La Pampa')),
    ('Toay', (SELECT id FROM provincias WHERE nombre = 'La Pampa'))
ON CONFLICT DO NOTHING;

-- Neuquén
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Neuquén', (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
    ('San Martín de los Andes', (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
    ('Villa La Angostura', (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
    ('Cutral Có', (SELECT id FROM provincias WHERE nombre = 'Neuquén')),
    ('Zapala', (SELECT id FROM provincias WHERE nombre = 'Neuquén'))
ON CONFLICT DO NOTHING;

-- Río Negro
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Viedma', (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
    ('San Carlos de Bariloche', (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
    ('General Roca', (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
    ('Cipolletti', (SELECT id FROM provincias WHERE nombre = 'Río Negro')),
    ('El Bolsón', (SELECT id FROM provincias WHERE nombre = 'Río Negro'))
ON CONFLICT DO NOTHING;

-- Chubut
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Rawson', (SELECT id FROM provincias WHERE nombre = 'Chubut')),
    ('Comodoro Rivadavia', (SELECT id FROM provincias WHERE nombre = 'Chubut')),
    ('Trelew', (SELECT id FROM provincias WHERE nombre = 'Chubut')),
    ('Puerto Madryn', (SELECT id FROM provincias WHERE nombre = 'Chubut')),
    ('Esquel', (SELECT id FROM provincias WHERE nombre = 'Chubut'))
ON CONFLICT DO NOTHING;

-- Santa Cruz
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Río Gallegos', (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
    ('Caleta Olivia', (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
    ('El Calafate', (SELECT id FROM provincias WHERE nombre = 'Santa Cruz')),
    ('Pico Truncado', (SELECT id FROM provincias WHERE nombre = 'Santa Cruz'))
ON CONFLICT DO NOTHING;

-- Tierra del Fuego
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Ushuaia', (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego')),
    ('Río Grande', (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego')),
    ('Tolhuin', (SELECT id FROM provincias WHERE nombre = 'Tierra del Fuego'))
ON CONFLICT DO NOTHING;

-- Formosa
INSERT INTO localidades (nombre, provincia_id) VALUES 
    ('Formosa', (SELECT id FROM provincias WHERE nombre = 'Formosa')),
    ('Clorinda', (SELECT id FROM provincias WHERE nombre = 'Formosa')),
    ('Pirané', (SELECT id FROM provincias WHERE nombre = 'Formosa'))
ON CONFLICT DO NOTHING;

-- ========================================
-- NACIONALIDADES
-- ========================================
INSERT INTO nacionalidades (nombre) VALUES ('Argentina') ON CONFLICT DO NOTHING;
INSERT INTO nacionalidades (nombre) VALUES ('Extranjera') ON CONFLICT DO NOTHING;

-- ========================================
-- BANCOS ARGENTINOS
-- ========================================
INSERT INTO bancos (nombre) VALUES ('Banco de la Nación Argentina') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Provincia de Buenos Aires') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Nuevo Banco de Santa Fe') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Ciudad de Buenos Aires') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Credicoop') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Galicia') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Macro') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Supervielle') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Santander Río') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('BBVA Argentina') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('HSBC Bank Argentina') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Patagonia') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Hipotecario') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco Comafi') ON CONFLICT DO NOTHING;
INSERT INTO bancos (nombre) VALUES ('Banco ICBC') ON CONFLICT DO NOTHING;

-- ========================================
-- HABITACIONES
-- ========================================

-- Individual Estándar (10 habitaciones - $50,800 por noche)
INSERT INTO habitaciones (nombre, precio, tipo_habitacion, estado_habitacion) VALUES 
    ('IE1', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE2', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE3', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE4', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE5', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE6', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE7', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE8', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE9', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE'),
    ('IE10', 50800, 'INDIVIDUAL_ESTANDAR', 'LIBRE')
ON CONFLICT DO NOTHING;

-- Doble Estándar (18 habitaciones - $70,230 por noche)
INSERT INTO habitaciones (nombre, precio, tipo_habitacion, estado_habitacion) VALUES 
    ('DE1', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE2', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE3', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE4', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE5', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE6', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE7', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE8', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE9', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE10', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE11', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE12', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE13', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE14', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE15', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE16', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE17', 70230, 'DOBLE_ESTANDAR', 'LIBRE'),
    ('DE18', 70230, 'DOBLE_ESTANDAR', 'LIBRE')
ON CONFLICT DO NOTHING;

-- Doble Superior (8 habitaciones - $90,560 por noche)
INSERT INTO habitaciones (nombre, precio, tipo_habitacion, estado_habitacion) VALUES 
    ('DS1', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS2', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS3', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS4', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS5', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS6', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS7', 90560, 'DOBLE_SUPERIOR', 'LIBRE'),
    ('DS8', 90560, 'DOBLE_SUPERIOR', 'LIBRE')
ON CONFLICT DO NOTHING;

-- Superior Family Plan (10 habitaciones - $110,500 por noche)
INSERT INTO habitaciones (nombre, precio, tipo_habitacion, estado_habitacion) VALUES 
    ('SFP1', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP2', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP3', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP4', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP5', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP6', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP7', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP8', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP9', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE'),
    ('SFP10', 110500, 'SUPERIOR_FAMILY_PLAN', 'LIBRE')
ON CONFLICT DO NOTHING;

-- Suite Doble (2 habitaciones - $128,600 por noche)
INSERT INTO habitaciones (nombre, precio, tipo_habitacion, estado_habitacion) VALUES 
    ('SD1', 128600, 'SUITE_DOBLE', 'LIBRE'),
    ('SD2', 128600, 'SUITE_DOBLE', 'LIBRE')
ON CONFLICT DO NOTHING;
