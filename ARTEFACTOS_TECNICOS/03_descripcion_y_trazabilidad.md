# Descripción técnica y trazabilidad del módulo

## 1. Propósito

El sistema permite gestionar la información principal de una tienda de celulares: clientes, productos y ventas. Cuenta con una interfaz web y servicios REST desarrollados en Java.

## 2. Tecnologías identificadas

- **Lenguaje:** Java 17.
- **Framework:** Spring Boot 3.3.5.
- **Gestión de dependencias y construcción:** Maven.
- **Acceso a datos:** Spring JDBC mediante `JdbcTemplate`.
- **Base de datos:** MariaDB.
- **Interfaz:** HTML, CSS y JavaScript.
- **Componente web adicional:** Servlet Jakarta y vista JSP.
- **Versionamiento:** Git y repositorio remoto GitHub.

## 3. Arquitectura general

La interfaz web envía solicitudes HTTP a los controladores REST. Los controladores validan los datos recibidos y utilizan `JdbcTemplate` para ejecutar consultas parametrizadas sobre MariaDB. Para la gestión de clientes también existe el Servlet `ClienteServlet`, que procesa solicitudes GET y POST y presenta la información mediante `clientes.jsp`.

## 4. Endpoints REST documentados en el código

| Recurso | Método HTTP | Ruta | Función |
|---|---|---|---|
| Clientes | GET | `/api/clientes` | Listar |
| Clientes | POST | `/api/clientes` | Registrar |
| Clientes | PUT | `/api/clientes/{id}` | Actualizar |
| Clientes | DELETE | `/api/clientes/{id}` | Eliminar |
| Productos | GET | `/api/productos` | Listar |
| Productos | POST | `/api/productos` | Registrar |
| Productos | PUT | `/api/productos/{id}` | Actualizar |
| Productos | DELETE | `/api/productos/{id}` | Eliminar |
| Ventas | GET | `/api/ventas` | Listar |
| Ventas | POST | `/api/ventas` | Registrar |
| Ventas | PUT | `/api/ventas/{id}` | Actualizar |
| Ventas | DELETE | `/api/ventas/{id}` | Eliminar |

El Servlet de clientes utiliza la ruta `/clientes-servlet`: GET lista o filtra clientes y POST registra un cliente.

## 5. Reglas y validaciones observadas

- Los campos de cliente (nombre, teléfono, correo y dirección) son obligatorios.
- Los campos de producto (modelo, marca, almacenamiento, color, precio y stock) son obligatorios.
- El precio y el stock del producto no pueden ser negativos.
- La fecha de venta debe tener formato de fecha válido y el total no puede ser negativo.
- Para registrar una venta, el cliente indicado debe existir.
- El sistema controla restricciones de integridad al intentar eliminar clientes o productos relacionados con ventas.
- Las consultas de escritura utilizan parámetros, evitando concatenar directamente los valores recibidos en las sentencias SQL.

## 6. Matriz de trazabilidad

| Necesidad funcional | Caso de uso | Componente de software | Persistencia |
|---|---|---|---|
| Administrar clientes | Consultar, registrar, actualizar y eliminar clientes | `ClienteController` | `clientes` |
| Buscar clientes | Buscar clientes por nombre o correo | `ClienteServlet` | `clientes` |
| Administrar productos | Consultar, registrar, actualizar y eliminar productos | `ProductoController` | `productos` |
| Administrar ventas | Consultar, registrar, actualizar y eliminar ventas | `VentaController` | `ventas`, `detalle_ventas` |
| Relacionar ventas con clientes | Registrar venta para un cliente existente | `VentaController` | FK `ventas.id_cliente` |
| Relacionar productos vendidos | Asociar productos a detalles de venta | Modelo relacional | `detalle_ventas` |

## 7. Alcance y consideraciones

Esta documentación describe los componentes presentes en el código fuente revisado. No afirma que existan funciones de autenticación, control de roles, facturación electrónica o actualización automática de inventario, ya que no forman parte de los componentes verificados para este documento.

## 8. Recomendación para la validación final

Antes de entregar, ejecutar el proyecto desde el entorno local con MariaDB disponible y comprobar al menos una operación de consulta y una operación de escritura para clientes, productos y ventas. Registrar en el informe final únicamente las pruebas que se hayan ejecutado satisfactoriamente.
