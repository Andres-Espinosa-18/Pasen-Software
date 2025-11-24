package ec.edu.espe.concurrencia1imagennnucleos;

import java.awt.image.BufferedImage;

/**
 *
 * @author Pilatasig David
 */
public class ProcesadorBN implements Runnable{

    private BufferedImage imagen;
    private int filaInicial;
    private int filaFinal;
    
    ProcesadorBN(BufferedImage imagen, int filaInicial, int filaFinal){
        this.imagen=imagen;
        this.filaInicial=filaInicial;
        this.filaFinal=filaFinal;
    }
    
    @Override
    public void run() {
        int width=imagen.getWidth();
        for(int y=filaInicial; y<filaFinal;y++){
            for(int x=0;x<width;x++){
                
                int pixel=imagen.getRGB(x, y);
                int alpha=(pixel>>24)&0xff;
                int red=(pixel>>16)&0xff;
                int green=(pixel>>8)&0xff;
                int blue=pixel&0xff;
                
                int gray=(red+green+blue)/3;
                int newPixel=(alpha<<24)|(gray<<16)|(gray<<8)|gray;
                
                imagen.setRGB(x, y, newPixel);
            }
        }
    }
    
}
