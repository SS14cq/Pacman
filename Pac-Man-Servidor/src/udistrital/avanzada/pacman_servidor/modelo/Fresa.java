package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una fresa (300 puntos).
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Fresa extends Fruta {
    
    public Fresa(int x, int y) {
        super(x, y, 300, "Fresa");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Cuerpo rojo de la fresa
        g.setColor(Color.RED);
        int[] xPoints = {x + 12, x + 20, x + 15, x + 9, x + 4};
        int[] yPoints = {y + 5, y + 10, y + 20, y + 20, y + 10};
        g.fillPolygon(xPoints, yPoints, 5);
        
        // Hojas verdes superiores
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 6, y, 12, 6);
        
        // Semillas amarillas
        g.setColor(Color.YELLOW);
        g.fillOval(x + 8, y + 10, 2, 2);
        g.fillOval(x + 14, y + 10, 2, 2);
        g.fillOval(x + 11, y + 14, 2, 2);
        g.fillOval(x + 8, y + 16, 2, 2);
        g.fillOval(x + 14, y + 16, 2, 2);
    }
}