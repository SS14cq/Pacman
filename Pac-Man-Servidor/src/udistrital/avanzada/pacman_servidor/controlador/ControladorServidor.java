package udistrital.avanzada.pacman_servidor.controlador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import udistrital.avanzada.pacman_servidor.conexion.*;
import udistrital.avanzada.pacman_servidor.dao.*;
import udistrital.avanzada.pacman_servidor.modelo.RegistroJuego;
import udistrital.avanzada.pacman_servidor.util.*;
import udistrital.avanzada.pacman_servidor.vista.PanelJuego;
import udistrital.avanzada.pacman_servidor.vista.VentanaJuego;

/**
 * Controlador principal del servidor.
 * Orquesta TODA la aplicación: vista, modelo, BD, sockets.
 * Es la VERTEBRA CENTRAL del sistema.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class ControladorServidor {
    
    // Controladores especializados
    private ControladorVista controladorVista;
    private ControladorModelo controladorModelo;
    
    // Configuración
    private ConfiguracionServidor config;
    
    // Conexiones
    private ConexionBD conexionBD;
    private UsuarioDAO usuarioDAO;
    private ArchivoResultadosManager archivoManager;
    
    // Servidor
    private ServerSocket serverSocket;
    private boolean servidorActivo;
    
    /**
     * Constructor que inicializa los controladores especializados.
     */
    public ControladorServidor() {
        this.controladorVista = new ControladorVista();
        this.controladorModelo = new ControladorModelo();
        this.servidorActivo = false;
    }
    
    /**
     * Inicia la aplicación del servidor.
     */
    public void iniciar() {
        // Crear y mostrar ventana de inicio
        controladorVista.crearVentanaInicio();
        configurarListenersVentanaInicio();
        controladorVista.mostrarVentanaInicio();
        
        controladorVista.agregarLog("=== Servidor Pac-Man Iniciado ===");
        controladorVista.agregarLog("Esperando configuración...");
    }
    
    /**
     * Configura los listeners de la ventana de inicio.
     */
    private void configurarListenersVentanaInicio() {
        // Listener para seleccionar properties
        controladorVista.getVentanaInicio().getBtnSeleccionarProperties()
            .addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionarArchivoProperties();
                }
            });
        
        // Listener para iniciar servidor
        controladorVista.getVentanaInicio().getBtnIniciarServidor()
            .addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iniciarServidor();
                }
            });
    }
    
    /**
     * Permite seleccionar el archivo properties con JFileChooser.
     */
    private void seleccionarArchivoProperties() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de configuración");
        
        int resultado = chooser.showOpenDialog(controladorVista.getVentanaInicio());
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            cargarConfiguracion(archivo);
        }
    }
    
    /**
     * Carga la configuración desde el archivo.
     * 
     * @param archivo Archivo properties
     */
    private void cargarConfiguracion(File archivo) {
        try {
            config = new ConfiguracionServidor();
            config.cargarDesdeArchivo(archivo);
            
            controladorVista.agregarLog("Configuración cargada: " + archivo.getName());
            controladorVista.agregarLog("Puerto: " + config.getPuerto());
            controladorVista.agregarLog("Base de datos: " + config.getDBUrl());
            
            // Inicializar conexión a BD
            inicializarBaseDeDatos();
            
            // Inicializar archivo de resultados
            archivoManager = new ArchivoResultadosManager(config.getRutaArchivoResultados());
            controladorVista.agregarLog("Archivo de resultados: " + config.getRutaArchivoResultados());
            
            // Habilitar botón de iniciar
            controladorVista.habilitarBotonIniciar(true);
            controladorVista.actualizarEstado("Configuración lista");
            
        } catch (IOException e) {
            controladorVista.agregarLog("ERROR: No se pudo cargar la configuración");
            JOptionPane.showMessageDialog(controladorVista.getVentanaInicio(),
                "Error al cargar configuración: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Inicializa la conexión a la base de datos y carga usuarios.
     */
    private void inicializarBaseDeDatos() {
        try {
            // Establecer conexión
            conexionBD = ConexionBD.getInstancia(
                config.getDBUrl(),
                config.getDBUsuario(),
                config.getDBPassword(),
                config.getDBDriver()
            );
            
            controladorVista.agregarLog("Conexión a BD establecida");
            
            // Inicializar DAO
            usuarioDAO = new UsuarioDAOImpl(conexionBD);
            
            // Cargar usuarios desde properties
            Map<String, String> usuarios = config.getUsuariosPrecargados();
            usuarioDAO.cargarUsuariosDesdeProperties(usuarios);
            
            controladorVista.agregarLog("Usuarios cargados: " + usuarios.size());
            
        } catch (SQLException | ClassNotFoundException e) {
            controladorVista.agregarLog("ERROR: No se pudo conectar a la BD");
            JOptionPane.showMessageDialog(controladorVista.getVentanaInicio(),
                "Error de base de datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Inicia el servidor de sockets.
     */
    private void iniciarServidor() {
        try {
            int puerto = config.getPuerto();
            serverSocket = new ServerSocket(puerto);
            servidorActivo = true;
            
            controladorVista.agregarLog("Servidor escuchando en puerto " + puerto);
            controladorVista.actualizarEstado("Escuchando conexiones");
            
            // Hilo para escuchar conexiones
            Thread hiloEscucha = new Thread(new Runnable() {
                @Override
                public void run() {
                    escucharConexiones();
                }
            });
            hiloEscucha.start();
            
        } catch (IOException e) {
            controladorVista.agregarLog("ERROR: No se pudo iniciar el servidor");
            JOptionPane.showMessageDialog(controladorVista.getVentanaInicio(),
                "Error al iniciar servidor: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Escucha conexiones de clientes en un bucle.
     */
    private void escucharConexiones() {
        while (servidorActivo) {
            try {
                controladorVista.agregarLog("Esperando cliente...");
                Socket socketCliente = serverSocket.accept();
                
                String ipCliente = socketCliente.getInetAddress().getHostAddress();
                controladorVista.agregarLog("Cliente conectado desde: " + ipCliente);
                
                // Lanzar hilo para atender al cliente
                HiloClienteServidor hiloCliente = new HiloClienteServidor(
                    socketCliente,
                    this,
                    usuarioDAO,
                    controladorModelo,
                    controladorVista,
                    archivoManager,
                    config
                );
                hiloCliente.start();
                
            } catch (IOException e) {
                if (servidorActivo) {
                    controladorVista.agregarLog("ERROR: Fallo al aceptar cliente");
                }
            }
        }
    }
    
    /**
     * Configura el listener del botón Salir en la ventana de juego.
     * 
     * @param ventanaJuego Ventana de juego
     */
    public void configurarBotonSalirJuego(VentanaJuego ventanaJuego) {
        ventanaJuego.getBtnSalir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRankingYCerrar();
            }
        });
    }
    
    /**
     * Configura la acción de dibujar en el panel de juego.
     * Inyecta la lógica desde el ControladorModelo.
     * 
     * @param panelJuego Panel donde se dibuja
     */
    public void configurarDibujadoJuego(PanelJuego panelJuego) {
        panelJuego.setAccionDibujar(new Runnable() {
            @Override
            public void run() {
                controladorModelo.dibujarJuego(panelJuego.getGraphics());
            }
        });
    }
    
    /**
     * Muestra el ranking final y cierra el servidor.
     */
    private void mostrarRankingYCerrar() {
        try {
            RegistroJuego mejor = archivoManager.obtenerMejorJugador();
            
            String mensaje;
            if (mejor != null) {
                mensaje = "=== MEJOR JUGADOR ===\n\n" +
                         "Nombre: " + mejor.getNombreJugador() + "\n" +
                         "Puntaje: " + mejor.getPuntaje() + "\n" +
                         "Tiempo: " + mejor.getTiempoSegundos() + " segundos";
            } else {
                mensaje = "No hay registros de juegos.";
            }
            
            JOptionPane.showMessageDialog(null, mensaje,
                "Ranking Final", JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar servidor
            servidorActivo = false;
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (conexionBD != null) {
                conexionBD.cerrarConexion();
            }
            
            System.exit(0);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error al leer ranking: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Agrega un mensaje al log desde otros componentes.
     * 
     * @param mensaje Mensaje a agregar
     */
    public void agregarLog(String mensaje) {
        controladorVista.agregarLog(mensaje);
    }
}