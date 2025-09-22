import kareltherobot.*;
import java.awt.Color;
class Racer extends Robot implements Runnable {
    private int beepers;
    private Color color;
    // Constructor
    public Racer(int street, int avenue, Direction direction, int beeps) {
        super(street, avenue, direction, beeps);
        beepers = beeps;
        World.setupThread(this);  // configura este robot para correr en un hilo
    }

    public Racer(int street, int avenue, Direction direction, int beeps, Color color) {
        super(street, avenue, direction, beeps, color);
        beepers = beeps;
        this.color = color;
        World.setupThread(this);
    }

    // Lógica del recorrido
    public void recorridoAzul() {
        if (frontIsClear()){
            move();

        } else{
            turnLeft();
        }
        if(nextToABeeper() &&  beepers< 4) {
            pickBeeper();
        }
    }
    public void recorridoVerde() {
        if (frontIsClear()){
            move();

        } else{
            turnLeft();
        }
         if(nextToABeeper() && beepers < 4) {
            pickBeeper();
        }
    }

    // Método que arranca el hilo
    public void run()
    {
        if (color == Color.blue) {
            recorridoAzul();
        }else{
            recorridoVerde();
        }
    }
}
