package udistrital.avanzada.pacman_servidor.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana inicial del servidor.
 * Permite seleccionar archivo properties e iniciar el servidor.
 * NO contiene lógica, solo componentes visuales.
 * 
 * @author Steban
 * @version 1.0
 */
public class VentanaInicioServidor extends JFrame {
    
    private JButton btnSeleccionarProperties;
    private JButton btnIniciarServidor;
    private JTextArea areaLog;
    private JLabel lblEstado;
    
    /**
     * Constructor que inicializa la interfaz.
     */
    public VentanaInicioServidor() {
        inicializarComponentes();
        configurarVentana();
    }
    
    /**
     * Inicializa todos los componentes visuales.
     */
    private void inicializarComponentes() {
        // Panel superior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSeleccionarProperties = new JButton("Seleccionar Properties");
        btnSeleccionarProperties.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnIniciarServidor = new JButton("Iniciar Servidor");
        btnIniciarServidor.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciarServidor.setEnabled(false);
        
        panelBotones.add(btnSeleccionarProperties);
        panelBotones.add(btnIniciarServidor);
        
        // Panel central - Log
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBackground(Color.BLACK);
        areaLog.setForeground(Color.GREEN);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log del Servidor"));
        
        // Panel inferior - Estado
        lblEstado = new JLabel("Estado: Esperando configuración...");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.add(lblEstado);
        
        // Agregar componentes
        add(panelBotones, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        add(panelEstado, BorderLayout.SOUTH);
    }
    
    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Servidor Pac-Man");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    // ========== Getters para que el Controlador acceda a componentes ==========
    
    public JButton getBtnSeleccionarProperties() {
        return btnSeleccionarProperties;
    }
    
    public JButton getBtnIniciarServidor() {
        return btnIniciarServidor;
    }
    
    // ========== Métodos públicos para actualizar la vista ==========
    
    /**
     * Agrega una línea al log del servidor.
     * 
     * @param mensaje Mensaje a agregar
     */
    public void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
    
    /**
     * Actualiza el texto del estado.
     * 
     * @param estado Nuevo estado
     */
    public void actualizarEstado(String estado) {
        SwingUtilities.invokeLater(() -> {
            lblEstado.setText("Estado: " + estado);
        });
    }
    
    /**
     * Habilita o deshabilita el botón de iniciar servidor.
     * 
     * @param habilitado true para habilitar
     */
    public void habilitarBotonIniciar(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            btnIniciarServidor.setEnabled(habilitado);
        });
    }
}