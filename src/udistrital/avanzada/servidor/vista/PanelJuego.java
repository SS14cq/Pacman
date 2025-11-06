/*
 * Panel personalizado que renderiza gráficamente el juego de Pac-Man.
 * Dibuja a Pac-Man, las frutas, el puntaje y las estadísticas del juego.
 * 
 * Esta clase sobrescribe paintComponent para realizar dibujado personalizado.
 * Se actualiza mediante repaint() cada vez que cambia el estado del juego.
 */
package udistrital.avanzada.servidor.vista;

/**
 *
 * @author juanr
 */
import javax.swing.*;
import java.awt.*;
import udistrital.avanzada.servidor.modelo.Fruta;
import udistrital.avanzada.servidor.modelo.Juego;
import udistrital.avanzada.servidor.modelo.PacMan;


public class PanelJuego extends JPanel {
    /** Referencia al juego que se está visualizando */
    private Juego juego;
    
    /**
     * Constructor del panel de juego.
     * 
     * @param juego Instancia del juego a dibujar
     */
    public PanelJuego(Juego juego) {
        this.juego = juego;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(600, 600));
    }
    
    /**
     * Método sobrescrito que dibuja todos los elementos del juego.
     * Se invoca automáticamente cuando se llama a repaint().
     * 
     * Dibuja en orden:
     * 1. Fondo negro
     * 2. Frutas no comidas
     * 3. Pac-Man
     * 4. Información de puntaje y estadísticas
     * 
     * @param g Contexto gráfico proporcionado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Activar anti-aliasing para mejor calidad visual
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar Pac-Man (círculo amarillo con boca)
        dibujarPacMan(g2d);
        
        // Dibujar frutas no comidas
        for (Fruta fruta : juego.getFrutas()) {
            if (!fruta.isComida()) {
                dibujarFruta(g2d, fruta);
            }
        }
        
        // Dibujar información en pantalla
        dibujarInformacion(g2d);
    }
    
    /**
     * Dibuja a Pac-Man en su posición actual.
     * Usa fillArc para crear efecto de boca abierta.
     * 
     * @param g2d Contexto gráfico 2D
     */
    private void dibujarPacMan(Graphics2D g2d) {
        PacMan pacman = juego.getPacman();
        g2d.setColor(Color.YELLOW);
        // Dibujar arco de 30° a 300° para simular boca
        g2d.fillArc(pacman.getX(), pacman.getY(), 30, 30, 30, 300);
        
        // Dibujar ojo
        g2d.setColor(Color.BLACK);
        g2d.fillOval(pacman.getX() + 18, pacman.getY() + 8, 5, 5);
    }
    
    /**
     * Dibuja una fruta en el panel con su color característico.
     * También muestra los puntos que otorga la fruta.
     * 
     * @param g2d Contexto gráfico 2D
     * @param fruta La fruta a dibujar
     */
    private void dibujarFruta(Graphics2D g2d, Fruta fruta) {
        // Seleccionar color según tipo de fruta
        Color color = obtenerColorFruta(fruta.getTipo());
        
        // Dibujar fruta como círculo
        g2d.setColor(color);
        g2d.fillOval(fruta.getX(), fruta.getY(), 20, 20);
        
        // Dibujar borde
        g2d.setColor(Color.WHITE);
        g2d.drawOval(fruta.getX(), fruta.getY(), 20, 20);
        
        // Dibujar puntos debajo de la fruta
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        String puntos = String.valueOf(fruta.getPuntos());
        FontMetrics fm = g2d.getFontMetrics();
        int anchoTexto = fm.stringWidth(puntos);
        g2d.drawString(puntos, fruta.getX() + 10 - anchoTexto/2, fruta.getY() - 5);
    }
    
    /**
     * Obtiene el color asociado a cada tipo de fruta.
     * 
     * @param tipo Nombre del tipo de fruta
     * @return Color correspondiente a la fruta
     */
    private Color obtenerColorFruta(String tipo) {
        switch (tipo) {
            case "Cereza": return Color.RED;
            case "Fresa": return Color.PINK;
            case "Naranja": return Color.ORANGE;
            case "Manzana": return Color.GREEN;
            case "Melon": return new Color(50, 205, 50);
            case "Galaxian": return Color.CYAN;
            case "Campana": return Color.YELLOW;
            case "Llave": return Color.MAGENTA;
            default: return Color.WHITE;
        }
    }
    
    /**
     * Dibuja la información del juego (puntaje, frutas comidas, instrucciones).
     * Se muestra en la parte superior del panel con fondo semi-transparente.
     * 
     * @param g2d Contexto gráfico 2D
     */
    private void dibujarInformacion(Graphics2D g2d) {
        PacMan pacman = juego.getPacman();
        
        // Fondo semi-transparente para el panel de información
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(10, 10, 580, 80, 15, 15);
        
        // Borde del panel
        g2d.setColor(new Color(52, 152, 219));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(10, 10, 580, 80, 15, 15);
        
        // Título
        g2d.setColor(new Color(241, 196, 15));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("PAC-MAN", 240, 38);
        
        // Puntaje
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Puntaje: " + pacman.getPuntajeTotal(), 30, 65);
        
        // Frutas comidas
        g2d.drawString("Frutas: " + pacman.getFrutasComidas() + "/4", 230, 65);
        
        // Indicador de progreso visual
        g2d.setColor(new Color(46, 204, 113));
        int anchoProgreso = (pacman.getFrutasComidas() * 100);
        g2d.fillRect(420, 50, anchoProgreso, 20);
        g2d.setColor(new Color(52, 73, 94));
        g2d.drawRect(420, 50, 150, 20);
        
        // Leyenda de progreso
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("Progreso", 455, 63);
    }
}