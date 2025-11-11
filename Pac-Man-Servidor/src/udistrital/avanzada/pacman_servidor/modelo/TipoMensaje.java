package udistrital.avanzada.pacman_servidor.modelo;

/**
 * Enumeración que representa los tipos de mensajes del protocolo de comunicación
 * entre cliente y servidor. Cada tipo tiene un código corto para transmisión eficiente.
 * 
 * @author Steban
 * @version 1.0
 */
public enum TipoMensaje {
    /**
     * Comando de movimiento enviado por el cliente
     */
    COMANDO("CMD"),
    
    /**
     * Mensaje de texto del servidor al cliente
     */
    MENSAJE_TEXTO("MSG"),
    
    /**
     * Frame de video enviado por el servidor
     */
    FRAME_VIDEO("FRM"),
    
    /**
     * Mensajes de autenticación (bidireccional)
     */
    AUTENTICACION("AUT"),
    
    /**
     * Resultado final del juego
     */
    RESULTADO("RES");
    
    private final String codigo;
    
    /**
     * Constructor de la enumeración.
     * 
     * @param codigo Código corto para transmisión por socket
     */
    TipoMensaje(String codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Obtiene el código del tipo de mensaje.
     * 
     * @return Código corto del mensaje
     */
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * Obtiene el TipoMensaje a partir de un código.
     * 
     * @param codigo Código a buscar
     * @return TipoMensaje correspondiente
     * @throws IllegalArgumentException si el código no existe
     */
    public static TipoMensaje fromCodigo(String codigo) {
        for (TipoMensaje tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de mensaje desconocido: " + codigo);
    }
}