package udistrital.avanzada.pacmangame.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase singleton para gestionar la conexión a la base de datos.
 * Cumple con SRP al encargarse solo de la conexión BD.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class ConexionBD {
    
    private static ConexionBD instancia;
    private String url;
    private String usuario;
    private String contrasena;
    
    /**
     * Constructor privado para patrón Singleton.
     * 
     * @param url URL de la base de datos
     * @param usuario Usuario de la base de datos
     * @param contrasena Contraseña de la base de datos
     */
    private ConexionBD(String url, String usuario, String contrasena) {
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
    
    /**
     * Obtiene la instancia única de ConexionBD.
     * 
     * @param url URL de la base de datos
     * @param usuario Usuario de la base de datos
     * @param contrasena Contraseña de la base de datos
     * @return Instancia de ConexionBD
     */
    public static ConexionBD obtenerInstancia(String url, String usuario, String contrasena) {
        if (instancia == null) {
            instancia = new ConexionBD(url, usuario, contrasena);
        }
        return instancia;
    }
    
    /**
     * Crea y retorna una conexión a la base de datos.
     * 
     * @return Conexión a la base de datos
     * @throws SQLException Si ocurre un error al conectar
     */
    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }
    
    /**
     * Verifica si la conexión es válida.
     * 
     * @return true si puede conectarse a la BD
     */
    public boolean verificarConexion() {
        try (Connection conn = obtenerConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}