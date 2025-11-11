package udistrital.avanzada.pacman_servidor.vista;

import java.awt.Graphics;
import javax.swing.JPanel;
import udistrital.avanzada.pacman_servidor.modelo.Juego;

/**
 * Panel personalizado donde se dibuja el juego de Pac-Man.
 * Delega el dibujado al modelo del juego.
 * Respeta el principio de separación: Vista solo dibuja, no conoce lógica.
 * 
 * @author Steban
 * @version 1.0
 */
public class PanelJuego extends JPanel {
    
    private Juego juego;
    
    /**
     * Constructor del panel.
     * 
     * @param juego Modelo del juego a dibujar
     */
    public PanelJuego(Juego juego) {
        this.juego = juego;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Delegar dibujado al modelo
        if (juego != null) {
            juego.dibujar(g);
        }
    }
}