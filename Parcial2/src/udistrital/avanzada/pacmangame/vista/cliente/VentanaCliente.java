package udistrital.avanzada.pacmangame.vista.cliente;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import udistrital.avanzada.pacmangame.controlador.cliente.ControladorCliente;

/**
 * Ventana del cliente.
 * Interfaz simple para enviar comandos y recibir mensajes.
 * Cumple con separación de eventos.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class VentanaCliente extends JFrame {
    
    private ControladorCliente controlador;
    private JTextArea areaMensajes;
    private JTextField campoEntrada;
    private JButton btnEnviar;
    private JButton btnConectar;
    private JButton btnDesconectar;
    private JScrollPane scrollMensajes;
    
    /**
     * Constructor de la ventana del cliente.
     * 
     * @param controlador Controlador del cliente
     */
    public VentanaCliente(ControladorCliente controlador) {
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
        
        btnConectar = new JButton("Conectar");
        btnDesconectar = new JButton("Desconectar");
        btnDesconectar.setEnabled(false);
        
        panelSuperior.add(btnConectar);
        panelSuperior.add(btnDesconectar);
        
        // Panel central con área de mensajes
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Mensajes del Servidor"));
        
        areaMensajes = new JTextArea(20, 50);
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        
        scrollMensajes = new JScrollPane(areaMensajes);
        panelCentral.add(scrollMensajes, BorderLayout.CENTER);
        
        // Panel inferior con entrada de comandos
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblEntrada = new JLabel("Comando: ");
        campoEntrada = new JTextField(30);
        campoEntrada.setEnabled(false);
        
        btnEnviar = new JButton("Enviar");
        btnEnviar.setEnabled(false);
        
        panelInferior.add(lblEntrada, BorderLayout.WEST);
        panelInferior.add(campoEntrada, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        
        // Agregar a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {
        btnConectar.addActionListener(new ManejadorConectar());
        btnDesconectar.addActionListener(new ManejadorDesconectar());
        btnEnviar.addActionListener(new ManejadorEnviar());
        
        // Enter en el campo de entrada
        campoEntrada.addActionListener(new ManejadorEnviar());
    }
    
    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Cliente Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Agrega un mensaje al área de mensajes.
     * 
     * @param mensaje Mensaje a agregar
     */
    public void agregarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }
    
    /**
     * Agrega un error al área de mensajes.
     * 
     * @param error Error a agregar
     */
    public void agregarError(String error) {
        areaMensajes.append("[ERROR] " + error + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }
    
    /**
     * Solicita el usuario al jugador.
     */
    public void solicitarUsuario() {
        agregarMensaje("\n=== Ingrese su nombre de usuario ===");
        campoEntrada.requestFocus();
    }
    
    /**
     * Solicita la contraseña al jugador.
     */
    public void solicitarContrasena() {
        agregarMensaje("\n=== Ingrese su contraseña ===");
        campoEntrada.requestFocus();
    }
    
    /**
     * Solicita un movimiento al jugador.
     */
    public void solicitarMovimiento() {
        agregarMensaje("\n>>> Ingrese dirección (Arriba, Abajo, Izquierda, Derecha):");
        campoEntrada.requestFocus();
    }
    
    /**
     * Listener para el botón Conectar.
     */
    private class ManejadorConectar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Abrir selector de archivo para propiedades
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de propiedades");
            fileChooser.setCurrentDirectory(new File("data"));
            
            int resultado = fileChooser.showOpenDialog(VentanaCliente.this);
            
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                
                try {
                    controlador.inicializar(archivo.getAbsolutePath());
                    
                    if (controlador.conectar()) {
                        btnConectar.setEnabled(false);
                        btnDesconectar.setEnabled(true);
                        campoEntrada.setEnabled(true);
                        btnEnviar.setEnabled(true);
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        VentanaCliente.this,
                        "Error al conectar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    
    /**
     * Listener para el botón Desconectar.
     */
    private class ManejadorDesconectar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controlador.desconectar();
            
            btnConectar.setEnabled(true);
            btnDesconectar.setEnabled(false);
            campoEntrada.setEnabled(false);
            btnEnviar.setEnabled(false);
        }
    }
    
    /**
     * Listener para el botón Enviar.
     */
    private class ManejadorEnviar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String mensaje = campoEntrada.getText().trim();
            
            if (!mensaje.isEmpty()) {
                controlador.enviarMensaje(mensaje);
                areaMensajes.append(">> " + mensaje + "\n");
                campoEntrada.setText("");
            }
        }
    }
}