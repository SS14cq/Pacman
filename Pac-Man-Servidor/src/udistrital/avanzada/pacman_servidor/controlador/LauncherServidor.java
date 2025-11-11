package udistrital.avanzada.pacman_servidor.controlador;

import javax.swing.UIManager;

/**
 * Clase principal del Servidor Pac-Man. Punto de entrada de la aplicación.
 *
 * @author Steban
 * @version 1.0
 */
public class LauncherServidor {

    /**
     * Método principal.
     * 
     * @param args Argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        // Instanciar el orquestador
        ControladorServidor controlador = new ControladorServidor();
        
        // Iniciar la aplicación
        controlador.iniciar();
        
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
