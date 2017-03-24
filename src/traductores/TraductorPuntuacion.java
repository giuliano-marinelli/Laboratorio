package traductores;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class TraductorPuntuacion implements Runnable {

    private final String[] index;
    private final String[] cod;
    private final LinkedTransferQueue<Integer> colaPunt;
    private final CountDownLatch finPunt;

    /**
     * Constructor del TraductorPuntuacion.
     *
     * @param cod
     * @param contenedor
     * @param finPunt
     */
    public TraductorPuntuacion(String[] cod, LinkedTransferQueue<Integer> contenedor,
            CountDownLatch finPunt) {
        index = new String[5];
        cargarIndex(index);
        this.cod = cod;
        this.colaPunt = contenedor;
        this.finPunt = finPunt;
    }

    /**
     * Carga el array que tiene el diccionario de puntuacion.
     *
     * @param ind
     */
    private void cargarIndex(String[] ind) {
        index[0] = ".";
        index[1] = ",";
        index[2] = "!";
        index[3] = "?";
        index[4] = "-";
    }

    /**
     * Cuerpo del hilo.
     *
     */
    @Override
    public void run() {
        try {
            int pos;
            String valorATraducir;
            String resultadoTraducido;
            //espera valores del buffer colaPunt hasta que se le envia un -1
            while ((pos = colaPunt.take()) != -1) {
                //traduce los simbolos
                valorATraducir = cod[pos];
                resultadoTraducido = index[Integer.parseInt(valorATraducir) - 53];
                cod[pos] = resultadoTraducido;
            }
            finPunt.countDown();
        } catch (InterruptedException ex) {
            Logger.getLogger(TraductorPuntuacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
