package udistrital.avanzada.pacman_cliente.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase que encapsula la conexión con el servidor.
 * MODIFICADA para usar DataInputStream/DataOutputStream
 * Compatible con el servidor.
 * @author Steban
 * @version 1.0
 */
public class ConexionServidor {
    
    public interface MensajeListener {
        void onMensajeRecibido(String mensaje);
        void onError(String error);
        void onConexionCerrada();
    }
    
    public interface FrameListener {
        void onFrameRecibido(byte[] frameBytes);
    }
    
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private boolean conectado;
    private MensajeListener listener;
    private FrameListener frameListener;
    private Thread threadEscucha;
    
    public ConexionServidor() {
        this.conectado = false;
    }
    
    public void setMensajeListener(MensajeListener listener) {
        this.listener = listener;
    }
    
    public void setFrameListener(FrameListener listener) {
        this.frameListener = listener;
    }
    
    public void conectar(String ip, int puerto) throws IOException {
        if (conectado) {
            throw new IllegalStateException("Ya existe una conexión activa");
        }
        
        socket = new Socket(ip, puerto);
        entrada = new DataInputStream(socket.getInputStream());
        salida = new DataOutputStream(socket.getOutputStream());
        conectado = true;
        
        iniciarEscucha();
    }
    
    private void iniciarEscucha() {
        threadEscucha = new Thread(() -> {
            try {
                while (conectado) {
                    // Leer tipo de mensaje
                    String tipo = entrada.readUTF();
                    
                    if ("FRAME_VIDEO".equals(tipo)) {
                        recibirFrame();
                    } else {
                        // Leer contenido del mensaje
                        String contenido = entrada.readUTF();
                        
                        if (listener != null) {
                            listener.onMensajeRecibido(contenido);
                        }
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
    
    private void recibirFrame() {
        try {
            int tamaño = entrada.readInt();
            byte[] frameBytes = new byte[tamaño];
            entrada.readFully(frameBytes);
            
            if (frameListener != null) {
                frameListener.onFrameRecibido(frameBytes);
            }
        } catch (IOException e) {
            if (conectado && listener != null) {
                listener.onError("Error al recibir frame: " + e.getMessage());
            }
        }
    }
    
    public void enviarMensaje(String mensaje) throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No hay conexión activa");
        }
        
        synchronized (salida) {
            salida.writeUTF("CMD");
            salida.writeUTF(mensaje);
            salida.flush();
        }
    }
    
    public void desconectar() {
        conectado = false;
        
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            // Ignorar
        }
    }
    
    public boolean isConectado() {
        return conectado && socket != null && !socket.isClosed();
    }
    
    public String getDireccionServidor() {
        if (socket != null && !socket.isClosed()) {
            return socket.getInetAddress().getHostAddress();
        }
        return null;
    }
    
    public int getPuertoServidor() {
        if (socket != null && !socket.isClosed()) {
            return socket.getPort();
        }
        return -1;
    }
}
