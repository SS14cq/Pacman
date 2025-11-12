package udistrital.avanzada.pacman_cliente.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener para el botón de desconectar.
 * Separación de eventos según buenas prácticas MVC.
 * 
 * @author Steban
 * @version 1.0
 */
public class ListenerBotonDesconectar implements ActionListener {
    
    /**
     * Interfaz callback para notificar al orquestador.
     */
    public interface AccionDesconectar {
        /**
         * Llamado cuando se presiona el botón desconectar.
         */
        void onSolicitarDesconexion();
    }
    
    private AccionDesconectar callback;
    
    /**
     * Constructor.
     * 
     * @param callback Callback a notificar
     */
    public ListenerBotonDesconectar(AccionDesconectar callback) {
        this.callback = callback;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (callback != null) {
            callback.onSolicitarDesconexion();
        }
    }
}