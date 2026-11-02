package co.sena.tienda.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Test
    void debeRechazarClienteSinNombre() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        ClienteController controller = new ClienteController(jdbc);

        Map<String, Object> datos = Map.of(
            "nombre", "",
            "telefono", "3001234567",
            "correo", "prueba@gmail.com",
            "direccion", "Bogota"
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> controller.crear(datos)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verifyNoInteractions(jdbc);
    }
}
