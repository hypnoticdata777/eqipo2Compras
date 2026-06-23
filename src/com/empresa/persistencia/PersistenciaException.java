package com.empresa.persistencia;

/**
 * Error de infraestructura que permite distinguir una falla de MySQL de una
 * validacion de negocio. Los Repository lanzan esta excepcion y la capa de
 * menu decide como presentarla al usuario.
 */
public class PersistenciaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
