package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una llave (5000 puntos).
 */
public class Llave extends Fruta {
    
    public Llave(int x, int y) {
        super(x, y, 5000, "Llave");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Cabeza de la llave
        g.setColor(new Color(255, 215, 0)); // Dorado
        g.fillOval(x, y, 10, 10);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, 10, 10);
        g.fillOval(x + 3, y + 3, 4, 4); // Agujero
        
        // Cuerpo
        g.setColor(new Color(255, 215, 0));
        g.fillRect(x + 8, y + 4, 12, 3);
        
        // Dientes
        g.fillRect(x + 18, y + 2, 2, 3);
        g.fillRect(x + 18, y + 5, 2, 3);
    }
}