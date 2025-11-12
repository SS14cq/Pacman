package udistrital.avanzada.pacmangame.vista.servidor;



import javax.swing.*;
import java.awt.*;
import java.util.List;
import udistrital.avanzada.pacmangame.controlador.gestores.GestorJuego;
import udistrital.avanzada.pacmangame.controlador.servidor.ControladorJuego;
import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.entidades.Fruta;
import udistrital.avanzada.pacmangame.modelo.entidades.PacMan;

/**
 * Panel donde se dibuja el juego de Pac-Man.
 * Solo se encarga de la visualización, sin lógica de negocio.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class PanelJuego extends JPanel {
    
    private ControladorJuego controlador;
    private GestorJuego gestorJuego;
    private int anchoTablero;
    private int altoTablero;
    
    /**
     * Constructor del panel de juego.
     * 
     * @param controlador Controlador del juego
     * @param anchoTablero Ancho del tablero
     * @param altoTablero Alto del tablero
     */
    public PanelJuego(ControladorJuego controlador, int anchoTablero, int altoTablero) {
        this.controlador = controlador;
        this.gestorJuego = controlador.getGestorJuego();
        this.anchoTablero = anchoTablero;
        this.altoTablero = altoTablero;
        
        setPreferredSize(new Dimension(anchoTablero, altoTablero));
        setBackground(Color.BLACK);
    }
    
    /**
     * Inicia el panel de juego.
     */
    public void iniciar() {
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Activar anti-aliasing para mejor calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar frutas
        dibujarFrutas(g2d);
        
        // Dibujar Pac-Man
        dibujarPacMan(g2d);
        
        // Dibujar información
        dibujarInformacion(g2d);
    }
    
    /**
     * Dibuja a Pac-Man en el panel.
     * 
     * @param g2d Graphics2D para dibujar
     */
    private void dibujarPacMan(Graphics2D g2d) {
        PacMan pacMan = gestorJuego.getPacMan();
        int tamanio = Constantes.TAMANIO_PACMAN;
        
        // Dibujar círculo amarillo
        g2d.setColor(Color.YELLOW);
        g2d.fillArc(
            pacMan.getX() - tamanio / 2,
            pacMan.getY() - tamanio / 2,
            tamanio,
            tamanio,
            30,  // Ángulo inicial (boca abierta)
            300  // Ángulo de apertura
        );
        
        // Dibujar ojo
        g2d.setColor(Color.BLACK);
        g2d.fillOval(
            pacMan.getX() - 3,
            pacMan.getY() - tamanio / 4,
            6,
            6
        );
    }
    
    /**
     * Dibuja las frutas en el panel.
     * 
     * @param g2d Graphics2D para dibujar
     */
    private void dibujarFrutas(Graphics2D g2d) {
        List<Fruta> frutas = gestorJuego.getFrutas();
        int tamanio = Constantes.TAMANIO_FRUTA;
        
        for (Fruta fruta : frutas) {
            if (!fruta.isComida()) {
                // Elegir color según el tipo de fruta
                Color color = obtenerColorFruta(fruta);
                g2d.setColor(color);
                
                // Dibujar fruta como círculo
                g2d.fillOval(
                    fruta.getX() - tamanio / 2,
                    fruta.getY() - tamanio / 2,
                    tamanio,
                    tamanio
                );
                
                // Dibujar borde
                g2d.setColor(Color.WHITE);
                g2d.drawOval(
                    fruta.getX() - tamanio / 2,
                    fruta.getY() - tamanio / 2,
                    tamanio,
                    tamanio
                );
                
                // Dibujar nombre de la fruta
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                String nombre = fruta.getTipo().getNombre();
                FontMetrics fm = g2d.getFontMetrics();
                int anchoTexto = fm.stringWidth(nombre);
                
                g2d.drawString(
                    nombre,
                    fruta.getX() - anchoTexto / 2,
                    fruta.getY() + tamanio / 2 + 15
                );
            }
        }
    }
    
    /**
     * Obtiene el color para representar una fruta.
     * 
     * @param fruta Fruta a colorear
     * @return Color para la fruta
     */
    private Color obtenerColorFruta(Fruta fruta) {
        switch (fruta.getTipo()) {
            case CEREZA:
                return new Color(220, 20, 60); // Rojo cereza
            case FRESA:
                return new Color(255, 99, 71); // Rojo fresa
            case NARANJA:
                return new Color(255, 165, 0); // Naranja
            case MANZANA:
                return new Color(50, 205, 50); // Verde manzana
            case MELON:
                return new Color(144, 238, 144); // Verde claro
            case GALAXIAN:
                return new Color(138, 43, 226); // Morado
            case CAMPANA:
                return new Color(255, 215, 0); // Dorado
            case LLAVE:
                return new Color(192, 192, 192); // Plateado
            default:
                return Color.WHITE;
        }
    }
    
    /**
     * Dibuja la información del juego (puntuación y tiempo).
     * 
     * @param g2d Graphics2D para dibujar
     */
    private void dibujarInformacion(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Puntuación
        String puntuacion = "Puntuación: " + 
            gestorJuego.getGestorPuntuacion().getPuntuacionTotal();
        g2d.drawString(puntuacion, 10, 20);
        
        // Tiempo
        long tiempoMs = gestorJuego.obtenerTiempoTranscurrido();
        double tiempoSeg = tiempoMs / 1000.0;
        String tiempo = String.format("Tiempo: %.1f s", tiempoSeg);
        g2d.drawString(tiempo, 10, 40);
        
        // Frutas restantes
        int frutasRestantes = 0;
        for (Fruta fruta : gestorJuego.getFrutas()) {
            if (!fruta.isComida()) {
                frutasRestantes++;
            }
        }
        String frutas = "Frutas restantes: " + frutasRestantes;
        g2d.drawString(frutas, 10, 60);
    }
}