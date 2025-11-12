package udistrital.avanzada.pacman_cliente.control;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del Cliente Pac-Man.
 * Punto de entrada de la aplicación.
 * 
 * RESPONSABILIDAD:
 * - Configurar Look and Feel
 * - Inicializar el orquestador en el EDT
 * - Manejar excepciones no capturadas
 * 
 * @author Steban
 * @version 1.0
 */
public class MainCliente {
    
    /**
     * Método principal que inicia el cliente.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Configurar manejador de excepciones no capturadas
        configurarManejadorExcepciones();
        
        // Configurar Look and Feel del sistema
        configurarLookAndFeel();
        
        // Iniciar aplicación en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear e inicializar el orquestador
                // El orquestador inicializa todo el sistema
                new ControladorPrincipal();
                
            } catch (Exception e) {
                System.err.println("Error fatal al iniciar el cliente:");
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    /**
     * Configura el Look and Feel para que use el del sistema operativo.
     */
    private static void configurarLookAndFeel() {
        try {
            // Usar el Look and Feel del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            // Si falla, usar el por defecto de Java
            System.err.println("No se pudo configurar Look and Feel: " + e.getMessage());
            System.err.println("Usando Look and Feel por defecto");
        }
    }
    
    /**
     * Configura un manejador global para excepciones no capturadas.
     * Útil para debugging y logging.
     */
    private static void configurarManejadorExcepciones() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Excepción no capturada en thread: " + thread.getName());
            throwable.printStackTrace();
            
            // Mostrar diálogo de error si es posible
            try {
                SwingUtilities.invokeLater(() -> {
                    javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Error fatal: " + throwable.getMessage() + "\n" +
                        "La aplicación se cerrará.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                });
            } catch (Exception e) {
                // Si falla el diálogo, solo imprimir
                System.err.println("No se pudo mostrar diálogo de error");
            }
        });
    }
}