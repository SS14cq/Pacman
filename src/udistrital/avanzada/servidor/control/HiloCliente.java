/*
 * Hilo que atiende a un cliente específico conectado al servidor.
 * Cada instancia de esta clase maneja la comunicación con un único cliente,
 * validando sus credenciales, gestionando el juego y enviando/recibiendo
 * comandos a través del socket.
 * 
 * Esta clase implementa el patrón Thread por cliente, permitiendo
 * concurrencia en el servidor. Cumple con SRP al enfocarse únicamente
 * en la comunicación y lógica de un cliente específico.
 * 
 */
package udistrital.avanzada.servidor.control;

/**
 *
 * @author juanr
 */
import java.io.*;
import java.net.Socket;

import udistrital.avanzada.servidor.modelo.Fruta;
import udistrital.avanzada.servidor.modelo.Juego;
import udistrital.avanzada.servidor.modelo.conexion.ArchivoResultados;
import udistrital.avanzada.servidor.modelo.dao.JugadorDAO;
import udistrital.avanzada.servidor.modelo.dao.JugadorDAOImpl;
import udistrital.avanzada.servidor.vista.VentanaJuego;
import udistrital.avanzada.servidor.vista.VentanaServidor;

public class HiloCliente extends Thread {
    /** Socket de comunicación con el cliente */
    private Socket socket;
    
    /** Stream para leer datos del cliente */
    private BufferedReader entrada;
    
    /** Stream para enviar datos al cliente */
    private PrintWriter salida;
    
    /** Referencia a la ventana del servidor para logging */
    private VentanaServidor ventanaServidor;
    
    /** Ventana del juego gráfico */
    private VentanaJuego ventanaJuego;
    
    /** Instancia del juego actual */
    private Juego juego;
    
    /** Nombre del jugador conectado */
    private String nombreJugador;
    
    /**
     * Constructor del hilo de atención al cliente.
     * 
     * @param socket Socket conectado al cliente
     * @param ventanaServidor Ventana del servidor para mensajes
     */
    public HiloCliente(Socket socket, VentanaServidor ventanaServidor) {
        this.socket = socket;
        this.ventanaServidor = ventanaServidor;
    }
    
    /**
     * Método principal del hilo que ejecuta el flujo completo
     * de atención al cliente:
     * 1. Inicializar streams de comunicación
     * 2. Validar credenciales
     * 3. Iniciar juego si las credenciales son válidas
     * 4. Procesar comandos de movimiento
     * 5. Finalizar juego y guardar resultados
     * 6. Cerrar conexión
     */
   
public void run() {
    try {
        // Inicializar streams de E/S
        entrada = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        salida = new PrintWriter(socket.getOutputStream(), true);
        
        // ✅ CORREGIDO: Usar IP del cliente en lugar de nombreJugador
        String clienteIP = socket.getInetAddress().getHostAddress();
        ventanaServidor.agregarMensaje(
            "[CONEXIÓN] Cliente desde " + clienteIP + " conectado - Streams iniciados"
        );
        
        // Validar credenciales del cliente
        if (!validarCredenciales()) {
            ventanaServidor.agregarMensaje(
                "[ACCESO DENEGADO] Credenciales inválidas desde " + clienteIP
            );
            cerrarConexion();
            return;
        }
        
        // ✅ Ahora nombreJugador SÍ tiene valor
        ventanaServidor.agregarMensaje(
            "[AUTENTICACIÓN] Usuario '" + nombreJugador + "' autenticado correctamente"
        );
        
        // Iniciar el juego gráfico
        iniciarJuego();
        
        // Loop principal del juego
        procesarJuego();
        
        // Finalizar y guardar resultados
        finalizarJuego();
        
    } catch (IOException e) {
        ventanaServidor.agregarMensaje(
            "[ERROR] Error con cliente " + 
            (nombreJugador != null ? nombreJugador : "desconocido") + 
            ": " + e.getMessage()
        );
    } finally {
        cerrarConexion();
    }
}
    
