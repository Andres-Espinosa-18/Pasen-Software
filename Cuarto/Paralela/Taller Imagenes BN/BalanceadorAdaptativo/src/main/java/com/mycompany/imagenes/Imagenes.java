package com.mycompany.imagenes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Imagenes {

    public static void main(String[] args) {

        // CONFIGURACIÓN
        String rutaEntrada = "../imagenes";
        String rutaSalida = "../imagen_gris";
        String rutaCSV = "../resultados.csv";
        int numeroDeHilos = 8;
        int ejecuciones=2;

        File carpetaEntrada = new File(rutaEntrada);
        File carpetaSalida = new File(rutaSalida);

        if (!carpetaEntrada.exists()) {
            System.out.println("Error: No existe la carpeta de entrada.");
            return;
        }
        if (!carpetaSalida.exists()) carpetaSalida.mkdir();

        // Cargar imágenes
        File[] imagenes = carpetaEntrada.listFiles(
                (d, n) -> n.toLowerCase().endsWith(".jpg") || n.toLowerCase().endsWith(".png")
        );

        if (imagenes == null || imagenes.length == 0) {
            System.out.println("No se encontraron imágenes.");
            return;
        }

        int totalImagenes = imagenes.length;

        // Crear CSV e imprimir encabezado
        try (FileWriter writer = new FileWriter(rutaCSV)) {
            writer.write("Ejecución,Hilos,Imagenes,Tiempo en ms\n");
        } catch (IOException e) {
            System.out.println("Error creando el archivo CSV.");
            return;
        }

        // ==============================
        // EJECUCIONES
        // ==============================
        for (int ejecucion = 1; ejecucion <= ejecuciones; ejecucion++) {

            List<Worker> workers = new ArrayList<>();
            List<Thread> hilos = new ArrayList<>();

            // Crear hilos
            for (int i = 0; i < numeroDeHilos; i++) {
                Worker w = new Worker(carpetaSalida);
                Thread t = new Thread(w, "Worker-" + i);
                workers.add(w);
                hilos.add(t);
                t.start();
            }

            BalanceadorCarga balanceador = new BalanceadorCarga(workers);

            long inicio = System.nanoTime();

            // Distribución adaptativa
            for (File img : imagenes) {
                balanceador.asignarImagen(img);
            }

            // Indicar fin de trabajos
            for (Worker w : workers) {
                w.detener();
            }

            // Esperar hilos
            for (Thread t : hilos) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long fin = System.nanoTime();
            long tiempoMs = (fin - inicio) / 1_000_000;

            // Guardar resultados en CSV
            try (FileWriter writer = new FileWriter(rutaCSV, true)) {
                writer.write(ejecucion + "," +
                             numeroDeHilos + "," +
                             totalImagenes + "," +
                             tiempoMs + "\n");
            } catch (IOException e) {
                System.out.println("Error escribiendo en el CSV.");
            }

            System.out.println("Ejecución " + ejecucion + " finalizada en " + tiempoMs + " ms");
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Todas las ejecuciones finalizaron.");
        System.out.println("Resultados guardados en: resultados.csv");
    }
}