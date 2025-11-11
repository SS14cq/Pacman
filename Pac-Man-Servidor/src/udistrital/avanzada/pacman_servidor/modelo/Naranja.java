package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa una naranja (500 puntos).
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Naranja extends Fruta {
    
    public Naranja(int x, int y) {
        super(x, y, 500, "Naranja");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Círculo naranja
        g.setColor(Color.ORANGE);
        g.fillOval(x + 2, y + 5, 18, 18);
        
        // Borde
        g.setColor(new Color(255, 140, 0));
        g.drawOval(x + 2, y + 5, 18, 18);
        
        // Tallo verde
        g.setColor(new Color(34, 139, 34));
        g.fillRect(x + 10, y + 3, 3, 4);
        
        // Hoja
        g.fillOval(x + 13, y + 2, 5, 4);
        
        // Textura (puntos)
        g.setColor(new Color(255, 165, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g.fillOval(x + 6 + i * 4, y + 9 + j * 4, 1, 1);
            }
        }
    }
}