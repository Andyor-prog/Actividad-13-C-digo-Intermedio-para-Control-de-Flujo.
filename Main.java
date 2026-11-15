package act12_codigo_intermedio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
  Cervantes Mora Juan Pablo
  Muñoz Madriz Nadya Janneth
  Ramirez Torres Jesus Adrian
 */
public class Main {
    private static final List<String> INTEGRANTES = Arrays.asList(
            "Cervantes Mora Juan Pablo",
            "Muñoz Madriz Nadya Janneth",
            "Ramirez Torres Jesus Adrian");

    public static void main(String[] args) {
        System.out.println("Integrantes: " + String.join(", ", INTEGRANTES));
        System.out.println("ACTIVIDAD 13: CÓDIGO INTERMEDIO PARA CONTROL DE FLUJO");
        System.out.println("Demostración: condicionales, ciclos, métodos y clases\n");

        Generador3AC generador = new Generador3AC();

        generador.emitir("# 1. Estructura if-else: asigna el mayor valor");
        generador.generarIfElse("x > y",
                Arrays.asList("z = x"),
                Arrays.asList("z = y"));

        generador.emitir("# 2. Estructura switch: selección por casos y default");
        Map<String, List<String>> casos = new LinkedHashMap<>();
        casos.put("1", Arrays.asList("result = \"Uno\""));
        casos.put("2", Arrays.asList("result = \"Dos\""));
        generador.generarSwitch("n", casos, Arrays.asList("result = \"Otro\""));

        generador.emitir("# 3. Ciclo while");
        generador.generarWhile("i < 10", Arrays.asList("i = i + 1"));

        generador.emitir("# 4. Ciclo for: inicialización, condición, cuerpo e incremento");
        generador.generarFor("i = 0", "i < 5", "i = i + 1",
                Arrays.asList("sum = sum + i"));

        generador.emitir("# 5. Ciclo do-while: condición comprobada al final");
        generador.generarDoWhile(Arrays.asList("count = count + 1"), "count < 10");

        generador.emitir("# 6. Definición y llamada de método");
        String etiquetaSuma = generador.generarDefinicionMetodo("suma",
                Arrays.asList("a", "b"), new ArrayList<String>(), "a + b");
        generador.generarLlamadaMetodo(etiquetaSuma, Arrays.asList("5", "3"), "result");

        generador.emitir("# 7. Clase, atributo, instancia y llamada a método de instancia");
        generador.generarDefinicionClase("Persona", Arrays.asList("edad"));
        generador.generarDefinicionMetodo("Persona", "celebrarCumpleanos",
                Arrays.asList("this"), Arrays.asList("this.edad = this.edad + 1"), null);
        generador.generarCreacionObjeto("p", "Persona");
        generador.generarLlamadaMetodoObjeto("p", "Persona", "celebrarCumpleanos",
                new ArrayList<String>(), null);

        generador.emitir("# 8. Ejemplos de validación semántica");
        generador.validarDivisionCero("a", "b");
        generador.validarTipos("total", "10", "\"texto\"");

        System.out.println();
        generador.mostrarCodigoIntermedio();
        System.out.println("\nFIN DE LA EJECUCIÓN");
        System.out.println("Integrantes: " + String.join(", ", INTEGRANTES));
    }
}
