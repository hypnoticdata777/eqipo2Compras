package com.empresa.json.dto;

/**
 * Contrato recibido en productos.json, producido por Equipo 1.
 */
public class ProductoDTO {

    private final int idProducto;
    private final String nombre;
    private final String descripcion;
    private final double precio;
    private final int stock;
    private final int idCategoria;
    private final String nombreCategoria;
    private final int idAlmacen;
    private final String nombreAlmacen;
    private final boolean activo;

    public ProductoDTO(int idProducto, String nombre, String descripcion, double precio, int stock,
                       int idCategoria, String nombreCategoria, int idAlmacen,
                       String nombreAlmacen, boolean activo) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.idAlmacen = idAlmacen;
        this.nombreAlmacen = nombreAlmacen;
        this.activo = activo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public boolean isActivo() {
        return activo;
    }
}
