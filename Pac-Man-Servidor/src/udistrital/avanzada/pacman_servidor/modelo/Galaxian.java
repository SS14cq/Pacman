package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representa el símbolo Galaxian (2000 puntos).
 * Referencia al juego clásico de arcade.
 * 
 * @author Steban
 * @version 1.0
 */
public class Galaxian extends Fruta {
    
    public Galaxian(int x, int y) {
        super(x, y, 2000, "Galaxian");
    }
    
    @Override
    public void dibujar(Graphics g) {
        // Nave espacial estilo Galaxian
        
        // Cuerpo azul
        g.setColor(new Color(0, 100, 255));
        int[] xBody = {x + 12, x + 18, x + 15, x + 9, x + 6};
        int[] yBody = {y + 5, y + 12, y + 20, y + 20, y + 12};
        g.fillPolygon(xBody, yBody, 5);
        
        // Alas amarillas
        g.setColor(Color.YELLOW);
        int[] xWingL = {x + 6, x + 2, x + 9};
        int[] yWingL = {y + 12, y + 15, y + 15};
        g.fillPolygon(xWingL, yWingL, 3);
        
        int[] xWingR = {x + 18, x + 22, x + 15};
        int[] yWingR = {y + 12, y + 15, y + 15};
        g.fillPolygon(xWingR, yWingR, 3);
        
        // Cabina roja
        g.setColor(Color.RED);
        g.fillOval(x + 10, y + 8, 4, 4);
        
        // Detalles blancos
        g.setColor(Color.WHITE);
        g.fillOval(x + 11, y + 9, 2, 2);
    }
}