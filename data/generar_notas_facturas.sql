-- =============================================================================
-- DATOS DE PRUEBA: NOTAS DE CRÉDITO
-- Entidad: NotaCredito
-- Relación: Muchas Notas de Crédito pueden pertenecer a una Factura.
-- Estados: GENERADA, PARCIALMENTE_APLICADA, APLICADA, ANULADA
-- =============================================================================

-- ==========================================
-- PREREQUISITO: FACTURAS
-- ==========================================
-- Insertamos las facturas necesarias para que las Notas de Crédito tengan referencia válida.
-- IDs explícitos: 1, 2, 3, 4.

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (1, '2024-03-01', 101600.00, 'TIPO_B', 'EMITIDA');

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (2, '2024-03-02', 70230.00, 'TIPO_B', 'CANCELADA'); -- Pagada total

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (3, '2024-03-05', 250000.00, 'TIPO_A', 'EMITIDA');

INSERT INTO factura (id_factura, fecha_emision, importe_total, tipo_factura, estado_factura) 
VALUES (4, '2024-03-10', 90560.00, 'TIPO_B', 'PARCIALMENTE_PAGA');


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