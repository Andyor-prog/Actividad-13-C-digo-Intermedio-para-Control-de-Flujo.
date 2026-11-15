package act12_codigo_intermedio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
  Cervantes Mora Juan Pablo
  Muñoz Madriz Nadya Janneth
  Ramirez Torres Jesus Adrian
 */
public class Generador3AC {
    private int contadorTemporales;
    private int contadorEtiquetas;
    private final List<String> instrucciones = new ArrayList<>();

    public String nuevoTemporal() {
        return "t" + (++contadorTemporales);
    }

    public void emitir(String instruccion) {
        validarTexto(instruccion, "instrucción");
        instrucciones.add(instruccion.trim());
    }

    public List<String> obtenerInstrucciones() {
        return Collections.unmodifiableList(new ArrayList<>(instrucciones));
    }

    public void procesarAsignacionConstante(String variable, String valor) {
        validarIdentificador(variable, "variable");
        validarTexto(valor, "valor");
        emitir(variable + " = " + valor);
    }

    public void procesarAsignacionExpresion(String variable, String expresionFuente) {
        validarIdentificador(variable, "variable");
        validarTexto(expresionFuente, "expresión");
        String temporal = nuevoTemporal();
        emitir(temporal + " = " + expresionFuente);
        emitir(variable + " = " + temporal);
    }

    public void validarDivisionCero(String numerador, String denominador) {
        validarTexto(numerador, "numerador");
        validarTexto(denominador, "denominador");

        String etiquetaError = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();
        String temporal = nuevoTemporal();
        emitir("if " + denominador + " == 0 goto " + etiquetaError);
        emitir(temporal + " = " + numerador + " / " + denominador);
        emitir("result = " + temporal);
        emitir("goto " + etiquetaFin);
        emitir(etiquetaError + ":");
        emitir("error \"Division by zero\"");
        emitir(etiquetaFin + ":");
    }

    public void validarTipos(String variable, String operando1, String operando2) {
        validarIdentificador(variable, "variable");
        validarTexto(operando1, "primer operando");
        validarTexto(operando2, "segundo operando");

        boolean operando1Cadena = esLiteralCadena(operando1);
        boolean operando2Cadena = esLiteralCadena(operando2);
        if (operando1Cadena != operando2Cadena) {
            emitir("error \"Type mismatch\" # entero y cadena para " + variable);
            return;
        }
        procesarAsignacionExpresion(variable, operando1 + " + " + operando2);
    }

    public void generarIfElse(String condicion, List<String> bloqueIf, List<String> bloqueElse) {
        validarTexto(condicion, "condición");
        validarBloque(bloqueIf, "bloque if");
        validarBloque(bloqueElse, "bloque else");

        String temporal = nuevoTemporal();
        String etiquetaIf = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();
        emitir(temporal + " = " + condicion);
        emitir("if " + temporal + " goto " + etiquetaIf);
        emitirBloque(bloqueElse);
        emitir("goto " + etiquetaFin);
        emitir(etiquetaIf + ":");
        emitirBloque(bloqueIf);
        emitir(etiquetaFin + ":");
    }

    public void generarSwitch(String selector, Map<String, List<String>> casos,
            List<String> bloqueDefault) {
        validarTexto(selector, "selector");
        if (casos == null) {
            throw new IllegalArgumentException("Los casos de switch no pueden ser null.");
        }
        validarBloque(bloqueDefault, "bloque default");
        for (Map.Entry<String, List<String>> caso : casos.entrySet()) {
            validarTexto(caso.getKey(), "valor de case");
            validarBloque(caso.getValue(), "bloque case");
        }

        String temporal = nuevoTemporal();
        String etiquetaFin = nuevaEtiqueta();
        String etiquetaDefault = nuevaEtiqueta();
        emitir(temporal + " = " + selector);

        List<String> etiquetasCasos = new ArrayList<>();
        for (Map.Entry<String, List<String>> caso : casos.entrySet()) {
            String etiqueta = nuevaEtiqueta();
            etiquetasCasos.add(etiqueta);
            emitir("if " + temporal + " == " + caso.getKey() + " goto " + etiqueta);
        }
        emitir("goto " + etiquetaDefault);

        int indice = 0;
        for (Map.Entry<String, List<String>> caso : casos.entrySet()) {
            emitir(etiquetasCasos.get(indice++) + ":");
            emitirBloque(caso.getValue());
            emitir("goto " + etiquetaFin);
        }
        emitir(etiquetaDefault + ":");
        emitirBloque(bloqueDefault);
        emitir(etiquetaFin + ":");
    }

