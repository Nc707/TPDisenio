# TPDisenio
Repositorio para el TP de diseño del grupo 15 - InBugWeThrust - 2025

## Descripción
Aplicación Spring Boot con arquitectura en capas para la gestión de entidades, desarrollada como parte del Trabajo Práctico de Diseño de Sistemas 2025.

## Tecnologías Utilizadas
- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**

---

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

1. **Java Development Kit (JDK) 21**
   - Verifica la instalación con: `java -version`
   
2. **Maven** (opcional, el proyecto incluye Maven Wrapper)
   - Verifica la instalación con: `mvn -v`

3. **PostgreSQL**
   - Versión recomendada: 12 o superior
   - Debe estar ejecutándose en `localhost:5432`

4. **Postman** (para pruebas de API)
   - Descarga desde: https://www.postman.com/downloads/

---

## Configuración de la Base de Datos

### 1. Instalar PostgreSQL
Si no tienes PostgreSQL instalado, descárgalo desde: https://www.postgresql.org/download/
### 2. Crear la Base de Datos
Una vez instalado PostgreSQL, abre una terminal de SQL (pgAdmin o psql) y ejecuta:

```sql
CREATE DATABASE "premier-suite-db";
```

### 3. Configurar Credenciales
El proyecto está configurado para usar las siguientes credenciales por defecto:
- **Usuario**: `postgres`
- **Contraseña**: `postgres`
- **Puerto**: `5432`
- **Base de datos**: `premier-suite-db`

Si tus credenciales son diferentes, modifica el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/premier-suite-db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

---

## Pasos para Ejecutar el Proyecto

### Opción 1: Usando Maven Wrapper (Recomendado)

En Windows PowerShell, desde la raíz del proyecto:

```powershell
# Compilar el proyecto
.\mvnw.cmd clean install

# Ejecutar la aplicación
.\mvnw.cmd spring-boot:run
```

### Opción 2: Usando Maven Instalado

```powershell
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Opción 3: Ejecutar el JAR generado

```powershell
# Compilar el proyecto
.\mvnw.cmd clean package

# Ejecutar el JAR
java -jar target/suite-0.0.1-SNAPSHOT.jar
```

### Verificar que la Aplicación Está Corriendo

La aplicación estará disponible en: **http://localhost:80**

Deberías ver en la consola mensajes indicando:
```
Started SuiteApplication in X.XXX seconds
```

---

## Pruebas con Postman

### Configuración Inicial de Postman

1. Abre Postman
2. Crea una nueva colección llamada "TPDisenio - Premier Suite"
3. La URL base para todas las peticiones es: `http://localhost:80`

### Endpoint 1: Crear un Ejemplo (POST)

**Descripción**: Guarda una nueva entidad en la base de datos.

- **Método**: `POST`
- **URL**: `http://localhost:80/examples/add`
- **Headers**:
  - `Content-Type`: `application/json`
- **Body** (raw JSON):
```json
{
  "number": 42,
  "anotherNumber": 100
}
```

**Respuesta Exitosa** (Status 200):
```json
1
```
*El número devuelto es el ID generado para la entidad.*

**Respuesta de Error** (Status 500):
- Se produce si hay algún error al guardar en la base de datos.

---

### Endpoint 2: Obtener un Ejemplo (GET)

**Descripción**: Recupera una entidad existente por su ID.

- **Método**: `GET`
- **URL**: `http://localhost:80/examples/get?id=1`
- **Params**:
  - `id`: `1` (el ID de la entidad que quieres recuperar)

**Respuesta Exitosa** (Status 200):
```json
{
  "number": 42,
  "anotherNumber": 100
}
```

**Respuesta de Error**:
- **Status 404**: No se encontró una entidad con ese ID
- **Status 500**: Error interno del servidor

---

### Flujo de Prueba Completo

1. **Crear varias entidades**:
   - Envía 3-4 peticiones POST con diferentes valores
   - Anota los IDs devueltos

2. **Recuperar las entidades**:
   - Usa los IDs obtenidos para hacer peticiones GET
   - Verifica que los datos coincidan

3. **Probar casos de error**:
   - Intenta obtener una entidad con un ID inexistente (ej: 999)
   - Envía un POST con JSON mal formado

---

## Arquitectura del Sistema

### Arquitectura de Capas (Layered Architecture)

El proyecto sigue una **arquitectura en capas** típica de Spring Boot, separando responsabilidades:

```
┌─────────────────────────────────────┐
│     Capa de Presentación            │
│  (Controllers - REST API)           │
│  - ExampleDTOController             │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│     Capa de Negocio                 │
│  (Services - Lógica de Negocio)     │
│  - ExampleEntityService             │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│     Capa de Persistencia            │
│  (Repositories - Acceso a Datos)    │
│  - ExampleEntityRepository          │
└─────────────┬───────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│     Base de Datos                   │
│  PostgreSQL                         │
└─────────────────────────────────────┘
```

### Componentes Principales

#### 1. **Controllers** (`controllers/`)
- **Responsabilidad**: Manejar las peticiones HTTP (REST API)
- **Ejemplo**: `ExampleDTOController`
  - Define los endpoints REST (`/examples/add`, `/examples/get`)
  - Valida peticiones y respuestas
  - Maneja errores HTTP (404, 500)
  - Delega la lógica de negocio a los Services

