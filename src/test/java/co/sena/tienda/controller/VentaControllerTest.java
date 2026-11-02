package co.sena.tienda.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaControllerTest {

    @Test
    void debeRechazarVentaConFechaInvalida() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        VentaController controller = new VentaController(jdbc);

        Map<String, Object> datos = Map.of(
            "id_cliente", "4",
            "fecha", "fecha-invalida",
            "total", "2500000"
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> controller.crear(datos)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(jdbc);
    }
}