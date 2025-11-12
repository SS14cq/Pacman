package udistrital.avanzada.pacman_cliente.vista;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Listener para la tecla Enter en el campo de texto.
 * Permite enviar comandos con Enter.
 * 
 * @author Steban
 * @version 1.0
 */
public class ListenerTeclaEnter extends KeyAdapter {
    
    /**
     * Interfaz callback para notificar al orquestador.
     */
    public interface AccionEnter {
        /**
         * Llamado cuando se presiona Enter.
         */
        void onEnterPresionado();
    }
    
    private AccionEnter callback;
    
    /**
     * Constructor.
     * 
     * @param callback Callback a notificar
     */
    public ListenerTeclaEnter(AccionEnter callback) {
        this.callback = callback;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (callback != null) {
                callback.onEnterPresionado();
            }
        }
    }
}