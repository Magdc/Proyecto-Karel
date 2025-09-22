import kareltherobot.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
public class MiPrimerRobot implements Directions{
    private final List<Racer> racers = new ArrayList<>();
/*public static void main(String [] args)
{
// Usamos el archivo que creamos del mundo
 World.readWorld("Mundo.kwld");
World.setVisible(true);
// Coloca el robot en la posición inicial del mundo (1,1),
// mirando al Este, sin ninguna sirena.
Robot Karel = new Robot(1, 1, East, 0);

Robot azul = new Robot(1,1,East,0, Color.blue);

// Mover el robot 4 pasos
Karel.move();
 azul.move();
Karel.move();
 azul.move();
Karel.move();
 azul.move();
Karel.move();
 azul.move();
// Recoger los 5 beepers
Karel.pickBeeper();
Karel.pickBeeper();
Karel.pickBeeper();
Karel.pickBeeper();
Karel.pickBeeper();
// Girar a la izquierda y salir de los muros
Karel.turnLeft();
 azul.turnLeft();
Karel.move();
 azul.move();
Karel.move();
 azul.move();
// Poner los beepers fuera de los muros
Karel.putBeeper();
Karel.putBeeper();
Karel.putBeeper();
Karel.putBeeper();
Karel.putBeeper();
// Ponerse en otra posición y apagar el robot
Karel.move();
 azul.move();
Karel.turnOff();
 azul.turnOff();

}*/

    // Crear los 28 robots que van en la zona azul
    // Las posiciones van de (1,7) a (4,1)
    private static void crearZonaAzul() {
        for (int i = 1; i < 5 ; i++) {
            for (int j = 7; j > 0; j--) {
                switch (i) {
                    case 1,3: new Racer(i, j, East, 0, Color.blue).recorridoAzul(); break;
                    case 2,4: new Racer(i, j, West, 0, Color.blue).recorridoAzul(); break;
                }
            }
        }
    }
    // Crear los 28 robots que van en la zona verde
    private static void crearZonaVerde() {
        for (int i = 12; i < 17 ; i++) {
            for (int j = 30; j > 22; j--) {

                switch (i) {
                    case 12:if (j == 28 || j == 29 )new Racer(i, j, West, 0, Color.green); break;
                    case 13, 15: new Racer(i, j, West, 0, Color.green); break;
                    case 14: new Racer(i, j, East, 0, Color.green); break;
                    case 16: if(j==30 || j == 29) new Racer(i, j,West, 0, Color.green);
                }
            }
        }
    }
    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        World.setDelay(20); // opcional para ver la animación más clara
        crearZonaAzul();
        crearZonaVerde();
        // Dos robots en la misma posición y orientación
        //Robot first  = new Robot(1, 1, East, 0);               // rojo por defecto
        //Robot second = new Robot(1, 1, East, 0, Color.blue);   // azul

        // Crea los hilos
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

