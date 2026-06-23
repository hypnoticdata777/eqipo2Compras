package com.empresa.compras.compra;

import java.util.List;

public class CompraController {

    private final CompraService compraService;

    public CompraController() {
        this.compraService = new CompraService();
    }

    public String registrarCompra(int idProveedor, String fecha) {
        Compra nueva = new Compra(idProveedor, fecha);
        return compraService.registrarCompra(nueva);
    }

    public List<Compra> mostrarCompras() {
        return compraService.mostrarCompras();
    }

    public Compra buscarCompra(int idCompra) {
        return compraService.buscarCompra(idCompra);
    }

    public String confirmarCompra(int idCompra) {
        return compraService.confirmarCompra(idCompra);
    }

    public String cancelarCompra(int idCompra) {
        return compraService.cancelarCompra(idCompra);
    }
}
