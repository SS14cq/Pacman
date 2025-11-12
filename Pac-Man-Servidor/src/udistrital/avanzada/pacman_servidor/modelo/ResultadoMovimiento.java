package udistrital.avanzada.pacman_servidor.modelo;

/**
 * DTO que encapsula el resultado de un movimiento de Pac-Man.
 * Permite comunicar información sin violar Ley de Demeter.
 * 
 * @author Steban
 * @version 1.0
 */
public class ResultadoMovimiento {
    
    private int casillasMovidas;
    private boolean limiteAlcanzado;
    private boolean frutaComida;
    private String nombreFruta;
    private int puntosGanados;
    
    public ResultadoMovimiento() {
        this.casillasMovidas = 0;
        this.limiteAlcanzado = false;
        this.frutaComida = false;
    }
    
    // Getters y setters
    public int getCasillasMovidas() {
        return casillasMovidas;
    }
    
    public void setCasillasMovidas(int casillasMovidas) {
        this.casillasMovidas = casillasMovidas;
    }
    
    public boolean isLimiteAlcanzado() {
        return limiteAlcanzado;
    }
    
    public void setLimiteAlcanzado(boolean limiteAlcanzado) {
        this.limiteAlcanzado = limiteAlcanzado;
    }
    
    public boolean isFrutaComida() {
        return frutaComida;
    }
    
    public void setFrutaComida(boolean frutaComida) {
        this.frutaComida = frutaComida;
    }
    
    public String getNombreFruta() {
        return nombreFruta;
    }
    
    public void setNombreFruta(String nombreFruta) {
        this.nombreFruta = nombreFruta;
    }
    
    public int getPuntosGanados() {
        return puntosGanados;
    }
    
    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }
}