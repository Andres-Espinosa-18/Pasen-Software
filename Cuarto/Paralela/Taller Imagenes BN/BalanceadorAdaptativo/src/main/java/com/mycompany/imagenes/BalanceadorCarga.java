package com.mycompany.imagenes;

import java.io.File;
import java.util.List;

public class BalanceadorCarga {

    private final List<Worker> workers;

    public BalanceadorCarga(List<Worker> workers) {
        this.workers = workers;
    }

    // Asigna la imagen al worker con menor carga
    public void asignarImagen(File imagen) {
        Worker menosCargado = workers.get(0);

        for (Worker w : workers) {
            if (w.cargaActual() < menosCargado.cargaActual()) {
                menosCargado = w;
            }
        }

        menosCargado.agregarTrabajo(imagen);
    }
}
