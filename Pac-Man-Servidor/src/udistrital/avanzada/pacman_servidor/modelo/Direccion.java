package udistrital.avanzada.pacman_servidor.modelo;

/**
 * Enumeración que representa las direcciones de movimiento posibles para Pac-Man.
 * Cada dirección tiene asociados valores de desplazamiento en X e Y.
 * 
 * @author Steban
 * @version 1.0
 */
public enum Direccion {
    /**
     * Movimiento hacia arriba (decrementar Y)
     */
    ARRIBA(0, -1),
    
    /**
     * Movimiento hacia abajo (incrementar Y)
     */
    ABAJO(0, 1),
    
    /**
     * Movimiento hacia la izquierda (decrementar X)
     */
    IZQUIERDA(-1, 0),
    
    /**
     * Movimiento hacia la derecha (incrementar X)
     */
    DERECHA(1, 0);
    
    private final int deltaX;
    private final int deltaY;
    
    /**
     * Constructor de la enumeración.
     * 
     * @param deltaX Desplazamiento en el eje X
     * @param deltaY Desplazamiento en el eje Y
     */
    Direccion(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    
    /**
     * Obtiene el desplazamiento en X para esta dirección.
     * 
     * @return Desplazamiento en X (-1, 0 o 1)
     */
    public int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Obtiene el desplazamiento en Y para esta dirección.
     * 
     * @return Desplazamiento en Y (-1, 0 o 1)
     */
    public int getDeltaY() {
        return deltaY;
    }
}