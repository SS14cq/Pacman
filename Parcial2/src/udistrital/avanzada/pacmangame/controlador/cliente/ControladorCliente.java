package udistrital.avanzada.pacmangame.controlador.cliente;



import java.io.IOException;
import udistrital.avanzada.pacmangame.modelo.archivos.GestorPropiedades;
import udistrital.avanzada.pacmangame.vista.cliente.VentanaCliente;

/**
 * Controlador del cliente.
 * Coordina la vista, el modelo y la red del cliente.
 * Cumple con SRP al coordinar los componentes del cliente.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class ControladorCliente {
    
    private VentanaCliente vista;
    private Cliente cliente;
    private GestorPropiedades propiedades;
    
    /**
     * Constructor del controlador del cliente.
     */
    public ControladorCliente() {
        this.propiedades = new GestorPropiedades();
    }
    
    /**
     * Inicializa el cliente cargando configuración.
     * 
     * @param rutaPropiedades Ruta del archivo de propiedades
     * @throws IOException Si ocurre un error cargando propiedades
     */
    public void inicializar(String rutaPropiedades) throws IOException {
        propiedades.cargarArchivo(rutaPropiedades);
        
        String ip = propiedades.obtenerPropiedad("servidor.ip");
        int puerto = propiedades.obtenerPropiedadInt("servidor.puerto");
        
        cliente = new Cliente(ip, puerto, this);
        
        notificarMensaje("Cliente configurado para conectar a " + ip + ":" + puerto);
    }
    
    /**
     * Conecta el cliente al servidor.
     * 
     * @return true si la conexión fue exitosa
     */
    public boolean conectar() {
        return cliente.conectar();
    }
    
    /**
     * Desconecta el cliente del servidor.
     */
    public void desconectar() {
        if (cliente != null) {
            cliente.desconectar();
        }
    }
    
    /**
     * Envía un mensaje al servidor.
     * 
     * @param mensaje Mensaje a enviar
     */
    public void enviarMensaje(String mensaje) {
        if (cliente != null && cliente.isConectado()) {
            cliente.enviarMensaje(mensaje);
        }
    }
    
    /**
     * Solicita el usuario al cliente.
     */
    public void solicitarUsuario() {
        if (vista != null) {
            vista.solicitarUsuario();
        }
    }
    
    /**
     * Solicita la contraseña al cliente.
     */
    public void solicitarContrasena() {
        if (vista != null) {
            vista.solicitarContrasena();
        }
    }
    
    /**
     * Solicita un movimiento al cliente.
     */
    public void solicitarMovimiento() {
        if (vista != null) {
            vista.solicitarMovimiento();
        }
    }
    
    /**
     * Establece la vista del cliente.
     * 
     * @param vista Vista del cliente
     */
    public void setVista(VentanaCliente vista) {
        this.vista = vista;
    }
    
    /**
     * Notifica un mensaje a la vista.
     * 
     * @param mensaje Mensaje a notificar
     */
    public void notificarMensaje(String mensaje) {
        if (vista != null) {
            vista.agregarMensaje(mensaje);
        }
    }
    
    /**
     * Notifica un error a la vista.
     * 
     * @param error Mensaje de error
     */
    public void notificarError(String error) {
        if (vista != null) {
            vista.agregarError(error);
        }
    }
    
    /**
     * Verifica si el cliente está conectado.
     * 
     * @return true si está conectado
     */
    public boolean isConectado() {
        return cliente != null && cliente.isConectado();
    }
}