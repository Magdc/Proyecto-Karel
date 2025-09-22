import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.CountDownLatch;
public class MiPrimerRobot implements Directions{
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
    
 private static volatile boolean GO = false;

    // Tarea del robot (misma ruta del ejercicio 2)
    static class RacerTask implements Runnable {
        private final Robot bot;

        RacerTask(Robot bot) {
            this.bot = bot;
        }

        @Override
        public void run() {
            // Espera activa (muy breve) hasta que el main libere la salida
            while (!GO) {
                Thread.yield(); // cede CPU en lo que llega la señal
            }

            // === Recorrido ===
            for (int i = 0; i < 4; i++) bot.move();         // avanza 4
            for (int i = 0; i < 2; i++) {
        
                    bot.pickBeeper();
                
                

            }
               // recoge 5
            bot.turnLeft();
            bot.move(); bot.move();                         // sale de los muros
            for (int i = 0; i < 2; i++) bot.putBeeper();    // deja 5
            bot.move();
            bot.turnOff();
        }
    }

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        // World.setDelay(20); // opcional para ver la animación más clara

        // Dos robots en la misma posición y orientación
        Robot first  = new Robot(1, 1, East, 0);               // rojo por defecto
        Robot second = new Robot(1, 1, East, 0, Color.blue);   // azul

        // Crea los hilos
        Thread t1 = new Thread(new RacerTask(first),  "Racer-1");
        Thread t2 = new Thread(new RacerTask(second), "Racer-2");

        // Los arrancamos (quedan en espera hasta que GO sea true)
        t1.start();
        t2.start();

        // Pequeña pausa opcional para asegurar que ambos llegaron al "espera"
        // try { Thread.sleep(5); } catch (InterruptedException ignored) {}

        // ¡Salida simultánea!
        GO = true;

        // (Opcional) esperar a que terminen
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}

