/*
 * Ventana principal del servidor que muestra el log de eventos
 * y proporciona control para detener el servidor.
 * 
 * Esta clase es parte de la capa Vista en el patrón MVC.
 * No contiene lógica de negocio, solo presentación e interacción básica.
 * 
 * Componentes:
 * - Área de texto para mostrar logs del servidor
 * - Botón "Salir" que muestra el mejor jugador y cierra el servidor
 * 
 */
package udistrital.avanzada.servidor.vista;

/**
 *
 * @author juanr
 */
import javax.swing.*;
import java.awt.*;
import udistrital.avanzada.servidor.control.Servidor;
import udistrital.avanzada.servidor.modelo.conexion.ArchivoResultados;


public class VentanaServidor extends JFrame {
    /** Área de texto para mostrar mensajes y logs del servidor */
    private JTextArea areaLog;
    
    /** Botón para detener el servidor y mostrar resultados */
    private JButton btnSalir;
    
    /** Referencia al servidor para control */
    private Servidor servidor;
    
    /**
     * Constructor que inicializa la ventana del servidor.
     * Configura la interfaz gráfica y los manejadores de eventos.
     * 
     * @param servidor Instancia del servidor a controlar
     */
    public VentanaServidor(Servidor servidor) {
        this.servidor = servidor;
        inicializarComponentes();
        configurarEventos();
    }
    
    /**
     * Inicializa y configura todos los componentes de la interfaz.
     * Establece el layout, tamaño, posición y estilos visuales.
     */
    private void inicializarComponentes() {
        setTitle("Servidor Pac-Man - Panel de Control");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("🎮 SERVIDOR PAC-MAN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);
        
        // Área de log con scroll
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setBackground(new Color(30, 30, 30));
        areaLog.setForeground(new Color(0, 255, 0));
        areaLog.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(areaLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(44, 62, 80), 2),
            "Log del Servidor",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(44, 62, 80)
        ));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con botón
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(236, 240, 241));
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSalir = new JButton("🏆 Salir y Ver Mejor Jugador");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(250, 40));
        panelInferior.add(btnSalir);
        
        add(panelInferior, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Configura los manejadores de eventos para los componentes.
     * Separa la lógica de eventos según las buenas prácticas de MVC.
     */
    private void configurarEventos() {
        btnSalir.addActionListener(e -> manejarSalida());
    }
    
    /**
     * Maneja el evento de salida del servidor.
     * Muestra el mejor jugador en un diálogo y cierra la aplicación.
     */
    private void manejarSalida() {
        String mejorJugador = ArchivoResultados.obtenerMejorJugador();
        
        JOptionPane.showMessageDialog(
            this, 
            mejorJugador, 
            "🏆 Mejor Jugador del Día", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        servidor.detener();
        System.exit(0);
    }
    
    /**
     * Agrega un mensaje al área de log del servidor.
     * Este método es thread-safe usando SwingUtilities.invokeLater
     * para garantizar actualizaciones seguras desde múltiples hilos.
     * 
     * @param mensaje El mensaje a agregar al log
     */
    public void agregarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            // Auto-scroll al final
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
}
