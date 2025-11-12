package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase que representa al personaje Pac-Man.
 * Encapsula su posición, tamaño y comportamiento de movimiento.
 * Respeta el principio de Ley de Demeter al manejar internamente su estado.
 * 
 * @author Steban
 * @version 1.0
 */
public class PacMan {
    
    /**
     * Coordenada X de Pac-Man en píxeles
     */
    private int x;
    
    /**
     * Coordenada Y de Pac-Man en píxeles
     */
    private int y;
    
    /**
     * Tamaño (diámetro) de Pac-Man en píxeles
     */
    private static final int TAMANIO = 30;
    
    /**
     * Velocidad de movimiento en píxeles por casilla
     */
    private static final int VELOCIDAD = 20;
    
    /**
     * Número de casillas a mover por cada comando
     */
    private static final int CASILLAS_POR_MOVIMIENTO = 4;
    
    /**
     * Color de Pac-Man
     */
    private static final Color COLOR = Color.YELLOW;
    
    /**
     * Dirección actual de movimiento
     */
    private Direccion direccionActual;
    
    /**
     * Constructor de Pac-Man.
     * 
     * @param x Posición inicial en X
     * @param y Posición inicial en Y
     */
    public PacMan(int x, int y) {
        this.x = x;
        this.y = y;
        this.direccionActual = Direccion.DERECHA;
    }
    
    /**
     * Intenta mover a Pac-Man en la dirección indicada.
     * Mueve 4 casillas, deteniéndose si encuentra un límite.
     * 
     * @param direccion Dirección del movimiento
     * @param anchoMax Ancho máximo de la ventana
     * @param altoMax Alto máximo de la ventana
     * @return Número de casillas efectivamente movidas (0-4)
     */
    public int intentarMover(Direccion direccion, int anchoMax, int altoMax) {
        this.direccionActual = direccion;
        int casillasMovidas = 0;
        
        // Intentar mover 4 casillas
        for (int i = 0; i < CASILLAS_POR_MOVIMIENTO; i++) {
            int nuevoX = x + (direccion.getDeltaX() * VELOCIDAD);
            int nuevoY = y + (direccion.getDeltaY() * VELOCIDAD);
            
            // Verificar límites (dejando espacio para el tamaño de Pac-Man)
            if (nuevoX >= 0 && nuevoX + TAMANIO <= anchoMax &&
                nuevoY >= 0 && nuevoY + TAMANIO <= altoMax) {
                
                x = nuevoX;
                y = nuevoY;
                casillasMovidas++;
            } else {
                // Límite alcanzado, detener movimiento
                break;
            }
        }
        
        return casillasMovidas;
    }
    
    /**
     * Verifica si Pac-Man colisiona con una posición dada.
     * Usa detección de colisión circular.
     * 
     * @param objX Coordenada X del objeto
     * @param objY Coordenada Y del objeto
     * @param objTamanio Tamaño del objeto
     * @return true si hay colisión, false en caso contrario
     */
    public boolean colisionaCon(int objX, int objY, int objTamanio) {
        // Centro de Pac-Man
        int centroX = x + TAMANIO / 2;
        int centroY = y + TAMANIO / 2;
        
        // Centro del objeto
        int objCentroX = objX + objTamanio / 2;
        int objCentroY = objY + objTamanio / 2;
        
        // Distancia entre centros
        double distancia = Math.sqrt(
            Math.pow(centroX - objCentroX, 2) + 
            Math.pow(centroY - objCentroY, 2)
        );
        
        // Suma de radios
        double sumaRadios = (TAMANIO + objTamanio) / 2.0;
        
        return distancia < sumaRadios;
    }
    
    /**
     * Dibuja a Pac-Man en el contexto gráfico.
     * Respeta el principio de que el objeto se dibuja a sí mismo.
     * 
     * @param g Contexto gráfico donde dibujar
     */
    public void dibujar(Graphics g) {
        // Cuerpo amarillo
        g.setColor(COLOR);
        g.fillOval(x, y, TAMANIO, TAMANIO);
        
        // Boca (triángulo negro)
        g.setColor(Color.BLACK);
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        // Calcular puntos del triángulo según dirección
        switch (direccionActual) {
            case DERECHA:
                xPoints[0] = x + TAMANIO / 2;
                yPoints[0] = y + TAMANIO / 2;
                xPoints[1] = x + TAMANIO;
                yPoints[1] = y + TAMANIO / 4;
                xPoints[2] = x + TAMANIO;
                yPoints[2] = y + 3 * TAMANIO / 4;
                break;
                
            case IZQUIERDA:
                xPoints[0] = x + TAMANIO / 2;
                yPoints[0] = y + TAMANIO / 2;
                xPoints[1] = x;
                yPoints[1] = y + TAMANIO / 4;
                xPoints[2] = x;
                yPoints[2] = y + 3 * TAMANIO / 4;
                break;
                
            case ARRIBA:
                xPoints[0] = x + TAMANIO / 2;
                yPoints[0] = y + TAMANIO / 2;
                xPoints[1] = x + TAMANIO / 4;
                yPoints[1] = y;
                xPoints[2] = x + 3 * TAMANIO / 4;
                yPoints[2] = y;
                break;
                
            case ABAJO:
                xPoints[0] = x + TAMANIO / 2;
                yPoints[0] = y + TAMANIO / 2;
                xPoints[1] = x + TAMANIO / 4;
                yPoints[1] = y + TAMANIO;
                xPoints[2] = x + 3 * TAMANIO / 4;
                yPoints[2] = y + TAMANIO;
                break;
        }
        
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Ojo
        g.setColor(Color.BLACK);
        int eyeSize = 4;
        int eyeX = x + TAMANIO / 2 + (direccionActual.getDeltaX() * 5);
        int eyeY = y + TAMANIO / 3 + (direccionActual.getDeltaY() * 5);
        g.fillOval(eyeX, eyeY, eyeSize, eyeSize);
    }
    
    /**
     * Obtiene la coordenada X de Pac-Man.
     * 
     * @return Coordenada X
     */
    public int getX() {
        return x;
    }
    
    /**
     * Obtiene la coordenada Y de Pac-Man.
     * 
     * @return Coordenada Y
     */
    public int getY() {
        return y;
    }
    
    /**
     * Obtiene el tamaño de Pac-Man.
     * 
     * @return Tamaño en píxeles
     */
    public static int getTamanio() {
        return TAMANIO;
    }
}