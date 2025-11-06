/*
 * Controlador principal del cliente que gestiona la conexión con el servidor
 * y la comunicación bidireccional mediante sockets TCP.
 * 
 * Esta clase implementa el lado cliente del juego Pac-Man, permitiendo:
 * - Cargar configuración desde archivo properties
 * - Establecer conexión con el servidor
 * - Enviar credenciales y comandos de movimiento
 * - Recibir y procesar respuestas del servidor
 * 
 * Aplica el patrón MVC siendo el Controlador que coordina entre
 * la Vista (VentanaCliente) y la comunicación de red.
 * 
 */
package udistrital.avanzada.cliente.control;

/**
 *
 * @author juanr
 */
import java.io.*;
import java.net.Socket;
import java.net.ConnectException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import udistrital.avanzada.cliente.vista.VentanaCliente;

public class Cliente {
    /** Socket de conexión con el servidor */
    private Socket socket;
    
    /** Stream para leer mensajes del servidor */
    private BufferedReader entrada;
    
    /** Stream para enviar mensajes al servidor */
    private PrintWriter salida;
    
    /** Referencia a la ventana gráfica del cliente */
    private VentanaCliente ventana;
    
    /** Dirección IP o hostname del servidor */
    private String host;
    
    /** Puerto del servidor al que conectarse */
    private int puerto;
    
    /** Bandera que indica si el cliente está conectado */
    private boolean conectado;
    
    /**
     * Constructor del cliente.
     * Inicializa la ventana gráfica y configura el estado inicial.
     */
    public Cliente() {
        ventana = new VentanaCliente(this);
        conectado = false;
    }
    
    /**
     * Abre un diálogo de selección de archivo para cargar las propiedades
     * de conexión al servidor. El archivo debe contener:
     * - servidor.ip: Dirección del servidor
     * - servidor.puerto: Puerto del servidor
     * 
     * Implementa el requisito de usar JFileChooser para seleccionar
     * el archivo de propiedades.
     * 
     * @return true si las propiedades se cargaron correctamente, false en caso contrario
     */
    public boolean cargarPropiedades() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de propiedades del servidor");
        
        // Filtro para mostrar solo archivos .properties
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
            "Archivos Properties (*.properties)", "properties"
        );
        fileChooser.setFileFilter(filtro);
        fileChooser.setCurrentDirectory(new File("config"));
        
        int resultado = fileChooser.showOpenDialog(ventana);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            ventana.agregarMensaje("[CONFIG] Archivo seleccionado: " + 
                                  archivoSeleccionado.getName());
            
            return cargarDesdeArchivo(archivoSeleccionado);
        } else {
            ventana.agregarMensaje("[CONFIG] ✗ Selección de archivo cancelada");
            return false;
        }
    }
    /**
 * Carga propiedades desde un archivo específico sin usar JFileChooser.
 * Útil para carga automática desde main().
 * 
 * @param rutaArchivo Ruta del archivo de propiedades
 * @return true si se cargó correctamente
 */
