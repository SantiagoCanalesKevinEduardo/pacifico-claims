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
- **Docker & Docker Compose**: Contenerización y despliegue local de la solución.

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

## 📡 Ejemplos de Consumo de los Endpoints (cURL)

### 1. Autenticación (Obtener Token JWT)
El sistema requiere autenticación. Debes obtener un token JWT primero.
El usuario de pruebas configurado en las migraciones de BD es:
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

> **Nota:** En todas las peticiones posteriores a los endpoints `/claims/**`, se debe añadir la cabecera `Authorization: Bearer <TOKEN_JWT>`.

---

### 2. Crear un Siniestro (POST)
**Request:**
```bash
curl -X POST http://localhost:8080/claims \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_JWT>" \
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
Reemplaza `<CLAIM_ID>` por el ID UUID del siniestro devuelto en la creación.

**Request:**
```bash
curl -X GET http://localhost:8080/claims/<CLAIM_ID> \
  -H "Authorization: Bearer <TOKEN_JWT>"
```

---

### 4. Obtener todos los Siniestros (GET)
**Request:**
```bash
curl -X GET http://localhost:8080/claims \
  -H "Authorization: Bearer <TOKEN_JWT>"
```

---

### 5. Actualizar un Siniestro (PUT)
Reemplaza `<CLAIM_ID>` por el ID del siniestro que deseas actualizar.

**Request:**
```bash
curl -X PUT http://localhost:8080/claims/<CLAIM_ID> \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_JWT>" \
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
Reemplaza `<CLAIM_ID>` por el ID del siniestro que deseas eliminar.

**Request:**
```bash
curl -X DELETE http://localhost:8080/claims/<CLAIM_ID> \
  -H "Authorization: Bearer <TOKEN_JWT>"
```