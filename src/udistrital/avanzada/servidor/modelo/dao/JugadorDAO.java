/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.servidor.modelo.dao;

/**
 *
 * @author juanr
 */
public interface JugadorDAO {
    /**
     * Valida si un jugador existe en la base de datos con las credenciales proporcionadas.
     * Realiza una consulta a la base de datos para verificar la existencia
     * del usuario y que la contraseña coincida.
     * 
     * @param usuario El nombre de usuario a validar (no debe ser null)
     * @param contrasena La contraseña a validar (no debe ser null)
     * @return true si el jugador existe y la contraseña es correcta, false en caso contrario
     */
    boolean validarJugador(String usuario, String contrasena);
}