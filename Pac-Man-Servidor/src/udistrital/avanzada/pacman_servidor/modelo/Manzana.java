package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una manzana (700 puntos).
 * 
 * @author Steban
 * @version 1.0
 */
public class Manzana extends Fruta {
    
    public Manzana(int x, int y) {
        super(x, y, 700, "Manzana");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Cuerpo rojo
        g.setColor(Color.RED);
        g.fillOval(x + 3, y + 8, 8, 10);
        g.fillOval(x + 11, y + 8, 8, 10);
        g.fillOval(x + 5, y + 10, 12, 12);
        
        // Brillo (highlight)
        g.setColor(new Color(255, 100, 100));
        g.fillOval(x + 7, y + 11, 4, 4);
        
        // Tallo marrón
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x + 11, y + 5, 2, 5);
        
        // Hoja verde
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 13, y + 4, 5, 4);
    }
}