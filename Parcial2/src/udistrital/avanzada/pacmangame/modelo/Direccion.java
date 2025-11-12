package udistrital.avanzada.pacmangame.modelo;

/**
 * Enumeración que representa las direcciones de movimiento en el juego.
 * Cada dirección tiene un delta X, delta Y y un nombre descriptivo.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public enum Direccion {
    
    /**
     * Dirección hacia arriba (disminuye Y).
     */
    ARRIBA(0, -1, "Arriba"),
    
    /**
     * Dirección hacia abajo (aumenta Y).
     */
    ABAJO(0, 1, "Abajo"),
    
    /**
     * Dirección hacia la izquierda (disminuye X).
     */
    IZQUIERDA(-1, 0, "Izquierda"),
    
    /**
     * Dirección hacia la derecha (aumenta X).
     */
    DERECHA(1, 0, "Derecha");
    
    private final int deltaX;
    private final int deltaY;
    private final String nombre;
    
    /**
     * Constructor de Direccion.
     * 
     * @param deltaX Cambio en coordenada X
     * @param deltaY Cambio en coordenada Y
     * @param nombre Nombre descriptivo de la dirección
     */
    Direccion(int deltaX, int deltaY, String nombre) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.nombre = nombre;
    }
    
    /**
     * Obtiene el cambio en X.
     * 
     * @return Delta X
     */
    public int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Obtiene el cambio en Y.
     * 
     * @return Delta Y
     */
    public int getDeltaY() {
        return deltaY;
    }
    
    /**
     * Obtiene el nombre de la dirección.
     * 
     * @return Nombre de la dirección
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Convierte una cadena a una dirección.
     * No distingue entre mayúsculas y minúsculas.
     * 
     * @param texto Texto a convertir
     * @return Dirección correspondiente o null si no existe
     */
    public static Direccion fromString(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        
        for (Direccion d : values()) {
            if (d.nombre.equalsIgnoreCase(texto.trim())) {
                return d;
            }
        }
        return null;
    }
    
    /**
     * Obtiene la dirección opuesta.
     * 
     * @return Dirección opuesta
     */
    public Direccion getOpuesta() {
        switch (this) {
            case ARRIBA:
                return ABAJO;
            case ABAJO:
                return ARRIBA;
            case IZQUIERDA:
                return DERECHA;
            case DERECHA:
                return IZQUIERDA;
            default:
                return null;
        }
    }
    
    /**
     * Verifica si la dirección es horizontal.
     * 
     * @return true si es horizontal (izquierda o derecha)
     */
    public boolean esHorizontal() {
        return this == IZQUIERDA || this == DERECHA;
    }
    
    /**
     * Verifica si la dirección es vertical.
     * 
     * @return true si es vertical (arriba o abajo)
     */
    public boolean esVertical() {
        return this == ARRIBA || this == ABAJO;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}