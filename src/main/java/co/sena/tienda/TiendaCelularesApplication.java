package co.sena.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Punto de entrada de la aplicación web de gestión de la tienda de celulares.
 * Habilita el escaneo de componentes Spring y el registro de servlets.
 */
@SpringBootApplication
@ServletComponentScan
public class TiendaCelularesApplication extends SpringBootServletInitializer {

    /** Inicia el servidor y el contexto de Spring Boot. */
    public static void main(String[] args) {
        SpringApplication.run(TiendaCelularesApplication.class, args);
    }

    /** Permite desplegar la aplicación como archivo WAR en un contenedor Servlet. */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TiendaCelularesApplication.class);
    }
}
