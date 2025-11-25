

-- ==========================================
--                 HUESPEDES
-- ==========================================

-- HUÉSPED 1: Consumidor Final (DNI)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'DNI', '11223344', 'PEREZ', 'MARIA', 'CONSUMIDOR_FINAL', '1985-10-20', '1155551234', 'Abogada', 'Argentina',
    'San Martin', '500', '2000', 'Rosario', 'Santa Fe', 'maria.perez@test.com', NULL
);

-- HUÉSPED 2: Monotributista (Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'PASAPORTE', 'P87654321', 'SMITH', 'JOHN', 'MONOTRIBUTISTA', '1995-03-15', '4412345678', 'Consultor', 'Canadiense',
    'Av. Central', '12', 'B7000', 'Cordoba', 'Cordoba', 'john.smith@test.com', NULL
);

-- HUÉSPED 3: Responsable Inscripto (DNI con CUIT)
-- NOTA: El CUIT es obligatorio si la CategoriaFiscal es RESPONSABLE_INSCRIPTO (Validación en GestorHuespedes).
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'DNI', '30000000', 'GONZALEZ', 'CARLOS', 'RESPONSABLE_INSCRIPTO', '1978-07-01', '5491133334444', 'Gerente', 'Chilena',
    'Junin', '880', '1425', 'Buenos Aires', 'Buenos Aires', 'carlos.g@test.com', '2030000001'
);