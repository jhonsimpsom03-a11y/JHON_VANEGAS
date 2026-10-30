# Evidencia GA7-220501096-AA5-EV01
## Diseño y desarrollo de servicios web – caso de autenticación

### Objetivo
Implementar un servicio web REST para el registro de usuarios y la autenticación mediante usuario y contraseña.

### Diseño
La API expone dos operaciones:
- `POST /api/auth/register`: valida los datos, comprueba que el usuario no exista y almacena el hash de la contraseña.
- `POST /api/auth/login`: consulta el usuario y compara la contraseña recibida con el hash almacenado.

### Seguridad
Se utiliza BCrypt (`BCryptPasswordEncoder`) para generar y verificar hashes de contraseñas. Las consultas SQL usan parámetros para evitar concatenar directamente los datos recibidos.

### Respuestas
- Registro exitoso: HTTP 201.
- Autenticación exitosa: HTTP 200, `Autenticación satisfactoria`.
- Credenciales incorrectas: HTTP 401, `Error en la autenticación`.
- Usuario duplicado: HTTP 409.

### Herramientas
Java 17, Spring Boot 3.3.5, Spring Web, Spring JDBC, MariaDB, Maven y Git/GitHub.

### Puesta en marcha
1. Configurar las variables de conexión a MariaDB indicadas en `README.md`.
2. Ejecutar el bloque de creación de `usuarios_app` incluido al final de `tienda_celulares.sql`.
3. Ejecutar `mvn clean package` y luego `mvn spring-boot:run`.
4. Probar los endpoints con las solicitudes PowerShell documentadas en `README.md`.
