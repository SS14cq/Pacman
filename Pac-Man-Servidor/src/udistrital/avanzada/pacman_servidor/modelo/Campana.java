package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una campana (3000 puntos).
 * 
 * @author Steban
 * @version 1.0
 */
public class Campana extends Fruta {
    
    public Campana(int x, int y) {
        super(x, y, 3000, "Campana");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Cuerpo dorado de la campana
        g.setColor(new Color(255, 215, 0));
        int[] xPoints = {x + 12, x + 6, x + 5, x + 19, x + 18};
        int[] yPoints = {y + 6, y + 12, y + 18, y + 18, y + 12};
        g.fillPolygon(xPoints, yPoints, 5);
        
        // Borde inferior
        g.setColor(new Color(218, 165, 32));
        g.drawLine(x + 5, y + 18, x + 19, y + 18);
        g.drawArc(x + 5, y + 16, 14, 4, 0, -180);
        
        // Manija superior
        g.setColor(new Color(255, 215, 0));
        g.fillRect(x + 11, y + 3, 2, 4);
        g.fillOval(x + 9, y + 2, 6, 3);
        
        // Badajo (clapper)
        g.setColor(new Color(139, 69, 19));
        g.fillOval(x + 11, y + 17, 2, 4);
        
        // Brillo
        g.setColor(Color.YELLOW);
        g.drawLine(x + 8, y + 10, x + 8, y + 14);
    }
}