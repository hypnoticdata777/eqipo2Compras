package com.empresa.compras;

import com.empresa.compras.compra.Compra;
import com.empresa.compras.compra.CompraController;
import com.empresa.compras.detallecompra.DetalleCompra;
import com.empresa.compras.detallecompra.DetalleCompraController;
import com.empresa.compras.proveedor.Proveedor;
import com.empresa.compras.proveedor.ProveedorController;
import com.empresa.persistencia.PersistenciaException;

import java.util.List;
import java.util.Scanner;

/**
 * Submenu del Equipo 2 (Compras y proveedores) -> opcion 2 del menu general.
 *
 * Esta clase es la capa "App o menu": solo muestra opciones, recibe datos
 * del usuario, invoca al Controller y muestra resultados. No contiene SQL
 * ni reglas de negocio (regla del documento).
 *
 * Para integrarla: cuando te toque tu turno en App.java, en el switch de la
 * opcion 2 del menu principal solo necesitas:
 *
 *     case 2 -> new ComprasMenu().mostrarMenu();
 *
 * y un import com.empresa.compras.ComprasMenu; arriba del archivo.
 */
public class ComprasMenu {

    private final ProveedorController proveedorController = new ProveedorController();
    private final CompraController compraController = new CompraController();
    private final DetalleCompraController detalleCompraController = new DetalleCompraController();

    private final Scanner sc = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- COMPRAS Y PROVEEDORES ---");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Mostrar proveedores");
            System.out.println("3. Buscar proveedor");
            System.out.println("4. Desactivar proveedor");
            System.out.println("5. Registrar compra");
            System.out.println("6. Mostrar compras");
            System.out.println("7. Agregar producto a una compra");
            System.out.println("8. Mostrar detalles de una compra");
            System.out.println("9. Confirmar compra");
            System.out.println("10. Cancelar compra");
            System.out.println("11. Exportar entradas_inventario.json");
            System.out.println("0. Regresar al menu principal");
            System.out.print("Selecciona una opcion: ");

            opcion = leerEntero();

            try {
                ejecutarOpcion(opcion);
            } catch (PersistenciaException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarProveedor();
            case 2 -> mostrarProveedores();
            case 3 -> buscarProveedor();
            case 4 -> desactivarProveedor();
            case 5 -> registrarCompra();
            case 6 -> mostrarCompras();
            case 7 -> agregarProductoACompra();
            case 8 -> mostrarDetallesCompra();
            case 9 -> confirmarCompra();
            case 10 -> cancelarCompra();
            case 11 -> exportarEntradas();
            case 0 -> System.out.println("Regresando al menu principal...");
            default -> System.out.println("Opcion no valida.");
        }
    }

    private void registrarProveedor() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("RFC: ");
        String rfc = sc.nextLine();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Direccion: ");
        String direccion = sc.nextLine();

        String error = proveedorController.registrarProveedor(nombre, rfc, telefono, correo, direccion);
        System.out.println(error == null ? "Proveedor registrado correctamente." : error);
    }

    private void mostrarProveedores() {
        List<Proveedor> lista = proveedorController.mostrarProveedores();
        if (lista.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
            return;
        }
        for (Proveedor p : lista) {
            System.out.println(p);
        }
    }

    private void buscarProveedor() {
        System.out.print("ID del proveedor: ");
        int id = leerEntero();
        Proveedor p = proveedorController.buscarProveedor(id);
        System.out.println(p == null ? "No se encontro un proveedor con ese ID." : p);
    }

    private void desactivarProveedor() {
        System.out.print("ID del proveedor a desactivar: ");
        int id = leerEntero();
        String error = proveedorController.desactivarProveedor(id);
        System.out.println(error == null ? "Proveedor desactivado." : error);
    }

    private void registrarCompra() {
        System.out.print("ID del proveedor: ");
        int idProveedor = leerEntero();
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        String error = compraController.registrarCompra(idProveedor, fecha);
        System.out.println(error == null ? "Compra registrada en estado PENDIENTE." : error);
    }

    private void mostrarCompras() {
        List<Compra> lista = compraController.mostrarCompras();
        if (lista.isEmpty()) {
            System.out.println("No hay compras registradas.");
            return;
        }
        for (Compra c : lista) {
            System.out.println(c);
        }
    }

    private void agregarProductoACompra() {
        System.out.print("ID de la compra: ");
        int idCompra = leerEntero();
        System.out.print("ID del producto: ");
        int idProducto = leerEntero();
        System.out.print("Cantidad: ");
        int cantidad = leerEntero();
        System.out.print("Costo unitario: ");
        double costo = leerDouble();

        String error = detalleCompraController.agregarProductoACompra(idCompra, idProducto, cantidad, costo);
        System.out.println(error == null ? "Producto agregado a la compra. Total recalculado." : error);
    }

    private void mostrarDetallesCompra() {
        System.out.print("ID de la compra: ");
        int idCompra = leerEntero();
        List<DetalleCompra> detalles = detalleCompraController.mostrarDetalles(idCompra);
        if (detalles.isEmpty()) {
            System.out.println("Esta compra no tiene detalles registrados.");
            return;
        }
        for (DetalleCompra d : detalles) {
            System.out.println(d);
        }
    }

    private void confirmarCompra() {
        System.out.print("ID de la compra a confirmar: ");
        int idCompra = leerEntero();
        String error = compraController.confirmarCompra(idCompra);
        System.out.println(error == null ? "Compra confirmada." : error);
    }

    private void cancelarCompra() {
        System.out.print("ID de la compra a cancelar: ");
        int idCompra = leerEntero();
        String error = compraController.cancelarCompra(idCompra);
        System.out.println(error == null ? "Compra cancelada." : error);
    }

    private void exportarEntradas() {
        System.out.print("ID de la compra confirmada a exportar: ");
        int idCompra = leerEntero();
        String error = detalleCompraController.exportarEntradasInventario(idCompra);
        System.out.println(error == null ? "entradas_inventario.json generado correctamente." : error);
    }

    private int leerEntero() {
        while (!sc.hasNextInt()) {
            System.out.print("Ingresa un numero valido: ");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine(); // limpia el salto de linea pendiente
        return valor;
    }

    private double leerDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingresa un numero valido: ");
            sc.next();
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }
}
