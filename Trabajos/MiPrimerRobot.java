
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
                    case 12:if (j == 28 || j == 29 ){racers.add(new Racer(i, j, West, 0, Color.green));controller.ocuparPosicion(i,j);} break;
                    case 13, 15:{ racers.add(new Racer(i, j, West, 0, Color.green));controller.ocuparPosicion(i,j);} break;
                    case 14: {racers.add(new Racer(i, j, East, 0, Color.green));controller.ocuparPosicion(i,j);} break;
                    case 16: if(j==30 || j == 29) {racers.add(new Racer(i, j,West, 0, Color.green));controller.ocuparPosicion(i,j);}
                }
            }
        }
    }
    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        World.setDelay(6); // opcional para ver la animación más clara
        crearZonaAzul();
        crearZonaVerde();

        System.out.println(" //// PRIMERA VERSION DEL MAPA //// ");

        // Iniciar todos los robots
        for (Racer racer : racers) {
            new Thread(racer).start();
        }
        //crearZonaVerde();
        ;
        //NOTA TODO LOS METODOS DE ES TRAFFIC CONTROLLER ESTAN AL REVES CUANDO SE USEN SUS FUNCIONES TENER EN CUENTA
        //Racer racer1 = new Racer(13,23,East,0,Color.green);
        //Racer racer5 = new Racer(1,10,East,0,Color.blue);
        //controller.ocuparPosicion(1,12);
        //controller.ocuparPosicion(10,23);
        controller.imprimirMapa();
        //new Thread(racer1).start();
        //new Thread(racer5).start();


    }
}

