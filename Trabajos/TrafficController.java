
class TrafficController{
    private final int[][] mapa;// Matriz para representar el mapa de tráfico
    private final Object lock = new Object();

    // Los semaforos estan en la posicion (15,1), (25,1) y (30,9)
    // Posiciones de decision  11,1 23,11
    public static final int[][] rutaRapidaAzul = {
            {7,4},{6,4},{5,4},{4,4},{3,4},{2,4},{1,4},{1,3},{2,3},{3,3}
            ,{4,3},{5,3},{6,3},{7,3},{7,2},{6,2},{5,2},{4,2},{3,2},{2,2}, {1,2},
            {1,1},{2,1},{3,1},{4,1},{5,1},{6,1},{7,1}, {8,1}, {9,1}, {10,1}, {11,1}, {12,1}, {13,1},
            {14,1}, {15,1}, {16,1}, {17,1}, {18,1}, {19,1}, {20,1},
            {21,1}, {22,1}, {23,1}, {24,1}, {25,1}, {26,1}, {27,1},
            {28,1}, {29,1}, {30,1}, {30,2}, {30,3}, {30,4}, {30,5},
            {30,6}, {30,7}, {30,8}, {30,9}, {30,10}, {30,11}, {30,12}};

    // Los semaforos estan en la posicion (15,1), (25,1) y (30,9)
    public static final int[][] rutaLentaAzul = {
            {7,4},{6,4},{5,4},{4,4},{3,4},{2,4},{1,4},{1,3},{2,3}
            ,{3,3},{4,3},{5,3},{6,3},{7,3},{7,2},{6,2},{5,2},{4,2},
            {3,2},{2,2},{1,2},{1,1},{2,1},{3,1},{4,1},{5,1},{6,1},
            {7,1},{8,1}, {9,1}, {10,1}, {11,1}, {11,2}, {11,3},
            {11,4}, {11,5}, {11,6}, {11,7}, {11,8}, {11,9}, {11,10},
            {11,11},{10,11},{9,11},{8,11},{8,12},{8,13},{8,14},{9,14},
            {10,14},{11,14},{12,14},{13,14},{14,14},{15,14},{16,14},
            {16,13},{16,12},{16,11},{16,10},{15,10}, {14,10}, {13,10},
            {13,9}, {13,8}, {13,7}, {13,6}, {13,5}, {14,5}, {15,5},
            {16,5}, {17,5}, {18,5}, {19,5}, {20,5}, {20,6}, {20,7},
            {20,8}, {20,9}, {20,10}, {21,10}, {22,10}, {23,10}, {24,10},
            {25,10}, {26,10}, {27,10}, {28,10}, {29,10}, {30,10},
            {30,11}, {30,12}};
    public static int[][] rutaRapidaVerde = {
            {30,11},{30,12},{30,13},{30,14},{30,15},{30,16},{29,16},
            {29,15},{28,15},{27,15},{26,15},{25,15},{24,15},{23,15},
            {23,14},{24,14},{25,14},{26,14},{27,14},{28,14},{29,14},
            {29,13},{29,12},{28,12},{28,13},{27,13},{26,13},{25,13},
            {24,13},{23,13},{23,12},{23,11},{23,10},{24,10},{25,10},
            {26,10},{27,10},{28,10},{29,10},{30,10},{30,9},{30,8},
            {30,7},{30,6},{30,5},{29,5},{29,4},{29,3},{29,2},{29,1},
            {28,1},{27,1},{26,1},{26,2},{25,2},{24,2},{23,2},{22,2},
            {21,2},{21,1},{20,1},{19,1},{18,1},{17,1},{16,1},{16,2},
            {15,2},{14,2},{13,2},{12,2}, {11,2},{10,2},{9,2}
    };
    public static int[][] rutaLentaVerde = {{30,11},{30,12},{30,13},{30,14},{30,15},{30,16},{29,16},{29,15},{28,15},{27,15},{26,15},{25,15},{24,15},{23,15},
            {23,14},{24,14},{25,14},{26,14},{27,14},{28,14},{29,14},{29,13},{29,12},{28,12},{28,13},{27,13},{26,13},{25,13},{24,13},{23,13},{23,12},{23,11},
            {22,11},{21,11},{20,11},{20,12},{20,13},{20,14},{20,15},{20,16},{20,17},{20,18},{20,19},{19,19},{18,19},{18,18},{18,17},{18,16},{18,15},
            {17,15},{16,15},{15,15},{14,15},{13,15},{12,15},{11,15},{10,15},{9,15},{8,15},{7,15},{6,15},{5,15},{4,15},{3,15},{2,15},{1,15},{1,14},{1,13},
            {1,12},{1,11},{1,10},{2,10},{3,10},{4,10},{5,10},{6,10},{7,10},{8,10},{9,10},{10,10},{10,9},{10,8},{10,7},{10,6},{10,5},{10,4},{10,3},{10,2},
            {9,2},{8,2}
    };

    /*

    Vamos a definir las rutas y establecer las condiciones para que estas se puedan ejecutar
    0 -> libre
    1 -> ocupado

 */
    public TrafficController(int filas, int cols) {
        mapa = new int[filas][cols];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                mapa[i][j] = 0; // Inicializa todas las celdas como libres (0)
            }
        }
        //Ocupar celdas de inicio
        /*
        for (int i = 1; i < 5 ; i++) {
            for (int j = 7; j > 0; j--) {
                mapa[i - 1][j - 1] = 1;
            }
        }
        for (int i = 12; i < 17 ; i++) {
            for (int j = 30; j > 22; j--) {
                mapa[i - 1][j - 1] = 1;
            }
        }*/
        imprimirMapa();
    }
    public void imprimirMapa() {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                System.out.print(mapa[i][j] == 1 ? "#" : ".");
            }
            System.out.println();
        }
    }

    public boolean ocuparPosicion(int street, int avenue) {
        if (estaLibre(street, avenue)) {
            mapa[street - 1][avenue - 1]= 1;// Marca la celda como ocupada (1)
            return true; // Retorna true el robot se puede mover a esa posición
        } else {
            return false; // Retorna false si la celda ya está ocupada no se puede mover el robot

        }

    }
    public void liberarPosicion(int street, int avenue) {
        if (estaOcupada(street, avenue)) {
            mapa[street - 1][avenue - 1] = 0;
            // Marca la celda como libre (0)
        } else {
           // throw new IllegalArgumentException("Celda ya libre");
        }
    }
    public boolean estaOcupada(int street, int avenue) {
        return mapa[street - 1][avenue - 1] == 1; // Devuelve true si la celda está ocupada
    }
    public boolean estaLibre(int street, int avenue) {
        return mapa[street - 1][avenue - 1] == 0; // Devuelve true si la celda está libre
    }
    // Reservar un espacio en el mapa para un robot
    public void requestAndCommitMove(int fromStreet, int fromAvenue,
                                     int toStreet,   int toAvenue,
                                     Runnable doMove) {
        synchronized (lock) {
            while (!estaLibre(toStreet, toAvenue)) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            // RESERVA destino y libera origen dentro del lock (paso atómico en la matriz)
            ocuparPosicion(toStreet, toAvenue);
            liberarPosicion(fromStreet, fromAvenue);
        }
        // Mover FUERA del lock para no bloquear a los demás
        doMove.run();

        // Avisar que hubo cambios (otros pueden despertar)
        synchronized (lock) {
            lock.notifyAll();
        }
    }
    // necesito crear otra funcion que me ayude a definir que rutas se deben seguir y cuando deben parar y seguir
}
