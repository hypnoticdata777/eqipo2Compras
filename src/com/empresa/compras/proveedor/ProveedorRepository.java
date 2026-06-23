package com.empresa.compras.proveedor;

import com.empresa.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository: unico lugar donde se escribe SQL para la tabla `proveedor`.
 * No contiene Scanner, menus, ni reglas de negocio complejas (eso vive en Service).
 */
public class ProveedorRepository {

    public Proveedor guardar(Proveedor proveedor) {
        String sql = "INSERT INTO proveedor (nombre, rfc, telefono, correo, direccion, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getRfc());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getCorreo());
            ps.setString(5, proveedor.getDireccion());
            ps.setBoolean(6, proveedor.isActivo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    proveedor.setIdProveedor(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar proveedor: " + e.getMessage());
        }
        return proveedor;
    }

    public List<Proveedor> obtenerTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProveedor(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener proveedores: " + e.getMessage());
        }
        return lista;
    }

    public Proveedor buscarPorId(int idProveedor) {
        String sql = "SELECT * FROM proveedor WHERE id_proveedor = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProveedor(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor: " + e.getMessage());
        }
        return null; // el Service decide que hacer si no se encontro nada
    }

    public void actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedor SET nombre=?, rfc=?, telefono=?, correo=?, direccion=? " +
                "WHERE id_proveedor=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getRfc());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getCorreo());
            ps.setString(5, proveedor.getDireccion());
            ps.setInt(6, proveedor.getIdProveedor());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
        }
    }

    public void desactivar(int idProveedor) {
        String sql = "UPDATE proveedor SET activo = false WHERE id_proveedor = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al desactivar proveedor: " + e.getMessage());
        }
    }

    public boolean existeRfc(String rfc) {
        String sql = "SELECT 1 FROM proveedor WHERE rfc = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rfc);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si encontro al menos una fila con ese RFC
            }
        } catch (SQLException e) {
            System.out.println("Error al validar RFC: " + e.getMessage());
        }
        return false;
    }

    /**
     * Usado por CompraService para la regla "Proveedor existente".
     * Nota a proposito: el documento NO dice "proveedor existente y activo"
     * (a diferencia de Envios, donde si dice explicitamente "Transportista activo"),
     * asi que aqui solo se valida que el id exista, sin importar el estado activo.
     */
    public boolean existe(int idProveedor) {
        String sql = "SELECT 1 FROM proveedor WHERE id_proveedor = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al validar proveedor: " + e.getMessage());
        }
        return false;
    }

    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        return new Proveedor(
                rs.getInt("id_proveedor"),
                rs.getString("nombre"),
                rs.getString("rfc"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("direccion"),
                rs.getBoolean("activo")
        );
    }
}
