package udistrital.avanzada.pacmangame.controlador.servidor;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.Direccion;
import udistrital.avanzada.pacmangame.modelo.entidades.RegistroJuego;

/**
 * Hilo del servidor que atiende a un cliente específico.
 * Maneja la autenticación y el flujo del juego para un cliente.
 * Cumple con SRP al encargarse solo de la comunicación con un cliente.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class HiloServidor implements Runnable {
    
    private Socket socket;
    private ControladorServidor controlador;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int numeroCliente;
    private String nombreJugador;
    
    /**
     * Constructor del hilo servidor.
     * 
     * @param socket Socket del cliente
     * @param controlador Controlador del servidor
     * @param numeroCliente Número identificador del cliente
     */
    public HiloServidor(Socket socket, ControladorServidor controlador, int numeroCliente) {
        this.socket = socket;
        this.controlador = controlador;
        this.numeroCliente = numeroCliente;
    }
    
    @Override
    public void run() {
        try {
            inicializarStreams();
            
            if (autenticarCliente()) {
                controlador.notificarMensaje("Cliente #" + numeroCliente + 
                    " autenticado: " + nombreJugador);
                
                // Notificar al cliente que fue autenticado
                enviarMensaje(Constantes.MSG_AUTENTICACION_OK);
                
                // Iniciar el juego
                ejecutarJuego();
            } else {
                controlador.notificarMensaje("Cliente #" + numeroCliente + 
                    " rechazado: credenciales inválidas");
                enviarMensaje(Constantes.MSG_AUTENTICACION_FAIL);
            }
            
        } catch (IOException e) {
            controlador.notificarError("Error en hilo cliente #" + numeroCliente + 
                ": " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Inicializa los streams de entrada y salida.
     * 
     * @throws IOException Si ocurre un error al crear los streams
     */
    private void inicializarStreams() throws IOException {
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);
    }
    
    /**
     * Autentica al cliente solicitando usuario y contraseña.
     * 
     * @return true si la autenticación es exitosa
     * @throws IOException Si ocurre un error de comunicación
     */
    private boolean autenticarCliente() throws IOException {
        // Solicitar nombre de usuario
        enviarMensaje(Constantes.MSG_SOLICITUD_USUARIO);
        nombreJugador = entrada.readLine();
        
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            return false;
        }
        
        // Solicitar contraseña
        enviarMensaje(Constantes.MSG_SOLICITUD_PASSWORD);
        String contrasena = entrada.readLine();
        
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }
        
        // Validar credenciales con el controlador
        return controlador.validarCredenciales(nombreJugador, contrasena);
    }
    
    /**
     * Ejecuta el flujo del juego con el cliente.
     * 
     * @throws IOException Si ocurre un error de comunicación
     */
    private void ejecutarJuego() throws IOException {
        // Iniciar el juego en el controlador
        controlador.iniciarJuego(nombreJugador);
        
        boolean juegoActivo = true;
        
        while (juegoActivo) {
            // Solicitar movimiento al cliente
            enviarMensaje(Constantes.MSG_MOVIMIENTO);
            String direccionStr = entrada.readLine();
            
            if (direccionStr == null) {
                break;
            }
            
            // Convertir a dirección
            Direccion direccion = Direccion.fromString(direccionStr);
            
            if (direccion == null) {
                enviarMensaje("Dirección inválida. Use: Arriba, Abajo, Izquierda o Derecha");
                continue;
            }
            
            // Procesar movimiento
            int casillasMovidas = controlador.moverPacMan(direccion);
            
            // Informar al cliente
            if (casillasMovidas < Constantes.CASILLAS_POR_MOVIMIENTO) {
                enviarMensaje(Constantes.MSG_LIMITE_ALCANZADO + ":" + casillasMovidas);
            } else {
                enviarMensaje("Movido " + casillasMovidas + " casillas");
            }
            
            // Verificar si comió una fruta
            String frutaComida = controlador.verificarColision();
            if (frutaComida != null) {
                enviarMensaje(Constantes.MSG_FRUTA_COMIDA + ":" + frutaComida);
            }
            
            // Verificar si el juego terminó
            if (controlador.juegoTerminado()) {
                juegoActivo = false;
                finalizarJuego();
            }
        }
    }
    
    /**
     * Finaliza el juego y envía los resultados al cliente.
     * 
     * @throws IOException Si ocurre un error de comunicación
     */
    private void finalizarJuego() throws IOException {
        // Obtener resultados del juego
        RegistroJuego registro = controlador.finalizarJuego();
        
        // Enviar resultados al cliente
        String mensaje = String.format("%s|Jugador: %s|Puntaje: %d|Tiempo: %.2f segundos",
            Constantes.MSG_FIN_JUEGO,
            registro.getNombreJugador(),
            registro.getPuntaje(),
            registro.getTiempoEnSegundos()
        );
        
        enviarMensaje(mensaje);
        
        controlador.notificarMensaje("Juego finalizado para " + nombreJugador + 
            " - Puntaje: " + registro.getPuntaje());
    }
    
    /**
     * Envía un mensaje al cliente.
     * 
     * @param mensaje Mensaje a enviar
     */
    private void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }
    
    /**
     * Cierra la conexión con el cliente.
     */
    private void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            controlador.notificarMensaje("Conexión cerrada con cliente #" + numeroCliente);
            
        } catch (IOException e) {
            controlador.notificarError("Error al cerrar conexión: " + e.getMessage());
        }
    }
}