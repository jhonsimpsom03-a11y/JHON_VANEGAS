# Diagrama de casos de uso

```mermaid
flowchart LR
    A[Administrador / Usuario de la tienda]

    subgraph Sistema["Sistema web de gestión de tienda de celulares"]
      UC1((Consultar clientes))
      UC2((Registrar cliente))
      UC3((Actualizar cliente))
      UC4((Eliminar cliente))
      UC5((Buscar clientes))
      UC6((Consultar productos))
      UC7((Registrar producto))
      UC8((Actualizar producto))
      UC9((Eliminar producto))
      UC10((Consultar ventas))
      UC11((Registrar venta))
      UC12((Actualizar venta))
      UC13((Eliminar venta))
    end

    A --- UC1
    A --- UC2
    A --- UC3
    A --- UC4
    A --- UC5
    A --- UC6
    A --- UC7
    A --- UC8
    A --- UC9
    A --- UC10
    A --- UC11
    A --- UC12
    A --- UC13
```

## Descripción de los casos de uso

| Caso de uso | Descripción | Componente |
|---|---|---|
| Consultar clientes | Muestra los clientes registrados. | `ClienteController` / `ClienteServlet` |
| Registrar cliente | Guarda un cliente después de validar los campos obligatorios. | `ClienteController` / `ClienteServlet` |
| Actualizar cliente | Modifica los datos de un cliente existente. | `ClienteController` |
| Eliminar cliente | Elimina un cliente si no tiene ventas asociadas. | `ClienteController` |
| Buscar clientes | Filtra por nombre o correo. | `ClienteServlet` |
| Consultar productos | Lista los productos disponibles. | `ProductoController` |
| Registrar producto | Crea un producto validando precio y stock. | `ProductoController` |
| Actualizar producto | Modifica la información de un producto. | `ProductoController` |
| Eliminar producto | Elimina un producto si no está relacionado con detalles de ventas. | `ProductoController` |
| Consultar ventas | Lista las ventas con información del cliente. | `VentaController` |
| Registrar venta | Registra una venta asociada a un cliente existente. | `VentaController` |
| Actualizar venta | Modifica los datos generales de una venta. | `VentaController` |
| Eliminar venta | Elimina la venta y sus detalles asociados. | `VentaController` |

## Actor

**Administrador / Usuario de la tienda:** interactúa con el sistema para administrar clientes, productos y ventas. El proyecto no implementa autenticación ni roles en el alcance documentado.
