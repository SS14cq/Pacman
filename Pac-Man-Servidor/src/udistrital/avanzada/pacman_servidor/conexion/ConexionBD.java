package udistrital.avanzada.pacman_servidor.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión única a la base de datos MySQL.
 * Implementa patrón Singleton para evitar múltiples conexiones.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class ConexionBD {
    
    private static ConexionBD instancia;
    private Connection conexion;
    private String url;
    private String usuario;
    private String password;
    
    /**
     * Constructor privado (Singleton).
     * 
     * @param url URL de conexión JDBC
     * @param usuario Usuario de la BD
     * @param password Contraseña de la BD
     * @param driver Clase del driver JDBC
     * @throws SQLException Si falla la conexión
     * @throws ClassNotFoundException Si no encuentra el driver
     */
    private ConexionBD(String url, String usuario, String password, String driver) 
            throws SQLException, ClassNotFoundException {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        
        // Cargar driver de MySQL
        Class.forName(driver);
        
        // Establecer conexión
        this.conexion = DriverManager.getConnection(url, usuario, password);
    }
    
    /**
     * Obtiene la instancia única de ConexionBD.
     * 
     * @param url URL de conexión
     * @param usuario Usuario de BD
     * @param password Password de BD
     * @param driver Driver JDBC
     * @return Instancia de ConexionBD
     * @throws SQLException Si falla la conexión
     * @throws ClassNotFoundException Si no encuentra el driver
     */
    public static ConexionBD getInstancia(String url, String usuario, String password, String driver) 
            throws SQLException, ClassNotFoundException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD(url, usuario, password, driver);
        }
        return instancia;
    }
    
    /**
     * Obtiene la conexión activa.
     * Verifica que esté abierta y la reconecta si es necesario.
     * 
     * @return Connection activa
     * @throws SQLException Si hay error de conexión
     */
    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(url, usuario, password);
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión a la base de datos.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si la conexión está activa.
     * 
     * @return true si está conectado, false en caso contrario
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}