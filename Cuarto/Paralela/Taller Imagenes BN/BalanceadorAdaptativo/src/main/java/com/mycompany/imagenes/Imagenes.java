package com.mycompany.imagenes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Imagenes {

    public static void main(String[] args) {

        // CONFIGURACIÓN
        String rutaEntrada = "../imagenes";
        String rutaSalida = "../imagen_gris";
        int numeroDeHilos = 8;

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

        // Crear workers
        List<Worker> workers = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();

        for (int i = 0; i < numeroDeHilos; i++) {
            Worker w = new Worker(carpetaSalida);
            Thread t = new Thread(w, "Worker-" + i);
            workers.add(w);
            hilos.add(t);
            t.start();
        }

        BalanceadorCarga balanceador = new BalanceadorCarga(workers);

        long inicio = System.nanoTime();

        // Distribución adaptativa del trabajo
        for (File img : imagenes) {
            balanceador.asignarImagen(img);
        }

        // Indicar fin de trabajos
        for (Worker w : workers) {
            w.detener();
        }

        // Esperar a que terminen los hilos
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long fin = System.nanoTime();

        System.out.println("--------------------------------------------------");
        System.out.println("Proceso finalizado correctamente.");
        System.out.println("Tiempo total: " + (fin - inicio) / 1_000_000 + " ms");
    }
}
