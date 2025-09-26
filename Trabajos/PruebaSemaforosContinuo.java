import kareltherobot.*;
import java.awt.Color;

public class PruebaSemaforosContinuo implements Directions{

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        World.setDelay(200); // más lento para observar el comportamiento
        
        TrafficController controller = new TrafficController(21, 31);
        
        System.out.println("=== PRUEBA DE VERIFICACIÓN CONTINUA DE SEMÁFOROS ===");
        System.out.println("Los robots en posiciones de semáforo verificarán continuamente");
        System.out.println("hasta que puedan adquirir el semáforo y avanzar.");
        System.out.println();
        
        // Crear múltiples robots que competirán por el mismo semáforo
        // Sección 1: (16,1) hasta (21,1)
        Racer robot1 = new Racer(1, 15, East, 0, Color.blue);    // En posición de semáforo (15,1)
        Racer robot2 = new Racer(2, 21, West, 0, Color.red);     // En posición de semáforo (21,2)
        Racer robot3 = new Racer(1, 14, East, 0, Color.cyan);    // Llegará a (15,1)
        
        // Sección 2: (26,1) hasta (29,1)
        Racer robot4 = new Racer(1, 25, East, 0, Color.green);   // En posición de semáforo (25,1)
        Racer robot5 = new Racer(2, 29, West, 0, Color.magenta); // En posición de semáforo (29,2)
        
        // Ocupar sus posiciones iniciales
        controller.ocuparPosicion(1, 15);
        controller.ocuparPosicion(2, 21);
        controller.ocuparPosicion(1, 14);
        controller.ocuparPosicion(1, 25);
        controller.ocuparPosicion(2, 29);
        
        System.out.println("Posiciones iniciales:");
        System.out.println("Robot Azul (1,15) - Posición de semáforo sección 1");
        System.out.println("Robot Rojo (2,21) - Posición de semáforo sección 1");  
        System.out.println("Robot Cyan (1,14) - Se moverá a posición de semáforo");
        System.out.println("Robot Verde (1,25) - Posición de semáforo sección 2");
        System.out.println("Robot Magenta (2,29) - Posición de semáforo sección 2");
        System.out.println();
        System.out.println("Estado inicial del mapa:");
        
        controller.imprimirMapa();
        
        // Iniciar todos los robots
        new Thread(robot1).start();
        new Thread(robot2).start();
        new Thread(robot3).start();
        new Thread(robot4).start();
        new Thread(robot5).start();
        
        // Mensaje informativo
        System.out.println("\n▶ Los robots verificarán continuamente si pueden adquirir sus semáforos...");
        System.out.println("▶ Solo uno podrá pasar por cada sección crítica a la vez.");
    }
}