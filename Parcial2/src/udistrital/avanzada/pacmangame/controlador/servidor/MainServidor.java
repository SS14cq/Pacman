package udistrital.avanzada.pacmangame.controlador.servidor;



import javax.swing.*;
import udistrital.avanzada.pacmangame.vista.servidor.VentanaServidor;

/**
 * Clase principal para ejecutar el servidor del juego Pac-Man.
 * Punto de entrada de la aplicación del servidor.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class MainServidor {
    
    /**
     * Método principal que inicia la aplicación del servidor.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Establecer Look and Feel del sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Si falla, usar el Look and Feel por defecto
                    System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
                }
                
                // Crear controlador
                ControladorServidor controlador = new ControladorServidor();
                
                // Crear y mostrar vista
                VentanaServidor ventana = new VentanaServidor(controlador);
                ventana.setVisible(true);
            }
        });
    }
}