package udistrital.avanzada.pacman_servidor.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import udistrital.avanzada.pacman_servidor.controlador.ControladorServidor;
import udistrital.avanzada.pacman_servidor.util.ConfiguracionServidor;

/**
 * Ventana principal del servidor Pac-Man.
 * Muestra log de eventos y botón de salida.
 * Aplica separación de eventos con clases listener independientes.
 * 
 * @author Steban
 * @version 1.0
 */
public class VentanaServidor extends JFrame {
    
    private JTextArea areaLog;
    private JButton btnSalir;
    private ControladorServidor controlador;
    
    /**
     * Constructor de la ventana.
     */
    public VentanaServidor() {
        inicializarComponentes();
        configurarVentana();
        iniciarServidor();
    }
    
    /**
     * Inicializa todos los componentes gráficos.
     */
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(45, 45, 48));
        
        // Área de log
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setBackground(new Color(30, 30, 30));
        areaLog.setForeground(new Color(0, 255, 0));
        areaLog.setCaretColor(Color.WHITE);
        
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(700, 400));
        scrollLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            "Log del Servidor",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(45, 45, 48));
        
        btnSalir = new JButton("Salir y Mostrar Mejor Jugador");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(280, 40));
        btnSalir.setEnabled(false);
        
        // Listener separado
        btnSalir.addActionListener(new ListenerBotonSalir(this));
        
        panelBotones.add(btnSalir);
        
        // Agregar componentes
        panelPrincipal.add(scrollLog, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Servidor Pac-Man - Universidad Distrital");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Listener de cierre separado
        addWindowListener(new ListenerCierreVentana(this));
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Inicia el proceso de carga de configuración y arranque del servidor.
     */
    private void iniciarServidor() {
        try {
            // Crear controlador
            controlador = new ControladorServidor(this);
            
            // Cargar configuración
            ConfiguracionServidor config = cargarConfiguracion();
            
            if (config != null) {
                // Inicializar servidor
                controlador.inicializarServidor(config);
            } else {
                // Usuario canceló
                mostrarError("Configuración no cargada. Cerrando aplicación.");
                System.exit(0);
            }
            
        } catch (Exception e) {
            mostrarError("Error fatal al iniciar servidor: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Carga la configuración desde archivo properties usando JFileChooser.
     * 
     * @return ConfiguracionServidor cargada o null si se cancela
     */
    private ConfiguracionServidor cargarConfiguracion() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de configuración (servidor.properties)");
        fileChooser.setCurrentDirectory(new File("data"));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivos Properties (*.properties)", "properties"
        );
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            try {
                ConfiguracionServidor config = new ConfiguracionServidor();
                config.cargarDesdeArchivo(archivo);
                
                mostrarMensaje("Configuración cargada: " + archivo.getAbsolutePath());
                return config;
                
            } catch (Exception e) {
                mostrarError("Error al cargar configuración: " + e.getMessage());
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Muestra un mensaje en el área de log.
     * Thread-safe mediante invokeLater.
     * 
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
    
    /**
     * Muestra un mensaje de error en el log.
     * 
     * @param mensaje Mensaje de error
     */
    public void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append("[ERROR] " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
    
    /**
     * Habilita o deshabilita el botón de salir.
     * 
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBotonSalir(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            btnSalir.setEnabled(habilitado);
        });
    }
    
    /**
     * Obtiene el controlador del servidor.
     * 
     * @return Controlador
     */
    public ControladorServidor getControlador() {
        return controlador;
    }
    
    /**
     * Solicita confirmación antes de cerrar la ventana.
     * 
     * @return true si se confirma el cierre, false en caso contrario
     */
    public boolean confirmarCierre() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de cerrar el servidor?\nEsto desconectará todos los clientes.",
            "Confirmar Cierre",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        return opcion == JOptionPane.YES_OPTION;
    }
}