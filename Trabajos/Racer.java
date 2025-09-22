import kareltherobot.*;
import java.awt.Color;

class Racer extends Robot implements Runnable {
    public Racer(int street, int avenue, Direction direction, int beeps) {
        super(street, avenue, direction, beeps);
        World.setupThread(this);  // configura este robot para correr en un hilo
    }

    public Racer(int street, int avenue, Direction direction, int beeps, Color color) {
        super(street, avenue, direction, beeps, color);
        World.setupThread(this);
    }

    // Lógica del recorrido
    public void race() {
        // Avanza 4 pasos hasta los beepers
        for (int i = 0; i < 4; i++) move();

        // Recoge 5 beepers
        for (int i = 0; i < 5; i++) pickBeeper();

        // Gira a la izquierda y sale de los muros
        turnLeft();
        move();
        move();

        // Deja los 5 beepers
        for (int i = 0; i < 5; i++) putBeeper();

        // Se mueve y apaga
        move();
        turnOff();
    }

    // Método que arranca el hilo
    public void run() {
        race();
    }
}
