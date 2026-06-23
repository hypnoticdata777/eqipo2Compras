package com.empresa.compras.detallecompra;

import com.empresa.json.dto.ProductoDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductoCatalogoService {

    private final ProductoCatalogoRepository repository;

    public ProductoCatalogoService() {
        this.repository = new ProductoCatalogoRepository();
    }

    public String importar(List<ProductoDTO> productos) {
        if (productos == null || productos.isEmpty()) {
            return "Error: productos.json no contiene productos.";
        }

        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < productos.size(); i++) {
            ProductoDTO producto = productos.get(i);
            String error = validar(producto, i);
            if (error != null) {
                return error;
            }
            if (!ids.add(producto.getIdProducto())) {
                return "Error: idProducto repetido en productos.json: " + producto.getIdProducto() + ".";
            }
        }

        repository.guardarOActualizarTodos(productos);
        return null;
    }

    private String validar(ProductoDTO producto, int indice) {
        String referencia = "producto " + indice;
        if (producto.getIdProducto() <= 0) {
            return "Error: idProducto debe ser mayor que cero en " + referencia + ".";
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return "Error: nombre obligatorio en " + referencia + ".";
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            return "Error: descripcion obligatoria en " + referencia + ".";
        }
        if (!Double.isFinite(producto.getPrecio()) || producto.getPrecio() <= 0) {
            return "Error: precio debe ser mayor que cero en " + referencia + ".";
        }
        if (producto.getStock() < 0) {
            return "Error: stock no puede ser negativo en " + referencia + ".";
        }
        if (producto.getIdCategoria() <= 0 || producto.getIdAlmacen() <= 0) {
            return "Error: categoria y almacen deben ser validos en " + referencia + ".";
        }
        if (producto.getNombreCategoria().trim().isEmpty() || producto.getNombreAlmacen().trim().isEmpty()) {
            return "Error: nombres de categoria y almacen obligatorios en " + referencia + ".";
        }
        return null;
    }
}
