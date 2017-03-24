package traductores_exe;

import java.awt.Point;
import java.util.concurrent.*;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class TraductorLetras implements Runnable {

    private final String[] cod;
    private int posIni;
    private int posFin;
    private final LinkedTransferQueue colaPunt;
    private final Point tradPoint;
    private static final String[] diccionario = {"a", "b", "c", "d", "e", "f", "g",
        "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
        "v", "w", "x", "y", "z"};

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
     * @param tradPoint
     */
    public TraductorLetras(String[] cod, LinkedTransferQueue colaPunt,
            Point tradPoint) {
        this.cod = cod;
        this.colaPunt = colaPunt;
        this.tradPoint = tradPoint;
    }

    /**
     * Cuerpo del hilo.
     */
    @Override
    public void run() {
        int letraSinTraducir;
        String letraTraducida;
        posIni = (int) tradPoint.getX();
        posFin = (int) tradPoint.getY();
        for (int i = posIni; i <= posFin; i++) {
            //obtiene la subcadena a decodificar
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
    }

}
