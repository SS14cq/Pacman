package udistrital.avanzada.pacman_servidor.controlador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JOptionPane;
import udistrital.avanzada.pacman_servidor.conexion.ArchivoResultadosManager;
import udistrital.avanzada.pacman_servidor.conexion.ConexionBD;
import udistrital.avanzada.pacman_servidor.dao.UsuarioDAO;
import udistrital.avanzada.pacman_servidor.dao.UsuarioDAOImpl;
import udistrital.avanzada.pacman_servidor.modelo.RegistroJuego;
import udistrital.avanzada.pacman_servidor.util.ConfiguracionServidor;
import udistrital.avanzada.pacman_servidor.vista.VentanaServidor;

/**
 * Controlador principal del servidor.
 * Gestiona el ServerSocket, acepta clientes y coordina toda la lógica del servidor.
 * Aplica el patrón MVC: separa lógica de control de vista y modelo.
 * 
 * @author Steban
 * @version 1.0
 */
public class ControladorServidor {
    
    private VentanaServidor vista;
    private ServerSocket serverSocket;
    private ConfiguracionServidor configuracion;
    private ConexionBD conexionBD;
    private UsuarioDAO usuarioDAO;
    private ArchivoResultadosManager archivoResultados;
    private boolean servidorActivo;
    private int contadorClientes;
    
    /**
     * Constructor del controlador.
     * Aplica inyección de dependencias.
     * 
     * @param vista Ventana del servidor
     */
    public ControladorServidor(VentanaServidor vista) {
        this.vista = vista;
        this.servidorActivo = false;
        this.contadorClientes = 0;
    }
    
    /**
     * Inicializa el servidor con la configuración cargada.
     * Conecta a BD, carga usuarios y levanta ServerSocket.
     * 
     * @param configuracion Configuración cargada del archivo properties
     */
    public void inicializarServidor(ConfiguracionServidor configuracion) {
        this.configuracion = configuracion;
        
        try {
            // Inicializar conexión a BD
            inicializarBaseDatos();
            
            // Inicializar archivo de resultados
            String rutaArchivo = configuracion.getRutaArchivoResultados();
            this.archivoResultados = new ArchivoResultadosManager(rutaArchivo);
            vista.mostrarMensaje("Archivo de resultados inicializado: " + rutaArchivo);
            
            // Cargar usuarios precargados en BD
            cargarUsuariosPrecargados();
            
            // Levantar ServerSocket
            int puerto = configuracion.getPuerto();
            serverSocket = new ServerSocket(puerto);
            servidorActivo = true;
            
            vista.mostrarMensaje("=== SERVIDOR INICIADO ===");
            vista.mostrarMensaje("Puerto: " + puerto);
            vista.mostrarMensaje("Esperando conexiones de clientes...");
            vista.habilitarBotonSalir(true);
            
            // Iniciar thread para escuchar clientes
            iniciarEscuchaClientes();
            
        } catch (SQLException e) {
            vista.mostrarError("Error al conectar con la base de datos: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            vista.mostrarError("Driver de MySQL no encontrado: " + e.getMessage());
        } catch (IOException e) {
            vista.mostrarError("Error al iniciar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Inicializa la conexión a la base de datos.
     * 
     * @throws SQLException Si hay error de conexión
     * @throws ClassNotFoundException Si no encuentra el driver
     */
    private void inicializarBaseDatos() throws SQLException, ClassNotFoundException {
        String url = configuracion.getDBUrl();
        String usuario = configuracion.getDBUsuario();
        String password = configuracion.getDBPassword();
        String driver = configuracion.getDBDriver();
        
        conexionBD = ConexionBD.getInstancia(url, usuario, password, driver);
        usuarioDAO = new UsuarioDAOImpl(conexionBD);
        
        vista.mostrarMensaje("Conexión a base de datos establecida");
    }
    
    /**
     * Carga los usuarios precargados desde properties a la BD.
     * 
     * @throws SQLException Si hay error en la carga
     */
    private void cargarUsuariosPrecargados() throws SQLException {
        Map<String, String> usuarios = configuracion.getUsuariosPrecargados();
        usuarioDAO.cargarUsuariosDesdeProperties(usuarios);
        vista.mostrarMensaje("Usuarios precargados: " + usuarios.size());
    }
    
    /**
     * Inicia un thread que escucha conexiones de clientes continuamente.
     */
    private void iniciarEscuchaClientes() {
        Thread threadEscucha = new Thread(() -> {
            while (servidorActivo) {
                try {
                    // Esperar cliente (bloqueante)
                    Socket socketCliente = serverSocket.accept();
                    contadorClientes++;
                    
                    String infoCliente = socketCliente.getInetAddress().getHostAddress() + 
                                       ":" + socketCliente.getPort();
                    vista.mostrarMensaje("\n>>> Cliente #" + contadorClientes + 
                                       " conectado desde: " + infoCliente);
                    
                    // Crear y lanzar thread de atención al cliente
                    HiloCliente hiloCliente = new HiloCliente(
                        socketCliente, 
                        usuarioDAO, 
                        archivoResultados,
                        configuracion,
                        vista,
                        contadorClientes
                    );
                    
                    Thread thread = new Thread(hiloCliente);
                    thread.start();
                    
                } catch (IOException e) {
                    if (servidorActivo) {
                        vista.mostrarError("Error al aceptar cliente: " + e.getMessage());
                    }
                }
            }
        });
        
        threadEscucha.setDaemon(true);
        threadEscucha.start();
    }
    
    /**
     * Detiene el servidor y muestra el mejor jugador.
     * Método llamado al presionar el botón "Salir".
     */
    public void detenerServidor() {
        try {
            servidorActivo = false;
            
            // Cerrar ServerSocket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                vista.mostrarMensaje("\n=== SERVIDOR DETENIDO ===");
            }
            
            // Mostrar mejor jugador
            mostrarMejorJugador();
            
            // Cerrar conexión BD
            if (conexionBD != null) {
                conexionBD.cerrarConexion();
                vista.mostrarMensaje("Conexión a BD cerrada");
            }
            
            // Cerrar ventana
            vista.dispose();
            System.exit(0);
            
        } catch (IOException e) {
            vista.mostrarError("Error al detener servidor: " + e.getMessage());
        }
    }
    
    /**
     * Busca y muestra el mejor jugador del archivo de resultados.
     * Muestra en una ventana emergente.
     */
    private void mostrarMejorJugador() {
        try {
            if (archivoResultados.estaVacio()) {
                JOptionPane.showMessageDialog(
                    vista,
                    "No hay registros de jugadores",
                    "Mejor Jugador",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            
            RegistroJuego mejor = archivoResultados.obtenerMejorJugador();
            
            String mensaje = String.format(
                "🏆 MEJOR JUGADOR 🏆\n\n" +
                "Nombre: %s\n" +
                "Puntaje: %d puntos\n" +
                "Tiempo: %d segundos\n\n" +
                "¡Felicidades!",
                mejor.getNombreJugador(),
                mejor.getPuntaje(),
                mejor.getTiempoSegundos()
            );
            
            JOptionPane.showMessageDialog(
                vista,
                mensaje,
                "Mejor Jugador",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            vista.mostrarMensaje("\n" + mensaje.replace("🏆 ", "").replace("🏆", ""));
            
        } catch (IOException e) {
            vista.mostrarError("Error al leer archivo de resultados: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el servidor está activo.
     * 
     * @return true si está activo, false en caso contrario
     */
    public boolean isServidorActivo() {
        return servidorActivo;
    }
}