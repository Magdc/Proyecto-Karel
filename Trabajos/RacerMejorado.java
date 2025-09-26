import kareltherobot.*;
import java.awt.Color;

class RacerMejorado extends Robot implements Runnable {
    private int beepers;
    private Color color;
    private Direction dir;
    private int street;
    private int avenue;
    private int[][] ruta;
    
    // Constructor
    public RacerMejorado(int street, int avenue, Direction direction, int beeps, Color color) {
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

    public RacerMejorado(int street, int avenue, Direction direction, int beeps, Color color, int[][] ruta) {
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
    
    public boolean safeMoveWithSemaphore() {
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
        ruta = (this.ruta == TrafficController.rutaRapidaAzul) ? TrafficController.rutaLentaAzul
                : TrafficController.rutaRapidaAzul;
        seguirRuta(ruta);
    }
    
    private void switchRouteVerde() {
        ruta = (this.ruta == TrafficController.rutaRapidaVerde) ? TrafficController.rutaLentaVerde
                : TrafficController.rutaRapidaVerde;
        seguirRuta(ruta);
    }

    private void switchColoraVerde() {
        ruta = TrafficController.rutaRapidaVerde;
        seguirRuta(ruta);
    }
    
    private void switchColoraAzul() {
        ruta = TrafficController.rutaRapidaAzul;
        seguirRuta(ruta);
    }

    public void seguirRuta(int [][] ruta) {
        for (int i = 0; i < ruta.length-1; i++) {
            if (avenue == ruta[i][0] && street == ruta[i][1]) {
                
                // Verificar si está en una posición de semáforo y evaluar si puede continuar
                boolean esPosicionSemaforo = 
                    (street == 1 && avenue == 15) ||  // Semáforo sección 1 entrada
                    (street == 2 && avenue == 21) ||  // Semáforo sección 1 entrada alternativa
                    (street == 1 && avenue == 25) ||  // Semáforo sección 2 entrada
                    (street == 2 && avenue == 29) ||  // Semáforo sección 2 entrada alternativa
                    (street == 4 && avenue == 30) ||  // Semáforo sección 3 entrada
                    (street == 10 && avenue == 29);   // Semáforo sección 3 entrada alternativa
                
                if (esPosicionSemaforo) {
                    // Intentar adquirir semáforo y avanzar
                    if (!safeMoveWithSemaphore()) {
                        // No pudo adquirir el semáforo, retrocede en el índice para reintentar
                        i = i - 1;
                        continue;
                    }
                } else {
                    // Lógica original para posiciones que no requieren semáforo
                    if (MiPrimerRobot.controller.estaOcupada(nextStreet(), nextAvenue())) {
                        turnLeft();
                        giro();
                        if ((avenue == 11 && street == 1)) {
                            switchRouteAzul();
                            break;
                        } else if ((avenue == 23 && street == 11)) {
                            switchRouteVerde();
                            break;
                        }
                    } else if ((avenue == 10 && street == 2) && 
                              (ruta == TrafficController.rutaRapidaVerde || ruta == TrafficController.rutaLentaVerde)) {
                        switchColoraAzul();
                        break;
                    } else if ((avenue == 30 && street == 11) && 
                              (ruta == TrafficController.rutaRapidaAzul || ruta == TrafficController.rutaLentaAzul)) {
                        switchColoraVerde();
                        break;
                    } else if (nextToABeeper() && beepers < 4) {
                        this.beepers = beepers + 1;
                        pickBeeper();
                        i = i - 1;
                    } else if (nextAvenue() == ruta[i + 1][0] && nextStreet() == ruta[i + 1][1]) {
                        safeMove();
                        MiPrimerRobot.controller.imprimirMapa();
                        cambiarValorAvenue();
                        cambiarValoreStreet();
                        
                        // Liberar semáforo si está en posición de liberación
                        MiPrimerRobot.controller.liberarSemaforo(street, avenue);
                    } else {
                        i = i - 1;
                        turnLeft();
                        giro();
                    }
                }
            }
        }
    }

    // Método que arranca el hilo
    public void run() {
        seguirRuta(ruta);
    }
}