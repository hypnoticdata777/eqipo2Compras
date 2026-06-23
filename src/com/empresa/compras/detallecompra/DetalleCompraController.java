package com.empresa.compras.detallecompra;

import java.util.List;

public class DetalleCompraController {

    private final DetalleCompraService detalleCompraService;

    public DetalleCompraController() {
        this.detalleCompraService = new DetalleCompraService();
    }

    public String agregarProductoACompra(int idCompra, int idProducto, int cantidad, double costoUnitario) {
        return detalleCompraService.agregarProductoACompra(idCompra, idProducto, cantidad, costoUnitario);
    }

    public List<DetalleCompra> mostrarDetalles(int idCompra) {
        return detalleCompraService.mostrarDetalles(idCompra);
    }

    public double recalcularTotal(int idCompra) {
        return detalleCompraService.recalcularTotal(idCompra);
    }

    public String exportarEntradasInventario(int idCompra) {
        return detalleCompraService.exportarEntradasInventario(idCompra);
    }
}
