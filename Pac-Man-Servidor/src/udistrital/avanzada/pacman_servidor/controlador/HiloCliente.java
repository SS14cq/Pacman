package udistrital.avanzada.pacman_servidor.controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import udistrital.avanzada.pacman_servidor.conexion.ArchivoResultadosManager;
import udistrital.avanzada.pacman_servidor.dao.UsuarioDAO;
import udistrital.avanzada.pacman_servidor.modelo.Direccion;
import udistrital.avanzada.pacman_servidor.modelo.Juego;
import udistrital.avanzada.pacman_servidor.modelo.RegistroJuego;
import udistrital.avanzada.pacman_servidor.modelo.ResultadoMovimiento;
import udistrital.avanzada.pacman_servidor.util.ConfiguracionServidor;
import udistrital.avanzada.pacman_servidor.vista.VentanaJuego;
import udistrital.avanzada.pacman_servidor.vista.VentanaServidor;

/**
 * Thread que atiende a un cliente específico.
 * Maneja autenticación, juego y comunicación por socket.
 * Cada cliente tiene su propio thread de atención.
 * 
 * @author Jhon Herrera Cubides
 * @version 1.0
 */
public class HiloCliente implements Runnable {
    
    private Socket socket;
    private UsuarioDAO usuarioDAO;
    private ArchivoResultadosManager archivoResultados;
    private ConfiguracionServidor configuracion;
    private VentanaServidor vistaServidor;
    private int numeroCliente;
    
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombreJugador;
    private Juego juego;
    private VentanaJuego ventanaJuego;
    
    /**
     * Constructor del hilo de atención.
     * 
     * @param socket Socket del cliente
     * @param usuarioDAO DAO para validación
     * @param archivoResultados Manager de archivo
     * @param configuracion Configuración del servidor
     * @param vistaServidor Vista principal del servidor
     * @param numeroCliente Número identificador del cliente
     */
    public HiloCliente(Socket socket, UsuarioDAO usuarioDAO, 
                      ArchivoResultadosManager archivoResultados,
                      ConfiguracionServidor configuracion,
                      VentanaServidor vistaServidor,
                      int numeroCliente) {
        this.socket = socket;
        this.usuarioDAO = usuarioDAO;
        this.archivoResultados = archivoResultados;
        this.configuracion = configuracion;
        this.vistaServidor = vistaServidor;
        this.numeroCliente = numeroCliente;
    }
    
    @Override
    public void run() {
        try {
            // Inicializar streams
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            // Autenticar cliente
            if (!autenticarCliente()) {
                cerrarConexion();
                return;
            }
            
            // Iniciar juego
            iniciarJuego();
            
            // Loop de comandos del juego
            procesarComandosJuego();
            
            // Finalizar juego
            finalizarJuego();
            
        } catch (IOException e) {
            vistaServidor.mostrarError("Error en cliente #" + numeroCliente + ": " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Autentica al cliente solicitando usuario y contraseña.
     * 
     * @return true si autenticación exitosa, false en caso contrario
     * @throws IOException Si hay error de comunicación
     */
    private boolean autenticarCliente() throws IOException {
        try {
            // Solicitar usuario
            salida.println("SOLICITAR_USUARIO");
            String usuario = entrada.readLine();
            
            // Solicitar contraseña
            salida.println("SOLICITAR_PASSWORD");
            String password = entrada.readLine();
            
            vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + 
                                       " - Intento de login: " + usuario);
            
            // Validar en BD
            if (usuarioDAO.validarCredenciales(usuario, password)) {
                nombreJugador = usuario;
                salida.println("AUTENTICACION_EXITOSA");
                vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + 
                                           " - Autenticación exitosa: " + usuario);
                return true;
            } else {
                salida.println("AUTENTICACION_FALLIDA");
                vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + 
                                           " - Autenticación fallida: " + usuario);
                return false;
            }
            
        } catch (SQLException e) {
            vistaServidor.mostrarError("Error de BD en autenticación: " + e.getMessage());
            salida.println("ERROR_SERVIDOR");
            return false;
        }
    }
    
