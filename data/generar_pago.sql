-- =============================================================================
-- DATOS DE PRUEBA: FACTURACIÓN Y PAGOS (Modelo V4)
-- Entidades: Factura, Pago, y Medios de Pago (Divisa, Tarjeta, Cheque)
-- =============================================================================

-- 1. FACTURAS
-- =======================
INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (1, '2024-03-01', 101600.00, 'TIPO_B', 'EMITIDA');

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (2, '2024-03-02', 70230.00, 'TIPO_B', 'CANCELADA');

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (3, '2024-03-05', 250000.00, 'TIPO_A', 'EMITIDA');

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (4, '2024-03-10', 90560.00, 'TIPO_B', 'PARCIALMENTE_PAGA');


-- 2. MEDIOS DE PAGO (Entidades Hijas de MetodoPago)
-- =======================

-- A. DIVISAS
-- Clase Divisa extends MetodoPago. ID: numero (String).
INSERT INTO divisa (numero, moneda, cotizacion) 
VALUES ('DIV-001', 'USD', '1000.00');

INSERT INTO divisa (numero, moneda, cotizacion) 
VALUES ('DIV-002', 'ARS', '1.00');

INSERT INTO divisa (numero, moneda, cotizacion) 
VALUES ('DIV-003', 'EUR', '1100.00');

-- B. TARJETAS
-- Clase Tarjeta extends MetodoPago. ID: numero (String).
INSERT INTO tarjeta (numero, banco, tipo) 
VALUES ('4500-0000-0000-0001', 'Santander', 'Crédito Visa');

INSERT INTO tarjeta (numero, banco, tipo) 
VALUES ('5400-0000-0000-0002', 'Galicia', 'Débito Mastercard');

-- C. CHEQUES
-- Clase Cheque extends MetodoPago. ID: numero (String).
-- Enums: TipoCheque (PROPIOS, TERCEROS), EstadoCheque (PENDIENTE, EN_CARTERA...)
INSERT INTO cheque (numero, fecha_cobro, banco, monto, tipo, estado_cheque, plazo)
VALUES ('CHQ-99887766', '2024-04-05', 'BBVA', 250000.00, 'TERCEROS', 'EN_CARTERA', '30 días');


-- 3. PAGOS
-- =======================
-- Vinculamos el pago con su medio correspondiente.
-- Nota: Dependiendo de tu estrategia JPA (@JoinColumn o herencia), 
-- estas columnas FK (divisa_numero, tarjeta_numero, etc.) pueden variar de nombre.

-- Pago 1: Divisa USD (Factura 1)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, divisa_numero) 
VALUES (1, 101600.00, 0.00, '2024-03-01', 'APLICADO', 1, 'DIV-001');

-- Pago 2: Tarjeta Visa (Factura 2)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, tarjeta_numero) 
VALUES (2, 70230.00, 0.00, '2024-03-02', 'APLICADO', 2, '4500-0000-0000-0001');

-- Pago 3: Cheque (Factura 3)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, cheque_numero) 
VALUES (3, 250000.00, 0.00, '2024-03-05', 'REGISTRADO', 3, 'CHQ-99887766');

-- Pago 4: Parte Tarjeta (Factura 4)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, tarjeta_numero) 
VALUES (4, 50000.00, 0.00, '2024-03-10', 'APLICADO', 4, '5400-0000-0000-0002');

-- Pago 5: Parte Efectivo/Divisa ARS (Factura 4)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, estado_pago, id_factura, divisa_numero) 
VALUES (5, 40560.00, 0.00, '2024-03-10', 'APLICADO', 4, 'DIV-002');