package com.sena.miprimeraapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private LinearLayout pantalla;

    private final int FONDO = Color.rgb(245, 247, 250);
    private final int AZUL = Color.rgb(25, 75, 145);
    private final int TEXTO = Color.rgb(45, 45, 45);

    private final ExecutorService ejecutor =
            Executors.newSingleThreadExecutor();

    private final Handler manejador =
            new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarInicio();
    }

    private void mostrarInicio() {

        pantalla = new LinearLayout(this);
        pantalla.setOrientation(LinearLayout.VERTICAL);
        pantalla.setGravity(Gravity.CENTER);
        pantalla.setPadding(35, 35, 35, 35);
        pantalla.setBackgroundColor(FONDO);

        TextView titulo = new TextView(this);
        titulo.setText("TIENDA DE CELULARES");
        titulo.setTextSize(27);
        titulo.setTextColor(AZUL);
        titulo.setGravity(Gravity.CENTER);
        titulo.setPadding(0, 20, 0, 10);

        TextView subtitulo = new TextView(this);
        subtitulo.setText("Sistema de gestión móvil");
        subtitulo.setTextSize(17);
        subtitulo.setTextColor(TEXTO);
        subtitulo.setGravity(Gravity.CENTER);
        subtitulo.setPadding(0, 0, 0, 30);

        pantalla.addView(titulo);
        pantalla.addView(subtitulo);

        agregarBoton("GESTIÓN DE CLIENTES", "clientes");
        agregarBoton("GESTIÓN DE PRODUCTOS", "productos");
        agregarBoton("GESTIÓN DE VENTAS", "ventas");

        setContentView(pantalla);
    }

    private void agregarBoton(String texto, String modulo) {

        Button boton = new Button(this);

        boton.setText(texto);
        boton.setTextSize(16);
        boton.setTextColor(Color.WHITE);

        boton.setBackgroundTintList(
                ColorStateList.valueOf(AZUL)
        );

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        70
                );

        parametros.setMargins(0, 10, 0, 10);

        pantalla.addView(boton, parametros);

        boton.setOnClickListener(v -> {

            if (modulo.equals("clientes")) {
                consultarClientes();
            } else if (modulo.equals("productos")) {
                consultarProductos();
            } else if (modulo.equals("ventas")) {
                consultarVentas();
            }
        });
    }

    private void consultarClientes() {

        consultarApi(
                "/api/clientes",
                "CLIENTES REGISTRADOS",
                "clientes"
        );
    }

    private void consultarProductos() {

        consultarApi(
                "/api/productos",
                "PRODUCTOS REGISTRADOS",
                "productos"
        );
    }

    private void consultarVentas() {

        consultarApi(
                "/api/ventas",
                "VENTAS REGISTRADAS",
                "ventas"
        );
    }

    private void consultarApi(
            String endpoint,
            String titulo,
            String tipo
    ) {

        mostrarCargando();

        ejecutor.execute(() -> {

            HttpURLConnection conexion = null;

            try {

                URL url = new URL(
                        "http://10.0.2.2:8080" + endpoint
                );

                conexion = (HttpURLConnection) url.openConnection();

                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(10000);
                conexion.setReadTimeout(10000);

                int respuesta = conexion.getResponseCode();

                if (respuesta != HttpURLConnection.HTTP_OK) {

                    throw new Exception(
                            "Error HTTP: " + respuesta
                    );
                }

                BufferedReader lector =
                        new BufferedReader(
                                new InputStreamReader(
                                        conexion.getInputStream()
                                )
                        );

                StringBuilder resultado =
                        new StringBuilder();

                String linea;

                while ((linea = lector.readLine()) != null) {
                    resultado.append(linea);
                }

                lector.close();

                JSONArray datos =
                        new JSONArray(resultado.toString());

                String listado;

                if (tipo.equals("clientes")) {

                    listado = procesarClientes(datos);

                } else if (tipo.equals("productos")) {

                    listado = procesarProductos(datos);

                } else {

                    listado = procesarVentas(datos);
                }

                manejador.post(() ->
                        mostrarResultado(titulo, listado)
                );

            } catch (Exception e) {

                String error = e.getMessage();

                if (error == null || error.isEmpty()) {
                    error = "No se pudo conectar con el servidor";
                }

                String mensajeError = error;

                manejador.post(() -> {

                    Toast.makeText(
                            MainActivity.this,
                            "Error: " + mensajeError,
                            Toast.LENGTH_LONG
                    ).show();

                    mostrarInicio();
                });

            } finally {

                if (conexion != null) {
                    conexion.disconnect();
                }
            }
        });
    }

    private String procesarClientes(JSONArray clientes)
            throws Exception {

        StringBuilder listado =
                new StringBuilder();

        listado.append("TOTAL DE CLIENTES: ");
        listado.append(clientes.length());
        listado.append("\n\n");

        for (int i = 0; i < clientes.length(); i++) {

            JSONObject cliente =
                    clientes.getJSONObject(i);

            listado.append("CLIENTE ");
            listado.append(i + 1);
            listado.append("\n");

            listado.append("ID: ");
            listado.append(
                    obtenerValor(
                            cliente,
                            "id_cliente",
                            "id"
                    )
            );
            listado.append("\n");

            listado.append("Nombre: ");
            listado.append(
                    cliente.optString(
                            "nombre",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Teléfono: ");
            listado.append(
                    cliente.optString(
                            "telefono",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Correo: ");
            listado.append(
                    cliente.optString(
                            "correo",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Dirección: ");
            listado.append(
                    cliente.optString(
                            "direccion",
                            "No registrada"
                    )
            );
            listado.append("\n");

            listado.append("------------------------------\n\n");
        }

        return listado.toString();
    }

    private String procesarProductos(JSONArray productos)
            throws Exception {

        StringBuilder listado =
                new StringBuilder();

        listado.append("TOTAL DE PRODUCTOS: ");
        listado.append(productos.length());
        listado.append("\n\n");

        for (int i = 0; i < productos.length(); i++) {

            JSONObject producto =
                    productos.getJSONObject(i);

            listado.append("PRODUCTO ");
            listado.append(i + 1);
            listado.append("\n");

            listado.append("ID: ");
            listado.append(
                    producto.optInt("id", 0)
            );
            listado.append("\n");

            listado.append("Modelo: ");
            listado.append(
                    producto.optString(
                            "modelo",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Marca: ");
            listado.append(
                    producto.optString(
                            "marca",
                            "No registrada"
                    )
            );
            listado.append("\n");

            listado.append("Almacenamiento: ");
            listado.append(
                    producto.optString(
                            "almacenamiento",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Color: ");
            listado.append(
                    producto.optString(
                            "color",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Precio: $");
            listado.append(
                    producto.optString(
                            "precio",
                            "0"
                    )
            );
            listado.append("\n");

            listado.append("Stock: ");
            listado.append(
                    producto.optInt("stock", 0)
            );
            listado.append("\n");

            listado.append("------------------------------\n\n");
        }

        return listado.toString();
    }

    private String procesarVentas(JSONArray ventas)
            throws Exception {

        StringBuilder listado =
                new StringBuilder();

        listado.append("TOTAL DE VENTAS: ");
        listado.append(ventas.length());
        listado.append("\n\n");

        for (int i = 0; i < ventas.length(); i++) {

            JSONObject venta =
                    ventas.getJSONObject(i);

            listado.append("VENTA ");
            listado.append(i + 1);
            listado.append("\n");

            listado.append("ID venta: ");
            listado.append(
                    obtenerValor(
                            venta,
                            "id_venta",
                            "id"
                    )
            );
            listado.append("\n");

            listado.append("Cliente: ");
            listado.append(
                    venta.optString(
                            "cliente",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("ID cliente: ");
            listado.append(
                    venta.optString(
                            "id_cliente",
                            "No registrado"
                    )
            );
            listado.append("\n");

            listado.append("Fecha: ");
            listado.append(
                    venta.optString(
                            "fecha",
                            "No registrada"
                    )
            );
            listado.append("\n");

            listado.append("Total: $");
            listado.append(
                    venta.optString(
                            "total",
                            "0"
                    )
            );
            listado.append("\n");

            listado.append("------------------------------\n\n");
        }

        return listado.toString();
    }

    private String obtenerValor(
            JSONObject objeto,
            String campoPrincipal,
            String campoAlternativo
    ) {

        if (objeto.has(campoPrincipal)) {
            return objeto.optString(
                    campoPrincipal,
                    "No registrado"
            );
        }

        return objeto.optString(
                campoAlternativo,
                "No registrado"
        );
    }

    private void mostrarCargando() {

        LinearLayout contenido =
                crearPantalla();

        contenido.setGravity(Gravity.CENTER);

        TextView mensaje =
                new TextView(this);

        mensaje.setText(
                "Consultando información..."
        );

        mensaje.setTextSize(21);
        mensaje.setTextColor(AZUL);
        mensaje.setGravity(Gravity.CENTER);

        contenido.addView(mensaje);

        setContentView(contenido);
    }

    private void mostrarResultado(
            String tituloTexto,
            String resultado
    ) {

        LinearLayout contenido =
                crearPantalla();

        TextView titulo =
                new TextView(this);

        titulo.setText(tituloTexto);
        titulo.setTextSize(23);
        titulo.setTextColor(AZUL);
        titulo.setGravity(Gravity.CENTER);
        titulo.setPadding(0, 15, 0, 20);

        TextView listado =
                new TextView(this);

        listado.setText(resultado);
        listado.setTextSize(16);
        listado.setTextColor(TEXTO);
        listado.setPadding(15, 15, 15, 15);

        ScrollView scroll =
                new ScrollView(this);

        scroll.addView(listado);

        contenido.addView(titulo);

        contenido.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        Button regresar =
                new Button(this);

        regresar.setText("REGRESAR AL MENÚ");

        regresar.setOnClickListener(
                v -> mostrarInicio()
        );

        contenido.addView(regresar);

        setContentView(contenido);
    }

    private LinearLayout crearPantalla() {

        LinearLayout contenido =
                new LinearLayout(this);

        contenido.setOrientation(
                LinearLayout.VERTICAL
        );

        contenido.setPadding(
                25,
                25,
                25,
                25
        );

        contenido.setBackgroundColor(FONDO);

        return contenido;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        ejecutor.shutdownNow();
    }
}