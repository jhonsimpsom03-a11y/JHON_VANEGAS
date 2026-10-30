# Artefactos técnicos – Tienda de celulares

Estos artefactos documentan el sistema web de gestión de una tienda de celulares desarrollado con Java, Spring Boot, Maven, JdbcTemplate y MariaDB.

## Contenido
- `01_diagrama_clases.md`: estructura lógica del sistema y relaciones de datos.
- `02_diagrama_casos_uso.md`: actores y funcionalidades principales.
- `03_descripcion_y_trazabilidad.md`: descripción del módulo y relación entre funcionalidades y componentes.

## Nota de alcance
Los diagramas representan la arquitectura y el modelo de datos que se observan en el proyecto entregado. La aplicación utiliza controladores REST y un Servlet para clientes; el acceso a datos se realiza mediante `JdbcTemplate`. No se representan clases entidad Java independientes porque el código actual trabaja con mapas (`Map<String, Object>`) y consultas SQL directas.
