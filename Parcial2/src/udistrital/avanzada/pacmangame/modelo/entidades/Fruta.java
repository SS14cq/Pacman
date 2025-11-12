package udistrital.avanzada.pacmangame.modelo.entidades;

import udistrital.avanzada.pacmangame.modelo.TipoFruta;



/**
 * Clase que representa una fruta en el juego.
 * Contiene su tipo, posición y estado.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Fruta {
    
    private TipoFruta tipo;
    private int x;
    private int y;
    private boolean comida;
    
    /**
     * Constructor de Fruta.
     * 
     * @param tipo Tipo de fruta
     * @param x Posición X
     * @param y Posición Y
     */
    public Fruta(TipoFruta tipo, int x, int y) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.comida = false;
    }
    
    /**
     * Obtiene el tipo de fruta.
     * 
     * @return Tipo de fruta
     */
    public TipoFruta getTipo() {
        return tipo;
    }
    
    /**
     * Obtiene la coordenada X de la fruta.
     * 
     * @return Posición X
     */
    public int getX() {
        return x;
    }
    
    /**
     * Obtiene la coordenada Y de la fruta.
     * 
     * @return Posición Y
     */
    public int getY() {
        return y;
    }
    
    /**
     * Verifica si la fruta ha sido comida.
     * 
     * @return true si fue comida, false en caso contrario
     */
    public boolean isComida() {
        return comida;
    }
    
    /**
     * Marca la fruta como comida.
     */
    public void marcarComida() {
        this.comida = true;
    }
    
    /**
     * Obtiene los puntos que otorga esta fruta.
     * 
     * @return Puntos de la fruta
     */
    public int getPuntos() {
        return tipo.getPuntos();
    }
    
    @Override
    public String toString() {
        return "Fruta{" +
                "tipo=" + tipo +
                ", x=" + x +
                ", y=" + y +
                ", comida=" + comida +
                '}';
    }
}