/*
 * Clase que representa al personaje principal Pac-Man.
 * Maneja la posición, movimiento, puntuación y contador de frutas comidas.
 * Implementa la lógica de movimiento con detección de límites del tablero.
 */
package udistrital.avanzada.servidor.modelo;

/**
 *
 * @author juanr
 */
public class PacMan {
    /** Coordenada X actual de Pac-Man en el tablero */
    private int x;
    
    /** Coordenada Y actual de Pac-Man en el tablero */
    private int y;
    
    /** Puntuación total acumulada durante el juego */
    private int puntajeTotal;
    
    /** Cantidad de frutas comidas hasta el momento */
    private int frutasComidas;
    
    /** Tamaño del sprite de Pac-Man en píxeles */
    private static final int TAMANO = 30;
    
    /**
     * Constructor que inicializa a Pac-Man en una posición específica.
     * La puntuación y las frutas comidas se inicializan en cero.
     * 
     * @param centroX Posición inicial en el eje X (generalmente el centro del panel)
     * @param centroY Posición inicial en el eje Y (generalmente el centro del panel)
     */
    public PacMan(int centroX, int centroY) {
        this.x = centroX;
        this.y = centroY;
        this.puntajeTotal = 0;
        this.frutasComidas = 0;
    }
    
    /**
     * Mueve a Pac-Man en una dirección específica un número de casillas.
     * El movimiento se detiene al alcanzar los límites del tablero.
     * Cada casilla equivale a 10 píxeles de desplazamiento.
     * 
     * @param direccion La dirección del movimiento: "ARRIBA", "ABAJO", "IZQUIERDA" o "DERECHA"
     * @param casillas El número de casillas a intentar moverse (generalmente 4)
     * @param limiteX El límite horizontal del panel de juego en píxeles
     * @param limiteY El límite vertical del panel de juego en píxeles
     * @return El número de casillas realmente movidas antes de alcanzar un límite
     */
    public int mover(String direccion, int casillas, int limiteX, int limiteY) {
        int movidas = 0;
        final int PASO = 10; // Tamaño de cada casilla en píxeles
        
        for (int i = 0; i < casillas; i++) {
            int nuevoX = x;
            int nuevoY = y;
            
            // Calcular nueva posición según la dirección
            switch (direccion.toUpperCase()) {
                case "ARRIBA":
                    nuevoY -= PASO;
                    break;
                case "ABAJO":
                    nuevoY += PASO;
                    break;
                case "IZQUIERDA":
                    nuevoX -= PASO;
                    break;
                case "DERECHA":
                    nuevoX += PASO;
                    break;
                default:
                    return movidas; // Dirección inválida, no se mueve
            }
            
            // Verificar que la nueva posición esté dentro de los límites
            if (nuevoX >= 0 && nuevoX <= limiteX - TAMANO && 
                nuevoY >= 0 && nuevoY <= limiteY - TAMANO) {
                x = nuevoX;
                y = nuevoY;
                movidas++;
            } else {
                break; // Se alcanzó el límite, detener movimiento
            }
        }
        return movidas;
    }
    
    /**
     * Agrega puntos a la puntuación total de Pac-Man.
     * Se invoca cuando Pac-Man come una fruta.
     * 
     * @param puntos La cantidad de puntos a agregar (debe ser positivo)
     */
    public void agregarPuntos(int puntos) {
        this.puntajeTotal += puntos;
    }
    
    /**
     * Incrementa en uno el contador de frutas comidas.
     * El juego termina cuando se alcanzan 4 frutas comidas.
     */
    public void incrementarFrutasComidas() {
        this.frutasComidas++;
    }
    
    /**
     * Obtiene la coordenada X actual de Pac-Man.
     * 
     * @return Posición X en píxeles
     */
    public int getX() { 
        return x; 
    }
    
    /**
     * Obtiene la coordenada Y actual de Pac-Man.
     * 
     * @return Posición Y en píxeles
     */
    public int getY() { 
        return y; 
    }
    
    /**
     * Obtiene la puntuación total acumulada.
     * 
     * @return Puntos totales obtenidos
     */
    public int getPuntajeTotal() { 
        return puntajeTotal; 
    }
    
    /**
     * Obtiene la cantidad de frutas comidas.
     * 
     * @return Número de frutas comidas (0-4)
     */
    public int getFrutasComidas() { 
        return frutasComidas; 
    }
    
    /**
     * Obtiene el tamaño del sprite de Pac-Man.
     * 
     * @return Tamaño en píxeles (30px)
     */
    public static int getTamano() {
        return TAMANO;
    }
}