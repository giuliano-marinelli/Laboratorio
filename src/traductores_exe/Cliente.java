package traductores_exe;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
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
        //buffer entre el cliente y los raductores de letras
        LinkedList<Point> arrayTrad = new LinkedList();
        //buffer entre los traductores de letras y el traductor de puntuacion
        LinkedTransferQueue<Integer> colaPunt = new LinkedTransferQueue();
        int i = 0;
        while (i < cod.length) {
            int j = random.nextInt(tamSubcadena) + i;
            if (j > cod.length - 1) {
                j = cod.length - 1;
            }
            arrayTrad.add(new Point(i, j));
            i = j + 1;
        }
        //imprimir subcadenas para los traductores
        //System.out.println(colaTrad.toString());
        //indica que el traductor de puntuacion termino
        CountDownLatch finPunt = new CountDownLatch(1);
        //crea y lanza el traductor de puntuacion
        TraductorPuntuacion tp = new TraductorPuntuacion(cod, colaPunt, finPunt);
        Thread creatortp = new Thread(tp);
        creatortp.start();

        //crea un executor con un pool de hilos traductores
        ExecutorService es = Executors.newFixedThreadPool(cantTrad);
        //envia los runnable traductor de letras con las subcadenas a traducir
        //al executor, este los asigna a los hilos del pool a medida que los
        //anteriores van concluyendo
        TraductorLetras tl;
        for (int k = 0; k < arrayTrad.size(); k++) {
            tl = new TraductorLetras(cod, colaPunt, arrayTrad.get(k));
            es.execute(tl);
        }
        //cuando terminen todos los hilos traductores de letras, el shutdown 
        //se desbloquea
        es.shutdown();
        //bloquea hasta que todos los hilos traductores de letras terminan
        es.awaitTermination(1, TimeUnit.MINUTES);
        //cuando el hilo traductor de puntuacion reciba -1 al llamar al método 
        //take de colaPunt, terminará.
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
