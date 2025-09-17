# FeatureFlag API
[![Java Version](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.5.5-green)](https://spring.io/projects/spring-boot)

Backend REST para la **gestión dinámica de feature flags** por entorno (**DEV, STAGING, PROD**) y por cliente (`clientId`). Permite **crear**, **activar / desactivar** y **consultar** flags, además de **autenticación JWT** y **gestión básica de roles**.

> Proyecto realizado por: **Álvaro**, **Diego**, **Javi**, **Pablo**.

---

## ✨ Características

- **Feature Flags** por entorno y/o cliente, con `enabledByDefault`.
- **Autenticación** con **JWT (Bearer)** y **Spring Security**.
- **Autorización** por roles (ADMIN / USER).
- **Documentación OpenAPI/Swagger** integrada.
- **Persistencia** con **Spring Data JPA** (usamos DBMS **PostgreSQL**).
- **Manejo de errores** consistente (401/403 JSON).
- **DTOs + Mappers** para separar transporte y dominio.

---

## 🧱 Tecnologías

- Java **17**, Spring Boot **3.5.5**
- Spring Web, Spring Security, Spring Data JPA, Validation
- Base de datos PostgreSQL
- OpenAPI/Swagger (springdoc **2.8.12**)
- JWT (**io.jsonwebtoken 0.11.5**)
- Lombok
- JUnit 5, Mockito

---

## 📦 Instalación & Ejecución

### 1) Requisitos
- Java 17
- Maven 3.9+
- PostgreSQL 13+

### 2) Variables de entorno

El `application.yml` lee los parametros de conexión desde archivo ".env" (con el fin de ser más seguros con la información sensible)

```properties
spring:
  config:
    import: optional:file:.env.[properties]
  application:
    name: "featureflag"

  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```
### 3) Ejecutar

```bash
# Compilar y correr tests
mvn clean verify

# Ejecutar la app
mvn spring-boot:run
# La app levanta en http://localhost:8080
# Se puede ejecutar Swagger desde: http://localhost:8080/swagger-ui/index.html

```

---

## 🔐 Seguridad

- **Stateless JWT** (`SessionCreationPolicy.STATELESS`).
- Rutas públicas:
  - `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`, `/error`
- El resto de endpoints requieren **Bearer token** en `Authorization`.

**Manejo de errores (JSON):**

- `401 Unauthorized` → no autenticado.
- `403 Forbidden` → sin permisos suficientes.
Ejemplo de respuesta /:

```json
{
  "timestamp": "2025-09-17T10:11:12Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication is required to access this resource",
  "path": "/api/features"
}
```

**Roles definidos:** `ADMIN`, `USER`, `VIEWER`.

> En esta versión, los controladores usan `@PreAuthorize` con **ADMIN/USER**. El rol **VIEWER** existe en el modelo, pero no tiene endpoints asignados (sin acceso a `/api/features/**` por ahora).

---

## 📘 Documentación Swagger

- UI: `http://localhost:8080/swagger-ui.html`
(también disponible como `/swagger-ui/index.html`)
- OpenAPI JSON: `/v3/api-docs`

---

## 🔑 Autenticación (Auth)

### Registro

`POST /api/auth/register`

Request

```json
{
  "username": "Prueba",
  "password": "Prueba1!"
}
```

> La contraseña debe tener 6–16 caracteres, con minúscula, mayúscula, número y carácter especial.
Si `username` = "admin", se asigna rol ADMIN automáticamente (regla de negocio).

Response `201`
```json
{
  "username": "Prueba",
  "role": "USER"
}
```

---

### Login

`POST /api/auth/login`

Request
```json
{
  "username": "Prueba",
  "password": "Prueba1!"
}

```

Response `200`

```json
{
"accessToken": "<JWT>",
"tokenType": "Bearer",
"username": "Prueba",
"role": "USER",
"expiresAt": 1737225600000
}
```

**Uso del token:**
`/Authorization: Bearer <JWT>`


---

## 🔁 Endpoints Disponibles

| MÉTODO HTTP | ENDPOINT | DESCRIPCIÓN |
|:-----------:|:--- |:--- |
|   `POST`    | `/api/auth/register` | Registro de usuario |
|   `POST`    | `/api/auth/login` | Login y JWT |
|   `POST`    | `/api/features` | Crear nueva feature |
|   `GET`    | `/api/features` | Listar features |
|   `GET`    | `/api/features/{id}` | Detalle de feature |
|   `POST`    | `/api/features/{id}/enable` | Activar feature para cliente/entorno |
|   `POST`    | `/api/features/{id}/disable` | Desactivar feature para cliente/entorno |
|   `GET`    | `/api/features/check` | Verificar si una feature está activa |

---

## 🧪 Tests
```bash
mvn -q -DskipTests=false test
```

Incluye:
- `spring-boot-starter-test`
- `spring-security-test`
- `junit-jupiter-api 5.13.4`
- `mockito-core 5.19.0`

---

## 🗂️ Estructura (resumen)
```textplain
com.bytescolab.featureflag
├─ config/                   SwaggerConfig.java
├─ controller/               (AuthController, FeatureController, UserController)
├─ dto/ 
    ├─ feature/
    ├─ auth/
├─ mapper/                   (FeatureMapper, UserMapper)
├─ model/
    ├─ entity/               (User, Feature, FeatureConfig)
    ├─ enums/                (Role, Environment)
├─ repository/               (FeatureRepository, FeatureConfigRepository, UserRepository)
├─ security/                 (config, jwt, handlers, auth)
└─ service/                  (auth, feature, user)

```
---

## 👥 Autores

Made with 💓 by:

- [Álvaro Barba](https://github.com/Asintomatic)
- [Diego Guaman](https://github.com/diegoguaman)
- [Javi Sánchez](https://github.com/jsbrb)
- [Pablo Rodríguez](https://github.com/pablins)

---

## 🧩 Notas y futuras mejoras
- Asignar permisos de **VIEWER** (solo lectura) en controladores.
- Semántica de errores homogénea también para `IllegalArgumentException` y `RuntimeException`.
- Semillas de datos (admin inicial) y Docker Compose (PostgreSQL + app).

## 🤝 Contribuciones

¡Son bienvenidas! Si tienes ideas para mejorar este proyecto, por favor crea un "**Pull Request**" o abre un "**issue**".

## Licencia 🖨

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.