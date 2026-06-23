package com.empresa.compras.compra;

/**
 * Modelo (Entidad) que representa la tabla `compra` en MySQL.
 * Solo atributos, constructores, getters, setters y toString(). Sin SQL ni Scanner.
 */
public class Compra {

    private int idCompra;
    private int idProveedor;
    private String fecha;
    private double total;
    private String estado; // PENDIENTE, CONFIRMADA o CANCELADA

    public Compra() {
    }

    // Constructor para una compra NUEVA: nace PENDIENTE y con total en 0
    // (el total real se calcula despues, a partir de los detalles que se vayan agregando)
    public Compra(int idProveedor, String fecha) {
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.total = 0.0;
        this.estado = "PENDIENTE";
    }

    // Constructor completo - lo usa el Repository para reconstruir una compra que vino de MySQL
    public Compra(int idCompra, int idProveedor, String fecha, double total, String estado) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", idProveedor=" + idProveedor +
                ", fecha='" + fecha + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}
