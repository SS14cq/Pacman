package udistrital.avanzada.pacmangame.modelo.dao;

import udistrital.avanzada.pacmangame.modelo.entidades.Jugador;


/**
 * Interface para el acceso a datos de Jugador.
 * Define el contrato para las operaciones de persistencia.
 * Cumple con el Principio de Inversión de Dependencias (DIP).
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public interface IJugadorDAO {
    
    /**
     * Valida las credenciales de un jugador.
     * 
     * @param nombre Nombre del jugador
     * @param contrasena Contraseña del jugador
     * @return true si las credenciales son válidas, false en caso contrario
     * @throws Exception Si ocurre un error en la validación
     */
    boolean validarCredenciales(String nombre, String contrasena) throws Exception;
    
    /**
     * Obtiene un jugador por su nombre.
     * 
     * @param nombre Nombre del jugador
     * @return Jugador encontrado o null si no existe
     * @throws Exception Si ocurre un error en la consulta
     */
    Jugador obtenerJugadorPorNombre(String nombre) throws Exception;
    
    /**
     * Inserta un nuevo jugador en la base de datos.
     * 
     * @param jugador Jugador a insertar
     * @return true si la inserción fue exitosa
     * @throws Exception Si ocurre un error en la inserción
     */
    boolean insertarJugador(Jugador jugador) throws Exception;
}