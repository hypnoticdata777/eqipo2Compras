package com.empresa.compras.proveedor;

/**
 * Modelo (Entidad) que representa la tabla `proveedor` en MySQL.
 *
 * Reglas del proyecto para esta capa (Model):
 *  - SOLO atributos, constructor, getters, setters, toString().
 *  - NO debe contener SQL, Scanner, menus, ni conexion a MySQL.
 */
public class Proveedor {

    private int idProveedor;
    private String nombre;
    private String rfc;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;

    // Constructor vacio - lo usa el Repository para reconstruir objetos vacios antes de llenarlos
    public Proveedor() {
    }

    // Constructor para un proveedor NUEVO (sin id todavia, MySQL lo autogenera al guardar)
    public Proveedor(String nombre, String rfc, String telefono, String correo, String direccion) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = true; // todo proveedor nuevo nace activo
    }

    // Constructor completo - lo usa el Repository para reconstruir un proveedor que vino de MySQL
    public Proveedor(int idProveedor, String nombre, String rfc, String telefono, String correo,
                      String direccion, boolean activo) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.rfc = rfc;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", rfc='" + rfc + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                '}';
    }
}