public boolean cargarPropiedadesDesdeRuta(String rutaArchivo) {
    return cargarDesdeArchivo(new File(rutaArchivo));
}
    
    /**
     * Carga las propiedades desde el archivo especificado.
     * 
     * @param archivo Archivo .properties a leer
     * @return true si la carga fue exitosa, false en caso contrario
     */
    public boolean cargarDesdeArchivo(File archivo) {
        Properties props = new Properties();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(archivo);
            props.load(fis);
            
            host = props.getProperty("servidor.ip");
            String puertoStr = props.getProperty("servidor.puerto");
            
            // Validar que existan las propiedades requeridas
            if (host == null || puertoStr == null) {
                ventana.agregarMensaje("[CONFIG] ✗ Faltan propiedades requeridas");
                return false;
            }
            
            puerto = Integer.parseInt(puertoStr);
            
            ventana.agregarMensaje("[CONFIG] ✓ Propiedades cargadas:");
            ventana.agregarMensaje("  - Servidor: " + host);
            ventana.agregarMensaje("  - Puerto: " + puerto);
            return true;
            
        } catch (IOException e) {
            ventana.agregarMensaje("[ERROR] Error leyendo archivo: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            ventana.agregarMensaje("[ERROR] Puerto inválido en archivo de propiedades");
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // Log silencioso
                }
            }
        }
    }
    
    /**
     * Establece la conexión con el servidor utilizando sockets TCP.
     * Crea los streams de entrada y salida, y lanza un hilo para
     * escuchar mensajes del servidor de forma asíncrona.
     * 
     * @throws RuntimeException Si ya existe una conexión activa
     */
    public void conectar() {
    if (conectado) {
        ventana.agregarMensaje("[CONEXIÓN] Ya existe una conexión activa");
        return;
    }
    
    // ✅ VALIDAR que las propiedades estén cargadas
    if (host == null || puerto == 0) {
        ventana.agregarMensaje("[ERROR] Debe cargar las propiedades primero");
        ventana.agregarMensaje("[INFO] Use el botón 'Cargar Propiedades'");
        return;
    }
    
    try {
        ventana.agregarMensaje("[CONEXIÓN] Intentando conectar a " + host + ":" + puerto);
        socket = new Socket(host, puerto);
        // ... resto del código
            
            // Inicializar streams de comunicación
            entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            conectado = true;
            ventana.agregarMensaje("[CONEXIÓN] ✓ Conectado exitosamente al servidor");
            ventana.agregarMensaje("=".repeat(50));
            
            // Iniciar hilo para escuchar mensajes del servidor
            Thread hiloEscucha = new Thread(this::escucharServidor);
            hiloEscucha.setDaemon(true); // Hilo daemon para no bloquear el cierre
            hiloEscucha.start();
            
        } catch (ConnectException e) {
            ventana.agregarMensaje("[ERROR] ✗ No se pudo conectar al servidor");
            ventana.agregarMensaje("  Verifique que el servidor esté ejecutándose");
        } catch (IOException e) {
            ventana.agregarMensaje("[ERROR] ✗ Error de conexión: " + e.getMessage());
        }
    }
    
    /**
     * Método que ejecuta el hilo de escucha de mensajes del servidor.
     * Lee continuamente mensajes del servidor y los procesa.
     * Se ejecuta en un hilo separado para no bloquear la interfaz gráfica.
     */
    private void escucharServidor() {
        try {
            String mensaje;
            while (conectado && (mensaje = entrada.readLine()) != null) {
                procesarMensaje(mensaje);
            }
        } catch (IOException e) {
            if (conectado) {
                ventana.agregarMensaje("[CONEXIÓN] ✗ Conexión con servidor perdida");
            }
        } finally {
            conectado = false;
            ventana.deshabilitarTodo();
        }
    }
    
    /**
     * Procesa los mensajes recibidos del servidor según el protocolo establecido.
     * Interpreta comandos y actualiza la interfaz según corresponda.
     * 
     * Protocolo de mensajes:
     * - SOLICITAR_CREDENCIALES: Habilitar campos de login
     * - CREDENCIALES_VALIDAS: Credenciales aceptadas
     * - CREDENCIALES_INVALIDAS: Credenciales rechazadas, cerrar conexión
     * - JUEGO_INICIADO: Juego comenzó en el servidor
     * - SOLICITAR_MOVIMIENTO: Habilitar controles de movimiento
     * - LIMITE_ALCANZADO: Pac-Man alcanzó un límite del tablero
     * - MOVIMIENTO_OK: Movimiento ejecutado correctamente
     * - FRUTA_COMIDA:tipo:puntos: Se comió una fruta
     * - JUEGO_TERMINADO:nombre:puntaje:tiempo: Juego finalizado
     * 
     * @param mensaje El mensaje recibido del servidor
     */
    private void procesarMensaje(String mensaje) {
        ventana.agregarMensaje("[SERVIDOR] " + mensaje);
        
        // Procesar según el tipo de mensaje
        if (mensaje.equals("SOLICITAR_CREDENCIALES")) {
            ventana.agregarMensaje("\n>>> Por favor ingrese sus credenciales <<<\n");
            ventana.habilitarCredenciales();
            
        } else if (mensaje.equals("CREDENCIALES_VALIDAS")) {
            ventana.agregarMensaje("[AUTENTICACIÓN] ✓ Acceso concedido");
            ventana.agregarMensaje("=".repeat(50));
            
        } else if (mensaje.equals("CREDENCIALES_INVALIDAS")) {
            ventana.agregarMensaje("[AUTENTICACIÓN] ✗ Credenciales incorrectas");
            ventana.agregarMensaje("[SISTEMA] Cerrando conexión...\n");
            cerrarConexion();
            
        } else if (mensaje.equals("JUEGO_INICIADO")) {
            ventana.agregarMensaje("\n" + "=".repeat(50));
            ventana.agregarMensaje("🎮 EL JUEGO HA COMENZADO 🎮");
            ventana.agregarMensaje("El servidor está mostrando el juego gráficamente");
            ventana.agregarMensaje("Use los controles para mover a Pac-Man");
            ventana.agregarMensaje("=".repeat(50) + "\n");
            
        } else if (mensaje.equals("SOLICITAR_MOVIMIENTO")) {
            ventana.habilitarMovimiento();
            ventana.agregarMensaje(">>> Ingrese su movimiento <<<");
            
        } else if (mensaje.equals("LIMITE_ALCANZADO")) {
            ventana.agregarMensaje("⚠ ¡LÍMITE DEL TABLERO ALCANZADO!");
            ventana.agregarMensaje("   Intente moverse en otra dirección");
            
        } else if (mensaje.equals("MOVIMIENTO_OK")) {
            ventana.agregarMensaje("✓ Movimiento ejecutado correctamente");
            
        } else if (mensaje.startsWith("FRUTA_COMIDA:")) {
            procesarFrutaComida(mensaje);
            
        } else if (mensaje.startsWith("JUEGO_TERMINADO:")) {
            procesarFinJuego(mensaje);
        }
    }
    
    /**
     * Procesa el mensaje de fruta comida y muestra la información.
     * 
     * @param mensaje Mensaje en formato "FRUTA_COMIDA:tipo:puntos"
     */
    private void procesarFrutaComida(String mensaje) {
        String[] partes = mensaje.split(":");
        if (partes.length == 3) {
            String tipo = partes[1];
            String puntos = partes[2];
            
            ventana.agregarMensaje("\n" + "★".repeat(50));
            ventana.agregarMensaje("🍓 ¡FRUTA COMIDA! 🍓");
            ventana.agregarMensaje("   Tipo: " + tipo);
            ventana.agregarMensaje("   Puntos: +" + puntos);
            ventana.agregarMensaje("★".repeat(50) + "\n");
        }
    }
    
    /**
     * Procesa el mensaje de fin de juego y muestra las estadísticas finales.
     * 
     * @param mensaje Mensaje en formato "JUEGO_TERMINADO:nombre:puntaje:tiempo"
     */
    private void procesarFinJuego(String mensaje) {
        String[] partes = mensaje.split(":");
        if (partes.length == 4) {
            String nombre = partes[1];
            String puntaje = partes[2];
            String tiempo = partes[3];
            
            ventana.agregarMensaje("\n\n" + "=".repeat(50));
            ventana.agregarMensaje("🏆 JUEGO TERMINADO 🏆");
            ventana.agregarMensaje("=".repeat(50));
            ventana.agregarMensaje("Jugador: " + nombre);
            ventana.agregarMensaje("Puntaje Final: " + puntaje + " puntos");
            ventana.agregarMensaje("Tiempo Total: " + tiempo + " segundos");
            ventana.agregarMensaje("=".repeat(50));
            ventana.agregarMensaje("\n¡Gracias por jugar!\n");
            
            // Cerrar conexión después de mostrar resultados
            cerrarConexion();
        }
    }
    
    /**
     * Envía las credenciales del usuario al servidor para autenticación.
     * 
     * @param usuario Nombre de usuario
     * @param contrasena Contraseña del usuario
     */
    public void enviarCredenciales(String usuario, String contrasena) {
        if (salida != null && conectado) {
            salida.println(usuario);
            salida.println(contrasena);
            ventana.agregarMensaje("[CLIENTE] Credenciales enviadas");
        } else {
            ventana.agregarMensaje("[ERROR] No hay conexión con el servidor");
        }
    }
    
    /**
     * Envía un comando de movimiento al servidor.
     * El movimiento debe ser: ARRIBA, ABAJO, IZQUIERDA o DERECHA.
     * 
     * @param direccion Dirección del movimiento
     */
    public void enviarMovimiento(String direccion) {
        if (salida != null && conectado) {
            String comando = "MOVER:" + direccion.toUpperCase();
            salida.println(comando);
            ventana.agregarMensaje("[CLIENTE] → " + direccion);
        } else {
            ventana.agregarMensaje("[ERROR] No hay conexión con el servidor");
        }
    }
    
    /**
     * Cierra de forma ordenada la conexión con el servidor.
     * Libera todos los recursos asociados (streams y socket).
     */
    private void cerrarConexion() {
        conectado = false;
        
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            ventana.agregarMensaje("[SISTEMA] Conexión cerrada correctamente");
            ventana.deshabilitarTodo();
            
        } catch (IOException e) {
            ventana.agregarMensaje("[ERROR] Error cerrando conexión: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el cliente está conectado al servidor.
     * 
     * @return true si hay conexión activa, false en caso contrario
     */
    public boolean estaConectado() {
        return conectado;
    }
    
    /**
     * Método principal para iniciar la aplicación cliente.
     * Punto de entrada del programa cliente.
     * 
     * @param args Argumentos de línea de comando (no utilizados)
     */
    /**
 * Método principal para iniciar la aplicación cliente.
 * Punto de entrada del programa cliente.
 * 
 * @param args Argumentos de línea de comando (no utilizados)
 */
public static void main(String[] args) {
    try {
        javax.swing.UIManager.setLookAndFeel(
            javax.swing.UIManager.getSystemLookAndFeelClassName()
        );
    } catch (Exception e) {
        System.err.println("No se pudo configurar Look and Feel");
    }
    
    javax.swing.SwingUtilities.invokeLater(() -> {
        Cliente cliente = new Cliente();
        
        // Intentar carga automática
        File archivoDefault = new File("config/cliente.properties");
        if (archivoDefault.exists()) {
            if (cliente.cargarDesdeArchivo(archivoDefault)) {
                cliente.conectar();
            }
        }
    });
}
    }
