package udistrital.avanzada.pacman_servidor.modelo;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.pacman_servidor.util.FabricaFrutas;

/**
 * Clase principal que gestiona la lógica del juego Pac-Man.
 * Coordina Pac-Man, frutas y reglas del juego.
 * 
 * @author Steban
 * @version 1.0
 */
public class Juego {
    
    private PacMan pacman;
    private List<Fruta> frutas;
    private int puntajeTotal;
    private long tiempoInicio;
    private int anchoVentana;
    private int altoVentana;
    private String nombreJugador;
    private static final int CANTIDAD_FRUTAS = 4;
    
    /**
     * Constructor del juego.
     * 
     * @param anchoVentana Ancho del área de juego
     * @param altoVentana Alto del área de juego
     */
    public Juego(int anchoVentana, int altoVentana) {
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
        this.frutas = new ArrayList<>();
        this.puntajeTotal = 0;
    }
    
    /**
     * Inicializa el juego: coloca Pac-Man al centro y genera frutas.
     * 
     * @param nombreJugador Nombre del jugador
     */
    public void inicializar(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        
        // Pac-Man en el centro
        int centroX = anchoVentana / 2;
        int centroY = altoVentana / 2;
        this.pacman = new PacMan(centroX, centroY);
        
        // Generar 4 frutas aleatorias
        for (int i = 0; i < CANTIDAD_FRUTAS; i++) {
            int[] pos = FabricaFrutas.generarPosicionAleatoria(anchoVentana, altoVentana);
            Fruta fruta = FabricaFrutas.crearFrutaAleatoria(pos[0], pos[1]);
            frutas.add(fruta);
        }
        
        // Iniciar cronómetro
        this.tiempoInicio = System.currentTimeMillis();
    }
    
    /**
     * Mueve a Pac-Man y procesa colisiones.
     * Encapsula toda la lógica de movimiento.
     * 
     * @param direccion Dirección del movimiento
     * @return Resultado del movimiento
     */
    public ResultadoMovimiento moverPacMan(Direccion direccion) {
        ResultadoMovimiento resultado = new ResultadoMovimiento();
        
        // Mover Pac-Man
        int casillasMovidas = pacman.intentarMover(direccion, anchoVentana, altoVentana);
        resultado.setCasillasMovidas(casillasMovidas);
        
        // Verificar si alcanzó límite
        if (casillasMovidas < 4) {
            resultado.setLimiteAlcanzado(true);
        }
        
        // Verificar colisiones con frutas
        Fruta frutaColisionada = verificarColisionConFrutas();
        if (frutaColisionada != null) {
            puntajeTotal += frutaColisionada.getPuntos();
            frutas.remove(frutaColisionada);
            
            resultado.setFrutaComida(true);
            resultado.setNombreFruta(frutaColisionada.getNombre());
            resultado.setPuntosGanados(frutaColisionada.getPuntos());
        }
        
        return resultado;
    }
    
    /**
     * Verifica colisión de Pac-Man con todas las frutas.
     * Método privado - detalle de implementación.
     * 
     * @return Fruta colisionada o null
     */
    private Fruta verificarColisionConFrutas() {
        for (Fruta fruta : frutas) {
            if (pacman.colisionaCon(fruta.getX(), fruta.getY(), Fruta.getTamanio())) {
                return fruta;
            }
        }
        return null;
    }
    
    /**
     * Dibuja todos los elementos del juego.
     * Delega el dibujado a cada objeto.
     * 
     * @param g Contexto gráfico
     */
    public void dibujar(Graphics g) {
        pacman.dibujar(g);
        
        for (Fruta fruta : frutas) {
            fruta.dibujar(g);
        }
    }
    
    /**
     * Verifica si el juego terminó (todas las frutas comidas).
     * 
     * @return true si terminó, false en caso contrario
     */
    public boolean isJuegoTerminado() {
        return frutas.isEmpty();
    }
    
    /**
     * Calcula el tiempo transcurrido en segundos.
     * 
     * @return Tiempo en segundos
     */
    public long getTiempoTranscurrido() {
        return (System.currentTimeMillis() - tiempoInicio) / 1000;
    }
    
    public int getPuntajeTotal() {
        return puntajeTotal;
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    public int getFrutasRestantes() {
        return frutas.size();
    }
}