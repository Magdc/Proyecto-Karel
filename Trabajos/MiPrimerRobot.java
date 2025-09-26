
import kareltherobot.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
public class MiPrimerRobot implements Directions{
    private static final List<Racer> racers = new ArrayList<>();
    public static final TrafficController controller = new TrafficController(21, 31);

    // Crear los 28 robots que van en la zona azul
    // Las posiciones van de (1,7) a (4,1)
    private static void crearZonaAzul() {
        for (int i = 1; i < 5 ; i++) {
            for (int j = 7; j > 0; j--) {
                switch (i) {
                        case 1,3: {racers.add(new Racer(i, j, East, 0, Color.blue));controller.ocuparPosicion(i,j);} break;
                    case 2,4: {racers.add(new Racer(i, j, West, 0, Color.blue));controller.ocuparPosicion(i,j);} break;
                }
            }
        }
    }
    // Crear los 28 robots que van en la zona verde
    private static void crearZonaVerde() {
        for (int i = 12; i < 17 ; i++) {
            for (int j = 30; j > 22; j--) {

                switch (i) {
                    case 12:if (j == 28 || j == 29 )racers.add(new Racer(i, j, West, 0, Color.green));controller.ocuparPosicion(i,j); break;
                    case 13, 15: racers.add(new Racer(i, j, West, 0, Color.green));controller.ocuparPosicion(i,j); break;
                    case 14: racers.add(new Racer(i, j, East, 0, Color.green));controller.ocuparPosicion(i,j); break;
                    case 16: if(j==30 || j == 29) racers.add(new Racer(i, j,West, 0, Color.green));controller.ocuparPosicion(i,j);
                }
            }
        }
    }
    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        World.setDelay(3); // opcional para ver la animación más clara
        crearZonaAzul();
        System.out.println(" //// PRIMERA VERSION DEL MAPA //// ");
        controller.imprimirMapa();
        // Iniciar todos los robots
        for (Racer racer : racers) {
            new Thread(racer).start();
        }
        //crearZonaVerde();
        /*

        Racer racer1 = new Racer(1,7,East,0,Color.blue);
        Racer racer2 = new Racer(1,6,East,0,Color.blue);
        Racer racer3 = new Racer(1,5,East,0,Color.blue);
        //Racer racer2 = new Racer(7,2,East,Color.BLUE);
        //Racer racer3 = new Racer(7,3,East,Color.YELLOW);

        new Thread(racer1).start();
        new Thread(racer2).start();
        new Thread(racer3).start();
        //new Thread(racer2).start();
        //new Thread(racer3).start();
        // Dos robots en la misma posición y orientación
        //Robot first  = new Robot(1, 1, East, 0);               // rojo por defecto
        //Robot second = new Robot(1, 1, East, 0, Color.blue);   // azul

        // Crea los hilos/----
        //Thread t1 = new Thread(new RacerTask(first),  "Racer-1");
        //Thread t2 = new Thread(new RacerTask(second), "Racer-2");


        // (Opcional) esperar a que terminen
       /* try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }*/
    }
}