    /**
     * Solicita y valida las credenciales del jugador contra la base de datos.
     * Utiliza el patrón DAO para acceder a los datos de jugadores.
     * 
     * Protocolo de comunicación:
     * 1. Servidor envía: "SOLICITAR_CREDENCIALES"
     * 2. Cliente envía: usuario (línea 1) y contraseña (línea 2)
     * 3. Servidor valida en BD usando JugadorDAO
     * 4. Servidor responde: "CREDENCIALES_VALIDAS" o "CREDENCIALES_INVALIDAS"
     * 
     * @return true si las credenciales son válidas, false en caso contrario
     * @throws IOException Si hay error en la comunicación con el cliente
     */
    private boolean validarCredenciales() throws IOException {
    salida.println("SOLICITAR_CREDENCIALES");
    ventanaServidor.agregarMensaje("[AUTENTICACIÓN] Solicitando credenciales...");
    
    String usuario = entrada.readLine();
    String contrasena = entrada.readLine();
    
    if (usuario == null || contrasena == null) {
        salida.println("CREDENCIALES_INVALIDAS");
        ventanaServidor.agregarMensaje("[AUTENTICACIÓN] ✗ Credenciales vacías o conexión cerrada");
        return false;
    }
    
    // ✅ MEJORADO: Log del intento de autenticación
    ventanaServidor.agregarMensaje("[AUTENTICACIÓN] Validando usuario: '" + usuario + "'");
    
    // Usar DAO para validar en base de datos
    JugadorDAO dao = new JugadorDAOImpl();
    boolean valido = dao.validarJugador(usuario, contrasena);
    
    if (valido) {
        nombreJugador = usuario;
        salida.println("CREDENCIALES_VALIDAS");
        ventanaServidor.agregarMensaje("[AUTENTICACIÓN] ✓ Usuario validado: " + usuario);
        return true;
    } else {
        salida.println("CREDENCIALES_INVALIDAS");
        ventanaServidor.agregarMensaje("[AUTENTICACIÓN] ✗ Credenciales incorrectas para: " + usuario);
        return false;
    }
}
    
    /**
     * Inicializa la interfaz gráfica del juego en el servidor.
     * Crea una nueva instancia del juego con dimensiones 600x600
     * y muestra la ventana donde se visualizará el juego.
     * 
     * El cliente es notificado del inicio del juego pero no puede
     * ver la interfaz gráfica (solo el servidor la visualiza).
     */
    private void iniciarJuego() {
        juego = new Juego(600, 600);
        ventanaJuego = new VentanaJuego(juego, nombreJugador);
        ventanaJuego.setVisible(true);
        
        salida.println("JUEGO_INICIADO");
        ventanaServidor.agregarMensaje("[JUEGO] Iniciado para " + nombreJugador);
        ventanaServidor.agregarMensaje("[JUEGO] Frutas generadas: 4");
    }
    
