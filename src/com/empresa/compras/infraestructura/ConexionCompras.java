package com.empresa.compras.infraestructura;

import com.empresa.Conexion;
import com.empresa.persistencia.PersistenciaException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Adaptador del Equipo 2: respeta el Conexion.java compartido y agrega una
 * validacion segura. Solo usa configuracion externa cuando fue proporcionada.
 */
public final class ConexionCompras {

    private ConexionCompras() {
    }

    public static Connection obtener() {
        String url = configuracion("DB_URL", "db.url");
        String usuario = configuracion("DB_USER", "db.user");
        String password = configuracion("DB_PASSWORD", "db.password");

        if (url != null || usuario != null || password != null) {
            return conectarConConfiguracion(url, usuario, password);
        }

        Connection conexion = Conexion.getConexion();
        if (conexion == null) {
            throw new PersistenciaException(
                    "No fue posible conectar con MySQL. Verifica el conector, el servidor y las credenciales.",
                    null
            );
        }
        return conexion;
    }

    private static Connection conectarConConfiguracion(String url, String usuario, String password) {
        String urlFinal = url == null ? "jdbc:mysql://localhost:3306/sistema_empresa" : url;
        String usuarioFinal = usuario == null ? "root" : usuario;
        String passwordFinal = password == null ? "" : password;

        try {
            return DriverManager.getConnection(urlFinal, usuarioFinal, passwordFinal);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No fue posible conectar con MySQL. Verifica el conector, el servidor y las credenciales.",
                    e
            );
        }
    }

    private static String configuracion(String variableEntorno, String propiedad) {
        String valorPropiedad = System.getProperty(propiedad);
        if (valorPropiedad != null && !valorPropiedad.isBlank()) {
            return valorPropiedad;
        }

        String valorEntorno = System.getenv(variableEntorno);
        return valorEntorno == null || valorEntorno.isBlank() ? null : valorEntorno;
    }
}
