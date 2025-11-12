package udistrital.avanzada.pacmangame.controlador.servidor;

import udistrital.avanzada.pacmangame.controlador.gestores.GestorJuego;
import udistrital.avanzada.pacmangame.modelo.Direccion;
import udistrital.avanzada.pacmangame.modelo.entidades.Fruta;
import udistrital.avanzada.pacmangame.modelo.entidades.RegistroJuego;
import udistrital.avanzada.pacmangame.vista.servidor.PanelJuego;



/**
 * Controlador del juego Pac-Man.
 * Coordina la lógica del juego con la vista.
 * Cumple con SRP al coordinar solo el flujo del juego.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class ControladorJuego {
    
    private GestorJuego gestorJuego;
    private PanelJuego panelJuego;
    private String nombreJugador;
    private ControladorServidor controladorServidor;
    
    /**
     * Constructor del controlador de juego.
     * 
     * @param anchoTablero Ancho del tablero
     * @param altoTablero Alto del tablero
     * @param nombreJugador Nombre del jugador
     * @param controladorServidor Controlador del servidor
     */
    public ControladorJuego(int anchoTablero, int altoTablero, String nombreJugador, 
                           ControladorServidor controladorServidor) {
        this.nombreJugador = nombreJugador;
        this.controladorServidor = controladorServidor;
        this.gestorJuego = new GestorJuego(anchoTablero, altoTablero);
        this.panelJuego = new PanelJuego(this, anchoTablero, altoTablero);
        
        // Iniciar el panel de juego
        panelJuego.iniciar();
    }
    
    /**
     * Mueve Pac-Man en una dirección.
     * 
     * @param direccion Dirección del movimiento
     * @return Número de casillas movidas
     */
    public int moverPacMan(Direccion direccion) {
        int casillasMovidas = gestorJuego.getGestorMovimiento().moverPacMan(
            gestorJuego.getPacMan(), 
            direccion
        );
        
        // Actualizar vista
        panelJuego.repaint();
        
        return casillasMovidas;
    }
    
    /**
     * Verifica colisión con frutas.
     * 
     * @return Mensaje con la fruta comida o null
     */
    public String verificarColision() {
        Fruta fruta = gestorJuego.verificarColisionConFrutas();
        
        if (fruta != null) {
            // Actualizar vista
            panelJuego.repaint();
            
            return String.format("%s (+%d puntos) - Total: %d",
                fruta.getTipo().getNombre(),
                fruta.getPuntos(),
                gestorJuego.getGestorPuntuacion().getPuntuacionTotal()
            );
        }
        
        return null;
    }
    
    /**
     * Verifica si el juego ha terminado.
     * 
     * @return true si el juego terminó
     */
    public boolean juegoTerminado() {
        return gestorJuego.juegoTerminado();
    }
    
    /**
     * Obtiene el registro del juego finalizado.
     * 
     * @return Registro del juego
     */
    public RegistroJuego obtenerRegistroJuego() {
        int puntaje = gestorJuego.getGestorPuntuacion().getPuntuacionTotal();
        long tiempo = gestorJuego.obtenerTiempoTranscurrido();
        
        return new RegistroJuego(nombreJugador, puntaje, tiempo);
    }
    
    /**
     * Obtiene el gestor de juego.
     * 
     * @return Gestor de juego
     */
    public GestorJuego getGestorJuego() {
        return gestorJuego;
    }
    
    /**
     * Obtiene el panel de juego.
     * 
     * @return Panel de juego
     */
    public PanelJuego getPanelJuego() {
        return panelJuego;
    }
}