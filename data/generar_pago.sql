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
