package udistrital.avanzada.pacman_servidor.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener para el botón de salir del servidor.
 * Separa el manejo de eventos según las buenas prácticas de MVC.
 * 
 * @author Steban
 * @version 1.0
 */
public class ListenerBotonSalir implements ActionListener {
    
    private VentanaServidor ventana;
    
    /**
     * Constructor del listener.
     * 
     * @param ventana Ventana asociada
     */
    public ListenerBotonSalir(VentanaServidor ventana) {
        this.ventana = ventana;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Confirmar antes de cerrar
        if (ventana.confirmarCierre()) {
            ventana.getControlador().detenerServidor();
        }
    }
}