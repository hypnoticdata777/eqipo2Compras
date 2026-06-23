package com.empresa.compras.detallecompra;

import com.empresa.Conexion;
import com.empresa.persistencia.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraRepository {

    public DetalleCompra guardar(DetalleCompra detalle) {
        String sql = "INSERT INTO detalle_compra (id_compra, id_producto, cantidad, costo_unitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detalle.getIdCompra());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getCostoUnitario());
            ps.setDouble(5, detalle.getSubtotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setIdDetalleCompra(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw error("guardar el detalle de compra", e);
        }
        return detalle;
    }

    public List<DetalleCompra> obtenerPorCompra(int idCompra) {
        List<DetalleCompra> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_compra WHERE id_compra = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDetalle(rs));
                }
            }
        } catch (SQLException e) {
            throw error("consultar los detalles de compra", e);
        }
        return lista;
    }

    // Recalcula el total real sumando los subtotales guardados en MySQL (fuente de verdad)
    public double sumarTotalPorCompra(int idCompra) {
        String sql = "SELECT SUM(subtotal) AS total FROM detalle_compra WHERE id_compra = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total"); // SUM regresa 0 automaticamente si no hay filas
                }
            }
        } catch (SQLException e) {
            throw error("calcular el total de la compra", e);
        }
        return 0.0;
    }

    /**
     * Valida "Producto existente" consultando DIRECTAMENTE la tabla compartida `producto`.
     *
     * A proposito NO se importan las clases del Equipo 1 (Producto / ProductoRepository):
     * las 4 equipos comparten la misma base de datos "sistema_empresa", asi que el
     * Repository puede consultar cualquier tabla del esquema con SQL plano sin necesitar
     * el codigo Java de otro equipo. Esto evita dependencias entre paquetes de distintos
     * equipos (regla 11: "cada equipo solamente modificara sus paquetes") y permite que
     * el modulo de Compras compile y se pruebe de forma totalmente independiente.
     */
    public boolean existeProducto(int idProducto) {
        String sql = "SELECT 1 FROM producto WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw error("validar la existencia del producto", e);
        }
    }

    private DetalleCompra mapearDetalle(ResultSet rs) throws SQLException {
        return new DetalleCompra(
                rs.getInt("id_detalle_compra"),
                rs.getInt("id_compra"),
                rs.getInt("id_producto"),
                rs.getInt("cantidad"),
                rs.getDouble("costo_unitario"),
                rs.getDouble("subtotal")
        );
    }

    private PersistenciaException error(String operacion, SQLException causa) {
        return new PersistenciaException("No se pudo " + operacion + ".", causa);
    }
}
