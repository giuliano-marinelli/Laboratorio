package traductores;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Semaphore;
import tecladoIn.TecladoIn;

/**
 *
 * @author Bermudez Martin, Kurchan Ines, Marinelli Giuliano
 */
public class Cliente {

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        System.out.println("Ingrese enteros separados por espacios: ");
        String cadena = TecladoIn.readLine();
        System.out.println("Ingrese cantidad de traductores: ");
        int cantTrad = TecladoIn.readLineInt();
        System.out.println("Ingrese tamaño maximo de subcadena: ");
        int tamSubcadena = TecladoIn.readLineInt();
        String[] cod = cadena.split(" ");
        //buffer entre el cliente y los traductores de letras
        LinkedTransferQueue<Point> colaTrad = new LinkedTransferQueue();
        //buffer entre los traductores de letras y el traductor de puntuacion
        LinkedTransferQueue<Integer> colaPunt = new LinkedTransferQueue();
        //genera el rango de valores que va a traducir cada traductor letras y 
        //los pone en el buffer colaTrad
        int i = 0;
        while (i < cod.length) {
            int j = random.nextInt(tamSubcadena) + i;
            if (j > cod.length - 1) {
                j = cod.length - 1;
            }
            colaTrad.put(new Point(i, j));
            i = j + 1;
        }
        //imprimir subcadenas para los traductores
        //System.out.println(colaTrad.toString());
        //indica que los traductores de letras terminaron
        CountDownLatch finTrad = new CountDownLatch(cantTrad);
        //indica que el traductor de puntuacion termino
        CountDownLatch finPunt = new CountDownLatch(1);
        //utilizado para sincronizar el acceso de los traductores de letras al
        //buffer colaTrad
        Semaphore sincTake = new Semaphore(1);
        //crea y lanza el traductor de puntuacion
        TraductorPuntuacion tp = new TraductorPuntuacion(cod, colaPunt, finPunt);
        Thread creatortp = new Thread(tp);
        creatortp.start();
        //crea y lanza los traductores de letras
        for (int k = 0; k < cantTrad; k++) {
            TraductorLetras tl = new TraductorLetras(cod, colaPunt, colaTrad, finTrad, sincTake);
            Thread creatortl = new Thread(tl);
            creatortl.start();
        }
        //cuando terminen todos los hilos traductores de letras, se romperá la 
        //barrera finTrad
        finTrad.await();
        //cuando el hilo traductor de puntuacion reciba -1 al llamar al método 
        //take de colaPunt, terminará
        colaPunt.add(-1);

        //espera a que el traductor de puntuacion termine
        finPunt.await();

        //imprime los resultados de la traduccion
        System.out.print("Resultado de traduccion: ");
        System.out.print("[");
        for (int j = 0; j < cod.length; j++) {
            System.out.print(cod[j]);
        }
        System.out.println("]");
    }

}
