package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una cereza (100 puntos).
 */
public class Cereza extends Fruta {
    
    public Cereza(int x, int y) {
        super(x, y, 100, "Cereza");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Dos círculos rojos (cerezas)
        g.setColor(Color.RED);
        g.fillOval(x, y + 5, 10, 10);
        g.fillOval(x + 12, y + 5, 10, 10);
        
        // Tallo verde
        g.setColor(new Color(34, 139, 34));
        g.drawLine(x + 5, y + 5, x + 10, y);
        g.drawLine(x + 17, y + 5, x + 10, y);
    }
}