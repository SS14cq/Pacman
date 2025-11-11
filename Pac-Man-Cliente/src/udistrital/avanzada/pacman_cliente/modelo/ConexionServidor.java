package udistrital.avanzada.pacman_cliente.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Clase que encapsula la conexión con el servidor.
 * NO tiene dependencias de vista.
 * Solo maneja comunicación por socket.
 * 
 * PRINCIPIO: Single Responsibility
 * - Solo maneja la conexión y comunicación socket
 * - No conoce el orquestador (usa callback)
 * - No conoce la vista
 * 
 * PATRÓN: Callback (Listener) para notificar mensajes recibidos
 * 
 * @author Equipo Desarrollo
 * @version 1.0
 */
public class ConexionServidor {
    
    /**
     * Interfaz para callback de mensajes recibidos.
     * El orquestador implementará esta interfaz.
     */
    public interface MensajeListener {
        /**
         * Llamado cuando se recibe un mensaje del servidor.
         * 
         * @param mensaje Mensaje recibido
         */
        void onMensajeRecibido(String mensaje);
        
        /**
         * Llamado cuando hay un error de conexión.
         * 
         * @param error Descripción del error
         */
        void onError(String error);
        
        /**
         * Llamado cuando la conexión se cierra.
         */
        void onConexionCerrada();
    }
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private boolean conectado;
    private MensajeListener listener;
    private Thread threadEscucha;
    
    /**
     * Constructor.
     */
    public ConexionServidor() {
        this.conectado = false;
    }
    
    /**
     * Establece el listener para notificaciones.
     * 
     * @param listener Listener a notificar
     */
    public void setMensajeListener(MensajeListener listener) {
        this.listener = listener;
    }
    
    /**
     * Conecta al servidor.
     * 
     * @param ip Dirección IP del servidor
     * @param puerto Puerto del servidor
     * @throws IOException Si hay error de conexión
     */
    public void conectar(String ip, int puerto) throws IOException {
        if (conectado) {
            throw new IllegalStateException("Ya existe una conexión activa");
        }
        
        // Crear socket
        socket = new Socket(ip, puerto);
        
        // Crear streams
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
        
        conectado = true;
        
        // Iniciar thread de escucha
        iniciarEscucha();
    }
    
    /**
     * Inicia un thread que escucha mensajes del servidor.
     */
    private void iniciarEscucha() {
        threadEscucha = new Thread(() -> {
            try {
                String linea;
                while (conectado && (linea = entrada.readLine()) != null) {
                    // Notificar al listener (orquestador)
                    if (listener != null) {
                        final String mensaje = linea;
                        listener.onMensajeRecibido(mensaje);
                    }
                }
            } catch (IOException e) {
                if (conectado && listener != null) {
                    listener.onError("Error de lectura: " + e.getMessage());
                }
            } finally {
                if (conectado) {
                    desconectar();
                    if (listener != null) {
                        listener.onConexionCerrada();
                    }
                }
            }
        });
        
        threadEscucha.setDaemon(true);
        threadEscucha.start();
    }
    
    /**
     * Envía un mensaje al servidor.
     * 
     * @param mensaje Mensaje a enviar
     * @throws IOException Si hay error de envío
     */
    public void enviarMensaje(String mensaje) throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No hay conexión activa");
        }
        
        if (salida == null) {
            throw new IOException("Stream de salida no inicializado");
        }
        
        salida.println(mensaje);
    }
    
    /**
     * Desconecta del servidor.
     */
    public void desconectar() {
        conectado = false;
        
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignorar errores al cerrar
        }
    }
    
    /**
     * Verifica si está conectado.
     * 
     * @return true si está conectado
     */
    public boolean isConectado() {
        return conectado && socket != null && !socket.isClosed();
    }
    
    /**
     * Obtiene la dirección del servidor conectado.
     * 
     * @return Dirección IP o null si no está conectado
     */
    public String getDireccionServidor() {
        if (socket != null && !socket.isClosed()) {
            return socket.getInetAddress().getHostAddress();
        }
        return null;
    }
    
    /**
     * Obtiene el puerto del servidor conectado.
     * 
     * @return Puerto o -1 si no está conectado
     */
    public int getPuertoServidor() {
        if (socket != null && !socket.isClosed()) {
            return socket.getPort();
        }
        return -1;
    }
}