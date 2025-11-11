package udistrital.avanzada.pacman_servidor.vista;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Listener para el evento de cierre de ventana (X).
 * Maneja el cierre apropiado del servidor.
 * 
 * @author Jhon Herrera Cubides
 * @version 1.0
 */
public class ListenerCierreVentana extends WindowAdapter {
    
    private VentanaServidor ventana;
    
    /**
     * Constructor del listener.
     * 
     * @param ventana Ventana asociada
     */
    public ListenerCierreVentana(VentanaServidor ventana) {
        this.ventana = ventana;
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        // Confirmar antes de cerrar
        if (ventana.confirmarCierre()) {
            if (ventana.getControlador() != null) {
                ventana.getControlador().detenerServidor();
            } else {
                System.exit(0);
            }
        }
    }
}