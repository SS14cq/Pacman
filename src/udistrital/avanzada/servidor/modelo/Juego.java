/*
 * Clase principal que gestiona la lógica completa del juego Pac-Man.
 * Coordina la interacción entre Pac-Man y las frutas, detecta colisiones,
 * controla el tiempo de juego y determina cuándo finaliza la partida.
 * 
 * Esta clase sigue el principio de responsabilidad única (SRP) al enfocarse
 * únicamente en la lógica del juego, sin incluir aspectos de red o persistencia.
 */
package udistrital.avanzada.servidor.modelo;

/**
 *
 * @author juanr
 */
import udistrital.avanzada.servidor.modelo.Fruta;
import java.util.ArrayList;
import java.util.Random;

public class Juego {
    /** Instancia de Pac-Man controlada en este juego */
    private PacMan pacman;
    
    /** Lista de frutas disponibles en el tablero */
    private ArrayList<Fruta> frutas;
    
    /** Tiempo de inicio del juego en milisegundos */
    private long tiempoInicio;
    
    /** Tiempo de finalización del juego en milisegundos */
    private long tiempoFin;
    
    /** Ancho del panel de juego en píxeles */
    private int anchoPanel;
    
    /** Alto del panel de juego en píxeles */
    private int altoPanel;
    
    /** Array con los nombres de los tipos de frutas disponibles */
    private static final String[] TIPOS_FRUTAS = {
        "Cereza", "Fresa", "Naranja", "Manzana", 
        "Melon", "Galaxian", "Campana", "Llave"
    };
    
    /** Array con los puntos correspondientes a cada tipo de fruta */
    private static final int[] PUNTOS_FRUTAS = {
        100, 300, 500, 700, 1000, 2000, 3000, 5000
    };
    
    /** Número total de frutas que deben generarse en el juego */
    private static final int TOTAL_FRUTAS = 4;
    
    /** Radio de colisión en píxeles para detectar cuando Pac-Man come una fruta */
    private static final int RADIO_COLISION = 30;
    
    /**
     * Constructor que inicializa un nuevo juego.
     * Crea a Pac-Man en el centro del tablero, genera frutas aleatorias
     * y marca el tiempo de inicio del juego.
     * 
     * @param ancho Ancho del panel de juego en píxeles (debe ser positivo)
     * @param alto Alto del panel de juego en píxeles (debe ser positivo)
     */
    public Juego(int ancho, int alto) {
        this.anchoPanel = ancho;
        this.altoPanel = alto;
        this.pacman = new PacMan(ancho / 2, alto / 2);
        this.frutas = new ArrayList<>();
        generarFrutas();
        this.tiempoInicio = System.currentTimeMillis();
    }
    
    /**
     * Genera aleatoriamente 4 frutas en el tablero de juego.
     * Cada fruta tiene un tipo y valor aleatorio, y se posiciona
     * en coordenadas aleatorias dentro de los límites del panel.
     * 
     * Las frutas se generan con un margen de 25 píxeles desde los bordes
     * para evitar que aparezcan pegadas a las paredes.
     */
    private void generarFrutas() {
        Random rand = new Random();
        for (int i = 0; i < TOTAL_FRUTAS; i++) {
            int indice = rand.nextInt(TIPOS_FRUTAS.length);
            int x = rand.nextInt(anchoPanel - 50) + 25;
            int y = rand.nextInt(altoPanel - 50) + 25;
            
            Fruta fruta = new Fruta(
                TIPOS_FRUTAS[indice], 
                PUNTOS_FRUTAS[indice], 
                x, y
            );
            frutas.add(fruta);
        }
    }
    
    /**
     * Verifica si hay colisión entre Pac-Man y alguna fruta no comida.
     * Utiliza la distancia euclidiana para determinar la colisión.
     * Si detecta una colisión, marca la fruta como comida y actualiza
     * la puntuación y contador de frutas de Pac-Man.
     * 
     * @return La fruta que fue comida, o null si no hubo colisión
     */
    public Fruta verificarColision() {
        for (Fruta fruta : frutas) {
            if (!fruta.isComida()) {
                // Calcular distancia euclidiana entre Pac-Man y la fruta
                int distancia = (int) Math.sqrt(
                    Math.pow(pacman.getX() - fruta.getX(), 2) + 
                    Math.pow(pacman.getY() - fruta.getY(), 2)
                );
                
                // Si la distancia es menor al radio de colisión
                if (distancia < RADIO_COLISION) {
                    fruta.setComida(true);
                    pacman.agregarPuntos(fruta.getPuntos());
                    pacman.incrementarFrutasComidas();
                    return fruta;
                }
            }
        }
        return null;
    }
    
    /**
     * Verifica si el juego ha terminado.
     * El juego termina cuando Pac-Man ha comido las 4 frutas.
     * 
     * @return true si se comieron las 4 frutas, false en caso contrario
     */
    public boolean juegoTerminado() {
        return pacman.getFrutasComidas() >= TOTAL_FRUTAS;
    }
    
    /**
     * Finaliza el juego registrando el tiempo de finalización.
     * Este método debe invocarse cuando el juego termina para
     * poder calcular correctamente la duración total del juego.
     */
    public void finalizarJuego() {
        this.tiempoFin = System.currentTimeMillis();
    }
    
    /**
     * Calcula y retorna el tiempo total de juego en segundos.
     * 
     * @return Duración del juego en segundos (tiempo fin - tiempo inicio)
     */
    public long getTiempoJuego() {
        return (tiempoFin - tiempoInicio) / 1000;
    }
    
    /**
     * Obtiene la instancia de Pac-Man del juego.
     * 
     * @return Objeto PacMan del juego actual
     */
    public PacMan getPacman() { 
        return pacman; 
    }
    
    /**
     * Obtiene la lista de frutas del juego.
     * 
     * @return ArrayList con todas las frutas (comidas y no comidas)
     */
    public ArrayList<Fruta> getFrutas() { 
        return frutas; 
    }
    
    /**
     * Obtiene el ancho del panel de juego.
     * 
     * @return Ancho en píxeles
     */
    public int getAnchoPanel() { 
        return anchoPanel; 
    }
    
    /**
     * Obtiene el alto del panel de juego.
     * 
     * @return Alto en píxeles
     */
    public int getAltoPanel() { 
        return altoPanel; 
    }
}