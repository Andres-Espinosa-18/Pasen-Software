package com.mycompany.imagenes;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Worker implements Runnable {

    private final BlockingQueue<File> cola;
    private final File carpetaSalida;
    private volatile boolean activo = true;

    public Worker(File carpetaSalida) {
        this.carpetaSalida = carpetaSalida;
        this.cola = new LinkedBlockingQueue<>();
    }

    // Agregar trabajo a la cola del hilo
    public void agregarTrabajo(File archivo) {
        cola.add(archivo);
    }

    // Tamaño de la cola (carga actual)
    public int cargaActual() {
        return cola.size();
    }

    // Indica que no habrá más trabajos
    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        try {
            while (activo || !cola.isEmpty()) {
                File archivo = cola.poll();
                if (archivo != null) {
                    FiltroGris.procesarImagen(archivo, carpetaSalida);
                } else {
                    Thread.sleep(10); // Evita consumo excesivo de CPU
                }
            }
            System.out.println("--> " + Thread.currentThread().getName() + " terminó.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
