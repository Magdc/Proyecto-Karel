
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
        if (color == Color.blue)
            this.ruta = TrafficController.rutaRapidaAzul;
        else{
            this.ruta = TrafficController.rutaRapidaVerde;
        }
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

    public void safeMove() {
        MiPrimerRobot.controller.requestAndCommitMove(street, avenue, nextStreet(), nextAvenue(), this::move);
    }
    
    public boolean safeMoveConSemaforo() {
        // Verificar si necesita adquirir un semáforo antes de moverse
        if (!MiPrimerRobot.controller.intentarAdquirirSemaforo(street, avenue)) {
            // No pudo adquirir el semáforo, debe esperar
            return false;
        }
        
        // Realizar el movimiento normal
        safeMove();
        cambiarValorAvenue();
        cambiarValoreStreet();
        
        // Liberar el semáforo si llegó a una posición de liberación
        MiPrimerRobot.controller.liberarSemaforo(street, avenue);
        return true;
    }

    private void switchRouteAzul() {
            ruta =  (this.ruta == TrafficController.rutaRapidaAzul) ? TrafficController.rutaLentaAzul
                    : TrafficController.rutaRapidaAzul;
        seguirRuta(ruta);
    }
    private void switchRouteVerde() {
        ruta = (this.ruta == TrafficController.rutaRapidaVerde) ? TrafficController.rutaLentaVerde
                : TrafficController.rutaRapidaVerde;
        seguirRuta(ruta);
    }

    private void switchColoraVerde() {
            ruta =  TrafficController.rutaRapidaVerde;
        seguirRuta(ruta);
    }
    private void switchColoraAzul() {
            ruta = TrafficController.rutaRapidaAzul;
        seguirRuta(ruta);
    }

    public void seguirRuta(int [][] ruta) {
        for (int i = 0; i < ruta.length-1; i++) {
           // System.out.println("Llegue hasta aqui, voy a evaluar si "+ruta[i][0] + " == "+ avenue +" y "+ ruta[i][1]+ " == "+ street);
            if ( avenue == ruta[i][0] && street == ruta[i][1]) {

                // System.out.println("Estoy en la posicion correcta: " + avenue + ", " + street);
                //System.out.println(nextAvenue());
                //System.out.println(nextStreet());
                if(frontIsClear() && MiPrimerRobot.controller.estaOcupada(nextStreet(),nextAvenue())){

                    if((avenue == 11 && street == 1)){
                        turnLeft();
                        giro();
                        switchRouteAzul();
                        break;
                    }
                    else if ((avenue ==23 && street ==11)){
                        turnLeft();
                        giro();
                        switchRouteVerde();
                        break;
                    }
                    
                }
                else if (frontIsClear() && (avenue == 10 && street ==2) && (ruta == TrafficController.rutaRapidaVerde || ruta == TrafficController.rutaLentaVerde)){
                    while(beepers>0){
                        putBeeper();
                        this.beepers = beepers - 1;
                    }
                    switchColoraAzul();
                    break;
                }
                else if (frontIsClear() && (avenue == 30 && street ==11) && (ruta == TrafficController.rutaRapidaAzul || ruta == TrafficController.rutaLentaAzul)){
                    while(beepers>0){
                        System.out.println("IMPRIMIENDO BEEPERSSSS");
                        putBeeper();
                        this.beepers = beepers - 1;
                    };
                    switchColoraVerde();
                    break;
                }
                //System.out.println("entre");
                //System.out.println("Esta el frente sin nadie? " + frontIsClear() + " Proxima avenida: "
                       // + nextAvenue() + " == " + ruta[i + 1][0] + " y Proxima calle: " + nextStreet() + " == " + ruta[i + 1][1]);
                if(nextToABeeper() &&  beepers< 4) {
                    this.beepers = beepers + 1;
                    pickBeeper();
                    i = i-1;
                }
                else if (frontIsClear() && nextAvenue() == ruta[i + 1][0] && nextStreet() == ruta[i + 1][1]) {
                    // Verificar si está en una posición que requiere semáforo
                    boolean esPosicionSemaforo = 
                        (street == 1 && avenue == 15) ||  // Semáforo sección 1 entrada
                        (street == 2 && avenue == 21) ||  // Semáforo sección 1 entrada alternativa
                        (street == 1 && avenue == 25) ||  // Semáforo sección 2 entrada
                        (street == 2 && avenue == 29) ||  // Semáforo sección 2 entrada alternativa
                        (street == 4 && avenue == 30) ||  // Semáforo sección 3 entrada
                        (street == 10 && avenue == 29);   // Semáforo sección 3 entrada alternativa
                    
                    if (esPosicionSemaforo) {
                        // Intentar continuamente adquirir el semáforo
                        boolean pudoMoverse = safeMoveConSemaforo();
                        if (!pudoMoverse) {
                            // No pudo adquirir el semáforo, retroceder en el índice para reintentar
                            i = i - 1;
                            // Pequeña pausa para no saturar el procesador
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            continue; // Volver a intentar
                        }
                        MiPrimerRobot.controller.imprimirMapa();
                    } else {
                        // Movimiento normal sin semáforo
                        safeMove();
                        cambiarValorAvenue();
                        cambiarValoreStreet();
                        
                        // Liberar semáforo si llegó a una posición de liberación
                        MiPrimerRobot.controller.liberarSemaforo(street, avenue);
                        MiPrimerRobot.controller.imprimirMapa();
                    }
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
        seguirRuta(ruta);
    }
}