    /**
     * Inicializa el juego: crea modelo y vista gráfica.
     */
    private void iniciarJuego() {
        int ancho = configuracion.getAnchoVentana();
        int alto = configuracion.getAltoVentana();
        
        // Crear modelo del juego
        juego = new Juego(ancho, alto);
        juego.inicializar(nombreJugador);
        
        // Crear ventana gráfica en EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            ventanaJuego = new VentanaJuego(juego, nombreJugador);
            ventanaJuego.setVisible(true);
        });
        
        vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + 
                                   " - Juego iniciado para: " + nombreJugador);
        
        // Notificar al cliente
        salida.println("JUEGO_INICIADO");
        salida.println("Bienvenido " + nombreJugador + "!");
        salida.println("Envía comandos: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
    }
    
    /**
     * Procesa comandos de movimiento del cliente hasta terminar el juego.
     * 
     * @throws IOException Si hay error de comunicación
     */
    private void procesarComandosJuego() throws IOException {
        while (!juego.isJuegoTerminado()) {
            // Solicitar movimiento
            salida.println("SOLICITAR_MOVIMIENTO");
            String comando = entrada.readLine();
            
            if (comando == null) {
                break; // Cliente desconectado
            }
            
            // Procesar comando
            procesarComando(comando.trim().toUpperCase());
        }
    }
    
    /**
     * Procesa un comando individual de movimiento.
     * 
     * @param comando Comando recibido (ARRIBA, ABAJO, IZQUIERDA, DERECHA)
     */
    private void procesarComando(String comando) {
        try {
            Direccion direccion = parsearDireccion(comando);
            
            if (direccion == null) {
                salida.println("ERROR: Comando inválido. Use: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
                return;
            }
            
            // Mover Pac-Man
            ResultadoMovimiento resultado = juego.moverPacMan(direccion);
            
            // Repintar ventana
            if (ventanaJuego != null) {
                ventanaJuego.repintar();
            }
            
            // Construir mensaje de respuesta
            StringBuilder respuesta = new StringBuilder();
            respuesta.append("Movimiento: ").append(comando).append("\n");
            respuesta.append("Casillas movidas: ").append(resultado.getCasillasMovidas()).append("/4\n");
            
            if (resultado.isLimiteAlcanzado()) {
                respuesta.append("¡Límite de ventana alcanzado!\n");
            }
            
            if (resultado.isFrutaComida()) {
                respuesta.append("¡Fruta comida! ").append(resultado.getNombreFruta())
                        .append(" (+").append(resultado.getPuntosGanados()).append(" puntos)\n");
            }
            
            respuesta.append("Puntaje total: ").append(juego.getPuntajeTotal()).append("\n");
            respuesta.append("Frutas restantes: ").append(juego.getFrutasRestantes()).append("\n");
            respuesta.append("Tiempo: ").append(juego.getTiempoTranscurrido()).append(" segundos");
            
            salida.println(respuesta.toString());
            
        } catch (Exception e) {
            salida.println("ERROR: " + e.getMessage());
            vistaServidor.mostrarError("Error procesando comando: " + e.getMessage());
        }
    }
    
    /**
     * Convierte string a dirección.
     * 
     * @param comando String del comando
     * @return Dirección o null si inválido
     */
    private Direccion parsearDireccion(String comando) {
        switch (comando) {
            case "ARRIBA": return Direccion.ARRIBA;
            case "ABAJO": return Direccion.ABAJO;
            case "IZQUIERDA": return Direccion.IZQUIERDA;
            case "DERECHA": return Direccion.DERECHA;
            default: return null;
        }
    }
    
    /**
     * Finaliza el juego: guarda resultado y lo envía al cliente.
     */
    private void finalizarJuego() {
        try {
            int puntaje = juego.getPuntajeTotal();
            long tiempo = juego.getTiempoTranscurrido();
            
            // Guardar en archivo
            RegistroJuego registro = new RegistroJuego(nombreJugador, puntaje, tiempo);
            archivoResultados.guardarResultado(registro);
            
            // Enviar resultado al cliente
            String mensajeFinal = String.format(
                "\n=== JUEGO TERMINADO ===\n" +
                "Jugador: %s\n" +
                "Puntaje: %d puntos\n" +
                "Tiempo: %d segundos\n" +
                "¡Gracias por jugar!",
                nombreJugador, puntaje, tiempo
            );
            
            salida.println("JUEGO_TERMINADO");
            salida.println(mensajeFinal);
            
            vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + 
                                       " - Juego finalizado. Puntaje: " + puntaje);
            
            // Cerrar ventana de juego después de 3 segundos
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(3000);
                    if (ventanaJuego != null) {
                        ventanaJuego.dispose();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
        } catch (IOException e) {
            vistaServidor.mostrarError("Error al guardar resultado: " + e.getMessage());
        }
    }
    
    /**
     * Cierra todos los recursos: streams y socket.
     */
    private void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            vistaServidor.mostrarMensaje("Cliente #" + numeroCliente + " desconectado");
            
        } catch (IOException e) {
            vistaServidor.mostrarError("Error al cerrar conexión: " + e.getMessage());
        }
    }
}