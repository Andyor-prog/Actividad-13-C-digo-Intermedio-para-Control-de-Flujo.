# Actividad 13: Código intermedio para control de flujo

Aplicación Java que genera código intermedio de tres direcciones (3AC) para
condicionales, selección, ciclos, métodos y objetos.

## Requisitos

- JDK 8 o posterior.
- Ejecutar los comandos desde la carpeta del proyecto.

## Compilar y ejecutar

En PowerShell:

```powershell
javac -encoding UTF-8 -d build Generador3AC.java Main.java PruebasGenerador3AC.java
java -cp build act12_codigo_intermedio.Main
```

La salida contiene ejemplos de `if-else`, `switch`, `while`, `for`,
`do-while`, definición/llamada de métodos, definición/uso de objetos y
validaciones semánticas. El archivo `salida-ejecucion.txt` contiene una
ejecución de referencia en UTF-8 para el reporte.

## Pruebas

```powershell
java -cp build act12_codigo_intermedio.PruebasGenerador3AC
```

Las pruebas comprueban las instrucciones generadas para cada estructura y que
se rechacen entradas inválidas como condiciones vacías, bloques nulos e
identificadores de método mal formados.
