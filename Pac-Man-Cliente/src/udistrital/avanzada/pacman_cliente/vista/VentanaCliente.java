package udistrital.avanzada.pacman_cliente.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Ventana principal del cliente Pac-Man.
 *
 * RESPONSABILIDAD: - SOLO mostrar y capturar información (GUI pura) - NO tiene
 * lógica de negocio - NO conoce el modelo - NO se comunica con el orquestador
 * directamente
 *
 * Los eventos están delegados a listeners externos.
 *
 * @author Steban
 * @version 1.0
 */
public class VentanaCliente extends JFrame {

    // ========== COMPONENTES GUI ==========
    private JTextField txtComando;
    private JButton btnEnviar;
    private JButton btnConectar;
    private JButton btnDesconectar;
    private JTextArea areaMensajes;
    private JLabel lblEstado;
    private JLabel lblJugador;
    private JLabel lblPuntaje;
    private JLabel lblTiempo;
    private JLabel lblFrutas;
    private PanelVideo panelVideo;
    private JPanel panelContenedor; // Para cambiar entre mensajes y video

    /**
     * Constructor. Inicializa SOLO la GUI.
     */
    public VentanaCliente() {
        inicializarComponentes();
        configurarVentana();
    }

    /**
     * Inicializa todos los componentes gráficos.
     */
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(45, 45, 48));

        // Panel superior: Estado
        JPanel panelEstado = crearPanelEstado();

        // Panel central: Mensajes
        JPanel panelMensajes = crearPanelMensajes();

        // Panel inferior: Comandos
        JPanel panelComandos = crearPanelComandos();

        // Ensamblar
        panelPrincipal.add(panelEstado, BorderLayout.NORTH);
        panelPrincipal.add(panelMensajes, BorderLayout.CENTER);
        panelPrincipal.add(panelComandos, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    /**
     * Crea el panel de estado con información del juego.
     *
     * @return Panel configurado
     */
    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 5));
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Estado del Juego",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.YELLOW
        ));

        // Primera fila
        lblEstado = crearEtiquetaEstado("Estado: Desconectado", Color.RED);
        lblJugador = crearEtiquetaEstado("Jugador: ---", Color.WHITE);
        lblPuntaje = crearEtiquetaEstado("Puntaje: 0", Color.CYAN);

        // Segunda fila
        lblTiempo = crearEtiquetaEstado("Tiempo: 0s", Color.CYAN);
        lblFrutas = crearEtiquetaEstado("Frutas: 0", Color.CYAN);
        JLabel lblEspacio = crearEtiquetaEstado("", Color.WHITE);

        panel.add(lblEstado);
        panel.add(lblJugador);
        panel.add(lblPuntaje);
        panel.add(lblTiempo);
        panel.add(lblFrutas);
        panel.add(lblEspacio);

        return panel;
    }

    /**
     * Crea una etiqueta de estado con estilo.
     *
     * @param texto Texto inicial
     * @param color Color del texto
     * @return JLabel configurado
     */
    private JLabel crearEtiquetaEstado(String texto, Color color) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(color);
        lbl.setOpaque(false);
        return lbl;
    }

    /**
     * Crea el panel de mensajes del servidor.
     *
     * @return Panel configurado
     */
    private JPanel crearPanelMensajes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 48));

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaMensajes.setBackground(new Color(30, 30, 30));
        areaMensajes.setForeground(new Color(0, 255, 0));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);

        // Panel de video (inicialmente oculto)
        panelVideo = new PanelVideo(600, 400);
        panelVideo.setVisible(false);

        // Panel contenedor que puede cambiar entre mensajes y video
        panelContenedor = new JPanel(new BorderLayout());
        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        panelContenedor.add(scrollMensajes, BorderLayout.CENTER);

        // Agregar panel contenedor en lugar del scroll directo
        panel.add(panelContenedor, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Actualiza el frame de video mostrado.
     *
     * @param frame Nueva imagen a mostrar
     */
    public void actualizarFrameVideo(BufferedImage frame) {
        if (panelVideo != null) {
            panelVideo.actualizarFrame(frame);
        }
    }

    /**
     * Muestra u oculta el panel de video.
     *
     * @param mostrar true para mostrar video, false para mostrar mensajes
     */
    public void mostrarPanelVideo(boolean mostrar) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panelContenedor.removeAll();

                if (mostrar) {
                    panelContenedor.add(panelVideo, BorderLayout.CENTER);
                    panelVideo.setVisible(true);
                } else {
                    JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
                    panelContenedor.add(scrollMensajes, BorderLayout.CENTER);
                    panelVideo.setVisible(false);
                }

                panelContenedor.revalidate();
                panelContenedor.repaint();
            }
        });
    }

    /**
     * Limpia el panel de video.
     */
    public void limpiarPanelVideo() {
        if (panelVideo != null) {
            panelVideo.limpiar();
        }
    }

    /**
     * Crea el panel de comandos y botones.
     *
     * @return Panel configurado
     */
    private JPanel crearPanelComandos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 48));

        // Panel de entrada
        JPanel panelEntrada = new JPanel(new BorderLayout(5, 0));
        panelEntrada.setBackground(new Color(45, 45, 48));

        JLabel lblComando = new JLabel("Comando:");
        lblComando.setForeground(Color.WHITE);
        lblComando.setFont(new Font("Arial", Font.BOLD, 12));

        txtComando = new JTextField();
        txtComando.setFont(new Font("Arial", Font.PLAIN, 14));
        txtComando.setEnabled(false);

        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviar.setBackground(new Color(40, 167, 69));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setEnabled(false);

        panelEntrada.add(lblComando, BorderLayout.WEST);
        panelEntrada.add(txtComando, BorderLayout.CENTER);
        panelEntrada.add(btnEnviar, BorderLayout.EAST);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(45, 45, 48));

        btnConectar = new JButton("Conectar al Servidor");
        btnConectar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConectar.setBackground(new Color(0, 123, 255));
        btnConectar.setForeground(Color.WHITE);
        btnConectar.setFocusPainted(false);
        btnConectar.setPreferredSize(new Dimension(180, 35));

        btnDesconectar = new JButton("Desconectar");
        btnDesconectar.setFont(new Font("Arial", Font.BOLD, 12));
        btnDesconectar.setBackground(new Color(220, 53, 69));
        btnDesconectar.setForeground(Color.WHITE);
        btnDesconectar.setFocusPainted(false);
        btnDesconectar.setPreferredSize(new Dimension(140, 35));
        btnDesconectar.setEnabled(false);

        panelBotones.add(btnConectar);
        panelBotones.add(btnDesconectar);

        panel.add(panelEntrada, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
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

    // ========== MÉTODOS PÚBLICOS PARA EL CONTROLADOR ==========
    /**
     * Agrega un mensaje al área de mensajes.
     *
     * @param mensaje Mensaje a mostrar
     */
    public void agregarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }

    /**
     * Limpia el área de mensajes.
     */
    public void limpiarMensajes() {
        areaMensajes.setText("");
    }

    /**
     * Obtiene el texto del campo de comando.
     *
     * @return Texto ingresado
     */
    public String getTextoComando() {
        return txtComando.getText().trim();
    }

    /**
     * Limpia el campo de comando.
     */
    public void limpiarComando() {
        txtComando.setText("");
    }

    /**
     * Actualiza la etiqueta de estado.
     *
     * @param estado Texto del estado
     * @param color Color del estado
     */
    public void actualizarEstado(String estado, Color color) {
        lblEstado.setText("Estado: " + estado);
        lblEstado.setForeground(color);
    }

    /**
     * Actualiza la etiqueta de jugador.
     *
     * @param nombre Nombre del jugador
     */
    public void actualizarJugador(String nombre) {
        lblJugador.setText("Jugador: " + (nombre != null ? nombre : "---"));
    }

    /**
     * Actualiza la información del juego.
     *
     * @param puntaje Puntaje actual
     * @param tiempo Tiempo transcurrido
     * @param frutas Frutas restantes
     */
    public void actualizarInfoJuego(int puntaje, long tiempo, int frutas) {
        lblPuntaje.setText("Puntaje: " + puntaje);
        lblTiempo.setText("Tiempo: " + tiempo + "s");
        lblFrutas.setText("Frutas: " + frutas);
    }

    /**
     * Habilita/deshabilita el campo de comando.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarComando(boolean habilitado) {
        txtComando.setEnabled(habilitado);
        btnEnviar.setEnabled(habilitado);
    }

    /**
     * Habilita/deshabilita el botón de conectar.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarBotonConectar(boolean habilitado) {
        btnConectar.setEnabled(habilitado);
    }

    /**
     * Habilita/deshabilita el botón de desconectar.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarBotonDesconectar(boolean habilitado) {
        btnDesconectar.setEnabled(habilitado);
    }

    // ========== GETTERS DE COMPONENTES (para listeners) ==========
    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    public JButton getBtnConectar() {
        return btnConectar;
    }

    public JButton getBtnDesconectar() {
        return btnDesconectar;
    }

    public JTextField getTxtComando() {
        return txtComando;
    }
}
