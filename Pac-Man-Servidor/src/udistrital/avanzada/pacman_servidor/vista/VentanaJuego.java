package udistrital.avanzada.pacman_servidor.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import udistrital.avanzada.pacman_servidor.modelo.Juego;

/**
 * Ventana que muestra el juego de Pac-Man en el servidor.
 * Solo visible en la máquina del servidor, no en el cliente.
 * Contiene el panel de dibujo y panel de información.
 * 
 * @author Jhon Herrera Cubides
 * @version 1.0
 */
public class VentanaJuego extends JFrame {
    
    private Juego juego;
    private PanelJuego panelJuego;
    private JLabel lblPuntaje;
    private JLabel lblTiempo;
    private JLabel lblFrutas;
    private String nombreJugador;
    
    /**
     * Constructor de la ventana de juego.
     * 
     * @param juego Modelo del juego
     * @param nombreJugador Nombre del jugador
     */
    public VentanaJuego(Juego juego, String nombreJugador) {
        this.juego = juego;
        this.nombreJugador = nombreJugador;
        
        inicializarComponentes();
        configurarVentana();
    }
    
    /**
     * Inicializa los componentes gráficos.
     */
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelPrincipal.setBackground(Color.BLACK);
        
        // Panel superior con título
        JPanel panelTitulo = crearPanelTitulo();
        
        // Panel del juego (donde se dibuja)
        panelJuego = new PanelJuego(juego);
        panelJuego.setPreferredSize(new Dimension(800, 600));
        panelJuego.setBackground(Color.BLACK);
        panelJuego.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        
        // Panel inferior con información
        JPanel panelInfo = crearPanelInformacion();
        
        // Agregar componentes
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelJuego, BorderLayout.CENTER);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Crea el panel con el título y nombre del jugador.
     * 
     * @return Panel del título
     */
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 25, 112)); // Azul medianoche
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("PAC-MAN - Jugador: " + nombreJugador);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.YELLOW);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblTitulo);
        
        return panel;
    }
    
    /**
     * Crea el panel con información del juego.
     * 
     * @return Panel de información
     */
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Etiquetas de información
        lblPuntaje = crearEtiquetaInfo("Puntaje: 0");
        lblTiempo = crearEtiquetaInfo("Tiempo: 0s");
        lblFrutas = crearEtiquetaInfo("Frutas: 4");
        
        panel.add(lblPuntaje);
        panel.add(crearSeparador());
        panel.add(lblTiempo);
        panel.add(crearSeparador());
        panel.add(lblFrutas);
        
        return panel;
    }
    
    /**
     * Crea una etiqueta de información con estilo.
     * 
     * @param texto Texto inicial
     * @return JLabel configurado
     */
    private JLabel crearEtiquetaInfo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }
    
    /**
     * Crea un separador visual.
     * 
     * @return JLabel usado como separador
     */
    private JLabel crearSeparador() {
        JLabel sep = new JLabel(" | ");
        sep.setFont(new Font("Arial", Font.BOLD, 16));
        sep.setForeground(Color.YELLOW);
        return sep;
    }
    
    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Pac-Man Server - " + nombreJugador);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Repinta el juego y actualiza la información.
     * Método público para que el controlador pueda actualizar la vista.
     */
    public void repintar() {
        // Actualizar información
        lblPuntaje.setText("Puntaje: " + juego.getPuntajeTotal());
        lblTiempo.setText("Tiempo: " + juego.getTiempoTranscurrido() + "s");
        lblFrutas.setText("Frutas: " + juego.getFrutasRestantes());
        
        // Repintar panel de juego
        panelJuego.repaint();
    }
}