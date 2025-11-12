package udistrital.avanzada.pacmangame.controlador.gestores;

import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.Direccion;
import udistrital.avanzada.pacmangame.modelo.entidades.PacMan;


/**
 * Gestor para manejar el movimiento de Pac-Man en el tablero.
 * Cumple con SRP al encargarse solo de la lógica de movimiento.
 * Valida límites del tablero y calcula posiciones.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class GestorMovimiento {
    
    private int anchoTablero;
    private int altoTablero;
    
    /**
     * Constructor del gestor de movimiento.
     * 
     * @param anchoTablero Ancho del tablero de juego
     * @param altoTablero Alto del tablero de juego
     */
    public GestorMovimiento(int anchoTablero, int altoTablero) {
        this.anchoTablero = anchoTablero;
        this.altoTablero = altoTablero;
    }
    
    /**
     * Intenta mover a Pac-Man en una dirección específica.
     * Mueve hasta 4 casillas o hasta encontrar un límite.
     * 
     * @param pacMan Pac-Man a mover
     * @param direccion Dirección del movimiento
     * @return Número de casillas que logró moverse (0-4)
     */
    public int moverPacMan(PacMan pacMan, Direccion direccion) {
        if (pacMan == null || direccion == null) {
            return 0;
        }
        
        int casillasMovidas = 0;
        int tamanio = Constantes.TAMANIO_CASILLA;
        
        for (int i = 0; i < Constantes.CASILLAS_POR_MOVIMIENTO; i++) {
            int nuevoX = pacMan.getX() + (direccion.getDeltaX() * tamanio);
            int nuevoY = pacMan.getY() + (direccion.getDeltaY() * tamanio);
            
            if (esMovimientoValido(nuevoX, nuevoY)) {
                pacMan.setX(nuevoX);
                pacMan.setY(nuevoY);
                casillasMovidas++;
            } else {
                // Detenerse al encontrar un límite
                break;
            }
        }
        
        // Actualizar dirección actual
        if (casillasMovidas > 0) {
            pacMan.setDireccionActual(direccion);
        }
        
        return casillasMovidas;
    }
    
    /**
     * Verifica si una posición es válida dentro del tablero.
     * Considera el tamaño de Pac-Man para evitar que salga del borde.
     * 
     * @param x Coordenada X a verificar
     * @param y Coordenada Y a verificar
     * @return true si la posición es válida
     */
    public boolean esMovimientoValido(int x, int y) {
        int margen = Constantes.TAMANIO_PACMAN / 2;
        
        return x >= margen && 
               x <= (anchoTablero - margen) && 
               y >= margen && 
               y <= (altoTablero - margen);
    }
    
    /**
     * Verifica si Pac-Man ha alcanzado un límite en una dirección.
     * 
     * @param pacMan Pac-Man a verificar
     * @param direccion Dirección a verificar
     * @return true si ha alcanzado el límite
     */
    public boolean haAlcanzadoLimite(PacMan pacMan, Direccion direccion) {
        if (pacMan == null || direccion == null) {
            return true;
        }
        
        int tamanio = Constantes.TAMANIO_CASILLA;
        int nuevoX = pacMan.getX() + (direccion.getDeltaX() * tamanio);
        int nuevoY = pacMan.getY() + (direccion.getDeltaY() * tamanio);
        
        return !esMovimientoValido(nuevoX, nuevoY);
    }
    
    /**
     * Calcula la distancia euclidiana entre dos puntos.
     * Útil para detectar colisiones.
     * 
     * @param x1 Coordenada X del primer punto
     * @param y1 Coordenada Y del primer punto
     * @param x2 Coordenada X del segundo punto
     * @param y2 Coordenada Y del segundo punto
     * @return Distancia euclidiana entre los puntos
     */
    public double calcularDistancia(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Calcula la distancia entre Pac-Man y un punto específico.
     * 
     * @param pacMan Pac-Man
     * @param x Coordenada X del punto
     * @param y Coordenada Y del punto
     * @return Distancia entre Pac-Man y el punto
     */
    public double calcularDistanciaDesdePacMan(PacMan pacMan, int x, int y) {
        if (pacMan == null) {
            return Double.MAX_VALUE;
        }
        return calcularDistancia(pacMan.getX(), pacMan.getY(), x, y);
    }
    
    /**
     * Verifica si Pac-Man está en una posición específica (con tolerancia).
     * 
     * @param pacMan Pac-Man a verificar
     * @param x Coordenada X objetivo
     * @param y Coordenada Y objetivo
     * @param tolerancia Radio de tolerancia en píxeles
     * @return true si Pac-Man está cerca de la posición
     */
    public boolean estaEnPosicion(PacMan pacMan, int x, int y, int tolerancia) {
        if (pacMan == null) {
            return false;
        }
        return calcularDistanciaDesdePacMan(pacMan, x, y) <= tolerancia;
    }
    
    /**
     * Obtiene el ancho del tablero.
     * 
     * @return Ancho del tablero
     */
    public int getAnchoTablero() {
        return anchoTablero;
    }
    
    /**
     * Obtiene el alto del tablero.
     * 
     * @return Alto del tablero
     */
    public int getAltoTablero() {
        return altoTablero;
    }
    
    /**
     * Mueve Pac-Man a una posición específica (sin validación).
     * Usar con precaución.
     * 
     * @param pacMan Pac-Man a mover
     * @param x Nueva coordenada X
     * @param y Nueva coordenada Y
     */
    public void moverAPosicion(PacMan pacMan, int x, int y) {
        if (pacMan != null) {
            pacMan.setX(x);
            pacMan.setY(y);
        }
    }
    
    /**
     * Centra Pac-Man en el tablero.
     * 
     * @param pacMan Pac-Man a centrar
     */
    public void centrarPacMan(PacMan pacMan) {
        if (pacMan != null) {
            pacMan.setX(anchoTablero / 2);
            pacMan.setY(altoTablero / 2);
        }
    }
    
    /**
     * Calcula cuántas casillas puede moverse en una dirección sin salirse.
     * 
     * @param pacMan Pac-Man
     * @param direccion Dirección del movimiento
     * @return Número de casillas que puede moverse (0-4)
     */
    public int calcularCasillasDisponibles(PacMan pacMan, Direccion direccion) {
        if (pacMan == null || direccion == null) {
            return 0;
        }
        
        int casillasDisponibles = 0;
        int tamanio = Constantes.TAMANIO_CASILLA;
        int x = pacMan.getX();
        int y = pacMan.getY();
        
        for (int i = 0; i < Constantes.CASILLAS_POR_MOVIMIENTO; i++) {
            x += direccion.getDeltaX() * tamanio;
            y += direccion.getDeltaY() * tamanio;
            
            if (esMovimientoValido(x, y)) {
                casillasDisponibles++;
            } else {
                break;
            }
        }
        
        return casillasDisponibles;
    }
    
    @Override
    public String toString() {
        return "GestorMovimiento{" +
                "anchoTablero=" + anchoTablero +
                ", altoTablero=" + altoTablero +
                '}';
    }
}