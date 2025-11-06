package udistrital.avanzada.servidor.modelo;

/*
 * Clase que representa un Jugador en el sistema.
 * Contiene las credenciales de acceso utilizadas para
 * la autenticación en el servidor.
 * 
 * Esta clase es un POJO (Plain Old Java Object) que
 * encapsula los datos de un jugador siguiendo el
 * principio de encapsulamiento de la POO.
 * 
 */


/**
 *
 * @author juanr
 */
public class Jugador {
    /** Nombre de usuario único del jugador */
    private String usuario;
    
    /** Contraseña del jugador para autenticación */
    private String contrasena;
    
    /**
     * Constructor que crea un jugador con sus credenciales.
     * 
     * @param usuario Nombre de usuario (no debe ser null ni vacío)
     * @param contrasena Contraseña del usuario (no debe ser null ni vacía)
     */
    public Jugador(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
    
    /**
     * Obtiene el nombre de usuario del jugador.
     * 
     * @return String con el nombre de usuario
     */
    public String getUsuario() { 
        return usuario; 
    }
    
    /**
     * Obtiene la contraseña del jugador.
     * 
     * @return String con la contraseña
     */
    public String getContrasena() { 
        return contrasena; 
    }
}