package traductores;

import java.awt.Point;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class TraductorLetras implements Runnable {

    private final String[] cod;
    private int posIni;
    private int posFin;
    private final LinkedTransferQueue colaPunt;
    private final LinkedTransferQueue colaTrad;
    private static final String[] diccionario = {"a", "b", "c", "d", "e", "f", "g",
        "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
        "v", "w", "x", "y", "z"};
    private final CountDownLatch finTrad;
    private final Semaphore sincTake;

    //NOTA: las mayúsculas son la posicion en el arreglo+26
    //Cuando recibe un 1, se fija en la posición de diccionario 1 y traduce a lo que esta ahi
    /*Cuando recibe cualquier numero menor a 26, se fija la posición de diccionario igual a 
     ese número y traduce. */
    //Cuando recibe cualquier numero mayor a 26 y menor a 53, se fija la posición de diccionario igual
    //a ese número y traduce la MAYUSCULA de lo que encuentra(si esta a, traduce A).
    //Si encuentra cualquier número mayor a 52 y menor o igual a 57, lo manda al traductor de puntuación.
    //Si encuentra un número mayor a 57 tira error
    /**
     * Constructor del TraductorLetras.
     *
     * @param cod
     * @param colaPunt
     * @param colaTrad
     * @param finTrad
     * @param sincTake
     */
    public TraductorLetras(String[] cod, LinkedTransferQueue colaPunt,
            LinkedTransferQueue colaTrad, CountDownLatch finTrad,
            Semaphore sincTake) {
        this.cod = cod;
        this.colaPunt = colaPunt;
        this.colaTrad = colaTrad;
        this.finTrad = finTrad;
        this.sincTake = sincTake;
    }

    /**
     * Cuerpo del hilo.
     */
    @Override
    public void run() {
        int letraSinTraducir;
        String letraTraducida;

        //repite hasta que el buffer colaTrad ya no tenga elementos
        while (!colaTrad.isEmpty()) {
            Point posiciones;
            try {
                posIni = 0;
                posFin = -1;
                //adquiere permiso para tomar un elemento del buffer colaTrad
                sincTake.acquire();
                if (!colaTrad.isEmpty()) {
                    //queda esperando hasta que haya algo en la cola de 
                    //subcadenas a traducir
                    posiciones = (Point) colaTrad.take();
                    posIni = (int) posiciones.getX();
                    posFin = (int) posiciones.getY();
                }
                //libera el permiso para que otro pueda tomar elementos del buffer
                sincTake.release();
                for (int i = posIni; i <= posFin; i++) {
                    //obtiene la subcadena a decodificar y la traduce
                    letraSinTraducir = Integer.parseInt(cod[i]);
                    if (letraSinTraducir >= 1 && letraSinTraducir <= 26) {
                        //traduce letras minusculas
                        letraTraducida = diccionario[letraSinTraducir - 1];
                        cod[i] = letraTraducida;
                    } else if (letraSinTraducir >= 27 && letraSinTraducir <= 52) {
                        //traduce letras mayusculas
                        letraTraducida = diccionario[letraSinTraducir - 27].toUpperCase();
                        cod[i] = letraTraducida;
                    } else if (letraSinTraducir >= 52 && letraSinTraducir <= 57) {
                        //envia el codigo a el traductor de puntuacion
                        colaPunt.add(i);
                    } else {
                        System.err.print("Error: número a traducir no está entre 1 y 57");
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(TraductorLetras.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //indica que el traductor finalizo
        finTrad.countDown();
    }

}
