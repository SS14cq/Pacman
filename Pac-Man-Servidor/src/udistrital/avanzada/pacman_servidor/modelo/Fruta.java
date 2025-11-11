package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Graphics;

/**
 * Clase abstracta que representa una fruta del juego.
 * Aplica OCP: abierta para extensión (nuevas frutas), cerrada para modificación.
 * 
 * @author Steban
 * @version 1.0
 */
public abstract class Fruta {
    
    protected int x;
    protected int y;
    protected static final int TAMANIO = 25;
    protected int puntos;
    protected String nombre;
    
    /**
     * Constructor de Fruta.
     * 
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param puntos Puntos que otorga
     * @param nombre Nombre de la fruta
     */
    public Fruta(int x, int y, int puntos, String nombre) {
        this.x = x;
        this.y = y;
        this.puntos = puntos;
        this.nombre = nombre;
    }
    
    /**
     * Dibuja la fruta. Cada subclase define su apariencia.
     * 
     * @param g Contexto gráfico
     */
    public abstract void dibujar(Graphics g);
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getPuntos() {
        return puntos;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public static int getTamanio() {
        return TAMANIO;
    }
}