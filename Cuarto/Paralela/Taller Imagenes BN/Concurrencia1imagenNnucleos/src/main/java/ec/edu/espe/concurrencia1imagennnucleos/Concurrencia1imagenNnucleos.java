package ec.edu.espe.concurrencia1imagennnucleos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;

/**
 *
 * @author Usuario
 */
public class Concurrencia1imagenNnucleos{
    
    private static final int N_HILOS = 4;

    public static void main(String[] args) {

        try {
            File carpetaEntrada = new File("../imagenes");
            File carpetaSalida = new File("../imagen_gris");
            carpetaSalida.mkdirs();

            File[] imagenes = carpetaEntrada.listFiles();
            long tiempoTotal = 0;

            // Pool fijo de hilos
            ExecutorService pool = Executors.newFixedThreadPool(N_HILOS);

            for (File archivoImagen : imagenes) {

                BufferedImage imagen = ImageIO.read(archivoImagen);
                if (imagen == null) {
                    System.out.println("No se pudo cargar: " + archivoImagen.getName());
                    continue;
                }

                System.out.println("\nProcesando " + archivoImagen.getName());

                long inicio = System.nanoTime();

                // Delegamos el trabajo a otra clase
                Administrador admin = new Administrador(imagen, N_HILOS);

                // Ejecuta y espera a que todos los hilos terminen
                admin.procesarImagen(pool);

                long fin = System.nanoTime();

                // Guardar imagen convertida
                File archivoSalida = new File(carpetaSalida, archivoImagen.getName());
                ImageIO.write(imagen, "jpg", archivoSalida);
                /*
                System.out.println("Guardada: " + archivoImagen.getName());
                System.out.println("Tiempo: " + (fin - inicio) / 1_000_000 + " ms");
                */
                tiempoTotal += (fin - inicio) / 1_000_000;
                
            }

            System.out.println("\nTiempo total: " + tiempoTotal + " ms");

            // Cerrar pool
            pool.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
