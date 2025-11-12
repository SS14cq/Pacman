package udistrital.avanzada.pacman_cliente.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel especializado para mostrar frames de video.
 * Actualiza la imagen mostrada de forma eficiente.
 * 
 * PRINCIPIO: Single Responsibility
 * - Solo maneja visualización de imágenes
 * - No conoce red ni lógica de negocio
 * 
 * @author Steban
 * @version 1.0
 */
public class PanelVideo extends JPanel {
    
    private BufferedImage frameActual;
    private final Object lock = new Object();
    private boolean escalar;
    
    /**
     * Constructor que inicializa el panel.
     * 
     * @param ancho Ancho del panel
     * @param alto Alto del panel
     * @param escalar Si debe escalar la imagen al tamaño del panel
     */
    public PanelVideo(int ancho, int alto, boolean escalar) {
        this.escalar = escalar;
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);
        setDoubleBuffered(true); // Para evitar parpadeo
    }
    
    /**
     * Constructor simplificado.
     * 
     * @param ancho Ancho del panel
     * @param alto Alto del panel
     */
    public PanelVideo(int ancho, int alto) {
        this(ancho, alto, true);
    }
    
    /**
     * Actualiza el frame mostrado.
     * Thread-safe para uso desde múltiples hilos.
     * 
     * @param nuevoFrame Nueva imagen a mostrar
     */
    public void actualizarFrame(BufferedImage nuevoFrame) {
        synchronized (lock) {
            this.frameActual = nuevoFrame;
        }
        
        // Redibujar en el EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }
    
    /**
     * Limpia el panel (muestra pantalla negra).
     */
    public void limpiar() {
        synchronized (lock) {
            this.frameActual = null;
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        synchronized (lock) {
            if (frameActual != null) {
                Graphics2D g2d = (Graphics2D) g;
                
                // Habilitar antialiasing para mejor calidad
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                if (escalar) {
                    // Escalar al tamaño del panel manteniendo aspecto
                    int panelAncho = getWidth();
                    int panelAlto = getHeight();
                    int imgAncho = frameActual.getWidth();
                    int imgAlto = frameActual.getHeight();
                    
                    double escalaAncho = (double) panelAncho / imgAncho;
                    double escalaAlto = (double) panelAlto / imgAlto;
                    double escala = Math.min(escalaAncho, escalaAlto);
                    
                    int nuevoAncho = (int) (imgAncho * escala);
                    int nuevoAlto = (int) (imgAlto * escala);
                    
                    int x = (panelAncho - nuevoAncho) / 2;
                    int y = (panelAlto - nuevoAlto) / 2;
                    
                    g2d.drawImage(frameActual, x, y, nuevoAncho, nuevoAlto, null);
                } else {
                    // Dibujar sin escalar, centrado
                    int x = (getWidth() - frameActual.getWidth()) / 2;
                    int y = (getHeight() - frameActual.getHeight()) / 2;
                    g2d.drawImage(frameActual, x, y, null);
                }
                
                g2d.dispose();
            } else {
                // Mostrar mensaje cuando no hay frame
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                String mensaje = "Esperando video...";
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g.drawString(mensaje, x, y);
            }
        }
    }
    
    /**
     * Obtiene el frame actual (para debugging).
     * 
     * @return Frame actual o null
     */
    public BufferedImage getFrameActual() {
        synchronized (lock) {
            return frameActual;
        }
    }
}
