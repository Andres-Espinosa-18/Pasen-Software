package com.mycompany.imagenes;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FiltroGris {

    public static void procesarImagen(File archivoEntrada, File carpetaSalida) {
        try {
            BufferedImage imagen = ImageIO.read(archivoEntrada);
            if (imagen == null) return;

            int ancho = imagen.getWidth();
            int alto = imagen.getHeight();

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

            File archivoSalida = new File(carpetaSalida, archivoEntrada.getName());
            ImageIO.write(imagen, "png", archivoSalida);

        } catch (Exception e) {
            System.out.println("Error procesando: " + archivoEntrada.getName());
        }
    }
}
