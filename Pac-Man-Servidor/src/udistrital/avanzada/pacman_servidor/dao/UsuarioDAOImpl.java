package udistrital.avanzada.pacman_servidor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import udistrital.avanzada.pacman_servidor.conexion.ConexionBD;

/**
 * Implementación del DAO para usuarios.
 * Gestiona todas las operaciones de BD relacionadas con usuarios.
 * 
 * @author Steban
 * @version 1.0
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor que recibe la conexión.
     * Inyección de dependencias.
     * 
     * @param conexionBD Instancia de ConexionBD
     */
    public UsuarioDAOImpl(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public boolean validarCredenciales(String nombre, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre = ? AND password = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void cargarUsuariosDesdeProperties(Map<String, String> usuarios) throws SQLException {
        for (Map.Entry<String, String> entry : usuarios.entrySet()) {
            String nombre = entry.getKey();
            String password = entry.getValue();
            
            // Solo insertar si no existe
            if (!existeUsuario(nombre)) {
                insertarUsuario(nombre, password);
            }
        }
    }
    
    @Override
    public boolean existeUsuario(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Inserta un nuevo usuario en la base de datos.
     * Método privado de ayuda.
     * 
     * @param nombre Nombre del usuario
     * @param password Contraseña del usuario
     * @throws SQLException Si hay error en la inserción
     */
    private void insertarUsuario(String nombre, String password) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, password) VALUES (?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
    }
}