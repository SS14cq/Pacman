/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Clase que representa una Fruta o Item del juego Pac-Man.
 * Cada fruta tiene un tipo específico, un valor en puntos,
 * una posición en el tablero y un estado que indica si ha sido comida.
**/
package udistrital.avanzada.servidor.modelo;

/**
 *
 * @author juanr
 */

public class Fruta {
    /** Tipo de fruta (Cereza, Fresa, Naranja, etc.) */
    private String tipo;
    
    /** Puntos que otorga la fruta al ser comida */
    private int puntos;
    
    /** Coordenada X de la posición de la fruta en el tablero */
    private int x;
    
    /** Coordenada Y de la posición de la fruta en el tablero */
    private int y;
    
    /** Estado que indica si la fruta ha sido comida por Pac-Man */
    private boolean comida;
    
    /**
     * Constructor que inicializa una fruta con todos sus atributos.
     * Por defecto, la fruta no está comida.
     * 
     * @param tipo El tipo de fruta (ejemplo: "Cereza", "Fresa")
     * @param puntos Los puntos que otorga al ser comida (debe ser positivo)
     * @param x La coordenada X en el tablero (0 <= x <= ancho del panel)
     * @param y La coordenada Y en el tablero (0 <= y <= alto del panel)
     */
    public Fruta(String tipo, int puntos, int x, int y) {
        this.tipo = tipo;
        this.puntos = puntos;
        this.x = x;
        this.y = y;
        this.comida = false;
    }
    
    /**
     * Obtiene el tipo de la fruta.
     * 
     * @return String con el tipo de fruta
     */
    public String getTipo() { 
        return tipo; 
    }
    
    /**
     * Obtiene los puntos que otorga la fruta.
     * 
     * @return Valor entero de puntos
     */
    public int getPuntos() { 
        return puntos; 
    }
    
    /**
     * Obtiene la coordenada X de la fruta.
     * 
     * @return Posición X en el tablero
     */
    public int getX() { 
        return x; 
    }
    
    /**
     * Obtiene la coordenada Y de la fruta.
     * 
     * @return Posición Y en el tablero
     */
    public int getY() { 
        return y; 
    }
    
    /**
     * Verifica si la fruta ha sido comida.
     * 
     * @return true si la fruta fue comida, false en caso contrario
     */
    public boolean isComida() { 
        return comida; 
    }
    
    /**
     * Establece el estado de la fruta (comida o no comida).
     * 
     * @param comida true para marcar como comida, false para disponible
     */
    public void setComida(boolean comida) { 
        this.comida = comida; 
    }
}