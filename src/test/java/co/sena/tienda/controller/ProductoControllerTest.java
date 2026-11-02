package co.sena.tienda.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @Test
    void debeRechazarProductoSinModelo() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        ProductoController controller = new ProductoController(jdbc);

        Map<String, Object> datos = Map.of(
            "marca", "Samsung",
            "almacenamiento", "256GB",
            "color", "Negro",
            "precio", "1500000",
            "stock", "10"
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> controller.crear(datos)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(jdbc);
    }
}