package udistrital.avanzada.pacmangame.vista.servidor;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import udistrital.avanzada.pacmangame.controlador.servidor.ControladorServidor;

/**
 * Ventana principal del servidor.
 * Muestra el estado del servidor y el panel de juego.
 * Cumple con separación de eventos (listener y performed).
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class VentanaServidor extends JFrame {
    
    private ControladorServidor controlador;
    private JTextArea areaLog;
    private JButton btnIniciar;
    private JButton btnSalir;
    private JPanel panelCentral;
    private JScrollPane scrollLog;
    
    /**
     * Constructor de la ventana del servidor.
     * 
     * @param controlador Controlador del servidor
     */
    public VentanaServidor(ControladorServidor controlador) {
        this.controlador = controlador;
        controlador.setVista(this);
        
        inicializarComponentes();
        configurarEventos();
        configurarVentana();
    }
    
    /**
     * Inicializa los componentes de la ventana.
     */
    private void inicializarComponentes() {
        // Panel superior con botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnIniciar = new JButton("Iniciar Servidor");
        btnSalir = new JButton("Salir");
        btnSalir.setEnabled(false);
        
        panelSuperior.add(btnIniciar);
        panelSuperior.add(btnSalir);
        
        // Panel central para el juego
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.BLACK);
        panelCentral.setPreferredSize(new Dimension(800, 600));
        
        // Panel inferior con log
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Log del Servidor"));
        
        areaLog = new JTextArea(10, 60);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        scrollLog = new JScrollPane(areaLog);
        panelInferior.add(scrollLog, BorderLayout.CENTER);
        
        // Agregar a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {
        btnIniciar.addActionListener(new ManejadorIniciar());
        btnSalir.addActionListener(new ManejadorSalir());
    }
    
    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Servidor Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Muestra el panel de juego en el centro.
     * 
     * @param panelJuego Panel del juego a mostrar
     */
    public void mostrarPanelJuego(JPanel panelJuego) {
        panelCentral.removeAll();
        panelCentral.add(panelJuego, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
    
    /**
     * Limpia el panel central de juego.
     */
    public void limpiarPanelJuego() {
        panelCentral.removeAll();
        panelCentral.setBackground(Color.BLACK);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
    
    /**
     * Agrega un mensaje al log.
     * 
     * @param mensaje Mensaje a agregar
     */
    public void agregarMensaje(String mensaje) {
        areaLog.append("[INFO] " + mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
    
    /**
     * Agrega un error al log.
     * 
     * @param error Error a agregar
     */
    public void agregarError(String error) {
        areaLog.append("[ERROR] " + error + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
    
    /**
     * Muestra un mensaje emergente.
     * 
     * @param titulo Título del mensaje
     * @param mensaje Contenido del mensaje
     */
    public void mostrarMensajeEmergente(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Listener para el botón Iniciar.
     */
    private class ManejadorIniciar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Abrir selector de archivo para propiedades
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de propiedades");
            fileChooser.setCurrentDirectory(new File("data"));
            
            int resultado = fileChooser.showOpenDialog(VentanaServidor.this);
            
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                
                try {
                    controlador.inicializar(archivo.getAbsolutePath());
                    controlador.iniciarServidor();
                    
                    btnIniciar.setEnabled(false);
                    btnSalir.setEnabled(true);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        VentanaServidor.this,
                        "Error al iniciar servidor: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    
    /**
     * Listener para el botón Salir.
     */
    private class ManejadorSalir implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mostrar mejor jugador
            controlador.mostrarMejorJugador();
            
            // Detener servidor
            controlador.detenerServidor();
            
            // Cerrar aplicación
            System.exit(0);
        }
    }
}