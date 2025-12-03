-- =============================================================================
-- DATOS DE PRUEBA: RESERVAS Y FICHAS DE EVENTO (Ampliado)
-- Dependencias: Requiere que existan Habitaciones y Huéspedes.
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