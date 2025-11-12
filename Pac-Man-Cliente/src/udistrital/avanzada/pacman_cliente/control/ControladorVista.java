package udistrital.avanzada.pacman_cliente.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import udistrital.avanzada.pacman_cliente.modelo.Estado;
import udistrital.avanzada.pacman_cliente.vista.VentanaCliente;

/**
 * Controlador intermediario que protege el acceso a la vista. El ORQUESTADOR
 * habla con este controlador, NO directamente con la ventana.
 *
 * RESPONSABILIDAD: - Exponer operaciones de la vista de forma controlada -
 * Garantizar thread-safety (invokeLater) para actualizaciones GUI - Traducir
 * datos del modelo a formato presentable - NO tiene lógica de negocio - NO
 * conoce el modelo
 *
 * PATRÓN: Facade + Adapter sobre la vista
 *
 * @author Steban
 * @version 1.0
 */
public class ControladorVista {

    private VentanaCliente ventana;

    /**
     * Constructor. Crea la ventana y la hace visible.
     */
    public ControladorVista() {
        // Crear ventana
        ventana = new VentanaCliente();
        ventana.setVisible(true);
    }

    /**
     * Obtiene la ventana (necesario para registrar listeners). El orquestador
     * necesita esto para conectar los listeners.
     *
     * @return Instancia de la ventana
     */
    public VentanaCliente getVentana() {
        return ventana;
    }

    // ========== OPERACIONES DE ACTUALIZACIÓN ==========
    /**
     * Muestra un mensaje en el área de mensajes. Thread-safe.
     *
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            ventana.agregarMensaje(mensaje);
        });
    }

    /**
     * Muestra un mensaje con prefijo de información.
     *
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarInfo(String mensaje) {
        mostrarMensaje("[INFO] " + mensaje);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje Mensaje de error
     */
    public void mostrarError(String mensaje) {
        mostrarMensaje("[ERROR] " + mensaje);
    }

    /**
     * Actualiza el frame de video en la ventana.
     *
     * @param frame Frame a mostrar
     */
    public void actualizarFrameVideo(BufferedImage frame) {
        if (ventana != null) {
            ventana.actualizarFrameVideo(frame);
        }
    }

    /**
     * Muestra u oculta el panel de video.
     *
     * @param mostrar true para mostrar, false para ocultar
     */
    public void mostrarPanelVideo(boolean mostrar) {
        if (ventana != null) {
            ventana.mostrarPanelVideo(mostrar);
        }
    }

    /**
     * Limpia el área de mensajes.
     */
    public void limpiarMensajes() {
        SwingUtilities.invokeLater(() -> {
            ventana.limpiarMensajes();
        });
    }

    /**
     * Obtiene el texto del comando ingresado.
     *
     * @return Texto del comando
     */
    public String obtenerComando() {
        return ventana.getTextoComando();
    }

    /**
     * Limpia el campo de comando.
     */
    public void limpiarComando() {
        SwingUtilities.invokeLater(() -> {
            ventana.limpiarComando();
        });
    }

    // ========== ACTUALIZACIONES DE ESTADO ==========
    /**
     * Actualiza el estado visual según el estado del modelo. Traduce Estado
     * enum a texto y color.
     *
     * @param estado Estado actual
     */
    public void actualizarEstado(Estado estado) {
        SwingUtilities.invokeLater(() -> {
            switch (estado) {
                case DESCONECTADO:
                    ventana.actualizarEstado("Desconectado", Color.RED);
                    ventana.habilitarBotonConectar(true);
                    ventana.habilitarBotonDesconectar(false);
                    ventana.habilitarComando(false);
                    ventana.actualizarJugador(null);
                    ventana.actualizarInfoJuego(0, 0, 0);
                    break;

                case CONECTANDO:
                    ventana.actualizarEstado("Conectando...", Color.ORANGE);
                    ventana.habilitarBotonConectar(false);
                    ventana.habilitarBotonDesconectar(false);
                    ventana.habilitarComando(false);
                    break;

                case AUTENTICANDO:
                    ventana.actualizarEstado("Autenticando", Color.YELLOW);
                    ventana.habilitarBotonConectar(false);
                    ventana.habilitarBotonDesconectar(true);
                    ventana.habilitarComando(false);
                    break;

                case JUGANDO:
                    ventana.actualizarEstado("Jugando", Color.GREEN);
                    ventana.habilitarBotonConectar(false);
                    ventana.habilitarBotonDesconectar(true);
                    ventana.habilitarComando(true);
                    break;

                case JUEGO_TERMINADO:
                    ventana.actualizarEstado("Juego Terminado", Color.CYAN);
                    ventana.habilitarBotonConectar(false);
                    ventana.habilitarBotonDesconectar(true);
                    ventana.habilitarComando(false);
                    break;
            }
        });
    }

