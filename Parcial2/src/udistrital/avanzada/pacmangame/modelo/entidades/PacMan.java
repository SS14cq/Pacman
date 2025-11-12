package udistrital.avanzada.pacmangame.modelo.entidades;

import udistrital.avanzada.pacmangame.modelo.Direccion;


/**
 * Clase que representa al personaje Pac-Man.
 * Contiene su posición y estado en el juego.
 * Cumple con SRP al manejar solo datos del personaje.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class PacMan {
    
    private int x;
    private int y;
    private Direccion direccionActual;
    
    /**
     * Constructor que inicializa Pac-Man en el centro del tablero.
     * 
     * @param anchoTablero Ancho del tablero de juego
     * @param altoTablero Alto del tablero de juego
     */
    public PacMan(int anchoTablero, int altoTablero) {
        this.x = anchoTablero / 2;
        this.y = altoTablero / 2;
        this.direccionActual = null;
    }
    
    /**
     * Obtiene la coordenada X de Pac-Man.
     * 
     * @return Posición X
     */
    public int getX() {
        return x;
    }
    
    /**
     * Establece la coordenada X de Pac-Man.
     * 
     * @param x Nueva posición X
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Obtiene la coordenada Y de Pac-Man.
     * 
     * @return Posición Y
     */
    public int getY() {
        return y;
    }
    
    /**
     * Establece la coordenada Y de Pac-Man.
     * 
     * @param y Nueva posición Y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Obtiene la dirección actual de movimiento.
     * 
     * @return Dirección actual
     */
    public Direccion getDireccionActual() {
        return direccionActual;
    }
    
    /**
     * Establece la dirección de movimiento.
     * 
     * @param direccionActual Nueva dirección
     */
    public void setDireccionActual(Direccion direccionActual) {
        this.direccionActual = direccionActual;
    }
    
    @Override
    public String toString() {
        return "PacMan{" +
                "x=" + x +
                ", y=" + y +
                ", direccion=" + direccionActual +
                '}';
    }
}