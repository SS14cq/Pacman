package udistrital.avanzada.pacmangame.controlador.cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import udistrital.avanzada.pacmangame.modelo.Constantes;

/**
 * Clase que representa el cliente del juego Pac-Man.
 * Se conecta al servidor y maneja la comunicación.
 * Cumple con SRP al encargarse solo de la comunicación con el servidor.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Cliente {
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String ip;
    private int puerto;
    private ControladorCliente controlador;
    private boolean conectado;
    
    /**
     * Constructor del cliente.
     * 
     * @param ip Dirección IP del servidor
     * @param puerto Puerto del servidor
     * @param controlador Controlador del cliente
     */
    public Cliente(String ip, int puerto, ControladorCliente controlador) {
        this.ip = ip;
        this.puerto = puerto;
        this.controlador = controlador;
        this.conectado = false;
    }
    
    /**
     * Conecta al cliente con el servidor.
     * 
     * @return true si la conexión fue exitosa
     */
    public boolean conectar() {
        try {
            socket = new Socket(ip, puerto);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            conectado = true;
            
            controlador.notificarMensaje("Conectado al servidor " + ip + ":" + puerto);
            
            // Iniciar hilo para recibir mensajes
            Thread hiloRecepcion = new Thread(new Runnable() {
                @Override
                public void run() {
                    recibirMensajes();
                }
            });
            hiloRecepcion.start();
            
            return true;
            
        } catch (IOException e) {
            controlador.notificarError("Error al conectar: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Recibe mensajes del servidor continuamente.
     */
    private void recibirMensajes() {
        try {
            String mensaje;
            while (conectado && (mensaje = entrada.readLine()) != null) {
                procesarMensaje(mensaje);
            }
        } catch (IOException e) {
            if (conectado) {
                controlador.notificarError("Error recibiendo mensaje: " + e.getMessage());
            }
        }
    }
    
    /**
     * Procesa un mensaje recibido del servidor.
     * 
     * @param mensaje Mensaje a procesar
     */
    private void procesarMensaje(String mensaje) {
        if (mensaje.startsWith(Constantes.MSG_SOLICITUD_USUARIO)) {
            controlador.solicitarUsuario();
            
        } else if (mensaje.startsWith(Constantes.MSG_SOLICITUD_PASSWORD)) {
            controlador.solicitarContrasena();
            
        } else if (mensaje.startsWith(Constantes.MSG_AUTENTICACION_OK)) {
            controlador.notificarMensaje("¡Autenticación exitosa! El juego comenzará en el servidor.");
            
        } else if (mensaje.startsWith(Constantes.MSG_AUTENTICACION_FAIL)) {
            controlador.notificarError("Autenticación fallida. Credenciales incorrectas.");
            desconectar();
            
        } else if (mensaje.startsWith(Constantes.MSG_MOVIMIENTO)) {
            controlador.solicitarMovimiento();
            
        } else if (mensaje.startsWith(Constantes.MSG_LIMITE_ALCANZADO)) {
            String[] partes = mensaje.split(":");
            if (partes.length > 1) {
                controlador.notificarMensaje("Límite alcanzado. Movido " + partes[1] + " casillas.");
            } else {
                controlador.notificarMensaje("Límite de la ventana alcanzado.");
            }
            
        } else if (mensaje.startsWith(Constantes.MSG_FRUTA_COMIDA)) {
            String[] partes = mensaje.split(":");
            if (partes.length > 1) {
                controlador.notificarMensaje("¡Fruta comida! " + partes[1]);
            }
            
        } else if (mensaje.startsWith(Constantes.MSG_FIN_JUEGO)) {
            String[] partes = mensaje.split("\\|");
            StringBuilder resultado = new StringBuilder("===== JUEGO TERMINADO =====\n");
            for (int i = 1; i < partes.length; i++) {
                resultado.append(partes[i]).append("\n");
            }
            resultado.append("==========================");
            controlador.notificarMensaje(resultado.toString());
            desconectar();
            
        } else {
            controlador.notificarMensaje("Servidor: " + mensaje);
        }
    }
    
    /**
     * Envía un mensaje al servidor.
     * 
     * @param mensaje Mensaje a enviar
     */
    public void enviarMensaje(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
        }
    }
    
    /**
     * Desconecta el cliente del servidor.
     */
    public void desconectar() {
        conectado = false;
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            controlador.notificarMensaje("Desconectado del servidor");
            
        } catch (IOException e) {
            controlador.notificarError("Error al desconectar: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el cliente está conectado.
     * 
     * @return true si está conectado
     */
    public boolean isConectado() {
        return conectado;
    }
}