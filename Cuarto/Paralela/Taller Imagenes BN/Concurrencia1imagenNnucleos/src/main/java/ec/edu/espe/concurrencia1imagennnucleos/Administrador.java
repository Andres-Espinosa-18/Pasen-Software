package ec.edu.espe.concurrencia1imagennnucleos;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 *
 * @author Pilatasig David
 */
public class Administrador {
    private BufferedImage imagen;
    private int nHilos;
    
    public Administrador(BufferedImage imagen, int nHilos){
        this.imagen=imagen;
        this.nHilos=nHilos;
    }
    
    public void procesarImagen(ExecutorService pool) throws Exception{
        int height=imagen.getHeight();
        int filasPorHilo=height/nHilos;
        
        List<Future<?>> tareas= new ArrayList<>();
        
        for(int i=0; i<nHilos;i++){
            int filaInicial=i*filasPorHilo;
            int filaFinal=(i==nHilos-1)?height:filaInicial+filasPorHilo;
            ProcesadorBN tarea= new ProcesadorBN(imagen, filaInicial, filaFinal);
            tareas.add(pool.submit(tarea));
        }
        
        for(Future<?> f:tareas){
            f.get();
        }
    }
}
