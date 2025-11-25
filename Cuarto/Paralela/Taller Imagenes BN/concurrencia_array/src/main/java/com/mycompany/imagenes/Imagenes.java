package com.mycompany.imagenes;

import java.io.File;

public class Imagenes {

    public static void main(String[] args) {
        // 1. CONFIGURACIÓN
        String rutaEntrada = "img"; 
        String rutaSalida = "img_bln";
        int totalImagenesAProcesar = 100;
        int numeroDeHilos = 4; // REQUERIMIENTO: Usar 5 hilos

        File carpetaEntrada = new File(rutaEntrada); // Asegúrate de que coincida con tu carpeta real
        File carpetaSalida = new File(rutaSalida);

        // Validaciones iniciales de carpetas
        if (!carpetaEntrada.exists()) {
            System.out.println("Error: No existe la carpeta " + rutaEntrada);
            return;
        }
        if (!carpetaSalida.exists()) carpetaSalida.mkdir();

        // 2. OBTENER IMÁGENES (USANDO ARRAYS)
        // Filtramos solo jpg/png
        File[] todosLosArchivos = carpetaEntrada.listFiles((d, n) -> n.endsWith(".jpg") || n.endsWith(".png"));
        
        if (todosLosArchivos == null || todosLosArchivos.length == 0) {
            System.out.println("No se encontraron imágenes en la carpeta.");
            return;
        }

        // Ajustamos la cantidad si hay menos de 25 imágenes
        if (todosLosArchivos.length < totalImagenesAProcesar) {
            System.out.println("Advertencia: Se necesitan 25 imágenes, pero solo hay " + todosLosArchivos.length);
            totalImagenesAProcesar = todosLosArchivos.length;
        }

        // Creamos el array exacto de trabajo (recortamos el array original a 25 o menos)
        File[] archivosAProcesar = new File[totalImagenesAProcesar];
        // System.arraycopy(origen, inicioOrigen, destino, inicioDestino, cantidad)
        System.arraycopy(todosLosArchivos, 0, archivosAProcesar, 0, totalImagenesAProcesar);

        System.out.println("Procesando " + archivosAProcesar.length + " imágenes con " + numeroDeHilos + " hilos.");

        // 3. DIVIDIR EL TRABAJO Y CREAR HILOS
        Thread[] hilos = new Thread[numeroDeHilos];
        
        // Cálculo del tamaño del bloque
        int tamanoBloque = archivosAProcesar.length / numeroDeHilos; 
        
        long inicio = System.nanoTime();

        for (int i = 0; i < numeroDeHilos; i++) {
            // Calcular índices de inicio y fin para el sub-array
            int inicioIndice = i * tamanoBloque;
            int finIndice;
            
            // Si es el último hilo, que tome todo lo que sobre (por si la división no es exacta)
            if (i == numeroDeHilos - 1) {
                finIndice = archivosAProcesar.length;
            } else {
                finIndice = inicioIndice + tamanoBloque;
            }

            // Calculamos cuántos archivos le tocan a este hilo
            int cantidadParaHilo = finIndice - inicioIndice;

            // Validación: Solo crear hilo si hay archivos para procesar
            if (cantidadParaHilo > 0) {
                // Creamos un sub-array específico para este hilo
                File[] trabajoParaHilo = new File[cantidadParaHilo];
                
                // Copiamos del array principal al sub-array del hilo
                System.arraycopy(archivosAProcesar, inicioIndice, trabajoParaHilo, 0, cantidadParaHilo);
                
                System.out.println("Asignando al Hilo " + i + ": " + trabajoParaHilo.length + " imágenes.");

                // Crear y arrancar el hilo pasando el ARRAY
                hilos[i] = new Thread(new FiltroGris(trabajoParaHilo, carpetaSalida));
                hilos[i].start();
            }
        }

        // 4. ESPERAR A LOS HILOS (JOIN)
        for (int i = 0; i < numeroDeHilos; i++) {
            if (hilos[i] != null) {
                try {
                    hilos[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        long fin = System.nanoTime();
        System.out.println("--------------------------------------------------");
        System.out.println("Proceso finalizado exitosamente.");
        System.out.println("Tiempo total: " + (fin - inicio) / 1_000_000 + " ms");
    }
}