package udistrital.avanzada.pacman_cliente.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener para el botón de enviar comando.
 * Separación de eventos según buenas prácticas MVC.
 * 
 * NO tiene lógica de negocio.
 * Solo notifica al orquestador a través de un callback.
 * 
 * @author Steban
 * @version 1.0
 */
public class ListenerBotonEnviar implements ActionListener {
    
    /**
     * Interfaz callback para notificar al orquestador.
     */
    public interface AccionEnviar {
        /**
         * Llamado cuando se presiona el botón enviar.
         */
        void onEnviarComando();
    }
    
    private AccionEnviar callback;
    
    /**
     * Constructor.
     * 
     * @param callback Callback a notificar
     */
    public ListenerBotonEnviar(AccionEnviar callback) {
        this.callback = callback;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (callback != null) {
            callback.onEnviarComando();
        }
    }
}