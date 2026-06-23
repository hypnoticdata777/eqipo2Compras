package com.empresa.json.exportador;

import com.empresa.compras.detallecompra.DetalleCompra;
import com.empresa.json.dto.EntradaInventarioDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Genera el archivo exportaciones/entradas_inventario.json a partir de los
 * detalles de una compra CONFIRMADA.
 *
 * El JSON se construye a mano con StringBuilder para NO depender de la
 * libreria Gson. Si tu equipo ya tiene lib/gson.jar agregado al proyecto
 * compartido, esto se puede reemplazar por Gson.toJson(lista) sin tocar
 * el DTO (la estructura del contrato no cambia, solo cambiaria como se
 * escribe a disco).
 */
public class ExportadorEntradasInventario {

    private static final String RUTA_SALIDA = "exportaciones/entradas_inventario.json";

    public boolean exportar(List<DetalleCompra> detalles, int idCompra) {
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No hay detalles para exportar en la compra " + idCompra);
            return false;
        }

        String fechaHoy = LocalDate.now().toString();
        List<EntradaInventarioDTO> entradas = new ArrayList<>();
        for (DetalleCompra d : detalles) {
            entradas.add(new EntradaInventarioDTO(
                    d.getIdCompra(),
                    d.getIdProducto(),
                    d.getCantidad(),
                    d.getCostoUnitario(),
                    fechaHoy
            ));
        }

        String json = construirJson(entradas);

        try (FileWriter writer = new FileWriter(RUTA_SALIDA)) {
            writer.write(json);
            System.out.println("Archivo generado: " + RUTA_SALIDA);
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir entradas_inventario.json: " + e.getMessage());
            return false;
        }
    }

    private String construirJson(List<EntradaInventarioDTO> entradas) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < entradas.size(); i++) {
            EntradaInventarioDTO e = entradas.get(i);
            sb.append("  {\n");
            sb.append("    \"idMovimiento\": ").append(e.getIdMovimiento()).append(",\n");
            sb.append("    \"idCompra\": ").append(e.getIdCompra()).append(",\n");
            sb.append("    \"idProducto\": ").append(e.getIdProducto()).append(",\n");
            sb.append("    \"cantidad\": ").append(e.getCantidad()).append(",\n");
            sb.append("    \"costoUnitario\": ").append(e.getCostoUnitario()).append(",\n");
            sb.append("    \"fecha\": \"").append(e.getFecha()).append("\",\n");
            sb.append("    \"tipo\": \"").append(e.getTipo()).append("\"\n");
            sb.append("  }");
            if (i < entradas.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]\n");
        return sb.toString();
    }
}
