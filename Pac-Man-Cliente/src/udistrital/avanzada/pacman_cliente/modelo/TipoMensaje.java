package udistrital.avanzada.pacman_cliente.modelo;

/**
 * Tipos de mensajes del protocolo cliente-servidor.
 * DEBE ser idéntico al del servidor.
 * 
 * @author Steban
 * @version 1.0
 */
public enum TipoMensaje {
    COMANDO("CMD"),
    MENSAJE_TEXTO("MSG"),
    FRAME_VIDEO("FRAME_VIDEO"),
    AUTENTICACION("AUTH"),
    RESULTADO("RES");
    
    private final String codigo;
    
    TipoMensaje(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public static TipoMensaje fromCodigo(String codigo) {
        for (TipoMensaje tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código desconocido: " + codigo);
    }
}