    /**
     * Procesa el bucle principal del juego.
     * Escucha comandos de movimiento del cliente, los ejecuta,
     * verifica colisiones con frutas y actualiza la interfaz gráfica.
     * 
     * Protocolo de movimiento:
     * 1. Servidor solicita: "SOLICITAR_MOVIMIENTO"
     * 2. Cliente envía: "MOVER:DIRECCION" (ARRIBA/ABAJO/IZQUIERDA/DERECHA)
     * 3. Servidor procesa movimiento de 4 casillas
     * 4. Servidor responde estado: "MOVIMIENTO_OK" o "LIMITE_ALCANZADO"
     * 5. Si hay colisión: "FRUTA_COMIDA:tipo:puntos"
     * 6. Repetir hasta que se coman 4 frutas
     * 
     * @throws IOException Si hay error en la comunicación
     */
    private void procesarJuego() throws IOException {
        salida.println("SOLICITAR_MOVIMIENTO");
        int movimientos = 0;
        
        while (!juego.juegoTerminado()) {
            String comando = entrada.readLine();
            
            if (comando == null) {
                ventanaServidor.agregarMensaje("[CONEXIÓN] Cliente desconectado abruptamente");
                break;
            }
            
            // Parsear comando de movimiento
            String[] partes = comando.split(":");
            if (partes.length == 2 && partes[0].equals("MOVER")) {
                String direccion = partes[1];
                movimientos++;
                
                ventanaServidor.agregarMensaje(
                    String.format("[%s] Movimiento #%d: %s", nombreJugador, movimientos, direccion)
                );
                
                // Ejecutar movimiento (4 casillas)
                int casillasMovidas = juego.getPacman().mover(
                    direccion, 4, 
                    juego.getAnchoPanel(), 
                    juego.getAltoPanel()
                );
                
                // Informar al cliente del resultado
                if (casillasMovidas < 4) {
                    salida.println("LIMITE_ALCANZADO");
                    ventanaServidor.agregarMensaje(
                        "[" + nombreJugador + "] ⚠ Límite alcanzado (movió " + 
                        casillasMovidas + " casillas)"
                    );
                } else {
                    salida.println("MOVIMIENTO_OK");
                }
                
                // Verificar colisión con frutas
                Fruta frutaComida = juego.verificarColision();
                if (frutaComida != null) {
                    salida.println("FRUTA_COMIDA:" + frutaComida.getTipo() + 
                                  ":" + frutaComida.getPuntos());
                    ventanaServidor.agregarMensaje(
                        String.format("[%s] 🍓 Comió: %s (+%d puntos) [Total: %d]",
                            nombreJugador, frutaComida.getTipo(), 
                            frutaComida.getPuntos(), juego.getPacman().getPuntajeTotal())
                    );
                }
                
                // Actualizar vista gráfica
                ventanaJuego.repaint();
                
                // Solicitar siguiente movimiento si el juego continúa
                if (!juego.juegoTerminado()) {
                    salida.println("SOLICITAR_MOVIMIENTO");
                }
            }
        }
    }
    
    /**
     * Finaliza el juego, calcula estadísticas y guarda resultados.
     * Registra el resultado en el archivo de acceso aleatorio,
     * envía la información al cliente y cierra la ventana del juego.
     * 
     * La información guardada incluye:
     * - Nombre del jugador
     * - Puntaje total obtenido
     * - Tiempo total de juego en segundos
     */
    private void finalizarJuego() {
        juego.finalizarJuego();
        
        int puntaje = juego.getPacman().getPuntajeTotal();
        long tiempo = juego.getTiempoJuego();
        
        // Guardar en archivo de acceso aleatorio
        ArchivoResultados.guardarResultado(nombreJugador, puntaje, tiempo);
        
        // Enviar resultado final al cliente
        salida.println("JUEGO_TERMINADO:" + nombreJugador + ":" + 
                      puntaje + ":" + tiempo);
        
        ventanaServidor.agregarMensaje("\n" + "=".repeat(50));
        ventanaServidor.agregarMensaje("[FINALIZADO] Jugador: " + nombreJugador);
        ventanaServidor.agregarMensaje("[FINALIZADO] Puntaje: " + puntaje);
        ventanaServidor.agregarMensaje("[FINALIZADO] Tiempo: " + tiempo + " segundos");
        ventanaServidor.agregarMensaje("=".repeat(50) + "\n");
        
        // Cerrar ventana de juego
        if (ventanaJuego != null) {
            ventanaJuego.dispose();
        }
    }
    
    /**
     * Cierra de forma ordenada todos los recursos asociados al cliente.
     * Incluye streams de E/S y el socket. No lanza excepciones.
     */
    private void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            ventanaServidor.agregarMensaje(
                "[DESCONEXIÓN] Cliente " + 
                (nombreJugador != null ? nombreJugador : "anónimo") + 
                " desconectado\n"
            );
        } catch (IOException e) {
            ventanaServidor.agregarMensaje("[ERROR] Error cerrando conexión: " + e.getMessage());
        }
    }
}