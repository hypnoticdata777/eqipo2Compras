package com.empresa.pruebas;

import com.empresa.compras.compra.Compra;
import com.empresa.compras.compra.CompraService;
import com.empresa.compras.detallecompra.DetalleCompra;
import com.empresa.compras.detallecompra.DetalleCompraService;
import com.empresa.compras.proveedor.Proveedor;
import com.empresa.compras.proveedor.ProveedorService;
import com.empresa.json.dto.ProductoDTO;
import com.empresa.json.exportador.ExportadorEntradasInventario;
import com.empresa.json.importador.ImportadorProductos;
import com.empresa.json.util.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class PruebasSinBaseDeDatos {

    public static void main(String[] args) throws Exception {
        probarValidacionesLocales();
        probarImportadorProductos();
        probarExportadorEntradas();
        System.out.println("PRUEBAS_OK: contratos JSON, calculos y validaciones locales.");
    }

    private static void probarValidacionesLocales() {
        Proveedor proveedorInvalido = new Proveedor(" ", "", "", "correo-sin-arroba", "");
        String errorProveedor = new ProveedorService().registrarProveedor(proveedorInvalido);
        verificar(errorProveedor != null && errorProveedor.contains("nombre"), "validacion de proveedor");

        Compra compraFechaInvalida = new Compra(1, "2026-02-30");
        String errorCompra = new CompraService().registrarCompra(compraFechaInvalida);
        verificar(errorCompra != null && errorCompra.contains("fecha"), "validacion de fecha");

        String errorDetalle = new DetalleCompraService().agregarProductoACompra(1, 1, 0, 10);
        verificar(errorDetalle != null && errorDetalle.contains("cantidad"), "validacion de cantidad");

        DetalleCompra detalle = new DetalleCompra(4, 8, 3, 12.50, 37.50);
        verificar(detalle.getSubtotal() == 37.50, "subtotal calculado");
    }

    private static void probarImportadorProductos() throws Exception {
        List<ProductoDTO> productos = new ImportadorProductos()
                .leer(Path.of("importaciones", "productos_ejemplo.json"));

        verificar(productos.size() == 1, "cantidad de productos importados");
        ProductoDTO producto = productos.get(0);
        verificar(producto.getIdProducto() == 1, "idProducto");
        verificar("Mouse inalambrico".equals(producto.getNombre()), "nombre de producto");
        verificar(producto.isActivo(), "estado activo");
    }

    private static void probarExportadorEntradas() throws Exception {
        DetalleCompra detalle = new DetalleCompra(7, 3, 2, 125.50, 251.00);
        boolean exportado = new ExportadorEntradasInventario()
                .exportar(List.of(detalle), 7, "2026-06-22");
        verificar(exportado, "exportacion de entradas");

        Path salida = Path.of("exportaciones", "entradas_inventario.json");
        Object raiz = new JsonParser(Files.readString(salida)).parsear();
        verificar(raiz instanceof List<?>, "raiz del JSON exportado");

        List<?> entradas = (List<?>) raiz;
        verificar(entradas.size() == 1, "cantidad de entradas exportadas");
        Map<?, ?> entrada = (Map<?, ?>) entradas.get(0);
        verificar(((Number) entrada.get("idCompra")).intValue() == 7, "idCompra exportado");
        verificar("2026-06-22".equals(entrada.get("fecha")), "fecha de compra exportada");
        verificar("ENTRADA".equals(entrada.get("tipo")), "tipo de movimiento exportado");

        Files.deleteIfExists(salida);
    }

    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) {
            throw new AssertionError("Fallo: " + nombre);
        }
    }
}
