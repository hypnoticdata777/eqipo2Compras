package com.empresa.compras.detallecompra;

/**
 * Modelo (Entidad) que representa la tabla `detalle_compra` en MySQL.
 * El subtotal SIEMPRE lo calcula el sistema (cantidad x costo_unitario),
 * nunca se recibe directamente de un usuario o formulario.
 */
public class DetalleCompra {

    private int idDetalleCompra;
    private int idCompra;
    private int idProducto;
    private int cantidad;
    private double costoUnitario;
    private double subtotal;

    public DetalleCompra() {
    }

    // Constructor para un detalle NUEVO: calcula el subtotal automaticamente
    public DetalleCompra(int idCompra, int idProducto, int cantidad, double costoUnitario) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.subtotal = cantidad * costoUnitario; // regla del documento: subtotal = cantidad x costo_unitario
    }

    // Constructor completo - lo usa el Repository para reconstruir un detalle que vino de MySQL
    public DetalleCompra(int idDetalleCompra, int idCompra, int idProducto, int cantidad,
                          double costoUnitario, double subtotal) {
        this.idDetalleCompra = idDetalleCompra;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" +
                "idDetalleCompra=" + idDetalleCompra +
                ", idCompra=" + idCompra +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", costoUnitario=" + costoUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