    /**
     * Actualiza el nombre del jugador mostrado.
     *
     * @param nombre Nombre del jugador
     */
    public void actualizarNombreJugador(String nombre) {
        SwingUtilities.invokeLater(() -> {
            ventana.actualizarJugador(nombre);
        });
    }

    /**
     * Actualiza la información del juego en progreso.
     *
     * @param puntaje Puntaje actual
     * @param tiempo Tiempo transcurrido en segundos
     * @param frutas Frutas restantes
     */
    public void actualizarInformacionJuego(int puntaje, long tiempo, int frutas) {
        SwingUtilities.invokeLater(() -> {
            ventana.actualizarInfoJuego(puntaje, tiempo, frutas);
        });
    }

    // ========== OPERACIONES DE CONTROL ==========
    /**
     * Habilita o deshabilita el campo de comando.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarComando(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            ventana.habilitarComando(habilitado);
        });
    }

    /**
     * Habilita o deshabilita el botón de conectar.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarBotonConectar(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            ventana.habilitarBotonConectar(habilitado);
        });
    }

    /**
     * Habilita o deshabilita el botón de desconectar.
     *
     * @param habilitado true para habilitar
     */
    public void habilitarBotonDesconectar(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            ventana.habilitarBotonDesconectar(habilitado);
        });
    }

    /**
     * Muestra un diálogo de entrada para solicitar datos al usuario.
     * Thread-safe.
     *
     * @param mensaje Mensaje del diálogo
     * @param titulo Título del diálogo
     * @return Texto ingresado o null si se cancela
     */
    public String solicitarEntrada(String mensaje, String titulo) {
        // Esto DEBE ejecutarse en EDT y esperar el resultado
        final String[] resultado = new String[1];

        if (SwingUtilities.isEventDispatchThread()) {
            resultado[0] = javax.swing.JOptionPane.showInputDialog(
                    ventana, mensaje, titulo, javax.swing.JOptionPane.QUESTION_MESSAGE
            );
        } else {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    resultado[0] = javax.swing.JOptionPane.showInputDialog(
                            ventana, mensaje, titulo, javax.swing.JOptionPane.QUESTION_MESSAGE
                    );
                });
            } catch (Exception e) {
                mostrarError("Error al solicitar entrada: " + e.getMessage());
                return null;
            }
        }

        return resultado[0];
    }

    /**
     * Muestra un diálogo de información.
     *
     * @param mensaje Mensaje a mostrar
     * @param titulo Título del diálogo
     */
    public void mostrarDialogoInfo(String mensaje, String titulo) {
        SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(
                    ventana,
                    mensaje,
                    titulo,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    /**
     * Muestra un diálogo de error.
     *
     * @param mensaje Mensaje de error
     * @param titulo Título del diálogo
     */
    public void mostrarDialogoError(String mensaje, String titulo) {
        SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(
                    ventana,
                    mensaje,
                    titulo,
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        });
    }

    /**
     * Solicita selección de archivo (para properties).
     *
     * @return Archivo seleccionado o null si se cancela
     */
    public java.io.File solicitarArchivoProperties() {
        final java.io.File[] resultado = new java.io.File[1];

        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de configuración");
            fileChooser.setCurrentDirectory(new java.io.File("data"));

            javax.swing.filechooser.FileNameExtensionFilter filter
                    = new javax.swing.filechooser.FileNameExtensionFilter(
                            "Archivos Properties (*.properties)", "properties"
                    );
            fileChooser.setFileFilter(filter);

            int opcion = fileChooser.showOpenDialog(ventana);

            if (opcion == javax.swing.JFileChooser.APPROVE_OPTION) {
                resultado[0] = fileChooser.getSelectedFile();
            }
        } catch (Exception e) {
            mostrarError("Error al seleccionar archivo: " + e.getMessage());
            return null;
        }

        return resultado[0];
    }

    /**
     * Cierra la ventana.
     */
    public void cerrarVentana() {
        SwingUtilities.invokeLater(() -> {
            if (ventana != null) {
                ventana.dispose();
            }
        });
    }
}
