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
