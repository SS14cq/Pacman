
package udistrital.avanzada.pacman_cliente.modelo;

/**
 * Estados posibles del cliente
 *
 * @author Steban
 * @version 1.0
 */
public enum Estado {
    
    DESCONECTADO,
    CONECTANDO,
    AUTENTICANDO,
    JUGANDO,
    ESPERANDO_INICIO,
    JUEGO_TERMINADO
}
