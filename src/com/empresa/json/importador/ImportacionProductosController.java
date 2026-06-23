package com.empresa.json.importador;

import com.empresa.compras.detallecompra.ProductoCatalogoService;
import com.empresa.json.dto.ProductoDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ImportacionProductosController {

    private final ImportadorProductos importador;
    private final ProductoCatalogoService service;

    public ImportacionProductosController() {
        this.importador = new ImportadorProductos();
        this.service = new ProductoCatalogoService();
    }

    public String importar(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return "Error: la ruta de productos.json es obligatoria.";
        }

        try {
            List<ProductoDTO> productos = importador.leer(Path.of(rutaArchivo.trim()));
            String error = service.importar(productos);
            return error == null ? "Importacion completada: " + productos.size() + " producto(s)." : error;
        } catch (IOException e) {
            return "Error al leer productos.json: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Error en productos.json: " + e.getMessage();
        }
    }
}
