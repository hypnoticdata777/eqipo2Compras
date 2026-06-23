package com.empresa.json.importador;

import com.empresa.json.dto.ProductoDTO;
import com.empresa.json.util.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportadorProductos {

    public List<ProductoDTO> leer(Path ruta) throws IOException {
        String contenido = Files.readString(ruta, StandardCharsets.UTF_8);
        Object raiz = new JsonParser(contenido).parsear();
        if (!(raiz instanceof List<?> elementos)) {
            throw new IllegalArgumentException("productos.json debe contener un arreglo en la raiz.");
        }

        List<ProductoDTO> productos = new ArrayList<>();
        for (int i = 0; i < elementos.size(); i++) {
            Object elemento = elementos.get(i);
            if (!(elemento instanceof Map<?, ?> objeto)) {
                throw new IllegalArgumentException("El producto en la posicion " + i + " no es un objeto.");
            }
            productos.add(convertir(objeto, i));
        }
        return productos;
    }

    private ProductoDTO convertir(Map<?, ?> objeto, int indice) {
        return new ProductoDTO(
                entero(objeto, "idProducto", indice),
                cadena(objeto, "nombre", indice),
                cadena(objeto, "descripcion", indice),
                decimal(objeto, "precio", indice),
                entero(objeto, "stock", indice),
                entero(objeto, "idCategoria", indice),
                cadena(objeto, "nombreCategoria", indice),
                entero(objeto, "idAlmacen", indice),
                cadena(objeto, "nombreAlmacen", indice),
                booleano(objeto, "activo", indice)
        );
    }

    private int entero(Map<?, ?> objeto, String campo, int indice) {
        Object valor = requerido(objeto, campo, indice);
        if (!(valor instanceof Number numero) || numero.doubleValue() != numero.intValue()) {
            throw tipoIncorrecto(campo, indice, "entero");
        }
        return numero.intValue();
    }

    private double decimal(Map<?, ?> objeto, String campo, int indice) {
        Object valor = requerido(objeto, campo, indice);
        if (!(valor instanceof Number numero)) {
            throw tipoIncorrecto(campo, indice, "numero");
        }
        return numero.doubleValue();
    }

    private String cadena(Map<?, ?> objeto, String campo, int indice) {
        Object valor = requerido(objeto, campo, indice);
        if (!(valor instanceof String cadena)) {
            throw tipoIncorrecto(campo, indice, "cadena");
        }
        return cadena;
    }

    private boolean booleano(Map<?, ?> objeto, String campo, int indice) {
        Object valor = requerido(objeto, campo, indice);
        if (!(valor instanceof Boolean booleano)) {
            throw tipoIncorrecto(campo, indice, "booleano");
        }
        return booleano;
    }

    private Object requerido(Map<?, ?> objeto, String campo, int indice) {
        if (!objeto.containsKey(campo) || objeto.get(campo) == null) {
            throw new IllegalArgumentException(
                    "Falta el campo obligatorio '" + campo + "' en el producto " + indice + "."
            );
        }
        return objeto.get(campo);
    }

    private IllegalArgumentException tipoIncorrecto(String campo, int indice, String tipo) {
        return new IllegalArgumentException(
                "El campo '" + campo + "' del producto " + indice + " debe ser " + tipo + "."
        );
    }
}
