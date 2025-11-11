package udistrital.avanzada.pacman_servidor.controlador;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import udistrital.avanzada.pacman_servidor.dao.*;
import udistrital.avanzada.pacman_servidor.conexion.*;
import udistrital.avanzada.pacman_servidor.modelo.*;
import udistrital.avanzada.pacman_servidor.util.ConfiguracionServidor;
import udistrital.avanzada.pacman_servidor.util.HiloEnvioFrames;
import udistrital.avanzada.pacman_servidor.vista.*;

/**
 * Hilo que atiende a un cliente conectado.
 * Gestiona autenticación, juego y cierre de sesión.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class HiloClienteServidor extends Thread {
    
    private Socket socketCliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    
    // Controladores
    private ControladorServidor controladorServidor;
    private ControladorModelo controladorModelo;
    private ControladorVista controladorVista;
    
    // Servicios
    private UsuarioDAO usuarioDAO;
    private ArchivoResultadosManager archivoManager;
    private ConfiguracionServidor config;
    
    // Estado del juego
    private String nombreJugador;
    private VentanaJuego ventanaJuego;
    private HiloEnvioFrames hiloStreaming;
    private volatile boolean juegoActivo;
    private Timer timerActualizacion;
    
    /**
     * Constructor del hilo.
     * 
     * @param socketCliente Socket del cliente conectado
     * @param controladorServidor Orquestador principal
     * @param usuarioDAO DAO para validación de usuarios
     * @param controladorModelo Controlador del modelo
     * @param controladorVista Controlador de la vista
     * @param archivoManager Gestor del archivo de resultados
     * @param config Configuración del servidor
     */
    public HiloClienteServidor(Socket socketCliente,
                               ControladorServidor controladorServidor,
                               UsuarioDAO usuarioDAO,
                               ControladorModelo controladorModelo,
                               ControladorVista controladorVista,
                               ArchivoResultadosManager archivoManager,
                               ConfiguracionServidor config) {
        this.socketCliente = socketCliente;
        this.controladorServidor = controladorServidor;
        this.usuarioDAO = usuarioDAO;
        this.controladorModelo = controladorModelo;
        this.controladorVista = controladorVista;
        this.archivoManager = archivoManager;
        this.config = config;
        this.juegoActivo = false;
    }
    
    @Override
    public void run() {
        try {
            // Inicializar streams
            entrada = new DataInputStream(socketCliente.getInputStream());
            salida = new DataOutputStream(socketCliente.getOutputStream());
            
            controladorServidor.agregarLog("Streams inicializados para cliente");
            
            // Fase 1: Autenticación
            if (!autenticar()) {
                controladorServidor.agregarLog("Cliente rechazado - autenticación fallida");
                return;
            }
            
            // Fase 2: Inicializar juego
            inicializarJuego();
            
            // Fase 3: Bucle de juego
            ejecutarBucleJuego();
            
            // Fase 4: Finalizar juego
            finalizarJuego();
            
        } catch (IOException e) {
            controladorServidor.agregarLog("Error en comunicación con cliente: " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
    }
    
    /**
     * Autentica al cliente contra la base de datos.
     * 
     * @return true si la autenticación es exitosa, false en caso contrario
     * @throws IOException Si hay error de comunicación
     */
    private boolean autenticar() throws IOException {
        // Solicitar usuario
        salida.writeUTF(TipoMensaje.AUTENTICACION.getCodigo());
        salida.writeUTF("SOLICITUD_USUARIO");
        salida.flush();
        
        String usuario = entrada.readUTF();
        controladorServidor.agregarLog("Usuario recibido: " + usuario);
        
        // Solicitar password
        salida.writeUTF(TipoMensaje.AUTENTICACION.getCodigo());
        salida.writeUTF("SOLICITUD_PASSWORD");
        salida.flush();
        
        String password = entrada.readUTF();
        
        // Validar con BD
        try {
            boolean esValido = usuarioDAO.validarCredenciales(usuario, password);
            
            if (esValido) {
                this.nombreJugador = usuario;
                
                salida.writeUTF(TipoMensaje.AUTENTICACION.getCodigo());
                salida.writeUTF("AUTENTICADO");
                salida.flush();
                
                controladorServidor.agregarLog("Cliente autenticado: " + usuario);
                return true;
            } else {
                salida.writeUTF(TipoMensaje.AUTENTICACION.getCodigo());
                salida.writeUTF("RECHAZADO");
                salida.flush();
                
                return false;
            }
            
        } catch (SQLException e) {
            controladorServidor.agregarLog("Error al validar usuario: " + e.getMessage());
            
            salida.writeUTF(TipoMensaje.AUTENTICACION.getCodigo());
            salida.writeUTF("ERROR_BD");
            salida.flush();
            
            return false;
        }
    }
    
    /**
     * Inicializa el juego para el cliente autenticado.
     * Crea ventana, modelo y hilo de streaming.
     */
    private void inicializarJuego() {
        controladorServidor.agregarLog("Inicializando juego para: " + nombreJugador);
        
        // Crear juego en el modelo
        int ancho = config.getAnchoVentana();
        int alto = config.getAltoVentana();
        controladorModelo.crearJuego(ancho, alto);
        controladorModelo.inicializarJuego(nombreJugador);
        
        // Crear ventana de juego
        ventanaJuego = controladorVista.crearVentanaJuego(ancho, alto);
        
        // Configurar dibujado
        PanelJuego panel = ventanaJuego.getPanelJuego();
        controladorServidor.configurarDibujadoJuego(panel);
        
        // Configurar botón salir (aunque el cliente no lo usará)
        controladorServidor.configurarBotonSalirJuego(ventanaJuego);
        
        // Actualizar estadísticas iniciales
        controladorVista.actualizarNombreJugador(nombreJugador);
        controladorVista.actualizarPuntaje(0);
        controladorVista.actualizarTiempo(0);
        controladorVista.actualizarFrutas(4);
        
        // Mostrar ventana
        controladorVista.mostrarVentanaJuego();
        
        // Iniciar streaming de video
        iniciarStreaming();
        
        // Timer para actualizar estadísticas
        iniciarTimerActualizacion();
        
        juegoActivo = true;
        
        controladorServidor.agregarLog("Juego iniciado para: " + nombreJugador);
    }
    
    /**
     * Inicia el hilo de streaming de video.
     */
    private void iniciarStreaming() {
        PanelJuego panel = ventanaJuego.getPanelJuego();
        int fps = config.getStreamingFPS();
        float calidad = config.getCalidadJPEG();
        
        hiloStreaming = new HiloEnvioFrames(salida, panel, fps, calidad);
        hiloStreaming.start();
        
        controladorServidor.agregarLog("Streaming iniciado: " + fps + " FPS");
    }
    
    /**
     * Inicia un timer para actualizar estadísticas periódicamente.
     */
    private void iniciarTimerActualizacion() {
        timerActualizacion = new Timer(1000, e -> {
            if (juegoActivo) {
                actualizarEstadisticas();
            }
        });
        timerActualizacion.start();
    }
    
    /**
     * Actualiza las estadísticas en la ventana de juego.
     */
    private void actualizarEstadisticas() {
        int puntaje = controladorModelo.obtenerPuntaje();
        long tiempo = controladorModelo.obtenerTiempo();
        int frutas = controladorModelo.obtenerFrutasRestantes();
        
        controladorVista.actualizarPuntaje(puntaje);
        controladorVista.actualizarTiempo(tiempo);
        controladorVista.actualizarFrutas(frutas);
    }
    
    /**
     * Bucle principal del juego.
     * Recibe comandos del cliente y procesa movimientos.
     * 
     * @throws IOException Si hay error de comunicación
     */
    private void ejecutarBucleJuego() throws IOException {
        controladorServidor.agregarLog("Entrando al bucle de juego");
        
        while (juegoActivo && !controladorModelo.juegoTerminado()) {
            try {
                // Leer tipo de mensaje
                String tipoStr = entrada.readUTF();
                TipoMensaje tipo = TipoMensaje.fromCodigo(tipoStr);
                
                if (tipo == TipoMensaje.COMANDO) {
                    procesarComando();
                }
                
            } catch (IOException e) {
                controladorServidor.agregarLog("Cliente desconectado");
                juegoActivo = false;
                break;
            }
        }
        
        controladorServidor.agregarLog("Juego terminado para: " + nombreJugador);
    }
    
    /**
     * Procesa un comando de movimiento del cliente.
     * 
     * @throws IOException Si hay error al enviar respuesta
     */
    private void procesarComando() throws IOException {
        // Leer dirección
        String comandoStr = entrada.readUTF();
        
        try {
            // Parsear dirección
            Direccion direccion = controladorModelo.parsearDireccion(comandoStr);
            
            // Mover Pac-Man
            ResultadoMovimiento resultado = controladorModelo.moverPacMan(direccion);
            
            // Redibujar
            controladorVista.redibujarPanelJuego();
            
            // Enviar resultado al cliente
            enviarResultadoMovimiento(resultado);
            
            // Actualizar estadísticas
            actualizarEstadisticas();
            
        } catch (IllegalArgumentException e) {
            // Comando inválido
            enviarMensajeTexto("COMANDO_INVALIDO");
        }
    }
    
    /**
     * Envía el resultado de un movimiento al cliente.
     * 
     * @param resultado Resultado del movimiento
     * @throws IOException Si hay error al enviar
     */
    private void enviarResultadoMovimiento(ResultadoMovimiento resultado) throws IOException {
        StringBuilder mensaje = new StringBuilder();
        
        // Información sobre límite
        if (resultado.isLimiteAlcanzado()) {
            mensaje.append("LIMITE_ALCANZADO");
            int casillas = resultado.getCasillasMovidas();
            if (casillas > 0) {
                mensaje.append("|Movido ").append(casillas).append(" casillas");
            }
        } else {
            mensaje.append("MOVIMIENTO_OK|4 casillas");
        }
        
        enviarMensajeTexto(mensaje.toString());
        
        // Información sobre fruta
        if (resultado.isFrutaComida()) {
            String mensajeFruta = String.format("FRUTA_COMIDA|%s|%d|Puntaje Total: %d",
                resultado.getNombreFruta(),
                resultado.getPuntosGanados(),
                controladorModelo.obtenerPuntaje());
            
            enviarMensajeTexto(mensajeFruta);
        }
    }
    
    /**
     * Envía un mensaje de texto al cliente.
     * 
     * @param mensaje Mensaje a enviar
     * @throws IOException Si hay error al enviar
     */
    private void enviarMensajeTexto(String mensaje) throws IOException {
        synchronized (salida) {
            salida.writeUTF(TipoMensaje.MENSAJE_TEXTO.getCodigo());
            salida.writeUTF(mensaje);
            salida.flush();
        }
    }
    
    /**
     * Finaliza el juego y guarda resultados.
     */
    private void finalizarJuego() {
        juegoActivo = false;
        
        // Detener timer
        if (timerActualizacion != null) {
            timerActualizacion.stop();
        }
        
        // Detener streaming
        if (hiloStreaming != null) {
            hiloStreaming.detener();
        }
        
        // Obtener datos finales
        int puntaje = controladorModelo.obtenerPuntaje();
        long tiempo = controladorModelo.obtenerTiempo();
        
        // Guardar en archivo
        try {
            RegistroJuego registro = new RegistroJuego(nombreJugador, puntaje, tiempo);
            archivoManager.guardarResultado(registro);
            
            controladorServidor.agregarLog("Resultado guardado: " + registro);
            
            // Enviar resultado final al cliente
            String mensajeFinal = String.format("JUEGO_TERMINADO|%s|%d|%d",
                nombreJugador, puntaje, tiempo);
            
            salida.writeUTF(TipoMensaje.RESULTADO.getCodigo());
            salida.writeUTF(mensajeFinal);
            salida.flush();
            
        } catch (IOException e) {
            controladorServidor.agregarLog("Error al guardar resultado: " + e.getMessage());
        }
        
        // Ocultar ventana de juego
        try {
            Thread.sleep(2000); // Dar tiempo para ver resultado
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        controladorVista.ocultarVentanaJuego();
    }
    
    /**
     * Cierra todos los recursos (streams, socket).
     */
    private void cerrarRecursos() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socketCliente != null) socketCliente.close();
            
            controladorServidor.agregarLog("Recursos liberados para: " + nombreJugador);
            
        } catch (IOException e) {
            controladorServidor.agregarLog("Error al cerrar recursos");
        }
    }
}