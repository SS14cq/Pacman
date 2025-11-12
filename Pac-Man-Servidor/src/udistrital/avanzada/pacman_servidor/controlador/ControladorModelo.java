package udistrital.avanzada.pacman_servidor.controlador;

import java.awt.Graphics;
import udistrital.avanzada.pacman_servidor.modelo.*;

/**
 * Controlador dedicado EXCLUSIVAMENTE al modelo.
 * Encapsula operaciones del modelo y evita que el ControladorServidor
 * conozca detalles internos de Juego, PacMan, etc.
 * 
 * @author Steban
 * @version 1.0
 */
public class ControladorModelo {
    
    private Juego juego;
    
    /**
     * Crea un nuevo juego.
     * 
     * @param anchoVentana Ancho del área de juego
     * @param altoVentana Alto del área de juego
     */
    public void crearJuego(int anchoVentana, int altoVentana) {
        this.juego = new Juego(anchoVentana, altoVentana);
    }
    
    /**
     * Inicializa el juego con el nombre del jugador.
     * 
     * @param nombreJugador Nombre del jugador
     */
    public void inicializarJuego(String nombreJugador) {
        if (juego != null) {
            juego.inicializar(nombreJugador);
        }
    }
    
    /**
     * Procesa un movimiento de Pac-Man.
     * 
     * @param direccion Dirección del movimiento
     * @return Resultado del movimiento
     */
    public ResultadoMovimiento moverPacMan(Direccion direccion) {
        if (juego != null) {
            return juego.moverPacMan(direccion);
        }
        return null;
    }
    
    /**
     * Dibuja el juego en un contexto gráfico.
     * 
     * @param g Contexto gráfico
     */
    public void dibujarJuego(Graphics g) {
        if (juego != null) {
            juego.dibujar(g);
        }
    }
    
    /**
     * Verifica si el juego terminó.
     * 
     * @return true si terminó, false en caso contrario
     */
    public boolean juegoTerminado() {
        return juego != null && juego.isJuegoTerminado();
    }
    
    /**
     * Obtiene el puntaje actual.
     * 
     * @return Puntaje total
     */
    public int obtenerPuntaje() {
        return juego != null ? juego.getPuntajeTotal() : 0;
    }
    
    /**
     * Obtiene el tiempo transcurrido.
     * 
     * @return Tiempo en segundos
     */
    public long obtenerTiempo() {
        return juego != null ? juego.getTiempoTranscurrido() : 0;
    }
    
    /**
     * Obtiene las frutas restantes.
     * 
     * @return Cantidad de frutas
     */
    public int obtenerFrutasRestantes() {
        return juego != null ? juego.getFrutasRestantes() : 0;
    }
    
    /**
     * Obtiene el nombre del jugador.
     * 
     * @return Nombre del jugador
     */
    public String obtenerNombreJugador() {
        return juego != null ? juego.getNombreJugador() : "";
    }
    
    /**
     * Convierte un String a Direccion.
     * 
     * @param comandoStr String con la dirección
     * @return Direccion correspondiente
     * @throws IllegalArgumentException si el comando es inválido
     */
    public Direccion parsearDireccion(String comandoStr) {
        return Direccion.valueOf(comandoStr);
    }
}