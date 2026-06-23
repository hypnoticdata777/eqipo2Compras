package com.empresa.compras.detallecompra;

import com.empresa.compras.compra.Compra;
import com.empresa.compras.compra.CompraService;
import com.empresa.json.exportador.ExportadorEntradasInventario;

import java.util.List;

public class DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraService compraService;
    private final ExportadorEntradasInventario exportador;

    public DetalleCompraService() {
        this.detalleCompraRepository = new DetalleCompraRepository();
        this.compraService = new CompraService();
        this.exportador = new ExportadorEntradasInventario();
    }

    public String agregarProductoACompra(int idCompra, int idProducto, int cantidad, double costoUnitario) {
        Compra compra = compraService.obtenerCompraValidada(idCompra);
        if (compra == null) {
            return "Error: la compra indicada no existe.";
        }
        if (!"PENDIENTE".equals(compra.getEstado())) {
            return "Error: solo se pueden agregar productos a una compra en estado PENDIENTE.";
        }
        if (!detalleCompraRepository.existeProducto(idProducto)) {
            return "Error: el producto indicado no existe.";
        }
        if (cantidad <= 0) {
            return "Error: la cantidad debe ser mayor que cero.";
        }
        if (costoUnitario <= 0) {
            return "Error: el costo unitario debe ser mayor que cero.";
        }

        double subtotal = cantidad * costoUnitario;
        DetalleCompra detalle = new DetalleCompra(idCompra, idProducto, cantidad, costoUnitario, subtotal);
        detalleCompraRepository.guardarYRecalcularTotal(detalle);
        return null;
    }

    public List<DetalleCompra> mostrarDetalles(int idCompra) {
        return detalleCompraRepository.obtenerPorCompra(idCompra);
    }

    // Vuelve a sumar el total real desde MySQL (fuente de verdad) y actualiza la tabla compra
    public double recalcularTotal(int idCompra) {
        double nuevoTotal = detalleCompraRepository.sumarTotalPorCompra(idCompra);
        compraService.actualizarTotal(idCompra, nuevoTotal);
        return nuevoTotal;
    }

    /**
     * Genera exportaciones/entradas_inventario.json con los detalles de una compra.
     * Solo se permite exportar compras CONFIRMADAS: una compra PENDIENTE todavia
     * puede cambiar (se le pueden seguir agregando productos), y una CANCELADA
     * nunca debio generar movimiento de inventario.
     */
    public String exportarEntradasInventario(int idCompra) {
        Compra compra = compraService.obtenerCompraValidada(idCompra);
        if (compra == null) {
            return "Error: la compra indicada no existe.";
        }
        if (!"CONFIRMADA".equals(compra.getEstado())) {
            return "Error: solo se pueden exportar entradas de una compra CONFIRMADA.";
        }

        List<DetalleCompra> detalles = detalleCompraRepository.obtenerPorCompra(idCompra);
        boolean exito = exportador.exportar(detalles, idCompra, compra.getFecha());

        return exito ? null : "Error: no se pudo generar el archivo JSON.";
    }
}