#### 2. **Services** (`services/`)
- **Responsabilidad**: Contener la lógica de negocio
- **Ejemplo**: `ExampleEntityService`
  - Transforma DTOs en Entidades y viceversa
  - Coordina operaciones con múltiples repositorios
  - Implementa reglas de negocio
  - Maneja excepciones de dominio

#### 3. **Repositories** (`repositories/`)
- **Responsabilidad**: Acceso a la base de datos
- **Ejemplo**: `ExampleEntityRepository`
  - Extiende `JpaRepository` de Spring Data
  - Proporciona operaciones CRUD automáticas
  - Permite consultas personalizadas

#### 4. **Model/Entities** (`model/`)
- **Responsabilidad**: Representar tablas de la base de datos
- **Ejemplo**: `ExampleEntity`
  - Anotaciones JPA (`@Entity`, `@Id`, `@Column`)
  - Mapea campos a columnas de la base de datos
  - Define relaciones entre entidades

#### 5. **DTOs** (`dto/`)
- **Responsabilidad**: Transferencia de datos entre capas
- **Ejemplo**: `ExampleDTO`
  - Desacopla la API de la estructura de la base de datos
  - Controla qué datos se exponen externamente
  - Facilita validaciones y transformaciones

### Flujo de una Petición

```
1. Cliente (Postman) → POST /examples/add
                        ↓
2. ExampleDTOController.addExample()
   - Recibe el JSON y lo convierte a ExampleDTO
                        ↓
3. ExampleEntityService.saveExampleEntity()
   - Convierte DTO → Entity
   - Aplica lógica de negocio
                        ↓
4. ExampleEntityRepository.save()
   - Persiste en PostgreSQL
                        ↓
5. Retorna ID generado
                        ↓
6. Respuesta al Cliente (Status 200 + ID)
```

### Principios de Diseño Aplicados

- **Separación de Responsabilidades**: Cada capa tiene un propósito específico
- **Inversión de Dependencias**: Los controladores dependen de interfaces, no implementaciones
- **DTOs para Desacoplamiento**: La capa de presentación no expone directamente las entidades
- **Repository Pattern**: Abstracción del acceso a datos con Spring Data JPA

### Arquitectura Recomendada para Escalabilidad

A medida que el proyecto crezca, se recomienda:

1. **Agregar Capa de Validación**:
   - Usar `@Valid` y `@Validated` en Controllers
   - Crear validadores personalizados

2. **Implementar Manejo de Excepciones Global**:
   - `@ControllerAdvice` para capturar excepciones
   - Respuestas de error estandarizadas

3. **Añadir Capa de Seguridad**:
   - Spring Security para autenticación/autorización
   - JWT para APIs RESTful

4. **Implementar Pruebas**:
   - Unit tests para Services
   - Integration tests para Controllers
   - Repository tests con base de datos en memoria (H2)

5. **Documentación de API**:
   - Integrar Swagger/OpenAPI
   - Generar documentación automática de endpoints

---

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/edu/inbugwethrust/premier/suite/
│   │   ├── SuiteApplication.java          # Clase principal
│   │   ├── controllers/                   # Controladores REST
│   │   │   └── ExampleDTOController.java
│   │   ├── services/                      # Lógica de negocio
│   │   │   └── ExampleEntityService.java
│   │   ├── repositories/                  # Acceso a datos
│   │   │   └── ExampleEntityRepository.java
│   │   ├── model/                         # Entidades JPA
│   │   │   └── ExampleEntity.java
│   │   ├── dto/                           # Data Transfer Objects
│   │   │   └── ExampleDTO.java
│   │   └── exceptions/                    # Excepciones personalizadas
│   └── resources/
│       └── application.properties         # Configuración
└── test/                                  # Pruebas unitarias
```

---

## Solución de Problemas Comunes

### Error: "Connection refused" o "Could not connect to database"
- Verifica que PostgreSQL esté corriendo
- Confirma que la base de datos `premier-suite-db` exista
- Revisa las credenciales en `application.properties`

### Error: "Port 80 already in use"
- Cambia el puerto en `application.properties`:
  ```properties
  server.port=8080
  ```
- Actualiza la URL en Postman a `http://localhost:8080`

### Error: "JAVA_HOME not found"
- Configura la variable de entorno `JAVA_HOME` apuntando a tu instalación de JDK 21

### La aplicación compila pero no inicia
- Revisa los logs en la consola
- Verifica que todas las dependencias se hayan descargado correctamente
- Ejecuta `.\mvnw.cmd clean install -U` para forzar actualización de dependencias

---

## Comandos Útiles

```powershell
# Limpiar archivos compilados
.\mvnw.cmd clean

# Compilar sin ejecutar tests
.\mvnw.cmd clean install -DskipTests

# Ver dependencias del proyecto
.\mvnw.cmd dependency:tree

# Ejecutar tests
.\mvnw.cmd test
```

---

## Contacto

Grupo 15 - InBugWeThrust  
Trabajo Práctico de Diseño de Sistemas - 2025
