package udistrital.avanzada.pacman_servidor.controlador;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import udistrital.avanzada.pacman_servidor.vista.VentanaServidor;

/**
 * Clase principal del Servidor Pac-Man. Punto de entrada de la aplicación.
 *
 * @author Steban
 * @version 1.0
 */
public class LauncherServidor {

    /**
     * Método principal que inicia el servidor.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        configurarLookAndFeel();

        // Iniciar interfaz en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            VentanaServidor ventana = new VentanaServidor();
            ventana.setVisible(true);
        });
    }

    /**
     * Configura el Look and Feel para que use el del sistema operativo.
     */
    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usa el por defecto
            System.err.println("No se pudo configurar Look and Feel: " + e.getMessage());
        }
    }
}
