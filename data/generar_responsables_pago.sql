-- =================================================================
-- 1. CARGA DE DATOS DE PRUEBA: PERSONA FÍSICA
-- =================================================================
-- Escenario: Cargar clientes individuales en distintos estados.

-- Caso 1: Cliente Físico Activo (ID autogenerado, ej: 1)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (true);

-- Caso 2: Cliente Físico Inactivo / Dado de baja (ID autogenerado, ej: 2)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (false);

-- Caso 3: Otro Cliente Activo (ID autogenerado, ej: 3)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (true);


-- =================================================================
-- 2. CARGA DE DATOS DE PRUEBA: PERSONA JURÍDICA
-- =================================================================
-- Escenario: Empresas con dirección completa (Embedded).

-- Caso 1: Empresa Estándar de Buenos Aires
INSERT INTO persona_juridica (cuit, razon_social, telefono, estado_persona_juridica, calle, numero, localidad, codigo_postal, provincia) 
VALUES ('30112233445', 'Tech Solutions S.A.', '011-4567-8900', true, 'Av. Corrientes', '1234', 'CABA', '1043', 'Buenos Aires');

-- Caso 2: Empresa del Interior (Rosario)
INSERT INTO persona_juridica (cuit, razon_social, telefono, estado_persona_juridica, calle, numero, localidad, codigo_postal, provincia) 
VALUES ('33998877669', 'Constructora del Norte SRL', '0341-222-3333', true, 'Bv. Oroño', '500', 'Rosario', '2000', 'Santa Fe');

-- Caso 3: Empresa Inactiva / En quiebra
INSERT INTO persona_juridica (cuit, razon_social, telefono, estado_persona_juridica, calle, numero, localidad, codigo_postal, provincia) 
VALUES ('30555555552', 'Hotel Supplies Ltd', '0223-444-5555', false, 'Av. Colón', '2020', 'Mar del Plata', '7600', 'Buenos Aires');

-- Caso 4: Empresa con nombre largo
INSERT INTO persona_juridica (cuit, razon_social, telefono, estado_persona_juridica, calle, numero, localidad, codigo_postal, provincia) 
VALUES ('30101010101', 'Cooperativa de Trabajo y Servicios Multiples de la Patagonia Austral Limitada', '02966-444444', true, 'Roca', '100', 'Rio Gallegos', '9400', 'Santa Cruz');

