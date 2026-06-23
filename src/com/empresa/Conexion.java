package com.empresa;

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

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }
}
