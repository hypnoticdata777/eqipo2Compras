package com.empresa.json.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser pequeno para los contratos JSON planos del proyecto. Soporta objetos,
 * arreglos, cadenas con escapes, numeros, booleanos y null sin dependencias.
 */
public class JsonParser {

    private final String texto;
    private int posicion;

    public JsonParser(String texto) {
        this.texto = texto;
    }

    public Object parsear() {
        omitirEspacios();
        Object valor = leerValor();
        omitirEspacios();
        if (posicion != texto.length()) {
            throw error("Contenido adicional despues del JSON");
        }
        return valor;
    }

    private Object leerValor() {
        omitirEspacios();
        if (posicion >= texto.length()) {
            throw error("Se esperaba un valor");
        }

        char actual = texto.charAt(posicion);
        return switch (actual) {
            case '{' -> leerObjeto();
            case '[' -> leerArreglo();
            case '"' -> leerCadena();
            case 't' -> leerLiteral("true", Boolean.TRUE);
            case 'f' -> leerLiteral("false", Boolean.FALSE);
            case 'n' -> leerLiteral("null", null);
            default -> {
                if (actual == '-' || Character.isDigit(actual)) {
                    yield leerNumero();
                }
                throw error("Valor JSON no reconocido");
            }
        };
    }

    private Map<String, Object> leerObjeto() {
        Map<String, Object> objeto = new LinkedHashMap<>();
        consumir('{');
        omitirEspacios();
        if (intentarConsumir('}')) {
            return objeto;
        }

        while (true) {
            omitirEspacios();
            if (posicion >= texto.length() || texto.charAt(posicion) != '"') {
                throw error("Las propiedades deben ser cadenas");
            }
            String clave = leerCadena();
            omitirEspacios();
            consumir(':');
            objeto.put(clave, leerValor());
            omitirEspacios();
            if (intentarConsumir('}')) {
                return objeto;
            }
            consumir(',');
        }
    }

    private List<Object> leerArreglo() {
        List<Object> arreglo = new ArrayList<>();
        consumir('[');
        omitirEspacios();
        if (intentarConsumir(']')) {
            return arreglo;
        }

        while (true) {
            arreglo.add(leerValor());
            omitirEspacios();
            if (intentarConsumir(']')) {
                return arreglo;
            }
            consumir(',');
        }
    }

    private String leerCadena() {
        consumir('"');
        StringBuilder resultado = new StringBuilder();
        while (posicion < texto.length()) {
            char actual = texto.charAt(posicion++);
            if (actual == '"') {
                return resultado.toString();
            }
            if (actual != '\\') {
                resultado.append(actual);
                continue;
            }

            if (posicion >= texto.length()) {
                throw error("Escape incompleto");
            }
            char escape = texto.charAt(posicion++);
            switch (escape) {
                case '"' -> resultado.append('"');
                case '\\' -> resultado.append('\\');
                case '/' -> resultado.append('/');
                case 'b' -> resultado.append('\b');
                case 'f' -> resultado.append('\f');
                case 'n' -> resultado.append('\n');
                case 'r' -> resultado.append('\r');
                case 't' -> resultado.append('\t');
                case 'u' -> resultado.append(leerUnicode());
                default -> throw error("Escape no valido");
            }
        }
        throw error("Cadena sin cerrar");
    }

    private char leerUnicode() {
        if (posicion + 4 > texto.length()) {
            throw error("Escape unicode incompleto");
        }
        String hexadecimal = texto.substring(posicion, posicion + 4);
        posicion += 4;
        try {
            return (char) Integer.parseInt(hexadecimal, 16);
        } catch (NumberFormatException e) {
            throw error("Escape unicode no valido");
        }
    }

    private Number leerNumero() {
        int inicio = posicion;
        if (texto.charAt(posicion) == '-') {
            posicion++;
        }
        leerDigitos();
        boolean decimal = false;
        if (posicion < texto.length() && texto.charAt(posicion) == '.') {
            decimal = true;
            posicion++;
            leerDigitos();
        }
        if (posicion < texto.length() && (texto.charAt(posicion) == 'e' || texto.charAt(posicion) == 'E')) {
            decimal = true;
            posicion++;
            if (posicion < texto.length() && (texto.charAt(posicion) == '+' || texto.charAt(posicion) == '-')) {
                posicion++;
            }
            leerDigitos();
        }

        String numero = texto.substring(inicio, posicion);
        try {
            return decimal ? Double.parseDouble(numero) : Long.parseLong(numero);
        } catch (NumberFormatException e) {
            throw error("Numero no valido");
        }
    }

    private void leerDigitos() {
        int inicio = posicion;
        while (posicion < texto.length() && Character.isDigit(texto.charAt(posicion))) {
            posicion++;
        }
        if (inicio == posicion) {
            throw error("Se esperaba un digito");
        }
    }

    private Object leerLiteral(String literal, Object valor) {
        if (!texto.startsWith(literal, posicion)) {
            throw error("Literal no valido");
        }
        posicion += literal.length();
        return valor;
    }

    private void consumir(char esperado) {
        omitirEspacios();
        if (posicion >= texto.length() || texto.charAt(posicion) != esperado) {
            throw error("Se esperaba '" + esperado + "'");
        }
        posicion++;
    }

    private boolean intentarConsumir(char esperado) {
        if (posicion < texto.length() && texto.charAt(posicion) == esperado) {
            posicion++;
            return true;
        }
        return false;
    }

    private void omitirEspacios() {
        while (posicion < texto.length() && Character.isWhitespace(texto.charAt(posicion))) {
            posicion++;
        }
    }

    private IllegalArgumentException error(String mensaje) {
        return new IllegalArgumentException(mensaje + " en la posicion " + posicion + ".");
    }
}
