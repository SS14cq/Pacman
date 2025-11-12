package udistrital.avanzada.pacman_servidor.util;

import java.util.Random;
import udistrital.avanzada.pacman_servidor.modelo.*;

/**
 * Fábrica para crear frutas aleatoriamente.
 * Aplica patrón Factory para desacoplar creación de uso.
 * 
 * @author Steban
 * @version 1.0
 */
public class FabricaFrutas {
    
    private static final Random random = new Random();
    
    /**
     * Tipos de frutas disponibles (índices 0-7)
     */
    private static final int NUM_TIPOS_FRUTAS = 8;
    
    /**
     * Crea una fruta aleatoria en la posición indicada.
     * 
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return Fruta creada
     */
    public static Fruta crearFrutaAleatoria(int x, int y) {
        int tipo = random.nextInt(NUM_TIPOS_FRUTAS);
        
        switch (tipo) {
            case 0: return new Cereza(x, y);
            case 1: return new Fresa(x, y);
            case 2: return new Naranja(x, y);
            case 3: return new Manzana(x, y);
            case 4: return new Melon(x, y);
            case 5: return new Galaxian(x, y);
            case 6: return new Campana(x, y);
            case 7: return new Llave(x, y);
            default: return new Cereza(x, y);
        }
    }
    
    /**
     * Genera posición aleatoria dentro de límites.
     * 
     * @param maxX Ancho máximo
     * @param maxY Alto máximo
     * @return Array [x, y]
     */
    public static int[] generarPosicionAleatoria(int maxX, int maxY) {
        int margen = 50; // Evitar bordes
        int x = margen + random.nextInt(maxX - 2 * margen);
        int y = margen + random.nextInt(maxY - 2 * margen);
        return new int[]{x, y};
    }
}