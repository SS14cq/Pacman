package udistrital.avanzada.pacman_cliente.control;

import java.io.IOException;
import udistrital.avanzada.pacman_cliente.modelo.ConexionServidor;
import udistrital.avanzada.pacman_cliente.modelo.Estado;
import udistrital.avanzada.pacman_cliente.modelo.EstadoCliente;

/**
 * Controlador intermediario que protege el acceso al modelo.
 * El ORQUESTADOR habla con este controlador, NO directamente con el modelo.
 * 
 * RESPONSABILIDAD:
 * - Exponer operaciones del modelo de forma controlada
 * - Validar operaciones antes de ejecutarlas en el modelo
 * - NO tiene lógica de negocio compleja
 * - NO conoce la vista
 * 
 * PATRÓN: Facade simplificado sobre el modelo
 * 
 * @author Steban
 * @version 1.0
 */
public class ControladorModelo {
    
    private EstadoCliente estado;
    private ConexionServidor conexion;
    
    /**
     * Constructor.
     * Inicializa las instancias del modelo.
     */
    public ControladorModelo() {
        this.estado = new EstadoCliente();
        this.conexion = new ConexionServidor();
    }
    
    /**
     * Establece el listener de mensajes de la conexión.
     * Este método permite al orquestador registrarse como listener.
     * 
     * @param listener Listener que recibirá notificaciones
     */
    public void setMensajeListener(ConexionServidor.MensajeListener listener) {
        conexion.setMensajeListener(listener);
    }
    
    /**
     * Intenta conectar al servidor.
     * Valida que no esté ya conectado antes de intentar.
     * 
     * @param ip Dirección IP del servidor
     * @param puerto Puerto del servidor
     * @throws IOException Si hay error de conexión
     * @throws IllegalStateException Si ya está conectado
     */
    public void conectarAlServidor(String ip, int puerto) throws IOException {
        // Validación: no conectar si ya está conectado
        if (conexion.isConectado()) {
            throw new IllegalStateException("Ya existe una conexión activa");
        }
        
        // Cambiar estado ANTES de conectar
        estado.cambiarEstado(Estado.CONECTANDO);
        
        try {
            conexion.conectar(ip, puerto);
            // Estado se cambiará a AUTENTICANDO cuando el servidor lo solicite
        } catch (IOException e) {
            // Si falla, volver a DESCONECTADO
            estado.cambiarEstado(Estado.DESCONECTADO);
            throw e;
        }
    }
    
    /**
     * Establece el listener para frames de video.
     * Delega al ConexionServidor.
     * 
     * @param listener Listener a notificar
     */
    public void setFrameListener(ConexionServidor.FrameListener listener) {
        if (conexion != null) {
            conexion.setFrameListener(listener);
        }
    }

    
    /**
     * Envía un mensaje al servidor.
     * Valida que esté conectado antes de enviar.
     * 
     * @param mensaje Mensaje a enviar
     * @throws IOException Si hay error de envío
     * @throws IllegalStateException Si no está conectado
     */
    public void enviarMensaje(String mensaje) throws IOException {
        if (!conexion.isConectado()) {
            throw new IllegalStateException("No hay conexión activa");
        }
        
        conexion.enviarMensaje(mensaje);
    }
    
    /**
     * Desconecta del servidor y reinicia el estado.
     */
    public void desconectar() {
        conexion.desconectar();
        estado.reiniciar();
    }
    
    /**
     * Marca al jugador como autenticado.
     * 
     * @param nombreJugador Nombre del jugador
     */
    public void establecerAutenticacion(String nombreJugador) {
        estado.establecerAutenticacion(nombreJugador);
    }
    
    /**
     * Cambia el estado actual del cliente.
     * 
     * @param nuevoEstado Nuevo estado
     */
    public void cambiarEstado(Estado nuevoEstado) {
        estado.cambiarEstado(nuevoEstado);
    }
    
    /**
     * Actualiza la información del juego en progreso.
     * 
     * @param puntaje Puntaje actual
     * @param tiempo Tiempo transcurrido
     * @param frutas Frutas restantes
     */
    public void actualizarEstadoJuego(int puntaje, long tiempo, int frutas) {
        estado.actualizarEstadoJuego(puntaje, tiempo, frutas);
    }
    
    // ========== CONSULTAS DE ESTADO ==========
    
    /**
     * Verifica si está conectado al servidor.
     * 
     * @return true si está conectado
     */
    public boolean isConectado() {
        return conexion.isConectado();
    }
    
    /**
     * Obtiene el estado actual del cliente.
     * 
     * @return Estado actual
     */
    public Estado getEstadoActual() {
        return estado.getEstadoActual();
    }
    
    /**
     * Obtiene el nombre del jugador.
     * 
     * @return Nombre del jugador o null si no está autenticado
     */
    public String getNombreJugador() {
        return estado.getNombreJugador();
    }
    
    /**
     * Verifica si el jugador está autenticado.
     * 
     * @return true si está autenticado
     */
    public boolean isAutenticado() {
        return estado.isAutenticado();
    }
    
    /**
     * Verifica si está jugando.
     * 
     * @return true si está jugando
     */
    public boolean isJugando() {
        return estado.isJugando();
    }
    
    /**
     * Obtiene el puntaje actual.
     * 
     * @return Puntaje
     */
    public int getPuntajeActual() {
        return estado.getPuntajeActual();
    }
    
    /**
     * Obtiene el tiempo transcurrido.
     * 
     * @return Tiempo en segundos
     */
    public long getTiempoTranscurrido() {
        return estado.getTiempoTranscurrido();
    }
    
    /**
     * Obtiene las frutas restantes.
     * 
     * @return Cantidad de frutas
     */
    public int getFrutasRestantes() {
        return estado.getFrutasRestantes();
    }
    
    /**
     * Obtiene información de conexión.
     * 
     * @return String con IP:Puerto o null si no está conectado
     */
    public String getInformacionConexion() {
        if (!conexion.isConectado()) {
            return null;
        }
        
        return String.format("%s:%d", 
            conexion.getDireccionServidor(),
            conexion.getPuertoServidor()
        );
    }
}