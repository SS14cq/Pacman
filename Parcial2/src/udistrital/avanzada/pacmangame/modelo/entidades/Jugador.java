package udistrital.avanzada.pacmangame.modelo.entidades;

/**
 * Clase que representa un jugador del sistema.
 * Cumple con el principio de Responsabilidad Única (SRP).
 * Solo contiene datos del jugador, sin lógica de negocio.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class Jugador {
    
    private String nombre;
    private String contrasena;
    
    /**
     * Constructor por defecto.
     */
    public Jugador() {
    }
    
    /**
     * Constructor con parámetros.
     * 
     * @param nombre Nombre del jugador
     * @param contrasena Contraseña del jugador
     */
    public Jugador(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
    
    /**
     * Obtiene el nombre del jugador.
     * 
     * @return Nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del jugador.
     * 
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la contraseña del jugador.
     * 
     * @return Contraseña del jugador
     */
    public String getContrasena() {
        return contrasena;
    }
    
    /**
     * Establece la contraseña del jugador.
     * 
     * @param contrasena Nueva contraseña
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Jugador jugador = (Jugador) obj;
        return nombre != null ? nombre.equals(jugador.nombre) : jugador.nombre == null;
    }
    
    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}