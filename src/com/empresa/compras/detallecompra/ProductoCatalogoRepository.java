package com.empresa.compras.detallecompra;

import com.empresa.compras.infraestructura.ConexionCompras;
import com.empresa.json.dto.ProductoDTO;
import com.empresa.persistencia.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Persistencia del contrato productos.json que Equipo 2 consume de Equipo 1.
 */
public class ProductoCatalogoRepository {

    public void guardarOActualizarTodos(List<ProductoDTO> productos) {
        String sql = "INSERT INTO producto " +
                "(id_producto, nombre, descripcion, precio, stock, id_categoria, id_almacen, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), descripcion=VALUES(descripcion), " +
                "precio=VALUES(precio), stock=VALUES(stock), id_categoria=VALUES(id_categoria), " +
                "id_almacen=VALUES(id_almacen), activo=VALUES(activo)";

        try (Connection con = ConexionCompras.obtener()) {
            boolean autoCommitOriginal = con.getAutoCommit();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (ProductoDTO producto : productos) {
                    ps.setInt(1, producto.getIdProducto());
                    ps.setString(2, producto.getNombre());
                    ps.setString(3, producto.getDescripcion());
                    ps.setDouble(4, producto.getPrecio());
                    ps.setInt(5, producto.getStock());
                    ps.setInt(6, producto.getIdCategoria());
                    ps.setInt(7, producto.getIdAlmacen());
                    ps.setBoolean(8, producto.isActivo());
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(autoCommitOriginal);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron importar los productos. Verifica categorias, almacenes y claves foraneas.",
                    e
            );
        }
    }
}
