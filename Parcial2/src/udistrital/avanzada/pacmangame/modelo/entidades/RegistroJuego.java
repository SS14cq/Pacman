package udistrital.avanzada.pacmangame.modelo.entidades;

/**
 * Clase que representa un registro de juego para el archivo de acceso aleatorio.
 * Contiene información de una partida completada.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class RegistroJuego {
    
    private String nombreJugador;
    private int puntaje;
    private long tiempoJuego; // en milisegundos
    private boolean activo; // Para marcar registros válidos
    
    /**
     * Constructor por defecto.
     */
    public RegistroJuego() {
        this.activo = true;
    }
    
    /**
     * Constructor con parámetros.
     * 
     * @param nombreJugador Nombre del jugador
     * @param puntaje Puntaje obtenido
     * @param tiempoJuego Tiempo de juego en milisegundos
     */
    public RegistroJuego(String nombreJugador, int puntaje, long tiempoJuego) {
        this.nombreJugador = nombreJugador;
        this.puntaje = puntaje;
        this.tiempoJuego = tiempoJuego;
        this.activo = true;
    }
    
    /**
     * Obtiene el nombre del jugador.
     * 
     * @return Nombre del jugador
     */
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    /**
     * Establece el nombre del jugador.
     * 
     * @param nombreJugador Nuevo nombre
     */
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    
    /**
     * Obtiene el puntaje.
     * 
     * @return Puntaje obtenido
     */
    public int getPuntaje() {
        return puntaje;
    }
    
    /**
     * Establece el puntaje.
     * 
     * @param puntaje Nuevo puntaje
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    
    /**
     * Obtiene el tiempo de juego.
     * 
     * @return Tiempo en milisegundos
     */
    public long getTiempoJuego() {
        return tiempoJuego;
    }
    
    /**
     * Establece el tiempo de juego.
     * 
     * @param tiempoJuego Tiempo en milisegundos
     */
    public void setTiempoJuego(long tiempoJuego) {
        this.tiempoJuego = tiempoJuego;
    }
    
    /**
     * Verifica si el registro está activo.
     * 
     * @return true si está activo
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Establece el estado del registro.
     * 
     * @param activo Nuevo estado
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    /**
     * Convierte el tiempo a formato legible (segundos).
     * 
     * @return Tiempo en segundos
     */
    public double getTiempoEnSegundos() {
        return tiempoJuego / 1000.0;
    }
    
    @Override
    public String toString() {
        return "RegistroJuego{" +
                "nombreJugador='" + nombreJugador + '\'' +
                ", puntaje=" + puntaje +
                ", tiempoJuego=" + getTiempoEnSegundos() + "s" +
                '}';
    }
}