-- ==========================================
-- TIPOS DE HABITACIÓN
-- ==========================================
INSERT INTO tipo_habitacion (nombre, costo_noche, maximo_huespedes) 
VALUES ('Individual Estándar', 50800.00, 1);

INSERT INTO tipo_habitacion (nombre, costo_noche, maximo_huespedes) 
VALUES ('Doble Estándar', 70230.00, 2);

INSERT INTO tipo_habitacion (nombre, costo_noche, maximo_huespedes) 
VALUES ('Doble Superior', 90560.00, 2);

INSERT INTO tipo_habitacion (nombre, costo_noche, maximo_huespedes) 
VALUES ('Superior Family Plan', 110500.00, 5);

INSERT INTO tipo_habitacion (nombre, costo_noche, maximo_huespedes) 
VALUES ('Suite Doble', 128600.00, 2);

-- ==========================================
-- HABITACIONES - PLANTA 1 (Habitaciones 101-124)
-- ==========================================

-- Individual Estándar (5 habitaciones en planta 1)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (101, 1, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (102, 1, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (103, 1, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (104, 1, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (105, 1, 'Individual Estándar');

-- Doble Estándar (9 habitaciones en planta 1)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (106, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (107, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (108, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (109, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (110, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (111, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (112, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (113, 1, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (114, 1, 'Doble Estándar');

-- Doble Superior (4 habitaciones en planta 1)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (115, 1, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (116, 1, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (117, 1, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (118, 1, 'Doble Superior');

-- Superior Family Plan (5 habitaciones en planta 1)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (119, 1, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (120, 1, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (121, 1, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (122, 1, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (123, 1, 'Superior Family Plan');

-- Suite Doble (1 habitación en planta 1)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (124, 1, 'Suite Doble');

-- ==========================================
-- HABITACIONES - PLANTA 2 (Habitaciones 201-224)
-- ==========================================

-- Individual Estándar (5 habitaciones en planta 2)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (201, 2, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (202, 2, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (203, 2, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (204, 2, 'Individual Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (205, 2, 'Individual Estándar');

-- Doble Estándar (9 habitaciones en planta 2)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (206, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (207, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (208, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (209, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (210, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (211, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (212, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (213, 2, 'Doble Estándar');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (214, 2, 'Doble Estándar');

-- Doble Superior (4 habitaciones en planta 2)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (215, 2, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (216, 2, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (217, 2, 'Doble Superior');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (218, 2, 'Doble Superior');

-- Superior Family Plan (5 habitaciones en planta 2)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (219, 2, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (220, 2, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (221, 2, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (222, 2, 'Superior Family Plan');
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (223, 2, 'Superior Family Plan');

-- Suite Doble (1 habitación en planta 2)
INSERT INTO habitacion (numero, piso, tipo_habitacion_id) VALUES (224, 2, 'Suite Doble');


--===============================
--          HUÉSPEDES 
-- ===============================

-- 1. Juan Pérez (Turista local, Familia)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '25111222', 'Pérez', 'Juan', 
    NULL, 'CONSUMIDOR_FINAL', '1980-05-15', 
    '011-15-5555-1234', 'juan.perez@email.com', 'Empleado Administrativo', 'Argentina',
    'Av. Santa Fe', '2040', '2', 'B', '1123', 'CABA', 'Buenos Aires', 'ACTIVO'
);

-- 2. María González (Esposa de Juan)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '26333444', 'González', 'María', 
    NULL, 'CONSUMIDOR_FINAL', '1982-11-20', 
    '011-15-5555-5678', 'maria.gonz@email.com', 'Docente', 'Argentina',
    'Av. Santa Fe', '2040', '2', 'B', '1123', 'CABA', 'Buenos Aires', 'ACTIVO'
);

-- 3. Carlos Rodríguez (Viajero de negocios - Responsable Inscripto)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '30999888', 'Rodríguez', 'Carlos', 
    '20309998881', 'RESPONSABLE_INSCRIPTO', '1985-03-10', 
    '0351-444-5555', 'carlos.rod@empresa.com', 'Ingeniero Civil', 'Argentina',
    'Av. Colón', '500', NULL, NULL, '5000', 'Córdoba Capital', 'Córdoba', 'ACTIVO'
);

-- 4. Laura García (Estudiante)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '42555666', 'García', 'Laura', 
    NULL, 'CONSUMIDOR_FINAL', '2000-07-25', 
    '0261-555-8888', 'lau.garcia22@email.com', 'Estudiante', 'Argentina',
    'Aristides Villanueva', '230', NULL, NULL, '5500', 'Mendoza Capital', 'Mendoza', 'ACTIVO'
);

-- 5. Joao Silva (Turista Brasileño - Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'PASAPORTE', 'BR123456', 'Silva', 'Joao', 
    NULL, 'CONSUMIDOR_FINAL', '1978-01-15', 
    '+55119999', 'joao.silva@uol.com.br', 'Abogado', 'Brasil',
    'Rua Augusta', '1500', NULL, NULL, '01305', 'São Paulo', 'São Paulo', 'ACTIVO'
);

-- 6. Emily Watson (Turista USA - Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'PASAPORTE', 'US987654', 'Watson', 'Emily', 
    NULL, 'CONSUMIDOR_FINAL', '1990-12-05', 
    '+12125551', 'emily.w@gmail.com', 'Periodista', 'Estados Unidos',
    '5th Avenue', '725', '10', 'A', '10022', 'New York', 'New York', 'ACTIVO'
);

-- 7. Sofía Martínez (Jubilada)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '10222333', 'Martínez', 'Sofía', 
    NULL, 'CONSUMIDOR_FINAL', '1955-09-30', 
    '0223-444-1111', 'sofia.mtz@email.com', 'Jubilada', 'Argentina',
    'Calle Güemes', '2800', NULL, NULL, '7600', 'Mar del Plata', 'Buenos Aires', 'ACTIVO'
);

-- 8. Miguel Ángel López (Turista Español )
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'PASAPORTE', 'ES556677', 'López', 'Miguel Ángel', 
    NULL, 'CONSUMIDOR_FINAL', '1975-06-18', 
    '+34600111', 'migue.lopez@madrid.es', 'Arquitecto', 'España',
    'Gran Vía', '45', '3', 'Ext', '28013', 'Madrid', 'Madrid', 'ACTIVO'
);

-- 9. Ana Fernández (Profesional Monotributista)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '28111999', 'Fernández', 'Ana', 
    '27281119992', 'MONOTRIBUTISTA', '1981-02-14', 
    '0341-555-7777', 'ana.fer@rosario.com', 'Diseñadora Gráfica', 'Argentina',
    'Bv. Oroño', '1200', NULL, NULL, '2000', 'Rosario', 'Santa Fe', 'ACTIVO'
);

-- 10. Lucas Benítez
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia, estado_huesped
) VALUES (
    'DNI', '38444555', 'Benítez', 'Lucas', 
    NULL, 'CONSUMIDOR_FINAL', '1995-08-08', 
    '0294-444-9999', 'lucas.bari@email.com', 'Guía de Turismo', 'Argentina',
    'Av. Bustillo', '1500', NULL, NULL, '8400', 'Bariloche', 'Río Negro', 'ACTIVO'
);

-- 11. María Pérez (Consumidor Final)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit, estado_huesped
) VALUES (
    'DNI', '11223344', 'PEREZ', 'MARIA', 'CONSUMIDOR_FINAL', '1985-10-20', '1155551234', 'Abogada', 'Argentina',
    'San Martin', '500', '2000', 'Rosario', 'Santa Fe', 'maria.perez@test.com', NULL, 'ACTIVO'
);

-- 12. John Smith (Monotributista)
-- CORRECCIÓN: 'P87654321' (9 chars) recortado a 'P8765432' (8 chars) para cumplir restricción @Column(length=8)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit, estado_huesped
) VALUES (
    'PASAPORTE', 'P8765432', 'SMITH', 'JOHN', 'MONOTRIBUTISTA', '1995-03-15', '4412345678', 'Consultor', 'Canadiense',
    'Av. Central', '12', 'B7000', 'Cordoba', 'Cordoba', 'john.smith@test.com', NULL, 'ACTIVO'
);

-- 13. Carlos Gonzalez (Responsable Inscripto)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit, estado_huesped
) VALUES (
    'DNI', '30000000', 'GONZALEZ', 'CARLOS', 'RESPONSABLE_INSCRIPTO', '1978-07-01', '5491133334444', 'Gerente', 'Chilena',
    'Junin', '880', '1425', 'Buenos Aires', 'Buenos Aires', 'carlos.g@test.com', '2030000001', 'ACTIVO'
);

-- ========================
-- 1. FACTURAS 
-- =======================
INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) VALUES (1, '2024-03-01', 101600.00, 'TIPO_B', 'EMITIDA');
INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) VALUES (2, '2024-03-02', 70230.00, 'TIPO_B', 'CANCELADA');
INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) VALUES (3, '2024-03-05', 250000.00, 'TIPO_A', 'EMITIDA');
INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) VALUES (4, '2024-03-10', 90560.00, 'TIPO_B', 'PARCIALMENTE_PAGA');

-- =======================
-- 2. MEDIOS DE PAGO
-- =======================

-- A. DIVISA USD (ID 1)
INSERT INTO metodo_pago (id) VALUES (1); 
-- CORREGIDO: metodo_pago_id -> id
INSERT INTO divisa (id, moneda, cotizacion, numero) 
VALUES (1, 'USD', '1000.00', 'DIV-001');

-- B. TARJETA VISA (ID 2)
INSERT INTO metodo_pago (id) VALUES (2);
-- CORREGIDO: metodo_pago_id -> id
INSERT INTO tarjeta (id, numero, banco, tipo) 
VALUES (2, '4500-0000-0000-0001', 'Santander', 'Crédito Visa');

-- C. CHEQUE DE TERCEROS (ID 3)
INSERT INTO metodo_pago (id) VALUES (3);
-- CORREGIDO: metodo_pago_id -> id
INSERT INTO cheque (id, numero, fecha_cobro, banco, monto, tipo, estado_cheque, plazo)
VALUES (3, 'CHQ-99887766', '2024-04-05', 'BBVA', 250000.00, 'TERCEROS', 'EN_CARTERA', '30 días');

-- D. OTRA TARJETA (ID 4)
INSERT INTO metodo_pago (id) VALUES (4);
-- CORREGIDO: metodo_pago_id -> id
INSERT INTO tarjeta (id, numero, banco, tipo) 
VALUES (4, '5400-0000-0000-0002', 'Galicia', 'Débito Mastercard');

-- E. OTRA DIVISA ARS (ID 5)
INSERT INTO metodo_pago (id) VALUES (5); 
-- CORREGIDO: metodo_pago_id -> id
INSERT INTO divisa (id, moneda, cotizacion, numero) 
VALUES (5, 'ARS', '1.00', 'DIV-002');

-- =======================
--  PAGOS
-- =======================
-- En Pago SI usaste @JoinColumn(name="metodo_pago_id"), así que aquí se mantiene igual.

-- Pago 1: Usa la Divisa USD (ID 1)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, metodo_pago_id) 
VALUES (1, 101600.00, 0.0, '2024-03-01', 'APLICADO', 1, 1);

-- Pago 2: Usa la Tarjeta Visa (ID 2)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, metodo_pago_id) 
VALUES (2, 70230.00, 0.0, '2024-03-02', 'APLICADO', 2, 2);

-- Pago 3: Usa el Cheque (ID 3)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, metodo_pago_id) 
VALUES (3, 250000.00, 0.0, '2024-03-05', 'REGISTRADO', 3, 3);

-- Pago 4: Usa la Tarjeta Débito (ID 4)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, metodo_pago_id) 
VALUES (4, 50000.00, 0.0, '2024-03-10', 'APLICADO', 4, 4);

-- Pago 5: Usa Divisa ARS (ID 5)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, metodo_pago_id) 
VALUES (5, 40560.00, 0.0, '2024-03-10', 'APLICADO', 4, 5);

-- ==========================================
-- NOTAS DE CRÉDITO
-- ==========================================

-- Caso 1: Nota de Crédito por devolución total (Factura 2)
-- Imaginemos que hubo un error en la Factura 2 (que estaba Cancelada/Pagada) y se devuelve todo.
INSERT INTO nota_credito (iva, importe_total, estado_nota_credito, id_factura) 
VALUES (12190.00, 70230.00, 'APLICADA', 2);

-- Caso 2: Nota de Crédito por descuento comercial posterior (Factura 4)
-- La factura 4 es de 90.560. Le hacemos un descuento de 10.000.
INSERT INTO nota_credito (iva, importe_total, estado_nota_credito, id_factura) 
VALUES (1735.00, 10000.00, 'GENERADA', 4);

-- Caso 3: Nota de Crédito anulada (Error de tipeo)
INSERT INTO nota_credito (iva, importe_total, estado_nota_credito, id_factura) 
VALUES (0.00, 50000.00, 'ANULADA', 1);

-- Caso 4: Nota de Crédito parcial (Factura 3)
INSERT INTO nota_credito (iva, importe_total, estado_nota_credito, id_factura) 
VALUES (2100.00, 12100.00, 'PARCIALMENTE_APLICADA', 3);

-- =================================================================
--  CARGA DE DATOS DE PRUEBA: PERSONA FÍSICA
-- ================================================================
-- Caso 1: Cliente Físico Activo (ID 1)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (true);

-- Caso 2: Cliente Físico Inactivo (ID 2)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (false);

-- Caso 3: Otro Cliente Activo (ID 3)
INSERT INTO persona_fisica (estado_persona_fisica) VALUES (true);


-- =================================================================
-- PERSONA JURÍDICA
-- =================================================================

-- Caso 1: Empresa Estándar de Buenos Aires
INSERT INTO persona_juridica (
    cuit, razon_social, telefono, estado_persona_juridica, 
    calle, numero, localidad, codigo_postal, provincia
) 
VALUES (
    '30112233445', 'Tech Solutions S.A.', '011-4567-8900', true, 
    'Av. Corrientes', '1234', 'CABA', '1043', 'Buenos Aires'
);

-- Caso 2: Empresa del Interior (Rosario)
INSERT INTO persona_juridica (
    cuit, razon_social, telefono, estado_persona_juridica, 
    calle, numero, localidad, codigo_postal, provincia
) 
VALUES (
    '33998877669', 'Constructora del Norte SRL', '0341-222-3333', true, 
    'Bv. Oroño', '500', 'Rosario', '2000', 'Santa Fe'
);

-- Caso 3: Empresa Inactiva / En quiebra
INSERT INTO persona_juridica (
    cuit, razon_social, telefono, estado_persona_juridica, 
    calle, numero, localidad, codigo_postal, provincia
) 
VALUES (
    '30555555552', 'Hotel Supplies Ltd', '0223-444-5555', false, 
    'Av. Colón', '2020', 'Mar del Plata', '7600', 'Buenos Aires'
);

-- Caso 4: Empresa con nombre largo
-- Nota: La columna 'razon_social' tiene un limite de 100 caracteres.
INSERT INTO persona_juridica (
    cuit, razon_social, telefono, estado_persona_juridica, 
    calle, numero, localidad, codigo_postal, provincia
) 
VALUES (
    '30101010101', 'Cooperativa de Trabajo y Servicios Multiples de la Patagonia Austral Limitada', '02966-444444', true, 
    'Roca', '100', 'Rio Gallegos', '9400', 'Santa Cruz'
);


-- =============================================================================
-- RESERVAS, ESTADIAS Y FICHAS DE EVENTO 
-- =============================================================================

-- ==========================================
-- ESCENARIO 1: RESERVA ACTIVA (FUTURA)
-- Huésped: Juan Pérez (DNI 25111222)
-- Habitación: 101
-- Fecha: Diciembre 2025
-- ==========================================

-- 1.1. Crear la Reserva
INSERT INTO reserva (id_reserva, estado_reserva, nombre_reserva, apellido_reserva, telefono_reserva)
VALUES (1, 'ACTIVA', 'Juan', 'Pérez', '011-15-5555-1234');

-- 1.2. Crear la Ficha de Evento (Bloqueo de calendario)
INSERT INTO ficha_evento (
    id_ficha_eventos, 
    estado, 
    fecha_inicio, 
    fecha_fin, 
    descripcion, 
    cancelado,
    habitacion_id, 
    reserva_id, 
    estadia_id,
    responsable_tipo_documento, 
    responsable_numero_documento
) VALUES (
    1, 
    'RESERVADA', 
    '2025-12-01 14:00:00', 
    '2025-12-10 10:00:00', 
    'Reserva de Vacaciones - Familia Pérez', 
    false,
    101,  -- Habitación
    1,    -- Reserva
    NULL, -- Sin estadía aún
    'DNI', '25111222' 
);


-- ==========================================
-- ESCENARIO 2: RESERVA CANCELADA
-- Huésped: Carlos Rodríguez (DNI 30999888)
-- Habitación: 102
-- ==========================================

-- 2.1. Crear la Reserva (Estado Cancelada)
INSERT INTO reserva (id_reserva, estado_reserva, nombre_reserva, apellido_reserva, telefono_reserva)
VALUES (2, 'CANCELADA', 'Carlos', 'Rodríguez', '0351-444-5555');

-- 2.2. Crear la Ficha de Evento (Cancelada = true)
INSERT INTO ficha_evento (
    id_ficha_eventos, 
    estado, 
    fecha_inicio, 
    fecha_fin, 
    descripcion, 
    cancelado,
    habitacion_id, 
    reserva_id, 
    estadia_id,
    responsable_tipo_documento, 
    responsable_numero_documento
) VALUES (
    2, 
    'RESERVADA', 
    '2025-11-20 14:00:00', 
    '2025-11-25 10:00:00', 
    'Reserva Cancelada por cliente', 
    true, -- Importante: flag de cancelado
    102, 
    2, 
    NULL,
    'DNI', '30999888'
);


-- ==========================================
-- ESCENARIO 3: ESTADÍA ACTUAL (CHECK-IN)
-- Huésped: Laura García (DNI 42555666) con Acompañante Joao Silva
-- Habitación: 103
-- ==========================================

-- 3.1. Crear la Reserva
INSERT INTO reserva (id_reserva, estado_reserva, nombre_reserva, apellido_reserva, telefono_reserva)
VALUES (3, 'EFECTUADA', 'Laura', 'García', '0261-555-8888');

-- 3.2. Crear la Estadía (Vinculada a la Reserva)
-- Nota: id_estadia es autogenerado, forzamos 1 para el ejemplo.
INSERT INTO estadia (id_estadia, id_reserva) 
VALUES (1, 3);

-- 3.3. Crear la Ficha de Evento (Estado OCUPADA)
INSERT INTO ficha_evento (
    id_ficha_eventos, 
    estado, 
    fecha_inicio, 
    fecha_fin, 
    descripcion, 
    cancelado,
    habitacion_id, 
    reserva_id, 
    estadia_id,
    responsable_tipo_documento, 
    responsable_numero_documento
) VALUES (
    3, 
    'OCUPADA', 
    '2025-10-15 12:00:00', 
    '2025-10-20 10:00:00', 
    'Ocupación en curso', 
    false,
    103, 
    3,   
    1,   
    'DNI', '42555666'
);

-- 3.4. Agregar Acompañante (Joao Silva - PASAPORTE BR123456)
INSERT INTO ficha_evento_acompanante (ficha_evento_id, huesped_tipo_doc, huesped_num_doc)
VALUES (3, 'PASAPORTE', 'BR123456');


-- ==========================================
-- ESCENARIO 4: WALK-IN (Llegada sin reserva previa)
-- Huésped: John Smith (PASAPORTE P8765432)
-- Habitación: 104
-- ==========================================

-- 4.1. Crear la Reserva (Se crea automáticamente al hacer check-in)
INSERT INTO reserva (id_reserva, estado_reserva, nombre_reserva, apellido_reserva, telefono_reserva)
VALUES (4, 'EFECTUADA', 'JOHN', 'SMITH', '4412345678');

-- 4.2. Crear la Estadía
INSERT INTO estadia (id_estadia, id_reserva) 
VALUES (2, 4);

-- 4.3. Crear la Ficha de Evento (OCUPADA)
INSERT INTO ficha_evento (
    id_ficha_eventos, 
    estado, 
    fecha_inicio, 
    fecha_fin, 
    descripcion, 
    cancelado,
    habitacion_id, 
    reserva_id, 
    estadia_id,
    responsable_tipo_documento, 
    responsable_numero_documento
) VALUES (
    4, 
    'OCUPADA', 
    '2025-11-28 14:00:00', 
    '2025-11-30 10:00:00', 
    'Walk-in John Smith', 
    false,
    104, 
    4,   
    2,   
    'PASAPORTE', 'P8765432'
);


-- ==========================================
-- ESCENARIO 5: ESTADÍA PASADA (HISTÓRICO)
-- Huésped: María Pérez (DNI 11223344)
-- Habitación: 105
-- ==========================================

-- 5.1. Reserva vieja
INSERT INTO reserva (id_reserva, estado_reserva, nombre_reserva, apellido_reserva, telefono_reserva)
VALUES (5, 'EFECTUADA', 'MARIA', 'PEREZ', '1155551234');

-- 5.2. Estadía vieja
INSERT INTO estadia (id_estadia, id_reserva) 
VALUES (3, 5);

-- 5.3. Ficha de Evento (OCUPADA pero en fechas pasadas)
INSERT INTO ficha_evento (
    id_ficha_eventos, 
    estado, 
    fecha_inicio, 
    fecha_fin, 
    descripcion, 
    cancelado,
    habitacion_id, 
    reserva_id, 
    estadia_id,
    responsable_tipo_documento, 
    responsable_numero_documento
) VALUES (
    5, 
    'OCUPADA', 
    '2025-01-10 14:00:00', 
    '2025-01-15 10:00:00', 
    'Estadía finalizada en Enero', 
    false,
    105, 
    5,   
    3,   
    'DNI', '11223344'
);

-- =============================================================================
-- SERVICIOS ADICIONALES 
-- =============================================================================

-- Servicios típicos de Estadía 1 (Pareja)
INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Cena Romántica en la habitación', 45000.00, 'BAR', 1);

INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Botella de Champagne', 25000.00, 'BAR', 1);

-- Ajuste: 'Late Check-out' no tiene un tipo claro en tu Enum actual (LAVADO_PLANCHADO, SAUNA, BAR).
-- Se asigna 'BAR' o el que consideres más cercano, o deberías agregar 'OTROS' al Enum.
-- Por ahora lo mapeamos a 'BAR' para que no falle el insert.
INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Late Check-out', 15000.00, 'BAR', 1);


-- Servicios típicos de Estadía 2 (Negocios)
INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Servicio de Lavandería (Traje)', 8500.00, 'LAVADO_PLANCHADO', 2);

INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Servicio de Lavandería (Camisas x3)', 12000.00, 'LAVADO_PLANCHADO', 2);

INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Consumo Frigobar (Agua x2, Snack)', 6500.00, 'BAR', 2);

-- Ajuste: 'Impresión documentos' tampoco encaja en el Enum actual. 
-- Lo asignamos a 'BAR' (como servicio vario) para evitar errores SQL.
INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Impresión documentos', 2500.00, 'BAR', 2);


-- Servicios típicos de Estadía 3 (Familia)
-- Ajuste: 'Cuna adicional' -> Asignado a 'BAR' por defecto ante falta de tipo 'MOBILIARIO' u 'OTROS'.
INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Cuna adicional', 10000.00, 'BAR', 3);

INSERT INTO servicios_adicionales (descripcion, precio, tipo_servicio, id_estadia) 
VALUES ('Desayuno a la habitación', 18000.00, 'BAR', 3);