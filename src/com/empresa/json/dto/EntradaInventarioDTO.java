package com.empresa.json.dto;

/**
 * DTO (Data Transfer Object) para el contrato entradas_inventario.json
 *   Productor: Equipo 2 (Compras)
 *   Consumidor: Equipo 1 (Inventario)
 *
 * IMPORTANTE: la estructura de este contrato NO se puede modificar (regla del proyecto).
 * Campos exactos esperados: idMovimiento, idCompra, idProducto, cantidad, costoUnitario,
 * fecha, tipo.
 *
 * idMovimiento se exporta como 0: el Equipo 1 es quien genera el id_movimiento real
 * (autoincremento de movimiento_inventario) al importar este archivo.
 */
public class EntradaInventarioDTO {

    private final int idMovimiento;
    private final int idCompra;
    private final int idProducto;
    private final int cantidad;
    private final double costoUnitario;
    private final String fecha;
    private final String tipo;

    public EntradaInventarioDTO(int idCompra, int idProducto, int cantidad, double costoUnitario, String fecha) {
        this.idMovimiento = 0; // placeholder; Equipo 1 lo asigna al insertar en MySQL
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.fecha = fecha;
        this.tipo = "ENTRADA"; // segun el contrato, este campo SIEMPRE es ENTRADA en este archivo
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }
}
