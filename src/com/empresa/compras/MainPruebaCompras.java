package com.empresa.compras;

/**
 * Arnes de prueba SOLO para el Equipo 2 (Compras).
 * Esto NO es el App.java final del proyecto compartido. Es para correr y
 * probar el paquete "compras" de forma aislada, como pide la regla 11 del
 * documento: "Antes de integrar, cada equipo deberá probar sus clases de
 * forma independiente."
 *
 * Una vez que confirmes que todo funciona aqui, integras el caso 2 del
 * switch en el App.java compartido (ver comentario en ComprasMenu.java).
 */
public class MainPruebaCompras {
    public static void main(String[] args) {
        new ComprasMenu().mostrarMenu();
    }
}
