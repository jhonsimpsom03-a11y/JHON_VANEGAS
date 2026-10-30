package co.sena.tienda.controller;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Expone los servicios REST para consultar y administrar clientes.
 * Utiliza JdbcTemplate para ejecutar consultas parametrizadas sobre MariaDB.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final JdbcTemplate jdbc;

    /** Recibe por inyección la herramienta de acceso a datos. */
    public ClienteController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /** Retorna los registros disponibles del recurso. */
    @GetMapping
    public List<Map<String, Object>> listar() {
        return jdbc.queryForList("SELECT id_cliente, nombre, telefono, correo, direccion FROM clientes ORDER BY id_cliente");
    }

    /** Valida y registra un nuevo elemento. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> crear(@RequestBody Map<String, Object> datos) {
        validar(datos);
        jdbc.update("INSERT INTO clientes (nombre, telefono, correo, direccion) VALUES (?, ?, ?, ?)",
            texto(datos, "nombre"), texto(datos, "telefono"), texto(datos, "correo"), texto(datos, "direccion"));
        return jdbc.queryForMap("SELECT * FROM clientes WHERE id_cliente = LAST_INSERT_ID()");
    }

    /** Actualiza un elemento existente identificado por su ID. */
    @PutMapping("/{id}")
    public Map<String, Object> actualizar(@PathVariable int id, @RequestBody Map<String, Object> datos) {
        validar(datos);
        int filas = jdbc.update("UPDATE clientes SET nombre=?, telefono=?, correo=?, direccion=? WHERE id_cliente=?",
            texto(datos, "nombre"), texto(datos, "telefono"), texto(datos, "correo"), texto(datos, "direccion"), id);
        if (filas == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        return jdbc.queryForMap("SELECT * FROM clientes WHERE id_cliente=?", id);
    }

    /** Elimina el elemento indicado y controla las restricciones relacionadas. */
    @DeleteMapping("/{id}")
    public Map<String, String> eliminar(@PathVariable int id) {
        try {
            int filas = jdbc.update("DELETE FROM clientes WHERE id_cliente=?", id);
            if (filas == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
            return Map.of("mensaje", "Cliente eliminado");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "No se puede eliminar el cliente porque tiene ventas asociadas.");
        }
    }

    private void validar(Map<String, Object> d) {
        for (String campo : List.of("nombre", "telefono", "correo", "direccion")) {
            if (d.get(campo) == null || d.get(campo).toString().isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo " + campo + " es obligatorio");
        }
    }
    private String texto(Map<String, Object> d, String k) { return d.get(k).toString().trim(); }
}
