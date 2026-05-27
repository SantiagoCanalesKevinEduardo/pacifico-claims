# Pacífico Seguros - Claims Service (Gestión de Siniestros)

Este repositorio contiene un microservicio reactivo y no bloqueante para la gestión de siniestros de seguros (Claims), construido bajo los principios de la **Arquitectura Hexagonal (Ports & Adapters)** y utilizando programación funcional de extremo a extremo.

---

## 🛠️ Stack Tecnológico

- **Java 21**: Uso de `records`, `sealed classes` y APIs funcionales.
- **Spring Boot 3.5.14**: Base del microservicio.
- **Spring WebFlux**: Reactividad no bloqueante (Routers y Handlers funcionales).
- **Spring Data R2DBC**: Persistencia reactiva a base de datos.
- **H2 Database**: Base de datos SQL en memoria con driver R2DBC.
- **Spring Security (Reactivo) + JWT**: Autenticación y autorización basada en tokens sin estado (stateless).
- **Lombok & MapStruct**: Generación de código repetitivo y mapeos de datos desacoplados.
- **JUnit 5, Mockito & StepVerifier**: Pruebas unitarias e integración reactivas.
- **JaCoCo**: Generación de reportes de cobertura de código.
- **Flyway**: Control de versiones y migraciones automatizadas para la base de datos SQL.
- **Springdoc OpenAPI / Swagger**: Autogeneración de documentación de API y entorno interactivo de pruebas en la web.
- **Maven**: Gestión de dependencias y automatización de la compilación y pruebas del ciclo de vida de desarrollo.
- **Spring Validation (Jakarta Validation)**: Validación robusta y declarativa de campos de entrada en los DTOs de petición.
- **Docker & Docker Compose**: Contenerización y despliegue local de la solución de forma aislada.

---

## 🚀 Instrucciones para Compilar y Ejecutar

### Requisitos Previos
- Docker y Docker Compose instalados en tu sistema.
- Java 21 y Maven (opcional si deseas ejecutar de forma local sin Docker, usando `mvnw`).

### Despliegue con Docker (Solución Completa)

1. Sitúate en la carpeta del servicio:
   ```bash
   cd claims-service
   ```

2. Construye la imagen de Docker y levanta el contenedor con un solo comando:
   ```bash
   docker compose up --build -d
   ```

3. El microservicio estará disponible en: `http://localhost:8080`

4. Para ver los logs del contenedor en tiempo real:
   ```bash
   docker compose logs -f
   ```

5. Para detener el servicio:
   ```bash
   docker compose down
   ```

---

## 🧪 Ejecución de Pruebas y Reporte de Cobertura

### Ejecutar los Tests
Para correr las pruebas unitarias y de integración del microservicio, ejecuta:
```bash
# Windows
.\mvnw.cmd clean test

# Linux/macOS
./mvnw clean test
```

### Reporte de Cobertura de Código (JaCoCo)
El plugin de **JaCoCo** está configurado para requerir una cobertura mínima del **70%** en la capa de servicios (`com.pacifico.claims.application.service`). 

El reporte HTML se genera automáticamente al ejecutar los tests en la ruta:
`claims-service/target/site/jacoco/index.html`

Puedes abrir este archivo en cualquier navegador para ver los detalles visuales de la cobertura.

---

## 📖 Documentación interactiva con Swagger UI

Los endpoints están completamente documentados y se pueden probar interactivamente a través de Swagger UI.

1. Inicia la aplicación y accede a:
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (o la ruta correspondiente en tu navegador).
2. Para probar los endpoints protegidos (`/claims/**`):
   - Haz una petición al endpoint `/auth/login` (con las credenciales indicadas abajo) para obtener el token JWT.
   - En Swagger UI, haz clic en el botón **Authorize** en la esquina superior derecha.
   - Pega tu token con el formato `Bearer <TU_TOKEN_JWT>` y haz clic en **Authorize**.

---

## 🔑 Datos de Prueba Pre-registrados (Seed Data)

El sistema ya viene precargado con datos de prueba configurados a través de Flyway en las migraciones de base de datos para facilitar el testeo inmediato:

### Usuarios y Permisos (para Login / Autenticación)
Existen 2 usuarios de prueba pre-cargados. Sin embargo, por reglas de seguridad, **solo el usuario con rol de analista (`analyst`) tiene permisos para interactuar con los endpoints de siniestros (`/claims/**`)**:
* **Analyst (Rol: ROLE_ANALYSIS)**:
  - **Username**: `analyst`
  - **Password**: `analyst123`
  - *Acceso*: Autorizado para todos los endpoints de Claims.
