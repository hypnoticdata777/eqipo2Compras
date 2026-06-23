package com.empresa.compras.compra;

import com.empresa.compras.proveedor.ProveedorService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CompraService {

    private static final List<String> ESTADOS_PERMITIDOS = List.of("PENDIENTE", "CONFIRMADA", "CANCELADA");

    private final CompraRepository compraRepository;
    private final ProveedorService proveedorService; // solo para validar la FK, nunca para modificar proveedores

    public CompraService() {
        this.compraRepository = new CompraRepository();
        this.proveedorService = new ProveedorService();
    }

    public String registrarCompra(Compra compra) {
        if (compra.getFecha() == null || compra.getFecha().trim().isEmpty()) {
            return "Error: la fecha es obligatoria.";
        }
        try {
            LocalDate.parse(compra.getFecha().trim());
        } catch (DateTimeParseException e) {
            return "Error: la fecha debe tener el formato YYYY-MM-DD y ser una fecha valida.";
        }
        if (!ESTADOS_PERMITIDOS.contains(compra.getEstado())) {
            return "Error: el estado de la compra no es valido.";
        }
        if (compra.getTotal() < 0) {
            return "Error: el total no puede ser negativo.";
        }
        if (!proveedorService.proveedorExiste(compra.getIdProveedor())) {
            return "Error: el proveedor indicado no existe.";
        }

        compra.setFecha(compra.getFecha().trim());
        compraRepository.guardar(compra);
        return null;
    }

    public List<Compra> mostrarCompras() {
        return compraRepository.obtenerTodos();
    }

    public Compra buscarCompra(int idCompra) {
        return compraRepository.buscarPorId(idCompra);
    }

    public String confirmarCompra(int idCompra) {
        Compra compra = compraRepository.buscarPorId(idCompra);
        if (compra == null) {
            return "Error: no existe una compra con ese ID.";
        }
        if ("CANCELADA".equals(compra.getEstado())) {
            return "Error: no se puede confirmar una compra que ya fue cancelada.";
        }
        if ("CONFIRMADA".equals(compra.getEstado())) {
            return "Error: esta compra ya estaba confirmada.";
        }

        compraRepository.actualizarEstado(idCompra, "CONFIRMADA");
        return null;
    }

    public String cancelarCompra(int idCompra) {
        Compra compra = compraRepository.buscarPorId(idCompra);
        if (compra == null) {
            return "Error: no existe una compra con ese ID.";
        }
        if ("CONFIRMADA".equals(compra.getEstado())) {
            return "Error: no se puede cancelar una compra ya confirmada (ya genero movimiento de inventario).";
        }

        compraRepository.actualizarEstado(idCompra, "CANCELADA");
        return null;
    }

    // Usado por DetalleCompraService para "Recalcular total" despues de agregar/quitar un producto
    public void actualizarTotal(int idCompra, double nuevoTotal) {
        compraRepository.actualizarTotal(idCompra, nuevoTotal);
    }

    // Usado por DetalleCompraService para validar la regla "Compra existente"
    public Compra obtenerCompraValidada(int idCompra) {
        return compraRepository.buscarPorId(idCompra);
    }
}
