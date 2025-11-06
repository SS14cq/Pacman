/*
 * Ventana principal del cliente que proporciona la interfaz gráfica
 * para la interacción con el juego de Pac-Man.
 * 
 * Esta clase implementa la capa Vista del patrón MVC.
 * Contiene todos los componentes de la interfaz gráfica y
 * los manejadores de eventos, delegando la lógica al Controlador.
 * 
 * Componentes principales:
 * - Panel de conexión con botones de configuración
 * - Panel de credenciales para autenticación
 * - Panel de control de movimiento (botones direccionales y campo de texto)
 * - Área de mensajes para mostrar comunicación con el servidor
 * 
 * Cumple con los principios de:
 * - Separación de responsabilidades (Vista/Controlador)
 * - Desacoplamiento de eventos (ActionListener separados)
 * - Alta cohesión (todo relacionado con la interfaz gráfica)
 */
package udistrital.avanzada.cliente.vista;

/**
 *
 * @author juanr
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import udistrital.avanzada.cliente.control.Cliente;


public class VentanaCliente extends JFrame {
    /** Referencia al controlador del cliente */
    private Cliente cliente;
    
    // Componentes de conexión
    /** Botón para cargar archivo de propiedades */
    private JButton btnCargarPropiedades;
    
    /** Botón para iniciar conexión con el servidor */
    private JButton btnConectar;
    
    // Componentes de credenciales
    /** Campo de texto para el nombre de usuario */
    private JTextField txtUsuario;
    
    /** Campo de contraseña oculta */
    private JPasswordField txtContrasena;
    
    /** Botón para enviar credenciales al servidor */
    private JButton btnEnviarCredenciales;
    
    // Componentes de juego
    /** Campo de texto para ingresar dirección de movimiento */
    private JTextField txtMovimiento;
    
    /** Botón para enviar movimiento desde campo de texto */
    private JButton btnEnviar;
    
    /** Botones direccionales para movimiento rápido */
    private JButton btnArriba;
    private JButton btnAbajo;
    private JButton btnIzquierda;
    private JButton btnDerecha;
    
    // Área de mensajes
    /** Área de texto para mostrar mensajes del servidor */
    private JTextArea areaMensajes;
    
    /** ScrollPane que contiene el área de mensajes */
    private JScrollPane scrollMensajes;
    
    /**
     * Constructor de la ventana del cliente.
     * Inicializa todos los componentes gráficos y configura los eventos.
     * 
     * @param cliente Referencia al controlador del cliente
     */
    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        inicializarComponentes();
        configurarEventos();
        setVisible(true);
    }
    
    /**
     * Inicializa y configura todos los componentes de la interfaz gráfica.
     * Establece layouts, tamaños, posiciones y estilos visuales.
     * Aplica principios de diseño para una interfaz agradable y funcional.
     */
    private void inicializarComponentes() {
        // Configuración básica de la ventana
        setTitle("Cliente Pac-Man");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(236, 240, 241));
        
        // Panel principal que contiene todo
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(236, 240, 241));
        
        // Panel superior con título
        JPanel panelTitulo = crearPanelTitulo();
        
        // Panel de conexión
        JPanel panelConexion = crearPanelConexion();
        
        // Panel de credenciales
        JPanel panelCredenciales = crearPanelCredenciales();
        
        // Panel de control superior (conexión + credenciales)
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));
        panelSuperior.setBackground(new Color(236, 240, 241));
        panelSuperior.add(panelConexion);
        panelSuperior.add(panelCredenciales);
        
        // Panel de movimiento
        JPanel panelMovimiento = crearPanelMovimiento();
        
        // Área de mensajes
        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Consolas", Font.PLAIN, 11));
        areaMensajes.setBackground(new Color(44, 62, 80));
        areaMensajes.setForeground(new Color(236, 240, 241));
        areaMensajes.setMargin(new Insets(10, 10, 10, 10));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        
        scrollMensajes = new JScrollPane(areaMensajes);
        scrollMensajes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "📟 Consola de Mensajes",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        scrollMensajes.setPreferredSize(new Dimension(680, 200));
        
        // Ensamblar paneles
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBackground(new Color(236, 240, 241));
        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(panelMovimiento, BorderLayout.CENTER);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(scrollMensajes, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Mensaje inicial
        agregarMensaje("=".repeat(50));
        agregarMensaje("🎮 CLIENTE PAC-MAN INICIADO");
        agregarMensaje("=".repeat(50));
        agregarMensaje("1. Cargue el archivo de propiedades del servidor");
        agregarMensaje("2. Conéctese al servidor");
        agregarMensaje("3. Ingrese sus credenciales");
        agregarMensaje("4. ¡Juegue y diviértase!");
        agregarMensaje("=".repeat(50) + "\n");
        
        setLocationRelativeTo(null);
    }
    
    /**
     * Crea el panel de título con diseño atractivo.
     * 
     * @return JPanel con el título configurado
     */
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(680, 60));
        
        JLabel lblTitulo = new JLabel("🎮 PAC-MAN CLIENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(241, 196, 15));
        panel.add(lblTitulo);
        
        return panel;
    }
    
    /**
     * Crea el panel de conexión con sus componentes.
     * 
     * @return JPanel configurado
     */
    private JPanel crearPanelConexion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "🔌 Conexión al Servidor",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        panel.setBackground(Color.WHITE);
        
        btnCargarPropiedades = new JButton("📁 Cargar Propiedades");
        btnCargarPropiedades.setFont(new Font("Arial", Font.BOLD, 12));
        btnCargarPropiedades.setBackground(new Color(52, 152, 219));
        btnCargarPropiedades.setForeground(Color.WHITE);
        btnCargarPropiedades.setFocusPainted(false);
        btnCargarPropiedades.setPreferredSize(new Dimension(200, 35));
        
        btnConectar = new JButton("🔗 Conectar");
        btnConectar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConectar.setBackground(new Color(46, 204, 113));
        btnConectar.setForeground(Color.WHITE);
        btnConectar.setFocusPainted(false);
        btnConectar.setEnabled(false);
        btnConectar.setPreferredSize(new Dimension(150, 35));
        
        panel.add(btnCargarPropiedades);
        panel.add(btnConectar);
        
        return panel;
    }
    
    /**
     * Crea el panel de credenciales con sus componentes.
     * 
     * @return JPanel configurado
     */
    private JPanel crearPanelCredenciales() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "🔐 Autenticación",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(155, 89, 182)
        ));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblUsuario, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsuario = new JTextField();
        txtUsuario.setEnabled(false);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsuario.setPreferredSize(new Dimension(200, 30));
        panel.add(txtUsuario, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblContrasena, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtContrasena = new JPasswordField();
        txtContrasena.setEnabled(false);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 12));
        txtContrasena.setPreferredSize(new Dimension(200, 30));
        panel.add(txtContrasena, gbc);
        
        // Botón
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.3;
        btnEnviarCredenciales = new JButton("✔ Enviar");
        btnEnviarCredenciales.setEnabled(false);
        btnEnviarCredenciales.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviarCredenciales.setBackground(new Color(155, 89, 182));
        btnEnviarCredenciales.setForeground(Color.WHITE);
        btnEnviarCredenciales.setFocusPainted(false);
        btnEnviarCredenciales.setPreferredSize(new Dimension(120, 65));
        panel.add(btnEnviarCredenciales, gbc);
        
        return panel;
    }
    
    /**
     * Crea el panel de control de movimiento con botones y campo de texto.
     * 
     * @return JPanel configurado
     */
    private JPanel crearPanelMovimiento() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "🎮 Control de Movimiento",
            0,
            0,
            new Font("Arial", Font.BOLD, 12),
            new Color(231, 76, 60)
        ));
        panel.setBackground(Color.WHITE);
        
        // Botones direccionales
        JPanel panelDirecciones = new JPanel(new GridBagLayout());
        panelDirecciones.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        btnArriba = crearBotonDireccional("↑ ARRIBA");
        btnAbajo = crearBotonDireccional("↓ ABAJO");
        btnIzquierda = crearBotonDireccional("← IZQUIERDA");
        btnDerecha = crearBotonDireccional("→ DERECHA");
        
        // Posicionar botones en forma de cruz
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelDirecciones.add(btnArriba, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelDirecciones.add(btnIzquierda, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel lblCentro = new JLabel("🕹️", SwingConstants.CENTER);
        lblCentro.setFont(new Font("Arial", Font.PLAIN, 30));
        panelDirecciones.add(lblCentro, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        panelDirecciones.add(btnDerecha, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelDirecciones.add(btnAbajo, gbc);
        
        deshabilitarBotonesMovimiento();
        
        // Campo de texto para movimiento manual
        JPanel panelTextoMovimiento = new JPanel(new BorderLayout(10, 10));
        panelTextoMovimiento.setBackground(Color.WHITE);
        panelTextoMovimiento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblInstruccion = new JLabel("O escriba: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
        lblInstruccion.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInstruccion.setForeground(new Color(127, 140, 141));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelEntrada = new JPanel(new BorderLayout(5, 5));
        panelEntrada.setBackground(Color.WHITE);
        
        txtMovimiento = new JTextField();
        txtMovimiento.setEnabled(false);
        txtMovimiento.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMovimiento.setPreferredSize(new Dimension(300, 35));
        
        btnEnviar = new JButton("➤ Enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviar.setBackground(new Color(231, 76, 60));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(100, 35));
        
        panelEntrada.add(txtMovimiento, BorderLayout.CENTER);
        panelEntrada.add(btnEnviar, BorderLayout.EAST);
        
        panelTextoMovimiento.add(lblInstruccion, BorderLayout.NORTH);
        panelTextoMovimiento.add(panelEntrada, BorderLayout.CENTER);
        
        panel.add(panelDirecciones, BorderLayout.CENTER);
        panel.add(panelTextoMovimiento, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crea un botón direccional con estilo uniforme.
     * 
     * @param texto Texto del botón
     * @return JButton configurado
     */
    private JButton crearBotonDireccional(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 11));
        boton.setBackground(new Color(52, 152, 219));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
        return boton;
    }
    
    /**
     * Configura todos los manejadores de eventos de los componentes.
     * Implementa la separación de eventos, listeners y ActionPerformed
     * según los requisitos del proyecto.
     */
    private void configurarEventos() {
        // Evento: Cargar propiedades
        btnCargarPropiedades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cliente.cargarPropiedades()) {
                    btnConectar.setEnabled(true);
                }
            }
        });
        
        // Evento: Conectar al servidor
        btnConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente.conectar();
                btnConectar.setEnabled(false);
                btnCargarPropiedades.setEnabled(false);
            }
        });
        
        // Evento: Enviar credenciales
        btnEnviarCredenciales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCredencialesAlServidor();
            }
        });
        
        // Evento: Enter en campo de contraseña
        txtContrasena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCredencialesAlServidor();
            }
        });
        
        // Evento: Enviar movimiento desde botón
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMovimientoTexto();
            }
        });
        
        // Evento: Enter en campo de movimiento
        txtMovimiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMovimientoTexto();
            }
        });
        
        // Eventos: Botones direccionales
        btnArriba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente.enviarMovimiento("ARRIBA");
            }
        });
        
        btnAbajo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente.enviarMovimiento("ABAJO");
            }
        });
        
        btnIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente.enviarMovimiento("IZQUIERDA");
            }
        });
        
        btnDerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente.enviarMovimiento("DERECHA");
            }
        });
        
        // Atajos de teclado para los botones direccionales
        configurarAtajosTeclado();
    }
    
    /**
     * Configura atajos de teclado para movimiento rápido.
     * Permite usar las flechas del teclado para mover a Pac-Man.
     */
    private void configurarAtajosTeclado() {
        KeyListener tecladoListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!btnArriba.isEnabled()) return; // Solo si el movimiento está habilitado
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        cliente.enviarMovimiento("ARRIBA");
                        break;
                    case KeyEvent.VK_DOWN:
                        cliente.enviarMovimiento("ABAJO");
                        break;
                    case KeyEvent.VK_LEFT:
                        cliente.enviarMovimiento("IZQUIERDA");
                        break;
                    case KeyEvent.VK_RIGHT:
                        cliente.enviarMovimiento("DERECHA");
                        break;
                }
            }
        };
        
        // Agregar el listener a todos los componentes relevantes
        txtMovimiento.addKeyListener(tecladoListener);
        btnArriba.addKeyListener(tecladoListener);
        btnAbajo.addKeyListener(tecladoListener);
        btnIzquierda.addKeyListener(tecladoListener);
        btnDerecha.addKeyListener(tecladoListener);
    }
    
    /**
     * Método auxiliar para enviar credenciales al servidor.
     * Valida que los campos no estén vacíos antes de enviar.
     */
    private void enviarCredencialesAlServidor() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            agregarMensaje("[ERROR] Usuario y contraseña no pueden estar vacíos");
            return;
        }
        
        cliente.enviarCredenciales(usuario, contrasena);
        deshabilitarCredenciales();
    }
    
    /**
     * Método auxiliar para enviar movimiento desde campo de texto.
     * Valida y normaliza la entrada del usuario.
     */
    private void enviarMovimientoTexto() {
        String movimiento = txtMovimiento.getText().trim().toUpperCase();
        
        if (movimiento.isEmpty()) {
            agregarMensaje("[ERROR] Debe ingresar una dirección");
            return;
        }
        
        // Validar que sea una dirección válida
        if (!movimiento.equals("ARRIBA") && !movimiento.equals("ABAJO") &&
            !movimiento.equals("IZQUIERDA") && !movimiento.equals("DERECHA")) {
            agregarMensaje("[ERROR] Dirección inválida. Use: ARRIBA, ABAJO, IZQUIERDA o DERECHA");
            return;
        }
        
        cliente.enviarMovimiento(movimiento);
        txtMovimiento.setText("");
        txtMovimiento.requestFocus();
    }
    
    /**
     * Agrega un mensaje al área de mensajes de forma thread-safe.
     * Usa SwingUtilities.invokeLater para garantizar actualizaciones
     * seguras desde múltiples hilos.
     * 
     * @param mensaje El mensaje a agregar
     */
    public void agregarMensaje(String mensaje) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                areaMensajes.append(mensaje + "\n");
                // Auto-scroll al final del área de texto
                areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
            }
        });
    }
    
    /**
     * Habilita los campos de credenciales para que el usuario pueda ingresar
     * su nombre de usuario y contraseña.
     * Se invoca cuando el servidor solicita credenciales.
     */
    public void habilitarCredenciales() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtUsuario.setEnabled(true);
                txtContrasena.setEnabled(true);
                btnEnviarCredenciales.setEnabled(true);
                txtUsuario.requestFocus();
            }
        });
    }
    
    /**
     * Deshabilita los campos de credenciales después de enviarlas.
     * Previene múltiples envíos accidentales.
     */
    private void deshabilitarCredenciales() {
        txtUsuario.setEnabled(false);
        txtContrasena.setEnabled(false);
        btnEnviarCredenciales.setEnabled(false);
    }
    
    /**
     * Habilita los controles de movimiento cuando el servidor
     * solicita un movimiento.
     * Permite al usuario enviar comandos de dirección.
     */
    public void habilitarMovimiento() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtMovimiento.setEnabled(true);
                btnEnviar.setEnabled(true);
                btnArriba.setEnabled(true);
                btnAbajo.setEnabled(true);
                btnIzquierda.setEnabled(true);
                btnDerecha.setEnabled(true);
                txtMovimiento.requestFocus();
            }
        });
    }
    
    /**
     * Deshabilita los botones de movimiento.
     * Se usa al iniciar la aplicación y después de cada movimiento.
     */
    private void deshabilitarBotonesMovimiento() {
        btnArriba.setEnabled(false);
        btnAbajo.setEnabled(false);
        btnIzquierda.setEnabled(false);
        btnDerecha.setEnabled(false);
    }
    
    /**
     * Deshabilita todos los controles de la interfaz.
     * Se invoca cuando se cierra la conexión con el servidor.
     */
    public void deshabilitarTodo() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btnConectar.setEnabled(false);
                deshabilitarCredenciales();
                txtMovimiento.setEnabled(false);
                btnEnviar.setEnabled(false);
                deshabilitarBotonesMovimiento();
                
                agregarMensaje("\n[SISTEMA] Todos los controles deshabilitados");
                agregarMensaje("[SISTEMA] Puede cerrar la ventana");
            }
        });
    }
}