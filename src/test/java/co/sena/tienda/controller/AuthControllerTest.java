package co.sena.tienda.controller;

import co.sena.tienda.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class AuthControllerTest {

    @Test
    void debeRegistrarUsuarioConContrasenaEncriptada() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthController controller = new AuthController(jdbc, encoder);

        AuthRequest datos = new AuthRequest("nuevo", "Clave123");

        when(jdbc.queryForObject(anyString(), eq(Integer.class), eq("nuevo")))
            .thenReturn(0);

        when(encoder.encode("Clave123")).thenReturn("hash_seguro");

        Map<String, Object> respuesta = controller.registrar(datos);

        assertEquals("Usuario registrado correctamente", respuesta.get("mensaje"));
        assertEquals("nuevo", respuesta.get("usuario"));

        verify(jdbc).update(anyString(), eq("nuevo"), eq("hash_seguro"));
        verify(encoder).encode("Clave123");
    }

    @Test
    void debeRechazarInicioSesionDeUsuarioInexistente() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthController controller = new AuthController(jdbc, encoder);

        AuthRequest datos = new AuthRequest("fantasma", "Clave123");

        when(jdbc.query(
            anyString(),
            any(RowMapper.class),
            eq("fantasma")
        )).thenReturn(List.of());

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> controller.iniciarSesion(datos)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
        verify(encoder, never()).matches(anyString(), anyString());
    }
}