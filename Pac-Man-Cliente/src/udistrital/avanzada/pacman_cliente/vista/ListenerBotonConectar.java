package udistrital.avanzada.pacman_cliente.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener para el botón de conectar.
 * Separación de eventos según buenas prácticas MVC.
 * 
 * @author Steban
 * @version 1.0
 */
public class ListenerBotonConectar implements ActionListener {
    
    /**
     * Interfaz callback para notificar al orquestador.
     */
    public interface AccionConectar {
        /**
         * Llamado cuando se presiona el botón conectar.
         */
        void onSolicitarConexion();
    }
    
    private AccionConectar callback;
    
    /**
     * Constructor.
     * 
     * @param callback Callback a notificar
     */
    public ListenerBotonConectar(AccionConectar callback) {
        this.callback = callback;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (callback != null) {
            callback.onSolicitarConexion();
        }
    }
}