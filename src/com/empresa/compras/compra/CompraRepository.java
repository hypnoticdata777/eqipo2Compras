package com.empresa.compras.compra;

import com.empresa.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraRepository {

    public Compra guardar(Compra compra) {
        String sql = "INSERT INTO compra (id_proveedor, fecha, total, estado) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, compra.getIdProveedor());
            ps.setString(2, compra.getFecha());
            ps.setDouble(3, compra.getTotal());
            ps.setString(4, compra.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    compra.setIdCompra(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar compra: " + e.getMessage());
        }
        return compra;
    }

    public List<Compra> obtenerTodos() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM compra";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCompra(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener compras: " + e.getMessage());
        }
        return lista;
    }

    public Compra buscarPorId(int idCompra) {
        String sql = "SELECT * FROM compra WHERE id_compra = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCompra(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar compra: " + e.getMessage());
        }
        return null;
    }

    public void actualizarEstado(int idCompra, String nuevoEstado) {
        String sql = "UPDATE compra SET estado = ? WHERE id_compra = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCompra);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de compra: " + e.getMessage());
        }
    }

    // Llamado desde DetalleCompraService cada vez que se agrega un producto (recalcular total)
    public void actualizarTotal(int idCompra, double nuevoTotal) {
        String sql = "UPDATE compra SET total = ? WHERE id_compra = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, idCompra);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar total de compra: " + e.getMessage());
        }
    }

    private Compra mapearCompra(ResultSet rs) throws SQLException {
        return new Compra(
                rs.getInt("id_compra"),
                rs.getInt("id_proveedor"),
                rs.getString("fecha"),
                rs.getDouble("total"),
                rs.getString("estado")
        );
    }
}
