/*
* Clase para gestionar el archivo de acceso aleatorio que almacena
 * los resultados de las partidas de Pac-Man.
 * 
 * Utiliza RandomAccessFile para permitir lectura y escritura directa
 * en posiciones específicas del archivo, manteniendo registros de tamaño fijo.
 * 
 * Cada registro contiene: nombre del jugador (50 chars), puntaje (int)
 * y tiempo de juego (long).
 * 
 * Esta clase cumple con SRP al enfocarse únicamente en la persistencia
 * de resultados en archivo binario.
 */
package udistrital.avanzada.servidor.modelo.conexion;

/**
 *
 * @author juanr
 */
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;

public class ArchivoResultados {
    /** Nombre del archivo de acceso aleatorio */
    private static final String NOMBRE_ARCHIVO = "data/resultados.dat";
    
    /** Longitud máxima del nombre del jugador en caracteres */
    private static final int LONGITUD_NOMBRE = 50;
    
    /** 
     * Longitud total de cada registro en bytes:
     * - Nombre: 50 chars * 2 bytes = 100 bytes
     * - Puntaje: 1 int = 4 bytes
     * - Tiempo: 1 long = 8 bytes
     * Total: 112 bytes por registro
     */
    private static final int LONGITUD_REGISTRO = (LONGITUD_NOMBRE * 2) + 4 + 8;
    
    /**
     * Constructor privado para prevenir instanciación.
     * Esta clase solo debe usarse a través de sus métodos estáticos.
     */
    private ArchivoResultados() {
        // Constructor privado para clase utilitaria
    }
    
    /**
     * Guarda un resultado de partida al final del archivo de acceso aleatorio.
     * Crea el archivo si no existe. El nombre se ajusta a 50 caracteres
     * con relleno de espacios si es necesario.
     * 
     * Estructura del registro:
     * 1. Nombre del jugador: 50 caracteres (100 bytes)
     * 2. Puntaje: entero de 4 bytes
     * 3. Tiempo: long de 8 bytes
     * 
     * @param nombre Nombre del jugador (será truncado o rellenado a 50 chars)
     * @param puntaje Puntaje obtenido en la partida (debe ser >= 0)
     * @param tiempo Tiempo de juego en segundos (debe ser > 0)
     * @throws RuntimeException Si hay error de escritura en el archivo
     */
    public static void guardarResultado(String nombre, int puntaje, long tiempo) {
    RandomAccessFile raf = null;
    
    try {
        // Crear directorio si no existe
        File archivo = new File(NOMBRE_ARCHIVO);
        File directorio = archivo.getParentFile();
        if (directorio != null && !directorio.exists()) {
            boolean creado = directorio.mkdirs();
            if (creado) {
                System.out.println("[ARCHIVO] Directorio 'data' creado exitosamente");
            }
        }
        
        raf = new RandomAccessFile(NOMBRE_ARCHIVO, "rw");
        raf.seek(raf.length());
        
        // ... resto del código
        
        System.out.println("[ARCHIVO] Resultado guardado: " + nombre + " - " + puntaje + " puntos");
        
    } catch (IOException e) {
        System.err.println("[ERROR] No se pudo guardar resultado: " + e.getMessage());
        throw new RuntimeException("Error guardando resultado: " + e.getMessage(), e);
    } finally {
        // ... cierre
    }
}
    /**
     * Lee todos los registros del archivo y determina el mejor jugador.
     * El mejor jugador es aquel con mayor puntaje, y en caso de empate,
     * el que completó el juego en menor tiempo.
     * 
     * @return String formateado con información del mejor jugador,
     *         o mensaje indicando que no hay resultados
     * * @throws RuntimeException Si hay error de lectura del archivo
     */
    public static String obtenerMejorJugador() {
        RandomAccessFile raf = null;
        String mejorJugador = "";
        int mejorPuntaje = -1;
        long menorTiempo = Long.MAX_VALUE;
        
        try {
            File archivo = new File(NOMBRE_ARCHIVO);
            if (!archivo.exists()) {
                return "No hay resultados disponibles";
            }
            
            raf = new RandomAccessFile(NOMBRE_ARCHIVO, "r");
            
            // Leer todos los registros
            while (raf.getFilePointer() < raf.length()) {
                // Leer nombre (50 caracteres)
                StringBuilder nombre = new StringBuilder();
                for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                    nombre.append(raf.readChar());
                }
                
                // Leer puntaje y tiempo
                int puntaje = raf.readInt();
                long tiempo = raf.readLong();
                
                // Comparar para encontrar el mejor
                if (puntaje > mejorPuntaje || 
                    (puntaje == mejorPuntaje && tiempo < menorTiempo)) {
                    mejorJugador = nombre.toString().trim();
                    mejorPuntaje = puntaje;
                    menorTiempo = tiempo;
                }
            }
            
            if (mejorPuntaje == -1) {
                return "No hay resultados disponibles";
            }
            
            return String.format("Mejor Jugador: %s - Puntaje: %d - Tiempo: %d segundos", 
                                mejorJugador, mejorPuntaje, menorTiempo);
            
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo resultados: " + e.getMessage(), e);
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    // Log del error
                }
            }
        }
    }
    
    /**
     * Obtiene la longitud en bytes de cada registro almacenado.
     * Útil para cálculos de posicionamiento en el archivo.
     * 
     * @return Longitud del registro en bytes (112 bytes)
     */
    public static int getLongitudRegistro() {
        return LONGITUD_REGISTRO;
    }
    
    /**
     * Verifica si el archivo de resultados existe.
     * 
     * @return true si el archivo existe, false en caso contrario
     */
    public static boolean archivoExiste() {
        return new File(NOMBRE_ARCHIVO).exists();
    }
    
    /**
     * Cuenta el número total de registros en el archivo.
     * 
     * @return Número de partidas guardadas
     * @throws RuntimeException Si hay error de lectura
     */
    public static int contarRegistros() {
        try (RandomAccessFile raf = new RandomAccessFile(NOMBRE_ARCHIVO, "r")) {
            long longitudArchivo = raf.length();
            return (int) (longitudArchivo / LONGITUD_REGISTRO);
        } catch (IOException e) {
            return 0;
        }
    }
}