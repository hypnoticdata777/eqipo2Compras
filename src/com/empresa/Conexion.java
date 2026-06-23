package com.empresa;

import com.empresa.persistencia.PersistenciaException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * REFERENCIA / RESPALDO SOLAMENTE.
 *
 * Regla del proyecto: "No modificar Conexion.java sin permiso".
 * Si tu equipo ya tiene un Conexion.java compartido en el repo de Live Share,
 * USA ESE y borra este archivo (o no lo copies al proyecto compartido).
 * Este archivo existe solo para que el paquete "compras" pueda compilar y
 * probarse de forma 100% independiente, como pide la regla 11 del documento:
 * "Antes de integrar, cada equipo deberá probar sus clases de forma independiente."
 *
 * Ajusta URL / USUARIO / PASSWORD a los datos reales de la base
 * "sistema_empresa" que les dé el profesor.
 */
public class Conexion {

    private static final String URL_PREDETERMINADA = "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USUARIO_PREDETERMINADO = "root";
    private static final String PASSWORD_PREDETERMINADO = "";

    public static Connection getConexion() {
        String url = obtenerConfiguracion("DB_URL", "db.url", URL_PREDETERMINADA);
        String usuario = obtenerConfiguracion("DB_USER", "db.user", USUARIO_PREDETERMINADO);
        String password = obtenerConfiguracion("DB_PASSWORD", "db.password", PASSWORD_PREDETERMINADO);

        try {
            return DriverManager.getConnection(url, usuario, password);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No fue posible conectar con MySQL. Verifica el conector, el servidor y las credenciales.",
                    e
            );
        }
    }

    private static String obtenerConfiguracion(String variableEntorno, String propiedad, String predeterminado) {
        String valorPropiedad = System.getProperty(propiedad);
        if (valorPropiedad != null && !valorPropiedad.isBlank()) {
            return valorPropiedad;
        }

        String valorEntorno = System.getenv(variableEntorno);
        return valorEntorno == null || valorEntorno.isBlank() ? predeterminado : valorEntorno;
    }
}
