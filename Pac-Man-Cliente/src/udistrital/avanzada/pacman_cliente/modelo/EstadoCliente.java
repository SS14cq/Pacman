package udistrital.avanzada.pacman_cliente.modelo;

/**
 * Clase que encapsula el estado actual del cliente.
 * NO tiene dependencias de vista ni de red.
 * Solo almacena y proporciona acceso al estado de la sesión.
 * 
 * PRINCIPIO: Single Responsibility
 * - Solo maneja el estado del cliente
 * - No conoce cómo se muestra (Vista)
 * - No conoce cómo se conecta (ConexionServidor)
 * 
 * @author Steban
 * @version 1.0
 */
public class EstadoCliente {
    
    private Estado estadoActual;
    private String nombreJugador;
    private boolean autenticado;
    private int puntajeActual;
    private long tiempoTranscurrido;
    private int frutasRestantes;
    
    /**
     * Constructor. Inicia en estado desconectado.
     */
    public EstadoCliente() {
        this.estadoActual = Estado.DESCONECTADO;
        this.autenticado = false;
        this.puntajeActual = 0;
        this.tiempoTranscurrido = 0;
        this.frutasRestantes = 0;
    }
    
    /**
     * Cambia el estado actual.
     * 
     * @param nuevoEstado Nuevo estado
     */
    public void cambiarEstado(Estado nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
    
    /**
     * Marca al cliente como autenticado y guarda el nombre.
     * 
     * @param nombreJugador Nombre del jugador autenticado
     */
    public void establecerAutenticacion(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.autenticado = true;
        this.estadoActual = Estado.JUGANDO;
    }
    
    /**
     * Actualiza la información del juego.
     * 
     * @param puntaje Puntaje actual
     * @param tiempo Tiempo transcurrido
     * @param frutas Frutas restantes
     */
    public void actualizarEstadoJuego(int puntaje, long tiempo, int frutas) {
        this.puntajeActual = puntaje;
        this.tiempoTranscurrido = tiempo;
        this.frutasRestantes = frutas;
    }
    
    /**
     * Reinicia el estado a desconectado.
     */
    public void reiniciar() {
        this.estadoActual = Estado.DESCONECTADO;
        this.autenticado = false;
        this.nombreJugador = null;
        this.puntajeActual = 0;
        this.tiempoTranscurrido = 0;
        this.frutasRestantes = 0;
    }
    
    // ========== GETTERS ==========
    
    public Estado getEstadoActual() {
        return estadoActual;
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    public boolean isAutenticado() {
        return autenticado;
    }
    
    public int getPuntajeActual() {
        return puntajeActual;
    }
    
    public long getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
    
    public int getFrutasRestantes() {
        return frutasRestantes;
    }
    
    /**
     * Verifica si está conectado al servidor.
     * 
     * @return true si está conectado
     */
    public boolean isConectado() {
        return estadoActual != Estado.DESCONECTADO;
    }
    
    /**
     * Verifica si el juego está en progreso.
     * 
     * @return true si está jugando
     */
    public boolean isJugando() {
        return estadoActual == Estado.JUGANDO;
    }
}