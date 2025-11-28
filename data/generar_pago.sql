-- ==========================================
-- FACTURAS (Necesarias antes de crear pagos)
-- ==========================================
-- Nota: Asumimos IDs explícitos para poder relacionar los pagos fácilmente en este script.

-- Factura 1: Pago simple en Divisa (Dólares)
INSERT INTO factura (id_factura, fecha_emision, importe_total) 
VALUES (1, '2024-03-01', 101600.00);

-- Factura 2: Pago con Tarjeta de Crédito
INSERT INTO factura (id_factura, fecha_emision, importe_total) 
VALUES (2, '2024-03-02', 70230.00);

-- Factura 3: Pago con Cheque Diferido
INSERT INTO factura (id_factura, fecha_emision, importe_total) 
VALUES (3, '2024-03-05', 250000.00);

-- Factura 4: Pago Mixto (Parte Tarjeta, Parte Efectivo/Divisa)
INSERT INTO factura (id_factura, fecha_emision, importe_total) 
VALUES (4, '2024-03-10', 90560.00);

-- ==========================================
-- PAGOS (Tabla Padre - Herencia JOINED)
-- ==========================================
-- Aquí insertamos la parte "genérica" del pago.
-- Los campos booleanos (valido) se asumen como 1 (true) o 0 (false).

-- Pago 1 (Para Factura 1): Será Divisa
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, valido, id_factura) 
VALUES (1, 101600.00, 0.00, '2024-03-01', true, 1);

-- Pago 2 (Para Factura 2): Será Tarjeta
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, valido, id_factura) 
VALUES (2, 70230.00, 0.00, '2024-03-02', true, 2);

-- Pago 3 (Para Factura 3): Será Cheque
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, valido, id_factura) 
VALUES (3, 250000.00, 0.00, '2024-03-05', true, 3);

-- Pagos 4 y 5 (Para Factura 4 - Pago dividido):
-- Pago 4: Tarjeta (50.000)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, valido, id_factura) 
VALUES (4, 50000.00, 0.00, '2024-03-10', true, 4);
-- Pago 5: Divisa/Efectivo (40.560)
INSERT INTO pago (id_pago, importe, vuelto, fecha_pago, valido, id_factura) 
VALUES (5, 40560.00, 0.00, '2024-03-10', true, 4);


-- ==========================================
-- DETALLE: DIVISA (Hereda de Pago)
-- ==========================================
-- Usamos el mismo ID que en la tabla 'pago'

-- Corresponde al Pago 1 (USD)
INSERT INTO divisa (id_pago, moneda, cotizacion) 
VALUES (1, 'USD', '1000.00'); -- Cotización como String según tu entidad

-- Corresponde al Pago 5 (ARS - Pesos Argentinos)
INSERT INTO divisa (id_pago, moneda, cotizacion) 
VALUES (5, 'ARS', '1.00');

-- ==========================================
-- DETALLE: TARJETA (Hereda de Pago)
-- ==========================================

-- Corresponde al Pago 2
INSERT INTO tarjeta (id_pago, banco, tipo) 
VALUES (2, 'Santander', 'Crédito Visa');

-- Corresponde al Pago 4
INSERT INTO tarjeta (id_pago, banco, tipo) 
VALUES (4, 'Galicia', 'Débito Mastercard');

-- ==========================================
-- DETALLE: CHEQUE (Hereda de Pago)
-- ==========================================

-- Corresponde al Pago 3
INSERT INTO cheque (id_pago, banco, tipo, numero, fecha_cobro) 
VALUES (3, 'BBVA', 'Diferido', 'CHQ-99887766', '2024-04-05');