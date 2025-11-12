package udistrital.avanzada.pacmangame.modelo;

/**
 * Enumeración que representa los tipos de frutas disponibles en el juego.
 * Cada fruta tiene un valor de puntos asociado.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public enum TipoFruta {
    CEREZA(100, "Cereza"),
    FRESA(300, "Fresa"),
    NARANJA(500, "Naranja"),
    MANZANA(700, "Manzana"),
    MELON(1000, "Melón"),
    GALAXIAN(2000, "Galaxian"),
    CAMPANA(3000, "Campana"),
    LLAVE(5000, "Llave");
    
    private final int puntos;
    private final String nombre;
    
    /**
     * Constructor de TipoFruta.
     * 
     * @param puntos Puntos que otorga la fruta
     * @param nombre Nombre descriptivo de la fruta
     */
    TipoFruta(int puntos, String nombre) {
        this.puntos = puntos;
        this.nombre = nombre;
    }
    
    /**
     * Obtiene los puntos de la fruta.
     * 
     * @return Puntos de la fruta
     */
    public int getPuntos() {
        return puntos;
    }
    
    /**
     * Obtiene el nombre de la fruta.
     * 
     * @return Nombre de la fruta
     */
    public String getNombre() {
        return nombre;
    }
}