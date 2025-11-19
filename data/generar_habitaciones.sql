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