    public void generarWhile(String condicion, List<String> cuerpo) {
        validarTexto(condicion, "condición");
        validarBloque(cuerpo, "cuerpo del while");

        String etiquetaInicio = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();
        String temporal = nuevoTemporal();
        emitir(etiquetaInicio + ":");
        emitir(temporal + " = " + condicion);
        emitir("if not " + temporal + " goto " + etiquetaFin);
        emitirBloque(cuerpo);
        emitir("goto " + etiquetaInicio);
        emitir(etiquetaFin + ":");
    }

    public void generarFor(String inicializacion, String condicion, String incremento,
            List<String> cuerpo) {
        validarTexto(inicializacion, "inicialización del for");
        validarTexto(condicion, "condición del for");
        validarTexto(incremento, "incremento del for");
        validarBloque(cuerpo, "cuerpo del for");

        String etiquetaInicio = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();
        String temporal = nuevoTemporal();
        emitir(inicializacion);
        emitir(etiquetaInicio + ":");
        emitir(temporal + " = " + condicion);
        emitir("if not " + temporal + " goto " + etiquetaFin);
        emitirBloque(cuerpo);
        emitir(incremento);
        emitir("goto " + etiquetaInicio);
        emitir(etiquetaFin + ":");
    }

    public void generarDoWhile(List<String> cuerpo, String condicion) {
        validarBloque(cuerpo, "cuerpo del do-while");
        validarTexto(condicion, "condición del do-while");

        String etiquetaInicio = nuevaEtiqueta();
        String temporal = nuevoTemporal();
        emitir(etiquetaInicio + ":");
        emitirBloque(cuerpo);
        emitir(temporal + " = " + condicion);
        emitir("if " + temporal + " goto " + etiquetaInicio);
    }

    public String generarDefinicionMetodo(String nombre, List<String> parametros,
            List<String> cuerpo, String expresionRetorno) {
        return generarDefinicionMetodo(null, nombre, parametros, cuerpo, expresionRetorno);
    }

    public String generarDefinicionMetodo(String clase, String nombre, List<String> parametros,
            List<String> cuerpo, String expresionRetorno) {
        validarIdentificador(nombre, "nombre de método");
        validarIdentificadores(parametros, "parámetro");
        validarBloque(cuerpo, "cuerpo del método");
        if (clase != null) {
            validarIdentificador(clase, "nombre de clase");
        }

        String etiquetaMetodo = etiquetaMetodo(clase, nombre);
        emitir(etiquetaMetodo + ":");
        for (String parametro : parametros) {
            emitir("param " + parametro);
        }
        emitirBloque(cuerpo);
        if (expresionRetorno == null || expresionRetorno.trim().isEmpty()) {
            emitir("return");
        } else {
            String temporal = nuevoTemporal();
            emitir(temporal + " = " + expresionRetorno);
            emitir("return " + temporal);
        }
        return etiquetaMetodo;
    }

    public void generarLlamadaMetodo(String etiquetaMetodo, List<String> argumentos,
            String variableResultado) {
        validarIdentificador(etiquetaMetodo, "etiqueta de método");
        validarParametros(argumentos);
        if (variableResultado != null) {
            validarIdentificador(variableResultado, "variable de resultado");
        }

        for (String argumento : argumentos) {
            emitir("param " + argumento);
        }
        if (variableResultado == null) {
            emitir("call " + etiquetaMetodo + ", " + argumentos.size());
        } else {
            String temporal = nuevoTemporal();
            emitir(temporal + " = call " + etiquetaMetodo + ", " + argumentos.size());
            emitir(variableResultado + " = " + temporal);
        }
    }

