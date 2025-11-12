package udistrital.avanzada.pacmangame.controlador.gestores;

/**
 * Gestor para manejar la puntuación del juego.
 * Cumple con SRP al encargarse solo de la puntuación.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class GestorPuntuacion {
    
    private int puntuacionTotal;
    
    /**
     * Constructor del gestor de puntuación.
     */
    public GestorPuntuacion() {
        this.puntuacionTotal = 0;
    }
    
    /**
     * Agrega puntos a la puntuación total.
     * 
     * @param puntos Puntos a agregar
     */
    public void agregarPuntos(int puntos) {
        this.puntuacionTotal += puntos;
    }
    
    /**
     * Obtiene la puntuación total actual.
     * 
     * @return Puntuación total
     */
    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }
    
    /**
     * Reinicia la puntuación a cero.
     */
    public void reiniciar() {
        this.puntuacionTotal = 0;
    }
    
    @Override
    public String toString() {
        return "Puntuación: " + puntuacionTotal;
    }
}