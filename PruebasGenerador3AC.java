package act12_codigo_intermedio;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
  Cervantes Mora Juan Pablo
  Muñoz Madriz Nadya Janneth
  Ramirez Torres Jesus Adrian
 */
public class PruebasGenerador3AC {
    public static void main(String[] args) {
        System.out.println("Integrantes: Cervantes Mora Juan Pablo, Muñoz Madriz Nadya Janneth, "
                + "Ramirez Torres Jesus Adrian");
        verificarGeneracionDeEstructuras();
        verificarEntradasInvalidas();
        System.out.println("PRUEBAS: todas las pruebas válidas e inválidas fueron aprobadas.");
        System.out.println("Integrantes: Cervantes Mora Juan Pablo, Muñoz Madriz Nadya Janneth, "
                + "Ramirez Torres Jesus Adrian");
    }

    private static void verificarGeneracionDeEstructuras() {
        Generador3AC generador = new Generador3AC();
        generador.generarIfElse("x > y", Arrays.asList("z = x"), Arrays.asList("z = y"));

        Map<String, List<String>> casos = new LinkedHashMap<>();
        casos.put("1", Arrays.asList("resultado = \"uno\""));
        casos.put("2", Arrays.asList("resultado = \"dos\""));
        generador.generarSwitch("opcion", casos, Arrays.asList("resultado = \"otro\""));

        generador.generarWhile("i < 10", Arrays.asList("i = i + 1"));
        generador.generarFor("j = 0", "j < 5", "j = j + 1",
                Arrays.asList("suma = suma + j"));
        generador.generarDoWhile(Arrays.asList("k = k + 1"), "k < 3");

        String suma = generador.generarDefinicionMetodo("suma", Arrays.asList("a", "b"),
                Arrays.asList(), "a + b");
        generador.generarLlamadaMetodo(suma, Arrays.asList("5", "3"), "resultado");

        generador.generarDefinicionClase("Persona", Arrays.asList("edad"));
        generador.generarDefinicionMetodo("Persona", "cumplir",
                Arrays.asList("this"), Arrays.asList("this.edad = this.edad + 1"), null);
        generador.generarCreacionObjeto("persona", "Persona");
        generador.generarLlamadaMetodoObjeto("persona", "Persona", "cumplir",
                Arrays.asList(), null);
        generador.validarDivisionCero("a", "b");
        generador.validarTipos("total", "10", "\"texto\"");
        generador.validarTipos("frase", "\"hola\"", "\"mundo\"");

        String codigo = String.join("\n", generador.obtenerInstrucciones());
        comprobarContiene(codigo, "if t1 goto L1", "if-else debe saltar al bloque verdadero");
        comprobarContiene(codigo, "if t2 == 1 goto", "switch debe comparar el primer caso");
        comprobarContiene(codigo, "resultado = \"otro\"", "switch debe emitir el default");
        comprobarContiene(codigo, "if not t3 goto", "while debe tener salto de salida");
        comprobarContiene(codigo, "if not t4 goto", "for debe probar su condición");
        comprobarContiene(codigo, "if t5 goto", "do-while debe probar la condición al final");
        comprobarContiene(codigo, "param a", "la definición del método debe recibir parámetros");
        comprobarContiene(codigo, "call metodo_suma, 2", "la llamada debe enviar sus argumentos");
        comprobarContiene(codigo, "# clase Persona", "debe generar la definición de clase");
        comprobarContiene(codigo, "persona = new Persona", "debe generar la creación del objeto");
        comprobarContiene(codigo, "call metodo_Persona_cumplir, 1",
                "la llamada de instancia debe enviar el receptor");
        comprobarContiene(codigo, "return t", "el método debe devolver su resultado");
        comprobarContiene(codigo, "error \"Division by zero\"",
                "la división debe detectar el denominador cero");
        comprobarContiene(codigo, "error \"Type mismatch\"",
                "la operación entre entero y cadena debe rechazarse");
        comprobarContiene(codigo, "frase = t", "operandos del mismo tipo deben aceptarse");
    }

    private static void verificarEntradasInvalidas() {
        final Generador3AC generador = new Generador3AC();
        comprobarRechazo(new Accion() {
            public void ejecutar() {
                generador.generarWhile(" ", Arrays.asList("x = 1"));
            }
        }, "condición vacía");
        comprobarRechazo(new Accion() {
            public void ejecutar() {
                generador.generarIfElse("x == 1", null, Arrays.asList("x = 0"));
            }
        }, "bloque if nulo");
        comprobarRechazo(new Accion() {
            public void ejecutar() {
                generador.generarDefinicionMetodo("no-es-un-identificador",
                        Arrays.asList(), Arrays.asList("x = 1"), null);
            }
        }, "nombre de método inválido");
        comprobarRechazo(new Accion() {
            public void ejecutar() {
                generador.generarDefinicionMetodo("metodo",
                        Arrays.asList("parámetro inválido"), Arrays.asList("x = 1"), null);
            }
        }, "parámetro inválido");
        comprobarRechazo(new Accion() {
            public void ejecutar() {
                generador.generarSwitch("x", null, Arrays.asList("x = 0"));
            }
        }, "casos nulos");
    }

    private static void comprobarContiene(String codigo, String fragmento, String mensaje) {
        if (!codigo.contains(fragmento)) {
            throw new AssertionError(mensaje + "; no se encontró: " + fragmento);
        }
    }

    private static void comprobarRechazo(Accion accion, String caso) {
        try {
            accion.ejecutar();
            throw new AssertionError("Se esperaba rechazar " + caso);
        } catch (IllegalArgumentException esperada) {
            // La entrada inválida fue rechazada según el contrato del generador.
        }
    }

    private interface Accion {
        void ejecutar();
    }
}
