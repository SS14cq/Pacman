package udistrital.avanzada.pacmangame.controlador.servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que representa el servidor del juego Pac-Man.
 * Escucha conexiones de clientes y crea hilos para atenderlos.
 * Cumple con SRP al encargarse solo de aceptar conexiones.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Servidor {
    
    private ServerSocket serverSocket;
    private int puerto;
    private boolean ejecutando;
    private ControladorServidor controlador;
    private int clientesAtendidos;
    
    /**
     * Constructor del servidor.
     * 
     * @param puerto Puerto en el que escuchará el servidor
     * @param controlador Controlador del servidor
     */
    public Servidor(int puerto, ControladorServidor controlador) {
        this.puerto = puerto;
        this.controlador = controlador;
        this.ejecutando = false;
        this.clientesAtendidos = 0;
    }
    
    /**
     * Inicia el servidor y comienza a escuchar conexiones.
     * 
     * @throws IOException Si ocurre un error al iniciar el servidor
     */
    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        ejecutando = true;
        
        controlador.notificarMensaje("Servidor iniciado en puerto " + puerto);
        controlador.notificarMensaje("Esperando conexiones de clientes...");
        
        // Hilo para escuchar conexiones
        Thread hiloEscucha = new Thread(new Runnable() {
            @Override
            public void run() {
                escucharConexiones();
            }
        });
        hiloEscucha.start();
    }
    
    /**
     * Escucha y acepta conexiones de clientes.
     */
    private void escucharConexiones() {
        while (ejecutando) {
            try {
                Socket socketCliente = serverSocket.accept();
                clientesAtendidos++;
                
                controlador.notificarMensaje("Cliente conectado: " + 
                    socketCliente.getInetAddress().getHostAddress());
                
                // Crear y lanzar hilo para atender al cliente
                HiloServidor hiloCliente = new HiloServidor(
                    socketCliente, 
                    controlador, 
                    clientesAtendidos
                );
                
                Thread hilo = new Thread(hiloCliente);
                hilo.start();
                
            } catch (IOException e) {
                if (ejecutando) {
                    controlador.notificarError("Error aceptando cliente: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Detiene el servidor y cierra el socket.
     */
    public void detener() {
        ejecutando = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                controlador.notificarMensaje("Servidor detenido");
            }
        } catch (IOException e) {
            controlador.notificarError("Error al cerrar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el servidor está ejecutándose.
     * 
     * @return true si está ejecutándose
     */
    public boolean isEjecutando() {
        return ejecutando;
    }
    
    /**
     * Obtiene el número de clientes atendidos.
     * 
     * @return Número de clientes atendidos
     */
    public int getClientesAtendidos() {
        return clientesAtendidos;
    }
    
    /**
     * Obtiene el puerto del servidor.
     * 
     * @return Puerto del servidor
     */
    public int getPuerto() {
        return puerto;
    }
}