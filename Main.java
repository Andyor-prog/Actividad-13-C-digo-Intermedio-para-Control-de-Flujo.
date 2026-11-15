package act12_codigo_intermedio;

import java.time.LocalDateTime;

/*
  Cervantes Mora Juan Pablo
  Muñoz Madriz Nadya Janneth
  Ramirez Torres Jesus Adrian
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("ACTIVIDAD 12: CÓDIGO INTERMEDIO 3AC");
        System.out.println("Integrantes:");
        System.out.println("  Cervantes Mora Juan Pablo");
        System.out.println("  Muñoz Madriz Nadya Janneth");
        System.out.println("  Ramirez Torres Jesus Adrian\n");

        Generador3AC gen = new Generador3AC();

        System.out.println("Evaluando códigos de prueba fuente y generando 3AC \n");

        // Códigos de prueba ---

        // Asignación de constantes
        gen.procesarAsignacionConstante("x", "5");
        gen.procesarAsignacionConstante("y", "10");

        // Uso de variables y operaciones
        gen.procesarAsignacionExpresion("z", "x + y");

        // Expresiones anidadas
        gen.procesarAsignacionExpresion("total", "(a + b) * (c - d)");

        // Expresiones lógicas
        gen.procesarAsignacionExpresion("flag", "(x > y) && (z < 10)");

        // Gestión de errores (División por cero y Tipos)
        gen.validarDivisionCero("a", "b");
        gen.validarTipos("total", "10", "\"string\"");

        // Resultado
        gen.mostrarCodigoIntermedio();

        System.out.println("\n FIN - ACTIVIDAD 12");
        System.out.println("  Cervantes Mora Juan Pablo");
        System.out.println("  Muñoz Madriz Nadya Janneth");
        System.out.println("  Ramirez Torres Jesus Adrian\n");
    }
}