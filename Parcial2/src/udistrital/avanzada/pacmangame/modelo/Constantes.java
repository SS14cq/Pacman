package udistrital.avanzada.pacmangame.modelo;

/**
 * Clase que contiene las constantes del juego Pac-Man.
 * Centraliza valores configurables y evita números mágicos.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Constantes {
    
    // Dimensiones del juego
    public static final int ANCHO_VENTANA = 800;
    public static final int ALTO_VENTANA = 600;
    public static final int TAMANIO_CASILLA = 20;
    
    // Movimiento
    public static final int CASILLAS_POR_MOVIMIENTO = 4;
    public static final int VELOCIDAD_MOVIMIENTO = 50; // milisegundos
    
    // Puntuación
    public static final int PUNTOS_CEREZA = 100;
    public static final int PUNTOS_FRESA = 300;
    public static final int PUNTOS_NARANJA = 500;
    public static final int PUNTOS_MANZANA = 700;
    public static final int PUNTOS_MELON = 1000;
    public static final int PUNTOS_GALAXIAN = 2000;
    public static final int PUNTOS_CAMPANA = 3000;
    public static final int PUNTOS_LLAVE = 5000;
    
    // Juego
    public static final int CANTIDAD_FRUTAS = 4;
    public static final int TAMANIO_PACMAN = 30;
    public static final int TAMANIO_FRUTA = 25;
    
    // Archivos
    public static final String ARCHIVO_REGISTROS = "data/jugadores.dat";
    public static final String CONFIG_SERVIDOR = "data/config_servidor.properties";
    public static final String CONFIG_CLIENTE = "data/config_cliente.properties";
    
    // Registro archivo aleatorio
    public static final int TAMANIO_NOMBRE = 50;
    public static final int TAMANIO_REGISTRO = 66; // 50 (nombre) + 4 (puntos) + 8 (tiempo) + 4 (activo)
    
    // Mensajes de red
    public static final String MSG_SOLICITUD_USUARIO = "USUARIO";
    public static final String MSG_SOLICITUD_PASSWORD = "PASSWORD";
    public static final String MSG_AUTENTICACION_OK = "AUTH_OK";
    public static final String MSG_AUTENTICACION_FAIL = "AUTH_FAIL";
    public static final String MSG_MOVIMIENTO = "MOVER";
    public static final String MSG_LIMITE_ALCANZADO = "LIMITE";
    public static final String MSG_FRUTA_COMIDA = "FRUTA";
    public static final String MSG_FIN_JUEGO = "FIN";
    
    private Constantes() {
        // Constructor privado para evitar instanciación
    }
}