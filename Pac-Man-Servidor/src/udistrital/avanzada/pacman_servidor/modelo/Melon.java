package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa un melón (1000 puntos).
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Melon extends Fruta {
    
    public Melon(int x, int y) {
        super(x, y, 1000, "Melon");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Cuerpo verde claro
        g.setColor(new Color(144, 238, 144));
        g.fillOval(x + 2, y + 6, 20, 16);
        
        // Rayas verdes oscuras
        g.setColor(new Color(34, 139, 34));
        for (int i = 0; i < 4; i++) {
            g.drawArc(x + 4 + i * 4, y + 6, 4, 16, 90, 180);
        }
        
        // Tallo marrón
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x + 10, y + 4, 3, 4);
        
        // Curva del tallo
        g.drawArc(x + 12, y + 2, 4, 4, 0, 180);
    }
}