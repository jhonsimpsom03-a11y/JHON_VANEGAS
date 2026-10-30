# Diagrama de clases y modelo de datos

## Componentes de la aplicación

```mermaid
classDiagram
    class TiendaCelularesApplication {
      +main(String[] args)
    }

    class ClienteController {
      -JdbcTemplate jdbc
      +listar()
      +crear(datos)
      +actualizar(id, datos)
      +eliminar(id)
      -validar(datos)
    }

    class ProductoController {
      -JdbcTemplate jdbc
      +listar()
      +crear(datos)
      +actualizar(id, datos)
      +eliminar(id)
      -validar(datos)
    }

    class VentaController {
      -JdbcTemplate jdbc
      +listar()
      +crear(datos)
      +actualizar(id, datos)
      +eliminar(id)
      -entero(datos, campo)
    }

    class ClienteServlet {
      -JdbcTemplate jdbc
      +init()
      +doGet(request, response)
      +doPost(request, response)
      -limpiar(valor)
    }

    class JdbcTemplate {
      +queryForList(sql, parametros)
      +queryForMap(sql, parametros)
      +update(sql, parametros)
    }

    class clientes {
      +int id_cliente
      +varchar nombre
      +varchar telefono
      +varchar correo
      +varchar direccion
    }

    class productos {
      +int ID
      +varchar MODELO
      +varchar MARCA
      +varchar ALMACENAMIENTO
      +varchar COLOR
      +decimal PRECIO
      +int STOCK
    }

    class ventas {
      +int id_venta
      +int id_cliente
      +date fecha
      +decimal total
    }

    class detalle_ventas {
      +int id_detalle
      +int id_venta
      +int id_producto
      +int cantidad
    }

    TiendaCelularesApplication ..> ClienteController : inicia contexto
    TiendaCelularesApplication ..> ProductoController : inicia contexto
    TiendaCelularesApplication ..> VentaController : inicia contexto
    TiendaCelularesApplication ..> ClienteServlet : registra Servlet

    ClienteController --> JdbcTemplate : consulta
    ProductoController --> JdbcTemplate : consulta
    VentaController --> JdbcTemplate : consulta
    ClienteServlet --> JdbcTemplate : consulta

    JdbcTemplate ..> clientes : SQL
    JdbcTemplate ..> productos : SQL
    JdbcTemplate ..> ventas : SQL
    JdbcTemplate ..> detalle_ventas : SQL

    clientes "1" --> "0..*" ventas : registra
    ventas "1" --> "1..*" detalle_ventas : contiene
    productos "1" --> "0..*" detalle_ventas : aparece en
```

## Explicación

- `TiendaCelularesApplication` es el punto de entrada de Spring Boot.
- `ClienteController`, `ProductoController` y `VentaController` exponen operaciones REST para gestionar la información.
- `ClienteServlet` ofrece una segunda interfaz web para consultar, filtrar y registrar clientes mediante una vista JSP.
- `JdbcTemplate` centraliza la comunicación con MariaDB mediante consultas parametrizadas.
- En el modelo relacional, un cliente puede tener varias ventas; una venta puede tener varios detalles; y un producto puede aparecer en diferentes detalles de venta.
