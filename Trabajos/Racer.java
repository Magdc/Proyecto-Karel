
import kareltherobot.*;
import java.awt.Color;
class Racer extends Robot implements Runnable {
    private int beepers;
    private Color color;
    private Direction dir;
    private int street;
    private int avenue;
    private int[][] ruta;
    // Constructor
    public Racer(int street, int avenue, Direction direction, int beeps, Color color) {
        super(street, avenue, direction, beeps);
        this.beepers = beeps;
        this.color = color;
        this.dir= direction;
        this.street = street;
        this.avenue = avenue;
        this.ruta = TrafficController.rutaRapidaAzul;
        World.setupThread(this);  // configura este robot para correr en un hilo
    }

    public Racer(int street, int avenue, Direction direction, int beeps, Color color, int[][] ruta) {
        super(street, avenue, direction, beeps, color);
        this.beepers = beeps;
        this.color = color;
        this.dir= direction;
        this.street = street;
        this.avenue = avenue;
        this.ruta = ruta;
        World.setupThread(this);
    }
    // cálculo de la siguiente posición al moverse en sentido sur o norte
    private int nextStreet() {
        if (dir.equals(North)) {
            return street + 1;
        } else if (dir.equals(South)) {
            return street - 1;
        }
        return street;
    }
    private void cambiarValoreStreet(){
        if (dir.equals(North)) {
            this.street = street + 1;
        } else if (dir.equals(South)) {
            this.street = street - 1;
        }
    }
    private void cambiarValorAvenue(){
        if (dir.equals(East)) {
            this.avenue = avenue + 1;
        } else if (dir.equals(West)) {
            this.avenue = avenue - 1;
        }
    }

    // calculo de la siguiente posición al moverse en sentido este u oeste
    private int nextAvenue() {
        if (dir.equals(East)) {
            return avenue + 1;
        } else if (dir.equals(West)) {
            return avenue - 1;
        }
        return avenue;
    }
    public void giro() {
        if (facingEast()) {
            dir = East;
        } else if (facingWest()) {
            dir = West;
        } else if (facingNorth()) {
            dir = North;
        } else if (facingSouth()) {
            dir = South;
        }
    }

    // Lógica del recorrido
    public void recorridoAzul() {

        System.out.println("Recorrido azul tratando de seguir aca");
        seguirRuta(ruta);
    }
    public void safeMove() {
        MiPrimerRobot.controller.requestAndCommitMove(street, avenue, nextStreet(), nextAvenue(), this::move);
    }
    public void seguirRuta(int [][] ruta) {
        for (int i = 0; i < ruta.length-1; i++) {
            System.out.println("Llegue hasta aqui, voy a evaluar si "+
                    ruta[i][0] + " == "+ avenue +" y "+ ruta[i][1]+ " == "+ street);
            if ( avenue == ruta[i][0] && street == ruta[i][1]) {
                System.out.println("entre");
                System.out.println("Esta el frente sin nadie? " + frontIsClear() + " Proxima avenida: "
                        + nextAvenue() + " == " + ruta[i + 1][0] + " y Proxima calle: " + nextStreet() + " == " + ruta[i + 1][1]);
                if(nextToABeeper() &&  beepers< 4) {
                    System.out.println("me enloqueci con los beepers");
                    this.beepers = beepers + 1;
                    pickBeeper();
                    i = i-1;
                    System.out.println("Tengo beepers: " + beepers);
                }
                else if (frontIsClear() && nextAvenue() == ruta[i + 1][0] && nextStreet() == ruta[i + 1][1]) {
                    System.out.println("Me puedo mover");
                    safeMove();
                    System.out.println("Lo logre señor");
                    cambiarValorAvenue();
                    cambiarValoreStreet();
                } else {
                    i = i - 1;
                    turnLeft();
                    giro();
                }
            }
        }

    }

    // Método que arranca el hilo
    public void run()
    {
        if (color == Color.blue) {
            recorridoAzul();
        }else{
            //recorridoVerde();
        }
    }
}
