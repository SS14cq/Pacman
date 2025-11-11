package udistrital.avanzada.pacman_servidor.dao;

import java.sql.SQLException;
import java.util.Map;

/**
 * Interfaz DAO para operaciones con usuarios.
 * Define el contrato para acceso a datos de usuarios.
 * Aplica Dependency Inversion Principle (SOLID-D).
 * 
 * @author Steban
 * @version 1.0
 */
public interface UsuarioDAO {
    
    /**
     * Valida las credenciales de un usuario.
     * 
     * @param nombre Nombre del usuario
     * @param password Contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     * @throws SQLException Si hay error en la consulta
     */
    boolean validarCredenciales(String nombre, String password) throws SQLException;
    
    /**
     * Carga usuarios desde un mapa al inicio del servidor.
     * Inserta usuarios que no existan en la BD.
     * 
     * @param usuarios Map con nombre y password
     * @throws SQLException Si hay error en la inserción
     */
    void cargarUsuariosDesdeProperties(Map<String, String> usuarios) throws SQLException;
    
    /**
     * Verifica si un usuario existe en la base de datos.
     * 
     * @param nombre Nombre del usuario
     * @return true si existe, false en caso contrario
     * @throws SQLException Si hay error en la consulta
     */
    boolean existeUsuario(String nombre) throws SQLException;
}