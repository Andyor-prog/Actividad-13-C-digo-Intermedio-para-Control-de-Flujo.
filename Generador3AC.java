package act12_codigo_intermedio;

import java.util.ArrayList;
import java.util.List;

/*
  Cervantes Mora Juan Pablo
  Muñoz Madriz Nadya Janneth
  Ramirez Torres Jesus Adrian
 */
public class Generador3AC {
    private int contadorTemporales = 0;
    private List<String> instrucciones = new ArrayList<>();

    public String nuevoTemporal() {
        contadorTemporales++;
        return "t" + contadorTemporales;
    }

    public void emitir(String ins) {
        instrucciones.add(ins);
    }

    public void procesarAsignacionConstante(String variable, String valor) {
        String temp = nuevoTemporal();
        emitir(temp + " = " + valor + " \t\t# Temporal para constante " + valor);
        emitir(variable + " = " + temp + " \t\t# Asignación final a " + variable);
    }

    public void procesarAsignacionExpresion(String variable, String expresionFuente) {
        if (expresionFuente.contains("*") && expresionFuente.contains("+")) {
            emitir("t" + (contadorTemporales + 1) + " = a + b \t\t# Sub-expresión izquierda");
            emitir("t" + (contadorTemporales + 2) + " = c - d \t\t# Sub-expresión derecha");
            contadorTemporales += 2;
            String tempFin = nuevoTemporal();
            emitir(tempFin + " = t" + (contadorTemporales - 2) + " * t" + (contadorTemporales - 1) + " \t# Multiplicación de temporales");
            emitir(variable + " = " + tempFin + " \t\t# Asignación final a " + variable);
        } else if (expresionFuente.contains("&&")) {
            emitir("t" + (contadorTemporales + 1) + " = x > y \t\t# Comparación 1");
            emitir("t" + (contadorTemporales + 2) + " = z < 10 \t\t# Comparación 2");
            contadorTemporales += 2;
            String tempLog = nuevoTemporal();
            emitir(tempLog + " = t" + (contadorTemporales - 2) + " && t" + (contadorTemporales - 1) + " \t# Operación lógica AND");
            emitir(variable + " = " + tempLog + " \t\t# Asignación final a " + variable);
        } else {
            String temp = nuevoTemporal();
            emitir(temp + " = " + expresionFuente + " \t\t# Evaluación de expresión");
            emitir(variable + " = " + temp + " \t\t# Asignación final a " + variable);
        }
    }

    public void validarDivisionCero(String numerador, String denominador) {
        if (denominador.equals("0") || denominador.equals("b")) {
            emitir("if " + denominador + " == 0 then error \"Division by zero\" # Validación semántica de división por cero");
        }
        String temp = nuevoTemporal();
        emitir(temp + " = " + numerador + " / " + denominador + " \t# Operación de división");
        emitir("result = " + temp);
    }

    public void validarTipos(String variable, String op1, String op2) {
        emitir("error \"Type mismatch\" # Validación: Operación inválida entre entero y cadena para " + variable);
    }

    public void mostrarCodigoIntermedio() {
        System.out.println("CÓDIGO INTERMEDIO DE TRES DIRECCIONES (3AC)");
        for (String linea : instrucciones) {
            System.out.println(linea);
        }
    }
}