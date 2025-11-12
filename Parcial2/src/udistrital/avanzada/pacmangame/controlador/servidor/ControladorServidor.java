package udistrital.avanzada.pacmangame.controlador.servidor;



import java.io.IOException;
import udistrital.avanzada.pacmangame.modelo.ConexionBD;
import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.archivos.GestorArchivoAleatorio;
import udistrital.avanzada.pacmangame.modelo.archivos.GestorPropiedades;
import udistrital.avanzada.pacmangame.modelo.dao.IJugadorDAO;
import udistrital.avanzada.pacmangame.modelo.dao.JugadorDAOImpl;
import udistrital.avanzada.pacmangame.modelo.entidades.RegistroJuego;
import udistrital.avanzada.pacmangame.vista.servidor.VentanaServidor;

/**
 * Controlador principal del servidor.
 * Coordina la vista, el modelo y la red del servidor.
 * Cumple con SRP al coordinar los componentes del servidor.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class ControladorServidor {
    
    private VentanaServidor vista;
    private Servidor servidor;
    private IJugadorDAO jugadorDAO;
    private GestorArchivoAleatorio gestorArchivo;
    private ControladorJuego controladorJuego;
    private GestorPropiedades propiedades;
    
    /**
     * Constructor del controlador del servidor.
     */
    public ControladorServidor() {
        this.propiedades = new GestorPropiedades();
    }
    
    /**
     * Inicializa el servidor cargando configuración y conectando BD.
     * 
     * @param rutaPropiedades Ruta del archivo de propiedades
     * @throws Exception Si ocurre un error en la inicialización
     */
    public void inicializar(String rutaPropiedades) throws Exception {
        // Cargar propiedades
        propiedades.cargarArchivo(rutaPropiedades);
        
        // Configurar conexión a BD
        String url = propiedades.obtenerPropiedad("bd.url");
        String usuario = propiedades.obtenerPropiedad("bd.usuario");
        String contrasena = propiedades.obtenerPropiedad("bd.contrasena");
        
        ConexionBD conexionBD = ConexionBD.obtenerInstancia(url, usuario, contrasena);
        
        // Verificar conexión
        if (!conexionBD.verificarConexion()) {
            throw new Exception("No se pudo conectar a la base de datos");
        }
        
        // Inicializar DAO
        jugadorDAO = new JugadorDAOImpl(conexionBD);
        
        // Inicializar gestor de archivo
        gestorArchivo = new GestorArchivoAleatorio(Constantes.ARCHIVO_REGISTROS);
        gestorArchivo.inicializarArchivo();
        
        // Obtener puerto
        int puerto = propiedades.obtenerPropiedadInt("servidor.puerto");
        
        // Crear servidor
        servidor = new Servidor(puerto, this);
        
        notificarMensaje("Servidor inicializado correctamente");
    }
    
    /**
     * Inicia el servidor.
     * 
     * @throws IOException Si ocurre un error al iniciar
     */
    public void iniciarServidor() throws IOException {
        servidor.iniciar();
    }
    
    /**
     * Detiene el servidor.
     */
    public void detenerServidor() {
        if (servidor != null) {
            servidor.detener();
        }
    }
    
    /**
     * Valida las credenciales de un jugador.
     * 
     * @param nombre Nombre del jugador
     * @param contrasena Contraseña del jugador
     * @return true si las credenciales son válidas
     */
    public boolean validarCredenciales(String nombre, String contrasena) {
        try {
            return jugadorDAO.validarCredenciales(nombre, contrasena);
        } catch (Exception e) {
            notificarError("Error validando credenciales: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inicia un juego para un jugador.
     * 
     * @param nombreJugador Nombre del jugador
     */
    public void iniciarJuego(String nombreJugador) {
        controladorJuego = new ControladorJuego(
            Constantes.ANCHO_VENTANA,
            Constantes.ALTO_VENTANA,
            nombreJugador,
            this
        );
        
        // Mostrar panel de juego en la vista
        if (vista != null) {
            vista.mostrarPanelJuego(controladorJuego.getPanelJuego());
        }
    }
    
    /**
     * Mueve Pac-Man en una dirección.
     * 
     * @param direccion Dirección del movimiento
     * @return Número de casillas movidas
     */
    public int moverPacMan(udistrital.avanzada.pacmangame.modelo.Direccion direccion) {
        if (controladorJuego != null) {
            return controladorJuego.moverPacMan(direccion);
        }
        return 0;
    }
    
    /**
     * Verifica colisión con frutas.
     * 
     * @return Mensaje con la fruta comida o null
     */
    public String verificarColision() {
        if (controladorJuego != null) {
            return controladorJuego.verificarColision();
        }
        return null;
    }
    
    /**
     * Verifica si el juego ha terminado.
     * 
     * @return true si el juego terminó
     */
    public boolean juegoTerminado() {
        if (controladorJuego != null) {
            return controladorJuego.juegoTerminado();
        }
        return false;
    }
    
    /**
     * Finaliza el juego y guarda el registro.
     * 
     * @return Registro del juego finalizado
     */
    public RegistroJuego finalizarJuego() {
        if (controladorJuego != null) {
            RegistroJuego registro = (RegistroJuego) controladorJuego.obtenerRegistroJuego(); //tener en cuenta
            
            // Guardar en archivo
            try {
                gestorArchivo.escribirRegistro(registro);
                notificarMensaje("Registro guardado: " + registro);
            } catch (IOException e) {
                notificarError("Error guardando registro: " + e.getMessage());
            }
            
            // Limpiar panel de juego
            if (vista != null) {
                vista.limpiarPanelJuego();
            }
            
            return registro;
        }
        return null;
    }
    
    /**
     * Muestra el mejor jugador del archivo.
     */
    public void mostrarMejorJugador() {
        try {
            RegistroJuego mejor = gestorArchivo.encontrarMejorJugador();
            
            if (mejor != null) {
                String mensaje = String.format(
                    "===== MEJOR JUGADOR =====\n" +
                    "Nombre: %s\n" +
                    "Puntaje: %d puntos\n" +
                    "Tiempo: %.2f segundos\n" +
                    "========================",
                    mejor.getNombreJugador(),
                    mejor.getPuntaje(),
                    mejor.getTiempoEnSegundos()
                );
                
                if (vista != null) {
                    vista.mostrarMensajeEmergente("Mejor Jugador", mensaje);
                }
            } else {
                if (vista != null) {
                    vista.mostrarMensajeEmergente("Sin Registros", 
                        "No hay registros de juegos completados.");
                }
            }
        } catch (IOException e) {
            notificarError("Error leyendo registros: " + e.getMessage());
        }
    }
    
    /**
     * Establece la vista del servidor.
     * 
     * @param vista Vista del servidor
     */
    public void setVista(VentanaServidor vista) {
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
}