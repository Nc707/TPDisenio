

-- ==========================================
--                 HUESPEDES
-- ==========================================
-- ==========================================
-- DATOS DE PRUEBA: HUÉSPEDES (10 Casos)
-- ==========================================

-- 1. Juan Pérez (Turista local, Familia)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '25111222', 'Pérez', 'Juan', 
    NULL, 'CONSUMIDOR_FINAL', '1980-05-15', 
    '011-15-5555-1234', 'juan.perez@email.com', 'Empleado Administrativo', 'Argentina',
    'Av. Santa Fe', '2040', '2', 'B', '1123', 'CABA', 'Buenos Aires'
);

-- 2. María González (Esposa de Juan)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '26333444', 'González', 'María', 
    NULL, 'CONSUMIDOR_FINAL', '1982-11-20', 
    '011-15-5555-5678', 'maria.gonz@email.com', 'Docente', 'Argentina',
    'Av. Santa Fe', '2040', '2', 'B', '1123', 'CABA', 'Buenos Aires'
);

-- 3. Carlos Rodríguez (Viajero de negocios - Responsable Inscripto)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '30999888', 'Rodríguez', 'Carlos', 
    '20309998881', 'RESPONSABLE_INSCRIPTO', '1985-03-10', 
    '0351-444-5555', 'carlos.rod@empresa.com', 'Ingeniero Civil', 'Argentina',
    'Av. Colón', '500', NULL, NULL, '5000', 'Córdoba Capital', 'Córdoba'
);

-- 4. Laura García (Estudiante)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '42555666', 'García', 'Laura', 
    NULL, 'CONSUMIDOR_FINAL', '2000-07-25', 
    '0261-555-8888', 'lau.garcia22@email.com', 'Estudiante', 'Argentina',
    'Aristides Villanueva', '230', NULL, NULL, '5500', 'Mendoza Capital', 'Mendoza'
);

-- 5. Joao Silva (Turista Brasileño - Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'PASAPORTE', 'BR123456', 'Silva', 'Joao', 
    NULL, 'CONSUMIDOR_FINAL', '1978-01-15', 
    '+55119999', 'joao.silva@uol.com.br', 'Abogado', 'Brasil',
    'Rua Augusta', '1500', NULL, NULL, '01305', 'São Paulo', 'São Paulo'
);

-- 6. Emily Watson (Turista USA - Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'PASAPORTE', 'US987654', 'Watson', 'Emily', 
    NULL, 'CONSUMIDOR_FINAL', '1990-12-05', 
    '+12125551', 'emily.w@gmail.com', 'Periodista', 'Estados Unidos',
    '5th Avenue', '725', '10', 'A', '10022', 'New York', 'New York'
);

-- 7. Sofía Martínez (Jubilada)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '10222333', 'Martínez', 'Sofía', 
    NULL, 'CONSUMIDOR_FINAL', '1955-09-30', 
    '0223-444-1111', 'sofia.mtz@email.com', 'Jubilada', 'Argentina',
    'Calle Güemes', '2800', NULL, NULL, '7600', 'Mar del Plata', 'Buenos Aires'
);

-- 8. Miguel Ángel López (Turista Español )
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'PASAPORTE', 'ES556677', 'López', 'Miguel Ángel', 
    NULL, 'CONSUMIDOR_FINAL', '1975-06-18', 
    '+34600111', 'migue.lopez@madrid.es', 'Arquitecto', 'España',
    'Gran Vía', '45', '3', 'Ext', '28013', 'Madrid', 'Madrid'
);

-- 9. Ana Fernández (Profesional Monotributista)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '28111999', 'Fernández', 'Ana', 
    '27281119992', 'MONOTRIBUTISTA', '1981-02-14', 
    '0341-555-7777', 'ana.fer@rosario.com', 'Diseñadora Gráfica', 'Argentina',
    'Bv. Oroño', '1200', NULL, NULL, '2000', 'Rosario', 'Santa Fe'
);

-- 10. Lucas Benítez
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, 
    cuit, categoria_fiscal, fecha_nacimiento, 
    telefono, email, ocupacion, nacionalidad,
    calle, numero, piso, departamento, codigo_postal, localidad, provincia
) VALUES (
    'DNI', '38444555', 'Benítez', 'Lucas', 
    NULL, 'CONSUMIDOR_FINAL', '1995-08-08', 
    '0294-444-9999', 'lucas.bari@email.com', 'Guía de Turismo', 'Argentina',
    'Av. Bustillo', '1500', NULL, NULL, '8400', 'Bariloche', 'Río Negro'
);
-- HUÉSPED 11: Consumidor Final (DNI)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'DNI', '11223344', 'PEREZ', 'MARIA', 'CONSUMIDOR_FINAL', '1985-10-20', '1155551234', 'Abogada', 'Argentina',
    'San Martin', '500', '2000', 'Rosario', 'Santa Fe', 'maria.perez@test.com', NULL
);

-- HUÉSPED 12: Monotributista (Pasaporte)
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'PASAPORTE', 'P87654321', 'SMITH', 'JOHN', 'MONOTRIBUTISTA', '1995-03-15', '4412345678', 'Consultor', 'Canadiense',
    'Av. Central', '12', 'B7000', 'Cordoba', 'Cordoba', 'john.smith@test.com', NULL
);
 
-- HUÉSPED 13: Responsable Inscripto (DNI con CUIT)
-- NOTA: El CUIT es obligatorio si la CategoriaFiscal es RESPONSABLE_INSCRIPTO (Validación en GestorHuespedes).
INSERT INTO huesped (
    tipo_documento, numero_documento, apellido, nombres, categoria_fiscal, fecha_nacimiento, telefono, ocupacion, nacionalidad,
    calle, numero, codigo_postal, localidad, provincia, email, cuit
)
VALUES (
    'DNI', '30000000', 'GONZALEZ', 'CARLOS', 'RESPONSABLE_INSCRIPTO', '1978-07-01', '5491133334444', 'Gerente', 'Chilena',
    'Junin', '880', '1425', 'Buenos Aires', 'Buenos Aires', 'carlos.g@test.com', '2030000001'
);