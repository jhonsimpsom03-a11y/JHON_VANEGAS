# Sistema web de gestión de tienda de celulares

Proyecto desarrollado con **Java 17**, **Spring Boot 3.3.5**, **Maven**, **Spring JDBC (`JdbcTemplate`)** y **MariaDB**. Incluye una interfaz web HTML/CSS/JavaScript, servicios REST para clientes, productos y ventas, y un módulo adicional de Servlet/JSP para clientes.

## Requisitos
- JDK 17 o superior.
- Maven 3.6 o superior.
- MariaDB/MySQL en ejecución.

## Preparar la base de datos
1. Abre MariaDB o phpMyAdmin.
2. Ejecuta el archivo `tienda_celulares.sql`. El script crea la base de datos y sus tablas.
3. El script de entrega contiene la estructura, sin registros personales de ejemplo.

La aplicación usa por defecto `root` sin contraseña en `localhost`. Si tu instalación tiene otra configuración, define las variables en PowerShell:

```powershell
$env:DB_URL="jdbc:mariadb://localhost:3306/tienda_celulares"
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE"
```

No incluyas contraseñas reales en archivos del proyecto ni en GitHub.

## Compilar y ejecutar
Desde PowerShell, ubicado en la carpeta que contiene `pom.xml`:

```powershell
mvn clean package
mvn spring-boot:run
```

Abre `http://localhost:8080`.

## Módulos y rutas

### API REST
- `/api/clientes`: GET, POST, PUT y DELETE.
- `/api/productos`: GET, POST, PUT y DELETE.
- `/api/ventas`: GET, POST, PUT y DELETE.

### Servlet y JSP
- `GET /clientes-servlet`: consulta los clientes.
- `GET /clientes-servlet?buscar=texto`: busca por nombre o correo.
- `POST /clientes-servlet`: registra un cliente.
- Vista JSP: `src/main/webapp/WEB-INF/jsp/clientes.jsp`.

## Organización
- `src/main/java`: clases Java, controladores REST y Servlet.
- `src/main/resources/static`: interfaz web.
- `src/main/webapp/WEB-INF/jsp`: vista JSP.
- `ARTEFACTOS_TECNICOS`: diagramas y trazabilidad del sistema.
- `INFORME_TECNICO_AA3_EV01.md`: descripción del módulo y validación de la evidencia.

## Versionamiento
El repositorio remoto del proyecto está indicado en el archivo `REPOSITORIO.txt`, ubicado en la raíz de la entrega.


## Servicio web de autenticación (AA5-EV01)

Se incorporó una API REST para registrar usuarios e iniciar sesión.

### Preparar la tabla de autenticación
Ejecuta nuevamente el bloque `CREATE TABLE IF NOT EXISTS usuarios_app` incluido al final de `tienda_celulares.sql`, o ejecuta ese bloque directamente en MariaDB. No elimina ni modifica las tablas existentes.

### Endpoints
- `POST /api/auth/register`: registra un usuario.
- `POST /api/auth/login`: verifica las credenciales.

Ambos endpoints reciben JSON con esta estructura:

```json
{
  "usuario": "aprendiz01",
  "contraseña": "clave123"
}
```

### Respuestas esperadas
- Registro correcto: HTTP 201 y mensaje `Usuario registrado correctamente`.
- Inicio de sesión correcto: HTTP 200 y mensaje `Autenticación satisfactoria`.
- Credenciales incorrectas: HTTP 401 y mensaje `Error en la autenticación`.
- Usuario repetido: HTTP 409.

Las contraseñas se almacenan como hash BCrypt mediante `spring-security-crypto`; nunca se guardan en texto plano.

### Pruebas con PowerShell
Con la aplicación ejecutándose en `http://localhost:8080`:

```powershell
$registro = @{
    usuario = "aprendiz01"
    contraseña = "clave123"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/register" -ContentType "application/json" -Body $registro

$login = @{
    usuario = "aprendiz01"
    contraseña = "clave123"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $login
```

Para comprobar el rechazo de una contraseña incorrecta, cambia `clave123` por otra contraseña en la solicitud de inicio de sesión; la API debe responder HTTP 401.

