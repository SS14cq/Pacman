/*
 * Clase principal del servidor que gestiona las conexiones de los clientes.
 * Implementa un servidor TCP usando ServerSocket que escucha en un puerto
 * específico y crea un hilo nuevo para cada cliente que se conecta.
 * 
 * Esta clase aplica:
 * - SRP: Solo se encarga de escuchar y aceptar conexiones
 * - OCP: Extensible mediante la clase HiloCliente
 * - Alta cohesión: Todas sus funciones están relacionadas con el servidor
 * 
 */
package udistrital.avanzada.servidor.control;

/**
 *
 * @author juanr
 */
import udistrital.avanzada.servidor.control.HiloCliente;
import java.io.*;
import java.net.*;
import java.util.Properties;
import udistrital.avanzada.servidor.modelo.conexion.ConexionDB;
import udistrital.avanzada.servidor.vista.VentanaServidor;


public class Servidor {
    /** Socket del servidor que escucha conexiones entrantes */
    private ServerSocket serverSocket;
    
    /** Puerto en el que el servidor escucha */
    private int puerto;
    
    /** Bandera que indica si el servidor está en ejecución */
    private boolean corriendo;
    
    /** Referencia a la ventana gráfica del servidor */
    private VentanaServidor ventana;
    
    /**
     * Constructor que inicializa el servidor.
     * Carga las propiedades desde archivo, inicializa la conexión a BD
     * y crea la interfaz gráfica del servidor.
     * 
     * @param rutaProperties Ruta al archivo .properties con la configuración
     * @throws RuntimeException Si no se puede cargar la configuración
     */
    /**
 * Constructor que inicializa el servidor.
 * Carga las propiedades desde archivo, inicializa la conexión a BD
 * y crea la interfaz gráfica del servidor.
 * 
 * @param rutaProperties Ruta al archivo .properties con la configuración
 * @throws RuntimeException Si no se puede cargar la configuración
 */
public Servidor(String rutaProperties) {
    // 1. Cargar propiedades del servidor primero
    cargarPropiedades(rutaProperties);
    
    // 2. Cargar propiedades de base de datos
    ConexionDB.cargarPropiedades(rutaProperties);
    
    // 3. Crear ventana con configuración ya cargada
    this.ventana = new VentanaServidor(this);
    
    // 4. Mostrar puerto cargado en la ventana
    ventana.agregarMensaje("[CONFIG] Puerto configurado: " + puerto);
    ventana.agregarMensaje("[CONFIG] Base de datos configurada");
    
    // 5. Marcar como corriendo
    this.corriendo = true;
}
    
    /**
     * Carga las propiedades de configuración del servidor desde archivo.
     * Extrae el puerto en el que el servidor debe escuchar.
     * Si hay error, usa puerto 5000 por defecto.
     * 
     * Propiedad esperada:
     * - servidor.puerto: Puerto de escucha del servidor
     * 
     * @param rutaProperties Ruta completa al archivo de propiedades
     */
    private void cargarPropiedades(String rutaProperties) {
        Properties props = new Properties();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(rutaProperties);
            props.load(fis);
            this.puerto = Integer.parseInt(props.getProperty("servidor.puerto"));
            
        } catch (IOException | NumberFormatException e) {
            // Si hay error, usar puerto por defecto
            this.puerto = 5000;
            ventana.agregarMensaje("Advertencia: Usando puerto por defecto 5000");
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
     * Inicia el servidor y comienza a escuchar conexiones entrantes.
     * Por cada conexión aceptada, crea un nuevo hilo (HiloCliente)
     * para atender al cliente de forma concurrente.
     * 
     * Este método se ejecuta en un bucle infinito hasta que se detenga
     * el servidor o ocurra un error fatal.
     * 
     * @throws RuntimeException Si hay error crítico al iniciar el servidor
     */
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(puerto);
            ventana.agregarMensaje("=================================");
            ventana.agregarMensaje("Servidor Pac-Man iniciado");
            ventana.agregarMensaje("Puerto: " + puerto);
            ventana.agregarMensaje("Esperando conexiones...");
            ventana.agregarMensaje("=================================");
            
            while (corriendo) {
                // Bloquea hasta que un cliente se conecte
                Socket socketCliente = serverSocket.accept();
                
                String ipCliente = socketCliente.getInetAddress().getHostAddress();
                ventana.agregarMensaje("\n[CONEXIÓN] Cliente desde: " + ipCliente);
                
                // Crear y lanzar hilo para atender al cliente
                HiloCliente hiloCliente = new HiloCliente(socketCliente, ventana);
                hiloCliente.start();
            }
            
        } catch (IOException e) {
            if (corriendo) {
                ventana.agregarMensaje("[ERROR] Error en servidor: " + e.getMessage());
                throw new RuntimeException("Error crítico en el servidor", e);
            }
        }
    }
    
    /**
     * Detiene el servidor de forma ordenada.
     * Cierra el ServerSocket y actualiza la bandera de ejecución.
     * Los hilos de clientes activos terminarán sus operaciones actuales.
     */
    public void detener() {
        corriendo = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                ventana.agregarMensaje("\n[SERVIDOR] Servidor detenido correctamente");
            }
        } catch (IOException e) {
            ventana.agregarMensaje("[ERROR] Error cerrando servidor: " + e.getMessage());
        }
    }
    
    /**
     * Método principal para iniciar el servidor.
     * Punto de entrada de la aplicación servidora.
     * 
     * @param args Argumentos de línea de comando (no utilizados)
     */
    public static void main(String[] args) {
        // Ruta del archivo de propiedades
        String rutaProperties = "config/servidor.properties";
        Servidor servidor = new Servidor(rutaProperties);
        servidor.iniciar();
    }
}