package udistrital.avanzada.pacmangame.controlador.gestores;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.TipoFruta;
import udistrital.avanzada.pacmangame.modelo.entidades.Fruta;
import udistrital.avanzada.pacmangame.modelo.entidades.PacMan;

/**
 * Gestor principal del juego Pac-Man.
 * Coordina la lógica del juego y el estado general.
 * Cumple con SRP al manejar solo la lógica del juego.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class GestorJuego {
    
    private PacMan pacMan;
    private List<Fruta> frutas;
    private GestorMovimiento gestorMovimiento;
    private GestorPuntuacion gestorPuntuacion;
    private Random random;
    private long tiempoInicio;
    
    /**
     * Constructor del gestor de juego.
     * 
     * @param anchoTablero Ancho del tablero
     * @param altoTablero Alto del tablero
     */
    public GestorJuego(int anchoTablero, int altoTablero) {
        this.pacMan = new PacMan(anchoTablero, altoTablero);
        this.frutas = new ArrayList<>();
        this.gestorMovimiento = new GestorMovimiento(anchoTablero, altoTablero);
        this.gestorPuntuacion = new GestorPuntuacion();
        this.random = new Random();
        this.tiempoInicio = System.currentTimeMillis();
        
        generarFrutasAleatorias(anchoTablero, altoTablero);
    }
    
    /**
     * Genera frutas en posiciones aleatorias del tablero.
     * 
     * @param anchoTablero Ancho del tablero
     * @param altoTablero Alto del tablero
     */
    private void generarFrutasAleatorias(int anchoTablero, int altoTablero) {
        TipoFruta[] tiposFrutas = TipoFruta.values();
        int margen = Constantes.TAMANIO_FRUTA * 2;
        
        for (int i = 0; i < Constantes.CANTIDAD_FRUTAS; i++) {
            TipoFruta tipo = tiposFrutas[random.nextInt(tiposFrutas.length)];
            
            int x = margen + random.nextInt(anchoTablero - 2 * margen);
            int y = margen + random.nextInt(altoTablero - 2 * margen);
            
            // Evitar que aparezca muy cerca de Pac-Man
            while (gestorMovimiento.calcularDistancia(x, y, pacMan.getX(), pacMan.getY()) < 100) {
                x = margen + random.nextInt(anchoTablero - 2 * margen);
                y = margen + random.nextInt(altoTablero - 2 * margen);
            }
            
            frutas.add(new Fruta(tipo, x, y));
        }
    }
    
    /**
     * Verifica si Pac-Man ha colisionado con alguna fruta.
     * 
     * @return Fruta colisionada o null si no hay colisión
     */
    public Fruta verificarColisionConFrutas() {
        int radioColision = (Constantes.TAMANIO_PACMAN + Constantes.TAMANIO_FRUTA) / 2;
        
        for (Fruta fruta : frutas) {
            if (!fruta.isComida()) {
                double distancia = gestorMovimiento.calcularDistancia(
                    pacMan.getX(), pacMan.getY(), 
                    fruta.getX(), fruta.getY()
                );
                
                if (distancia < radioColision) {
                    fruta.marcarComida();
                    gestorPuntuacion.agregarPuntos(fruta.getPuntos());
                    return fruta;
                }
            }
        }
        return null;
    }
    
    /**
     * Verifica si el juego ha terminado.
     * 
     * @return true si todas las frutas han sido comidas
     */
    public boolean juegoTerminado() {
        for (Fruta fruta : frutas) {
            if (!fruta.isComida()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Obtiene el tiempo transcurrido desde el inicio del juego.
     * 
     * @return Tiempo en milisegundos
     */
    public long obtenerTiempoTranscurrido() {
        return System.currentTimeMillis() - tiempoInicio;
    }
    
    /**
     * Obtiene Pac-Man.
     * 
     * @return Instancia de Pac-Man
     */
    public PacMan getPacMan() {
        return pacMan;
    }
    
    /**
     * Obtiene la lista de frutas.
     * 
     * @return Lista de frutas
     */
    public List<Fruta> getFrutas() {
        return frutas;
    }
    
    /**
     * Obtiene el gestor de movimiento.
     * 
     * @return Gestor de movimiento
     */
    public GestorMovimiento getGestorMovimiento() {
        return gestorMovimiento;
    }
    
    /**
     * Obtiene el gestor de puntuación.
     * 
     * @return Gestor de puntuación
     */
    public GestorPuntuacion getGestorPuntuacion() {
        return gestorPuntuacion;
    }
    
    /**
     * Reinicia el juego con nuevas frutas y posición inicial.
     * 
     * @param anchoTablero Ancho del tablero
     * @param altoTablero Alto del tablero
     */
    public void reiniciarJuego(int anchoTablero, int altoTablero) {
        this.pacMan = new PacMan(anchoTablero, altoTablero);
        this.frutas.clear();
        this.gestorPuntuacion.reiniciar();
        this.tiempoInicio = System.currentTimeMillis();
        generarFrutasAleatorias(anchoTablero, altoTablero);
    }
}