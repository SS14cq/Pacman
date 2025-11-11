package udistrital.avanzada.pacman_servidor.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel donde se dibuja el juego de Pac-Man.
 * Solo responsable de renderizar, no tiene lógica.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class PanelJuego extends JPanel {
    
    private Runnable accionDibujar;
    
    /**
     * Constructor del panel de juego.
     * 
     * @param ancho Ancho del panel
     * @param alto Alto del panel
     */
    public PanelJuego(int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }
    
    /**
     * Establece la acción a ejecutar al dibujar.
     * El controlador inyectará la lógica de dibujado.
     * 
     * @param accion Runnable con la lógica de dibujado
     */
    public void setAccionDibujar(Runnable accion) {
        this.accionDibujar = accion;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Si hay una acción de dibujar configurada, ejecutarla
        if (accionDibujar != null) {
            accionDibujar.run();
        }
    }
    
    /**
     * Método para solicitar redibujar desde el exterior.
     */
    public void redibujar() {
        SwingUtilities.invokeLater(this::repaint);
    }
}