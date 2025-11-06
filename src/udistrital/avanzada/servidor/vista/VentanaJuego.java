/*
 * Ventana que contiene el panel gráfico donde se visualiza el juego.
 * Muestra a Pac-Man, las frutas y la información del jugador.
 * 
 * Esta ventana se crea para cada partida y se cierra al finalizar el juego.
 * Es parte de la capa Vista y no contiene lógica de negocio.
 */
package udistrital.avanzada.servidor.vista;

/**
 *
 * @author juanr
 */
import udistrital.avanzada.servidor.vista.PanelJuego;
import javax.swing.*;
import java.awt.*;
import udistrital.avanzada.servidor.modelo.Juego;


public class VentanaJuego extends JFrame {
    /** Panel personalizado donde se dibuja el juego */
    private PanelJuego panelJuego;
    
    /**
     * Constructor que inicializa la ventana del juego.
     * 
     * @param juego Instancia del juego a visualizar
     * @param nombreJugador Nombre del jugador actual
     */
    public VentanaJuego(Juego juego, String nombreJugador) {
        setTitle("Pac-Man - Jugador: " + nombreJugador);
        setSize(620, 680);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        
        panelJuego = new PanelJuego(juego);
        add(panelJuego);
        
        setLocationRelativeTo(null);
    }
}
