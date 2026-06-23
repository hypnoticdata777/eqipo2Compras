package com.empresa.compras.proveedor;

import java.util.List;

/**
 * Service: aqui viven las validaciones y reglas de negocio del modulo Proveedor.
 * El Service llama al Repository SOLO despues de validar.
 *
 * Patron usado para reportar errores: los metodos que registran/modifican algo
 * regresan String. Si regresan null = todo salio bien. Si regresan texto = ese
 * es el mensaje de error que hay que mostrarle al usuario. Es un patron simple
 * y suficiente para este proyecto (sin necesidad de crear excepciones propias).
 */
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService() {
        this.proveedorRepository = new ProveedorRepository();
    }

    public String registrarProveedor(Proveedor proveedor) {
        normalizar(proveedor);
        String error = validar(proveedor);
        if (error != null) {
            return error;
        }

        if (proveedorRepository.existeRfc(proveedor.getRfc())) {
            return "Error: ya existe un proveedor registrado con ese RFC.";
        }

        proveedorRepository.guardar(proveedor);
        return null; // null = registro exitoso, sin errores
    }

    public List<Proveedor> mostrarProveedores() {
        return proveedorRepository.obtenerTodos();
    }

    public Proveedor buscarProveedor(int idProveedor) {
        return proveedorRepository.buscarPorId(idProveedor);
    }

    public String desactivarProveedor(int idProveedor) {
        Proveedor existente = proveedorRepository.buscarPorId(idProveedor);
        if (existente == null) {
            return "Error: no existe un proveedor con ese ID.";
        }
        proveedorRepository.desactivar(idProveedor);
        return null;
    }

    /**
     * Usado por CompraService para validar la regla "Proveedor existente"
     * sin que CompraService tenga que hablar directo con ProveedorRepository.
     */
    public boolean proveedorExiste(int idProveedor) {
        return proveedorRepository.existe(idProveedor);
    }

    private String validar(Proveedor proveedor) {
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            return "Error: el nombre del proveedor es obligatorio.";
        }
        if (proveedor.getRfc() == null || proveedor.getRfc().trim().isEmpty()) {
            return "Error: el RFC es obligatorio.";
        }
        if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
            return "Error: el telefono es obligatorio.";
        }
        if (proveedor.getCorreo() == null || !proveedor.getCorreo().contains("@")) {
            return "Error: el correo debe tener un formato valido (debe contener @).";
        }
        return null; // sin errores
    }

    private void normalizar(Proveedor proveedor) {
        if (proveedor.getNombre() != null) {
            proveedor.setNombre(proveedor.getNombre().trim());
        }
        if (proveedor.getRfc() != null) {
            proveedor.setRfc(proveedor.getRfc().trim().toUpperCase());
        }
        if (proveedor.getTelefono() != null) {
            proveedor.setTelefono(proveedor.getTelefono().trim());
        }
        if (proveedor.getCorreo() != null) {
            proveedor.setCorreo(proveedor.getCorreo().trim().toLowerCase());
        }
        if (proveedor.getDireccion() != null) {
            proveedor.setDireccion(proveedor.getDireccion().trim());
        }
    }
}
