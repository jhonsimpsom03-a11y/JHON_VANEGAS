package co.sena.tienda.controller;

import co.sena.tienda.dto.AuthRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Servicio REST para registrar cuentas e iniciar sesión.
 * Las contraseñas se comparan mediante BCrypt y no se almacenan en texto plano.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra una cuenta nueva. El nombre de usuario debe ser único.
     * Ejemplo: POST /api/auth/register
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registrar(@Valid @RequestBody AuthRequest datos) {
        String usuario = datos.usuario().trim();

        // Comprueba primero si el nombre de usuario ya está registrado.
        Integer existentes = jdbc.queryForObject(
            "SELECT COUNT(*) FROM usuarios_app WHERE usuario = ?", Integer.class, usuario);

        if (existentes != null && existentes > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "El nombre de usuario ya está registrado");
        }

        // BCrypt genera un hash seguro de la contraseña antes de guardarla.
        String hash = passwordEncoder.encode(datos.contraseña());
        jdbc.update("INSERT INTO usuarios_app (usuario, password_hash) VALUES (?, ?)",
            usuario, hash);

        return Map.of(
            "mensaje", "Usuario registrado correctamente",
            "usuario", usuario
        );
    }

    /**
     * Verifica las credenciales y responde con éxito o error de autenticación.
     * Ejemplo: POST /api/auth/login
     */
    @PostMapping("/login")
    public Map<String, Object> iniciarSesion(@Valid @RequestBody AuthRequest datos) {
        String usuario = datos.usuario().trim();

        // Busca el hash almacenado para el usuario recibido.
        var resultados = jdbc.query(
            "SELECT password_hash FROM usuarios_app WHERE usuario = ?",
            (rs, rowNum) -> rs.getString("password_hash"),
            usuario
        );

        // La autenticación solo es correcta si el usuario existe y BCrypt coincide.
        if (resultados.isEmpty() ||
            !passwordEncoder.matches(datos.contraseña(), resultados.get(0))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Error en la autenticación");
        }

        return Map.of(
            "autenticado", true,
            "mensaje", "Autenticación satisfactoria",
            "usuario", usuario
        );
    }
}
