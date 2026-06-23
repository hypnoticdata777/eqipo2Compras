package com.empresa.compras.proveedor;

import java.util.List;

/**
 * Controller: recibe la solicitud desde el menu/App y la pasa al Service.
 * NO valida reglas de negocio, NO ejecuta SQL, NO abre archivos.
 */
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController() {
        this.proveedorService = new ProveedorService();
    }

    public String registrarProveedor(String nombre, String rfc, String telefono, String correo, String direccion) {
        Proveedor nuevo = new Proveedor(nombre, rfc, telefono, correo, direccion);
        return proveedorService.registrarProveedor(nuevo);
    }

    public List<Proveedor> mostrarProveedores() {
        return proveedorService.mostrarProveedores();
    }

    public Proveedor buscarProveedor(int idProveedor) {
        return proveedorService.buscarProveedor(idProveedor);
    }

    public String desactivarProveedor(int idProveedor) {
        return proveedorService.desactivarProveedor(idProveedor);
    }
}
