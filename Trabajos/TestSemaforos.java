import kareltherobot.*;
import java.awt.Color;

public class TestSemaforos implements Directions{

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        World.setDelay(100); // más lento para ver mejor el comportamiento
        
        TrafficController controller = new TrafficController(21, 31);
        
        // Crear dos robots que van a competir por la misma sección crítica
        Racer robot1 = new Racer(1, 14, East, 0, Color.blue); // Cerca de semáforo en (15,1)
        Racer robot2 = new Racer(2, 20, West, 0, Color.green); // Cerca de semáforo en (21,2)
        
        // Ocupar sus posiciones iniciales
        controller.ocuparPosicion(1, 14);
        controller.ocuparPosicion(2, 20);
        
        System.out.println("=== PRUEBA DE SEMÁFOROS ===");
        System.out.println("Robot Azul en (1,14) -> irá hacia (15,1)");
        System.out.println("Robot Verde en (2,20) -> irá hacia (21,2)");
        System.out.println("Ambos competirán por la sección crítica (16,1)-(21,1)");
        
        controller.imprimirMapa();
        
        // Iniciar los robots
        new Thread(robot1).start();
        new Thread(robot2).start();
    }
}