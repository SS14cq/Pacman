package udistrital.avanzada.pacman_servidor.modelo;

import java.io.Serializable;

/**
 * Representa un registro de juego con información del jugador.
 * Estructura fija para almacenamiento en archivo de acceso aleatorio.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class RegistroJuego implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Longitud máxima del nombre (en caracteres)
     */
    public static final int LONGITUD_NOMBRE = 50;
    
    /**
     * Tamaño total del registro en bytes:
     * - Nombre: 50 chars * 2 bytes = 100 bytes
     * - Puntaje: int = 4 bytes
     * - Tiempo: long = 8 bytes
     * TOTAL: 112 bytes
     */
    public static final int TAMANIO_REGISTRO = (LONGITUD_NOMBRE * 2) + 4 + 8;
    
    private String nombreJugador;
    private int puntaje;
    private long tiempoSegundos;
    
    /**
     * Constructor vacío.
     */
    public RegistroJuego() {
        this.nombreJugador = "";
        this.puntaje = 0;
        this.tiempoSegundos = 0;
    }
    
    /**
     * Constructor con parámetros.
     * 
     * @param nombreJugador Nombre del jugador (máximo 50 caracteres)
     * @param puntaje Puntaje obtenido
     * @param tiempoSegundos Tiempo en segundos
     */
    public RegistroJuego(String nombreJugador, int puntaje, long tiempoSegundos) {
        setNombreJugador(nombreJugador);
        this.puntaje = puntaje;
        this.tiempoSegundos = tiempoSegundos;
    }
    
    /**
     * Establece el nombre del jugador.
     * Trunca o rellena a 50 caracteres.
     * 
     * @param nombre Nombre a establecer
     */
    public void setNombreJugador(String nombre) {
        if (nombre == null) {
            nombre = "";
        }
        
        // Truncar si es muy largo
        if (nombre.length() > LONGITUD_NOMBRE) {
            this.nombreJugador = nombre.substring(0, LONGITUD_NOMBRE);
        } else {
            this.nombreJugador = nombre;
        }
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    public int getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    
    public long getTiempoSegundos() {
        return tiempoSegundos;
    }
    
    public void setTiempoSegundos(long tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }
    
    @Override
    public String toString() {
        return String.format("Jugador: %s | Puntaje: %d | Tiempo: %d segundos",
                nombreJugador.trim(), puntaje, tiempoSegundos);
    }
}