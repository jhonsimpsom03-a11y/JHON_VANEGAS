# Informe técnico — GA7-220501096-AA3-EV01

## 1. Identificación del proyecto
**Proyecto:** Sistema web de gestión de una tienda de celulares.  
**Módulo desarrollado:** gestión web de clientes, productos y ventas, con servicios REST y un módulo de clientes implementado mediante Servlet y JSP.

## 2. Objetivo
Implementar y documentar un módulo funcional del proyecto de software seleccionado, aplicando un framework de desarrollo, buenas prácticas de organización del código y control de versiones.

## 3. Tecnologías y herramientas
- Java 17.
- Spring Boot 3.3.5.
- Maven para dependencias y construcción.
- Spring JDBC (`JdbcTemplate`) para acceso a datos.
- MariaDB como motor de base de datos.
- HTML, CSS y JavaScript para la interfaz.
- Jakarta Servlet y JSP para el módulo web de clientes.
- Git y GitHub para el versionamiento del proyecto.

## 4. Arquitectura y funcionamiento
La aplicación se ejecuta sobre Spring Boot. La interfaz web consume los endpoints REST expuestos por los controladores `ClienteController`, `ProductoController` y `VentaController`. Estos componentes validan los datos recibidos y realizan operaciones sobre MariaDB mediante `JdbcTemplate` y consultas parametrizadas.

Como complemento, `ClienteServlet` procesa solicitudes HTTP GET y POST. El método GET consulta o filtra clientes y entrega los resultados a `clientes.jsp`; el método POST valida los campos obligatorios, registra el cliente y redirige a la vista.

## 5. Funcionalidades implementadas
- Consultar, registrar, actualizar y eliminar clientes.
- Consultar, registrar, actualizar y eliminar productos.
- Consultar, registrar, actualizar y eliminar ventas.
- Buscar clientes por nombre o correo desde el Servlet.
- Registrar clientes mediante formulario procesado con POST.
- Validar campos y relaciones básicas, incluyendo la existencia del cliente al crear o actualizar una venta.
- Mantener la persistencia de la información en MariaDB.

## 6. Organización y buenas prácticas
El código se organiza por paquetes: controladores REST y Servlet. La configuración de conexión permite utilizar variables de entorno (`DB_URL`, `DB_USER` y `DB_PASSWORD`) para evitar incluir credenciales locales en el código. Las operaciones SQL reciben parámetros mediante `JdbcTemplate`. Se agregaron comentarios JavaDoc breves en las clases y operaciones principales para facilitar su comprensión.

## 7. Artefactos técnicos
La carpeta `ARTEFACTOS_TECNICOS` contiene:
- Diagrama de clases y modelo de datos.
- Diagrama de casos de uso.
- Descripción de arquitectura, funcionalidades y matriz de trazabilidad.

Los diagramas se elaboraron a partir de los componentes y tablas presentes en el proyecto.

## 8. Pruebas y validación
De acuerdo con las comprobaciones realizadas en el entorno local del aprendiz:

| Prueba | Resultado |
|---|---|
| Ejecución de `mvn clean package` | Correcta; se obtuvo `BUILD SUCCESS`. |
| Inicio de la aplicación con `mvn spring-boot:run` | Correcto. |
| Acceso a `http://localhost:8080` | Correcto. |
| Comprobación funcional de los módulos de clientes, productos y ventas | El aprendiz confirmó su funcionamiento. |

Estas comprobaciones fueron manuales en el entorno local. No se afirma la ejecución de una suite automatizada de pruebas. La conexión y las operaciones dependen de que MariaDB esté iniciado y configurado correctamente.

## 9. Control de versiones y entrega
El proyecto utiliza Git y cuenta con un repositorio remoto de GitHub. La dirección se incluye en `REPOSITORIO.txt`. La carpeta entregada contiene el código fuente, configuración, script de base de datos, artefactos técnicos e informe de esta evidencia.

## 10. Conclusión
Se dispone de un módulo web implementado con Spring Boot y tecnologías Java, conectado a MariaDB y organizado para la gestión de clientes, productos y ventas. La compilación y las comprobaciones funcionales reportadas en el entorno local permiten presentar el proyecto como desarrollo de la evidencia GA7-220501096-AA3-EV01.
