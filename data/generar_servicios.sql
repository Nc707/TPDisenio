-- =============================================================================
-- DATOS DE PRUEBA: SERVICIOS ADICIONALES (Corregido)
-- Tabla: servicios_adicionales
-- Columnas: id_servicio (auto), descripcion, precio, tipo_servicio, id_estadia
-- Notas:
-- 1. 'id_estadia' es obligatorio (@ManyToOne optional=false). Asumimos IDs 1, 2, 3.
-- 2. 'tipo_servicio' es un Enum (LAVADO_PLANCHADO, SAUNA, BAR).
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