    public void generarDefinicionClase(String nombre, List<String> atributos) {
        validarIdentificador(nombre, "nombre de clase");
        validarIdentificadores(atributos, "atributo");
        emitir("# clase " + nombre);
        for (String atributo : atributos) {
            emitir("# atributo " + nombre + "." + atributo);
        }
    }

    public void generarCreacionObjeto(String variable, String clase) {
        validarIdentificador(variable, "variable de objeto");
        validarIdentificador(clase, "nombre de clase");
        emitir(variable + " = new " + clase);
    }

    public void generarLlamadaMetodoObjeto(String variableObjeto, String clase, String metodo,
            List<String> argumentos, String variableResultado) {
        validarIdentificador(variableObjeto, "variable de objeto");
        validarIdentificador(clase, "nombre de clase");
        validarIdentificador(metodo, "nombre de método");
        if (argumentos == null) {
            throw new IllegalArgumentException("Los argumentos no pueden ser null.");
        }
        List<String> argumentosConReceptor = new ArrayList<>();
        argumentosConReceptor.add(variableObjeto);
        argumentosConReceptor.addAll(argumentos);
        generarLlamadaMetodo(etiquetaMetodo(clase, metodo), argumentosConReceptor,
                variableResultado);
    }

    public void mostrarCodigoIntermedio() {
        System.out.println("CÓDIGO INTERMEDIO DE TRES DIRECCIONES (3AC)");
        for (String instruccion : instrucciones) {
            System.out.println(instruccion);
        }
    }

    private String nuevaEtiqueta() {
        return "L" + (++contadorEtiquetas);
    }

    private String etiquetaMetodo(String clase, String nombre) {
        return clase == null ? "metodo_" + nombre : "metodo_" + clase + "_" + nombre;
    }

    private boolean esLiteralCadena(String valor) {
        String texto = valor.trim();
        return texto.length() >= 2 && texto.startsWith("\"") && texto.endsWith("\"");
    }

    private void emitirBloque(List<String> bloque) {
        for (String instruccion : bloque) {
            emitir(instruccion);
        }
    }

    private void validarParametros(List<String> valores) {
        if (valores == null) {
            throw new IllegalArgumentException("La lista no puede ser null.");
        }
        for (String valor : valores) {
            validarTexto(valor, "elemento de la lista");
        }
    }

    private void validarIdentificadores(List<String> valores, String nombre) {
        if (valores == null) {
            throw new IllegalArgumentException("La lista de " + nombre + "s no puede ser null.");
        }
        for (String valor : valores) {
            validarIdentificador(valor, nombre);
        }
    }

    private void validarBloque(List<String> bloque, String nombre) {
        if (bloque == null) {
            throw new IllegalArgumentException("El " + nombre + " no puede ser null.");
        }
        validarParametros(bloque);
    }

    private void validarIdentificador(String valor, String nombre) {
        validarTexto(valor, nombre);
        int primerCaracter = valor.codePointAt(0);
        if (!Character.isJavaIdentifierStart(primerCaracter)) {
            throw new IllegalArgumentException("Identificador inválido para " + nombre + ": " + valor);
        }
        for (int indice = Character.charCount(primerCaracter); indice < valor.length();) {
            int caracter = valor.codePointAt(indice);
            if (!Character.isJavaIdentifierPart(caracter)) {
                throw new IllegalArgumentException("Identificador inválido para " + nombre + ": " + valor);
            }
            indice += Character.charCount(caracter);
        }
    }

    private void validarTexto(String valor, String nombre) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor de " + nombre + " no puede estar vacío.");
        }
    }
}
