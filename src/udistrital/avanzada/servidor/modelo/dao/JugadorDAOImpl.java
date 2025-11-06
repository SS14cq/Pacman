/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.servidor.modelo.dao;

/**
 *
 * @author juanr
 */
import java.sql.*;
import udistrital.avanzada.servidor.modelo.conexion.ConexionDB;


public class JugadorDAOImpl implements JugadorDAO {
    
    /**
     * {@inheritDoc}
     * 
     * Implementación que realiza una consulta SQL preparada para
     * prevenir inyección SQL. Utiliza la clase ConexionDB para
     * obtener la conexión y garantiza el cierre de recursos.
     * 
     * @param usuario El nombre de usuario a validar
     * @param contrasena La contraseña a validar
     * @return true si las credenciales son válidas, false en caso contrario
     */
    @Override

public boolean validarJugador(String usuario, String contrasena) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        conn = ConexionDB.getConexion();
        String sql = "SELECT * FROM jugadores WHERE usuario = ? AND contrasena = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, usuario);
        stmt.setString(2, contrasena);
        
        // ✅ DEBUG: Imprimir query
        System.out.println("[DEBUG] Buscando usuario: " + usuario);
        
        rs = stmt.executeQuery();
        boolean encontrado = rs.next();
        
        // ✅ DEBUG: Imprimir resultado
        System.out.println("[DEBUG] Usuario encontrado: " + encontrado);
        
        return encontrado;
        
    } catch (SQLException e) {
        // ✅ DEBUG: Imprimir error
        System.err.println("[ERROR SQL] " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        ConexionDB.cerrarRecursos(conn, stmt, rs);
    }
}
}