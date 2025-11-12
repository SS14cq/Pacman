package udistrital.avanzada.pacman_servidor.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana que muestra el juego en ejecución.
 * Contiene el panel de juego y estadísticas.
 * 
 * @author Steban
 * @version 1.0
 */
public class VentanaJuego extends JFrame {
    
    private PanelJuego panelJuego;
    private JLabel lblPuntaje;
    private JLabel lblTiempo;
    private JLabel lblFrutas;
    private JLabel lblJugador;
    private JButton btnSalir;
    
    /**
     * Constructor que inicializa la ventana.
     * 
     * @param anchoVentana Ancho del área de juego
     * @param altoVentana Alto del área de juego
     */
    public VentanaJuego(int anchoVentana, int altoVentana) {
        inicializarComponentes(anchoVentana, altoVentana);
        configurarVentana();
    }
    
    /**
     * Inicializa componentes visuales.
     */
    private void inicializarComponentes(int ancho, int alto) {
        // Panel de juego (centro)
        panelJuego = new PanelJuego(ancho, alto);
        
        // Panel de estadísticas (derecha)
        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(new BoxLayout(panelEstadisticas, BoxLayout.Y_AXIS));
        panelEstadisticas.setPreferredSize(new Dimension(200, alto));
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        
        lblJugador = crearLabel("Jugador: ---");
        lblPuntaje = crearLabel("Puntaje: 0");
        lblTiempo = crearLabel("Tiempo: 0s");
        lblFrutas = crearLabel("Frutas: 4");
        
        panelEstadisticas.add(Box.createVerticalStrut(20));
        panelEstadisticas.add(lblJugador);
        panelEstadisticas.add(Box.createVerticalStrut(10));
        panelEstadisticas.add(lblPuntaje);
        panelEstadisticas.add(Box.createVerticalStrut(10));
        panelEstadisticas.add(lblTiempo);
        panelEstadisticas.add(Box.createVerticalStrut(10));
        panelEstadisticas.add(lblFrutas);
        panelEstadisticas.add(Box.createVerticalGlue());
        
        btnSalir = new JButton("Salir");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEstadisticas.add(btnSalir);
        panelEstadisticas.add(Box.createVerticalStrut(20));
        
        // Agregar componentes
        add(panelJuego, BorderLayout.CENTER);
        add(panelEstadisticas, BorderLayout.EAST);
    }
    
    /**
     * Crea un JLabel estilizado.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    /**
     * Configura propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Pac-Man - Juego en Ejecución");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }
    
    // ========== Getters ==========
    
    public PanelJuego getPanelJuego() {
        return panelJuego;
    }
    
    public JButton getBtnSalir() {
        return btnSalir;
    }
    
    // ========== Métodos públicos para actualizar vista ==========
    
    /**
     * Actualiza el nombre del jugador.
     */
    public void actualizarJugador(String nombre) {
        SwingUtilities.invokeLater(() -> {
            lblJugador.setText("Jugador: " + nombre);
        });
    }
    
    /**
     * Actualiza el puntaje mostrado.
     */
    public void actualizarPuntaje(int puntaje) {
        SwingUtilities.invokeLater(() -> {
            lblPuntaje.setText("Puntaje: " + puntaje);
        });
    }
    
    /**
     * Actualiza el tiempo transcurrido.
     */
    public void actualizarTiempo(long segundos) {
        SwingUtilities.invokeLater(() -> {
            lblTiempo.setText("Tiempo: " + segundos + "s");
        });
    }
    
    /**
     * Actualiza las frutas restantes.
     */
    public void actualizarFrutas(int cantidad) {
        SwingUtilities.invokeLater(() -> {
            lblFrutas.setText("Frutas: " + cantidad);
        });
    }
}