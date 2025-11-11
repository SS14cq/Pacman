package udistrital.avanzada.pacman_servidor.controlador;

import udistrital.avanzada.pacman_servidor.vista.VentanaInicioServidor;
import udistrital.avanzada.pacman_servidor.vista.VentanaJuego;

/**
 * Controlador dedicado EXCLUSIVAMENTE a la vista.
 * NO conoce el modelo, NO toma decisiones de negocio.
 * Solo actúa como puente entre ControladorServidor y las ventanas.
 * 
 * @author 
 * @version 1.0
 */
public class ControladorVista {
    
    private VentanaInicioServidor ventanaInicio;
    private VentanaJuego ventanaJuego;
    
    // ========== VENTANA DE INICIO ==========
    
    /**
     * Crea la ventana de inicio.
     * 
     * @return VentanaInicioServidor creada
     */
    public VentanaInicioServidor crearVentanaInicio() {
        ventanaInicio = new VentanaInicioServidor();
        return ventanaInicio;
    }
    
    /**
     * Muestra la ventana de inicio.
     */
    public void mostrarVentanaInicio() {
        if (ventanaInicio != null) {
            ventanaInicio.setVisible(true);
        }
    }
    
    /**
     * Agrega un mensaje al log.
     * 
     * @param mensaje Mensaje a mostrar
     */
    public void agregarLog(String mensaje) {
        if (ventanaInicio != null) {
            ventanaInicio.agregarLog(mensaje);
        }
    }
    
    /**
     * Actualiza el estado mostrado.
     * 
     * @param estado Texto del estado
     */
    public void actualizarEstado(String estado) {
        if (ventanaInicio != null) {
            ventanaInicio.actualizarEstado(estado);
        }
    }
    
    /**
     * Habilita o deshabilita el botón de iniciar.
     * 
     * @param habilitado true para habilitar
     */
    public void habilitarBotonIniciar(boolean habilitado) {
        if (ventanaInicio != null) {
            ventanaInicio.habilitarBotonIniciar(habilitado);
        }
    }
    
    /**
     * Obtiene la ventana de inicio (para que el orquestador agregue listeners).
     * 
     * @return VentanaInicioServidor
     */
    public VentanaInicioServidor getVentanaInicio() {
        return ventanaInicio;
    }
    
    // ========== VENTANA DE JUEGO ==========
    
    /**
     * Crea la ventana de juego.
     * 
     * @param ancho Ancho de la ventana
     * @param alto Alto de la ventana
     * @return VentanaJuego creada
     */
    public VentanaJuego crearVentanaJuego(int ancho, int alto) {
        ventanaJuego = new VentanaJuego(ancho, alto);
        return ventanaJuego;
    }
    
    /**
     * Muestra la ventana de juego.
     */
    public void mostrarVentanaJuego() {
        if (ventanaJuego != null) {
            ventanaJuego.setVisible(true);
        }
    }
    
    /**
     * Oculta la ventana de juego.
     */
    public void ocultarVentanaJuego() {
        if (ventanaJuego != null) {
            ventanaJuego.setVisible(false);
            ventanaJuego.dispose();
        }
    }
    
    /**
     * Actualiza el nombre del jugador en la vista.
     * 
     * @param nombre Nombre a mostrar
     */
    public void actualizarNombreJugador(String nombre) {
        if (ventanaJuego != null) {
            ventanaJuego.actualizarJugador(nombre);
        }
    }
    
    /**
     * Actualiza el puntaje en la vista.
     * 
     * @param puntaje Puntaje a mostrar
     */
    public void actualizarPuntaje(int puntaje) {
        if (ventanaJuego != null) {
            ventanaJuego.actualizarPuntaje(puntaje);
        }
    }
    
    /**
     * Actualiza el tiempo en la vista.
     * 
     * @param segundos Tiempo en segundos
     */
    public void actualizarTiempo(long segundos) {
        if (ventanaJuego != null) {
            ventanaJuego.actualizarTiempo(segundos);
        }
    }
    
    /**
     * Actualiza las frutas restantes en la vista.
     * 
     * @param cantidad Cantidad de frutas
     */
    public void actualizarFrutas(int cantidad) {
        if (ventanaJuego != null) {
            ventanaJuego.actualizarFrutas(cantidad);
        }
    }
    
    /**
     * Solicita redibujar el panel de juego.
     */
    public void redibujarPanelJuego() {
        if (ventanaJuego != null) {
            ventanaJuego.getPanelJuego().redibujar();
        }
    }
    
    /**
     * Obtiene la ventana de juego (para que el orquestador agregue listeners).
     * 
     * @return VentanaJuego
     */
    public VentanaJuego getVentanaJuego() {
        return ventanaJuego;
    }
}