# Evidencia GA7-220501096-AA4-EV03
## Componente front-end del proyecto formativo

### 1. Identificación
- Proyecto: Sistema web de gestión de tienda de celulares.
- Tecnologías: HTML5, CSS3, JavaScript, Java 17, Spring Boot 3.3.5, Maven y MariaDB.
- Arquitectura: aplicación web con interfaz estática servida por Spring Boot y comunicación con servicios REST.

### 2. Objetivo del componente front-end
Implementar una interfaz web que permita al usuario interactuar con los módulos de clientes, productos y ventas. La interfaz consume los servicios REST del backend y presenta los registros en tarjetas y formularios.

### 3. Componentes implementados
- **Inicio (`index.html`):** presenta el sistema y permite navegar a los módulos.
- **Clientes (`clientes.html`):** formulario y listado para crear, consultar, actualizar y eliminar clientes.
- **Productos (`productos.html`):** formulario y listado para administrar el catálogo y las existencias.
- **Ventas (`ventas.html`):** formulario y listado para registrar y administrar las ventas.
- **Estilos (`style.css`):** define colores, tipografía, tarjetas, formularios, botones y adaptación a pantallas pequeñas.
- **Lógica (`script.js`):** realiza solicitudes HTTP con `fetch`, procesa respuestas JSON, muestra mensajes y actualiza las vistas.

### 4. Integración con el backend
La interfaz consume las rutas REST bajo `/api`: `/api/clientes`, `/api/productos` y `/api/ventas`. Se utilizan los métodos HTTP GET, POST, PUT y DELETE según la operación. La función común `solicitud` centraliza el envío de peticiones y el tratamiento de errores. Los datos de texto recibidos se escapan antes de construir las tarjetas HTML.

### 5. Estándares y mantenibilidad
- HTML con idioma español, codificación UTF-8 y etiqueta `viewport` para adaptación a dispositivos.
- CSS compartido para evitar duplicar reglas visuales.
- JavaScript organizado por módulos funcionales y nombres descriptivos.
- Comentarios en los archivos para identificar su propósito y explicar las operaciones principales.
- Uso de `textContent` para presentar mensajes y escape de caracteres al insertar valores de texto en tarjetas.

### 6. Ejecución
1. Instalar JDK 17 o superior, Maven y MariaDB.
2. Crear la base de datos ejecutando `tienda_celulares.sql`.
3. Configurar `DB_URL`, `DB_USER` y `DB_PASSWORD` si la instalación local lo requiere.
4. Abrir una terminal en la carpeta que contiene `pom.xml`.
5. Ejecutar `mvn clean package` y luego `mvn spring-boot:run`.
6. Abrir `http://localhost:8080` en el navegador.

### 7. Versionamiento
El código fuente se encuentra organizado en un repositorio Git. La URL remota del proyecto se incluye en el archivo `ENLACE_REPOSITORIO.txt` ubicado en la raíz de la entrega.

### 8. Relación con artefactos anteriores
Se conserva la carpeta `ARTEFACTOS_TECNICOS`, que contiene el diagrama de clases, el diagrama de casos de uso y la descripción de trazabilidad elaborados previamente. Estos documentos sirven como referencia para los módulos representados en la interfaz.
