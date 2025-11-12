package udistrital.avanzada.pacmangame.modelo.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.pacmangame.modelo.ConexionBD;
import udistrital.avanzada.pacmangame.modelo.entidades.Jugador;

/**
 * Implementación del DAO para Jugador.
 * Maneja todas las operaciones de base de datos relacionadas con jugadores.
 * Cumple con SRP al encargarse solo de persistencia de jugadores.
 * Implementa la interfaz IJugadorDAO (Principio DIP).
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class JugadorDAOImpl implements IJugadorDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor que recibe la conexión a BD.
     * 
     * @param conexionBD Gestor de conexión a base de datos
     */
    public JugadorDAOImpl(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public boolean validarCredenciales(String nombre, String contrasena) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "SELECT COUNT(*) as total FROM jugadores WHERE nombre = ? AND contrasena = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, contrasena);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            return false;
            
        } catch (SQLException e) {
            throw new Exception("Error validando credenciales: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(rs, pstmt, conn);
        }
    }
    
    @Override
    public Jugador obtenerJugadorPorNombre(String nombre) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "SELECT * FROM jugadores WHERE nombre = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setNombre(rs.getString("nombre"));
                jugador.setContrasena(rs.getString("contrasena"));
                return jugador;
            }
            return null;
            
        } catch (SQLException e) {
            throw new Exception("Error obteniendo jugador: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(rs, pstmt, conn);
        }
    }
    
    @Override
    public boolean insertarJugador(Jugador jugador) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "INSERT INTO jugadores (nombre, contrasena) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jugador.getNombre());
            pstmt.setString(2, jugador.getContrasena());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new Exception("Error insertando jugador: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(null, pstmt, conn);
        }
    }
    
    /**
     * Obtiene todos los jugadores de la base de datos.
     * 
     * @return Lista de jugadores
     * @throws Exception Si ocurre un error en la consulta
     */
    public List<Jugador> obtenerTodosLosJugadores() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Jugador> jugadores = new ArrayList<>();
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "SELECT * FROM jugadores ORDER BY nombre";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setNombre(rs.getString("nombre"));
                jugador.setContrasena(rs.getString("contrasena"));
                jugadores.add(jugador);
            }
            
            return jugadores;
            
        } catch (SQLException e) {
            throw new Exception("Error obteniendo jugadores: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(rs, pstmt, conn);
        }
    }
    
    /**
     * Actualiza la contraseña de un jugador.
     * 
     * @param nombre Nombre del jugador
     * @param nuevaContrasena Nueva contraseña
     * @return true si se actualizó correctamente
     * @throws Exception Si ocurre un error en la actualización
     */
    public boolean actualizarContrasena(String nombre, String nuevaContrasena) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "UPDATE jugadores SET contrasena = ? WHERE nombre = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevaContrasena);
            pstmt.setString(2, nombre);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new Exception("Error actualizando contraseña: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(null, pstmt, conn);
        }
    }
    
    /**
     * Elimina un jugador de la base de datos.
     * 
     * @param nombre Nombre del jugador a eliminar
     * @return true si se eliminó correctamente
     * @throws Exception Si ocurre un error en la eliminación
     */
    public boolean eliminarJugador(String nombre) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "DELETE FROM jugadores WHERE nombre = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new Exception("Error eliminando jugador: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(null, pstmt, conn);
        }
    }
    
    /**
     * Verifica si existe un jugador con el nombre especificado.
     * 
     * @param nombre Nombre a verificar
     * @return true si existe un jugador con ese nombre
     * @throws Exception Si ocurre un error en la consulta
     */
    public boolean existeJugador(String nombre) throws Exception {
        return obtenerJugadorPorNombre(nombre) != null;
    }
    
    /**
     * Obtiene el número total de jugadores registrados.
     * 
     * @return Cantidad de jugadores
     * @throws Exception Si ocurre un error en la consulta
     */
    public int contarJugadores() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexionBD.obtenerConexion();
            String sql = "SELECT COUNT(*) as total FROM jugadores";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
            
        } catch (SQLException e) {
            throw new Exception("Error contando jugadores: " + e.getMessage(), e);
        } finally {
            cerrarRecursos(rs, pstmt, conn);
        }
    }
    
    /**
     * Cierra los recursos de base de datos de forma segura.
     * 
     * @param rs ResultSet a cerrar
     * @param pstmt PreparedStatement a cerrar
     * @param conn Connection a cerrar
     */
    private void cerrarRecursos(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            // Log error but continue closing other resources
        }
        
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            // Log error but continue closing other resources
        }
        
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // Log error
        }
    }
}