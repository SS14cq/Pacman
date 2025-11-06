/*
* Clase utilitaria para gestionar conexiones a la base de datos MySQL.
 * Implementa el patrón Singleton implícito para la configuración
 * y proporciona métodos estáticos para obtener conexiones.
 * 
 * Esta clase centraliza toda la lógica de conexión a la base de datos,
 * siguiendo el principio de responsabilidad única (SRP).
 * 
 */
package udistrital.avanzada.servidor.modelo.conexion;

/**
 *
 * @author juanr
 */
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConexionDB {
    /** URL de conexión a la base de datos */
    private static String URL;
    
    /** Usuario de la base de datos */
    private static String USUARIO;
    
    /** Contraseña de la base de datos */
    private static String CONTRASENA;
    
    /**
     * Constructor privado para prevenir instanciación.
     * Esta clase solo debe usarse a través de sus métodos estáticos.
     */
    private ConexionDB() {
        // Constructor privado para clase utilitaria
    }
    
    /**
     * Carga las propiedades de conexión desde un archivo .properties.
     * Este método debe invocarse antes de intentar obtener conexiones.
     * 
     * Las propiedades esperadas son:
     * - db.url: URL JDBC de la base de datos
     * - db.usuario: Usuario de MySQL
     * - db.contrasena: Contraseña de MySQL
     * 
     * @param rutaProperties Ruta completa al archivo de propiedades
     * @throws RuntimeException Si el archivo no existe o hay error de lectura
     */
    public static void cargarPropiedades(String rutaProperties) {
        Properties props = new Properties();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(rutaProperties);
            props.load(fis);
            URL = props.getProperty("db.url");
            USUARIO = props.getProperty("db.usuario");
            CONTRASENA = props.getProperty("db.contrasena");
            
            // Validar que se cargaron todas las propiedades requeridas
            if (URL == null || USUARIO == null || CONTRASENA == null) {
                throw new RuntimeException("Faltan propiedades requeridas en el archivo");
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error cargando propiedades de BD: " + e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // Log del error pero no lanzar excepción en finally
                }
            }
        }
    }
    
    /**
     * Obtiene una nueva conexión a la base de datos.
     * Antes de invocar este método, debe haberse llamado a cargarPropiedades().
     * 
     * @return Conexión activa a la base de datos
     * @throws SQLException Si hay error al establecer la conexión
     * @throws RuntimeException Si no se han cargado las propiedades
     */
    public static Connection getConexion() throws SQLException {
        if (URL == null || USUARIO == null || CONTRASENA == null) {
            throw new RuntimeException("Propiedades de BD no cargadas. Invocar cargarPropiedades() primero.");
        }
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
    
    /**
     * Cierra de forma segura los recursos de base de datos.
     * Este método no lanza excepciones, solo las registra.
     * Puede recibir valores null sin problemas.
     * 
     * @param conn Conexión a cerrar (puede ser null)
     * @param stmt Statement a cerrar (puede ser null)
     * @param rs ResultSet a cerrar (puede ser null)
     */
    public static void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            // Log del error sin interrumpir el cierre de otros recursos
        }
        
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            // Log del error
        }
        
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // Log del error
        }
    }
}