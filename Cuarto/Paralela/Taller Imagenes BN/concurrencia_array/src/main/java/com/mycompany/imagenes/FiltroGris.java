package com.mycompany.imagenes;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Hilo trabajador que procesa un ARRAY de imágenes asignadas.
 */
public class FiltroGris implements Runnable {
    
    // CAMBIO: Ahora usamos File[] en lugar de List<File>
    private final File[] archivosAsignados; 
    private final File carpetaSalida;

    // Constructor: Recibe el array de archivos para este hilo específico
    public FiltroGris(File[] archivosAsignados, File carpetaSalida) {
        this.archivosAsignados = archivosAsignados;
        this.carpetaSalida = carpetaSalida;
    }

    @Override
    public void run() {
        // Iteramos sobre el array (el foreach funciona igual para arrays)
        if (archivosAsignados != null) {
            for (File archivo : archivosAsignados) {
                if (archivo != null) {
                    procesarImagen(archivo);
                }
            }
        }
        System.out.println("--> Hilo " + Thread.currentThread().getId() + " terminó su lista de trabajo.");
    }

    private void procesarImagen(File archivoEntrada) {
        try {
            BufferedImage imagen = ImageIO.read(archivoEntrada);
            
            if (imagen == null) return;

            int ancho = imagen.getWidth();
            int alto = imagen.getHeight();

            // Conversión a blanco y negro
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel = imagen.getRGB(x, y);
                    int rojo = (pixel >> 16) & 0xff;
                    int verde = (pixel >> 8) & 0xff;
                    int azul = pixel & 0xff;
                    int gris = (rojo + verde + azul) / 3;
                    int nuevoPixel = (gris << 16) | (gris << 8) | gris;
                    imagen.setRGB(x, y, nuevoPixel);
                }
            }

            // Guardar
            File archivoSalida = new File(carpetaSalida, archivoEntrada.getName());
            ImageIO.write(imagen, "png", archivoSalida);

        } catch (Exception e) {
            System.out.println("Error en archivo: " + archivoEntrada.getName());
        }
    }
}