* **User (Rol: ROLE_USER)**:
  - **Username**: `user`
  - **Password**: `user123`
  - *Acceso*: No autorizado (retornará error HTTP `403 Forbidden`).

### Token JWT de Prueba Pre-generado (Validez de 1 Año)
Para facilitar el testeo inmediato de los endpoints protegidos (tanto en Swagger UI como vía cURL) sin necesidad de realizar previamente la petición de login, se ha generado el siguiente token con **vigencia de 1 año** que ya incluye el rol de analista (`ROLE_ANALYSIS`):

```text
eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw
```

### Clientes y Pólizas (para registrar Siniestros)
Para poder registrar un siniestro (Claim) exitosamente, debes utilizar un cliente y una póliza vigentes y vinculados entre sí. Usa los siguientes identificadores pre-cargados:
* **Customer ID (Cliente: Juan Perez)**: `a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d`
* **Policy ID (Póliza asociada: POL-100200)**: `11111111-2222-3333-4444-555555555555` (Póliza de salud activa)
* **Customer ID (Cliente: Maria Rodriguez)**: `f6e5d4c3-b2a1-0f9e-8d7c-6b5a4f3e2d1c`
* **Policy ID (Póliza asociada: POL-300400)**: `66666666-7777-8888-9999-000000000000` (Póliza activa)

### Siniestro de Prueba Pre-cargado
* **Claim ID**: `99999999-9999-9999-9999-999999999999` (Registrado para el cliente Juan Perez en la póliza POL-100200)

---

## 📡 Ejemplos de Consumo de los Endpoints (cURL)

### 1. Autenticación (Obtener Token JWT)
El sistema requiere autenticación. Debes obtener un token JWT primero utilizando el usuario de pruebas configurado en las migraciones de BD:
* **Username**: `analyst`
* **Password**: `analyst123`

**Request:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "analyst", "password": "analyst123"}'
```

**Response (Guardar el `token` devuelto):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

> **Nota:** En todas las peticiones posteriores a los endpoints `/claims/**`, se debe añadir la cabecera `Authorization: Bearer <TOKEN_JWT>`. Se puede usar el token de 1 año pre-generado.

---

### 2. Crear un Siniestro (POST)
**Request:**
```bash
curl -X POST http://localhost:8080/claims \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw" \
  -d '{
    "customerId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
    "policyId": "11111111-2222-3333-4444-555555555555",
    "claimType": "HEALTH",
    "description": "Consulta dental y curaciones básicas",
    "amount": 250.00
  }'
```

---

### 3. Obtener un Siniestro por ID (GET)
Reemplaza `<CLAIM_ID>` por el ID UUID del siniestro devuelto en la creación (o usa el pre-cargado `99999999-9999-9999-9999-999999999999`).

**Request:**
```bash
curl -X GET http://localhost:8080/claims/99999999-9999-9999-9999-999999999999 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw"
```

---

### 4. Obtener todos los Siniestros (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/claims \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw"
```

---

### 5. Actualizar un Siniestro (PUT)
Reemplaza `<CLAIM_ID>` por el ID del siniestro que deseas actualizar (p.ej. `99999999-9999-9999-9999-999999999999`).

**Request:**
```bash
curl -X PUT http://localhost:8080/claims/99999999-9999-9999-9999-999999999999 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw" \
  -d '{
    "customerId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
    "policyId": "11111111-2222-3333-4444-555555555555",
    "claimType": "HEALTH",
    "description": "Actualización: Consulta dental y radiografía extra",
    "amount": 350.00
  }'
```

---

### 6. Eliminar un Siniestro (DELETE)
Reemplaza `<CLAIM_ID>` por el ID del siniestro que deseas eliminar (p.ej. `99999999-9999-9999-9999-999999999999`).

**Request:**
```bash
curl -X DELETE http://localhost:8080/claims/99999999-9999-9999-9999-999999999999 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BTkFMWVNJUyJ9XSwic3ViIjoiYW5hbHlzdCIsImlhdCI6MTc3OTg2Mjc1NSwiZXhwIjoxODExMzk4NzU1fQ.nl-NsTJeiBQ9IGNzDC2JbdHt4iCIoMVrB7RdUTkoAP_yU0hDXnNHwSCjDub7kdK6anPOZiwyIEUPuL7AdfDknw"
```