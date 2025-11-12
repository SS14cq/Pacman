package udistrital.avanzada.pacman_cliente.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import udistrital.avanzada.pacman_cliente.modelo.ConexionServidor;
import udistrital.avanzada.pacman_cliente.modelo.Estado;
import udistrital.avanzada.pacman_cliente.util.ConfiguracionCliente;
import udistrital.avanzada.pacman_cliente.util.DecodificadorFrames;
import udistrital.avanzada.pacman_cliente.vista.ListenerBotonConectar;
import udistrital.avanzada.pacman_cliente.vista.ListenerBotonDesconectar;
import udistrital.avanzada.pacman_cliente.vista.ListenerBotonEnviar;
import udistrital.avanzada.pacman_cliente.vista.ListenerTeclaEnter;

/**
 * ORQUESTADOR PRINCIPAL - COLUMNA VERTEBRAL DEL CLIENTE.
 *
 * RESPONSABILIDAD CRÍTICA: - TODA la información fluye a través de esta clase -
 * Coordina comunicación entre ControladorVista y ControladorModelo - Implementa
 * la lógica del protocolo de comunicación - Maneja el flujo de estados del
 * cliente
 *
 * ARQUITECTURA: - NO accede directamente a Vista ni Modelo - USA
 * ControladorVista para actualizar GUI - USA ControladorModelo para
 * consultar/modificar estado - Implementa callbacks de ConexionServidor
 * (MensajeListener) - Implementa callbacks de Listeners de Vista
 *
 * PATRÓN: Mediator + Orchestrator
 *
 * @author Steban
 * @version 1.0
 */
public class ControladorPrincipal implements
        ConexionServidor.MensajeListener,
        ListenerBotonConectar.AccionConectar,
        ListenerBotonDesconectar.AccionDesconectar,
        ConexionServidor.FrameListener,
        ListenerBotonEnviar.AccionEnviar,
        ListenerTeclaEnter.AccionEnter {

    // ========== CONTROLADORES INTERMEDIARIOS ==========
    private ControladorVista controladorVista;
    private ControladorModelo controladorModelo;

    // ========== UTILIDADES ==========
    private ConfiguracionCliente configuracion;

    // ========== VARIABLES DE PROTOCOLO ==========
    private boolean esperandoUsuario;
    private boolean esperandoPassword;
    private boolean esperandoMovimiento;

    private DecodificadorFrames decodificador;
    private long contadorFrames;
    private long ultimoReporteFPS;
    private int framesRecibidos;

    /**
     * Constructor. Inicializa el sistema completo.
     */
    public ControladorPrincipal() {
        inicializar();
    }

    /**
     * Inicializa todos los componentes del sistema.
     */
    private void inicializar() {
        // 1. Crear controladores intermediarios
        controladorVista = new ControladorVista();
        controladorModelo = new ControladorModelo();

        // 2. Configurar modelo para que notifique a este orquestador
        controladorModelo.setMensajeListener(this);

        // 3. Esperar a que la ventana esté lista
        esperarVentanaLista();

        // 4. Registrar listeners de eventos
        registrarListeners();

        // 5. Mostrar mensaje inicial
        controladorVista.mostrarInfo("Cliente Pac-Man iniciado");
        controladorVista.mostrarInfo("Presione 'Conectar' para iniciar");

        // 6. Inicializar variables de protocolo
        reiniciarProtocolo();

        // 7. Inicializar decodificador de frames
        decodificador = new DecodificadorFrames();
        contadorFrames = 0;
        ultimoReporteFPS = System.currentTimeMillis();
        framesRecibidos = 0;

    }

    /**
     * Espera a que la ventana esté completamente inicializada.
     */
    private void esperarVentanaLista() {
        try {
            Thread.sleep(1000); // Pequeña espera para asegurar EDT
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Registra todos los listeners en los componentes de la vista.
     */
    private void registrarListeners() {
        // Obtener ventana a través del controlador
        var ventana = controladorVista.getVentana();

        // Registrar listeners con callbacks a este orquestador
        ventana.getBtnConectar().addActionListener(
                new ListenerBotonConectar(this)
        );

        ventana.getBtnDesconectar().addActionListener(
                new ListenerBotonDesconectar(this)
        );

        ventana.getBtnEnviar().addActionListener(
                new ListenerBotonEnviar(this)
        );

        ventana.getTxtComando().addKeyListener(
                new ListenerTeclaEnter(this)
        );
    }

    /**
     * Reinicia las variables del protocolo de comunicación.
     */
    private void reiniciarProtocolo() {
        esperandoUsuario = false;
        esperandoPassword = false;
        esperandoMovimiento = false;
    }

    // ========== IMPLEMENTACIÓN: AccionConectar ==========
    @Override
    public void onSolicitarConexion() {
        try {
            // 1. Solicitar archivo de configuración
            controladorVista.mostrarInfo("Seleccione el archivo de configuración");
            File archivo = controladorVista.solicitarArchivoProperties();

            if (archivo == null) {
                controladorVista.mostrarError("Operación cancelada");
                return;
            }

            // 2. Cargar configuración
            configuracion = new ConfiguracionCliente();
            configuracion.cargarDesdeArchivo(archivo);

            controladorVista.mostrarInfo("Configuración cargada: "
                    + configuracion.obtenerResumen());

            // 3. Conectar al servidor
            String ip = configuracion.getServidorIP();
            int puerto = configuracion.getServidorPuerto();

            controladorVista.mostrarInfo("Conectando a " + ip + ":" + puerto + "...");

            // Actualizar estado visual
            controladorModelo.cambiarEstado(Estado.CONECTANDO);
            controladorVista.actualizarEstado(Estado.CONECTANDO);

            // Conectar (asíncrono, las respuestas llegan por callback)
            controladorModelo.conectarAlServidor(ip, puerto);

            // Conectar (asíncrono, las respuestas llegan por callback)
            controladorModelo.conectarAlServidor(ip, puerto);

            // AGREGAR ESTAS LÍNEAS:
            // Configurar listener de frames
            controladorModelo.setFrameListener(this);

            controladorVista.mostrarInfo("Conexión establecida");

            controladorVista.mostrarInfo("Conexión establecida");
            controladorVista.mostrarInfo("Esperando solicitud del servidor...");

        } catch (IOException e) {
            controladorVista.mostrarError("Error de conexión: " + e.getMessage());
            controladorModelo.cambiarEstado(Estado.DESCONECTADO);
            controladorVista.actualizarEstado(Estado.DESCONECTADO);
        } catch (Exception e) {
            controladorVista.mostrarError("Error: " + e.getMessage());
            controladorModelo.cambiarEstado(Estado.DESCONECTADO);
            controladorVista.actualizarEstado(Estado.DESCONECTADO);
        }
    }

    /**
     * Implementación de FrameListener. Recibe frames de video y los visualiza.
     */
    @Override
    public void onFrameRecibido(byte[] frameBytes) {
        try {
            // Decodificar JPEG a imagen
            BufferedImage imagen = decodificador.decodificarJPEG(frameBytes);

            // Actualizar panel de video
            controladorVista.actualizarFrameVideo(imagen);

            // Estadísticas de FPS (opcional, para debugging)
            actualizarEstadisticasFPS();

        } catch (IOException e) {
            // Error al decodificar, ignorar este frame
            // (no mostrar error para evitar spam en la consola)
        }
    }

    /**
     * Actualiza estadísticas de FPS (frames por segundo). Solo para monitoreo,
     * se puede comentar en producción.
     */
    private void actualizarEstadisticasFPS() {
        framesRecibidos++;
        contadorFrames++;

        long ahora = System.currentTimeMillis();
        long transcurrido = ahora - ultimoReporteFPS;

        // Reportar cada 2 segundos
        if (transcurrido >= 2000) {
            double fps = (framesRecibidos * 1000.0) / transcurrido;
            controladorVista.mostrarInfo(String.format("FPS: %.1f (Total frames: %d)",
                    fps, contadorFrames));

            framesRecibidos = 0;
            ultimoReporteFPS = ahora;
        }
    }

    // ========== IMPLEMENTACIÓN: AccionDesconectar ==========
    @Override
    public void onSolicitarDesconexion() {
        try {
            controladorVista.mostrarInfo("Desconectando del servidor...");

            // Desconectar
            controladorModelo.desconectar();

            // Actualizar estado
            controladorVista.actualizarEstado(Estado.DESCONECTADO);
            controladorVista.mostrarInfo("Desconectado");

            // Reiniciar protocolo
            reiniciarProtocolo();

        } catch (Exception e) {
            controladorVista.mostrarError("Error al desconectar: " + e.getMessage());
        }
    }

    // ========== IMPLEMENTACIÓN: AccionEnviar y AccionEnter ==========
    @Override
    public void onEnviarComando() {
        procesarComando();
    }

    @Override
    public void onEnterPresionado() {
        procesarComando();
    }

    /**
     * Procesa el comando ingresado según el contexto actual.
     */
    private void procesarComando() {
        String comando = controladorVista.obtenerComando();

        if (comando.isEmpty()) {
            return;
        }

        try {
            // Determinar qué hacer según el estado del protocolo
            if (esperandoUsuario) {
                enviarUsuario(comando);
            } else if (esperandoPassword) {
                enviarPassword(comando);
            } else if (esperandoMovimiento) {
                enviarMovimiento(comando);
            } else {
                controladorVista.mostrarError("No se esperaba entrada en este momento");
            }

            // Limpiar campo de comando
            controladorVista.limpiarComando();

        } catch (IOException e) {
            controladorVista.mostrarError("Error al enviar: " + e.getMessage());
        }
    }

    /**
     * Envía el nombre de usuario al servidor.
     *
     * @param usuario Nombre de usuario
     * @throws IOException Si hay error de envío
     */
    private void enviarUsuario(String usuario) throws IOException {
        controladorVista.mostrarInfo("Enviando usuario: " + usuario);
        controladorModelo.enviarMensaje(usuario);
        esperandoUsuario = false;
    }

    /**
     * Envía la contraseña al servidor.
     *
     * @param password Contraseña
     * @throws IOException Si hay error de envío
     */
    private void enviarPassword(String password) throws IOException {
        controladorVista.mostrarInfo("Enviando contraseña: " + "*".repeat(password.length()));
        controladorModelo.enviarMensaje(password);
        esperandoPassword = false;
    }

    /**
     * Envía un movimiento al servidor. Valida que sea un comando válido.
     *
     * @param movimiento Comando de movimiento
     * @throws IOException Si hay error de envío
     */
    private void enviarMovimiento(String movimiento) throws IOException {
        // Validar comando
        String comandoUpper = movimiento.toUpperCase();

        if (!esComandoValido(comandoUpper)) {
            controladorVista.mostrarError("Comando inválido. Use: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
            return;
        }

        controladorVista.mostrarInfo("Enviando comando: " + comandoUpper);
        controladorModelo.enviarMensaje(comandoUpper);
        esperandoMovimiento = false;

        // Deshabilitar campo temporalmente
        controladorVista.habilitarComando(false);
    }

    /**
     * Verifica si un comando es válido.
     *
     * @param comando Comando a validar
     * @return true si es válido
     */
    private boolean esComandoValido(String comando) {
        return comando.equals("ARRIBA")
                || comando.equals("ABAJO")
                || comando.equals("IZQUIERDA")
                || comando.equals("DERECHA");
    }

    // ========== IMPLEMENTACIÓN: MensajeListener (Mensajes del Servidor) ==========
    @Override
    public void onMensajeRecibido(String mensaje) {
        // TODA la información del servidor pasa por aquí
        procesarMensajeServidor(mensaje);
    }

    @Override
    public void onError(String error) {
        controladorVista.mostrarError("Error de conexión: " + error);

        // Desconectar y actualizar estado
        controladorModelo.cambiarEstado(Estado.DESCONECTADO);
        controladorVista.actualizarEstado(Estado.DESCONECTADO);
        reiniciarProtocolo();
    }

    @Override
    public void onConexionCerrada() {
        controladorVista.mostrarInfo("Conexión cerrada por el servidor");

        // Actualizar estado
        controladorModelo.cambiarEstado(Estado.DESCONECTADO);
        controladorVista.actualizarEstado(Estado.DESCONECTADO);
        reiniciarProtocolo();
    }

    /**
     * Procesa un mensaje recibido del servidor. LÓGICA CENTRAL DEL PROTOCOLO.
     *
     * @param mensaje Mensaje del servidor
     */
    private void procesarMensajeServidor(String mensaje) {
        controladorVista.mostrarMensaje(mensaje);

        // Interpretar comandos del servidor
        switch (mensaje) {
            case "SOLICITAR_USUARIO":
                manejarSolicitudUsuario();
                break;

            case "SOLICITAR_PASSWORD":
                manejarSolicitudPassword();
                break;

            case "AUTENTICACION_EXITOSA":
                manejarAutenticacionExitosa();
                break;

            case "AUTENTICACION_FALLIDA":
                manejarAutenticacionFallida();
                break;

            case "JUEGO_INICIADO":
                manejarJuegoIniciado();
                break;

            case "SOLICITAR_MOVIMIENTO":
                manejarSolicitudMovimiento();
                break;

            case "JUEGO_TERMINADO":
                manejarJuegoTerminado();
                break;

            case "ERROR_SERVIDOR":
                manejarErrorServidor();
                break;

            default:
                // Mensaje informativo o resultado de movimiento
                procesarMensajeInformativo(mensaje);
                break;
        }
    }

    /**
     * Maneja solicitud de usuario del servidor.
     */
    private void manejarSolicitudUsuario() {
        controladorModelo.cambiarEstado(Estado.AUTENTICANDO);
        controladorVista.actualizarEstado(Estado.AUTENTICANDO);

        controladorVista.mostrarInfo("Ingrese su nombre de usuario:");
        esperandoUsuario = true;
        controladorVista.habilitarComando(true);
    }

    /**
     * Maneja solicitud de contraseña del servidor.
     */
    private void manejarSolicitudPassword() {
        controladorVista.mostrarInfo("Ingrese su contraseña:");
        esperandoPassword = true;
        controladorVista.habilitarComando(true);
    }

    /**
     * Maneja autenticación exitosa.
     */
    private void manejarAutenticacionExitosa() {
        String usuario = controladorVista.obtenerComando(); // Último ingresado
        controladorModelo.establecerAutenticacion(usuario);

        controladorVista.mostrarInfo("¡Autenticación exitosa!");
        controladorVista.actualizarNombreJugador(usuario);
    }

    /**
     * Maneja autenticación fallida.
     */
    private void manejarAutenticacionFallida() {
        controladorVista.mostrarError("Autenticación fallida. Desconectando...");

        // Desconectar automáticamente
        controladorModelo.desconectar();
        controladorVista.actualizarEstado(Estado.DESCONECTADO);
        reiniciarProtocolo();
    }

    /**
     * Maneja inicio del juego.
     */
    private void manejarJuegoIniciado() {
        controladorModelo.cambiarEstado(Estado.JUGANDO);
        controladorVista.actualizarEstado(Estado.JUGANDO);

        controladorVista.mostrarInfo("=== JUEGO INICIADO ===");
        controladorVista.mostrarInfo("Comandos: ARRIBA, ABAJO, IZQUIERDA, DERECHA");
        
        controladorVista.mostrarPanelVideo(true);
    }

    /**
     * Maneja solicitud de movimiento.
     */
    private void manejarSolicitudMovimiento() {
        esperandoMovimiento = true;
        controladorVista.habilitarComando(true);
    }

    /**
     * Maneja fin del juego.
     */
    private void manejarJuegoTerminado() {
        controladorModelo.cambiarEstado(Estado.JUEGO_TERMINADO);
        controladorVista.actualizarEstado(Estado.JUEGO_TERMINADO);

        controladorVista.mostrarInfo("=== JUEGO TERMINADO ===");
        esperandoMovimiento = false;
        
        controladorVista.mostrarPanelVideo(false);
    }

    /**
     * Maneja error del servidor.
     */
    private void manejarErrorServidor() {
        controladorVista.mostrarError("El servidor reportó un error");

        // Desconectar
        controladorModelo.desconectar();
        controladorVista.actualizarEstado(Estado.DESCONECTADO);
        reiniciarProtocolo();
    }

    /**
     * Procesa mensajes informativos (resultados de movimientos, etc). Extrae
     * información de puntaje, tiempo y frutas si está presente.
     *
     * @param mensaje Mensaje informativo
     */
    private void procesarMensajeInformativo(String mensaje) {
        // Intentar extraer información del juego
        try {
            if (mensaje.contains("Puntaje total:")) {
                extraerInfoJuego(mensaje);
            }
        } catch (Exception e) {
            // Si falla la extracción, solo mostrar el mensaje
        }
    }

    /**
     * Extrae información del juego de un mensaje del servidor. Ejemplo:
     * "Puntaje total: 1500\nFrutas restantes: 2\nTiempo: 45 segundos"
     *
     * @param mensaje Mensaje con información
     */
    private void extraerInfoJuego(String mensaje) {
        try {
            int puntaje = 0;
            long tiempo = 0;
            int frutas = 0;

            // Extraer puntaje
            if (mensaje.contains("Puntaje total:")) {
                String[] partes = mensaje.split("Puntaje total:");
                if (partes.length > 1) {
                    String numStr = partes[1].split("\\n")[0].trim();
                    puntaje = Integer.parseInt(numStr);
                }
            }

            // Extraer tiempo
            if (mensaje.contains("Tiempo:")) {
                String[] partes = mensaje.split("Tiempo:");
                if (partes.length > 1) {
                    String numStr = partes[1].split(" ")[0].trim();
                    tiempo = Long.parseLong(numStr);
                }
            }

            // Extraer frutas
            if (mensaje.contains("Frutas restantes:")) {
                String[] partes = mensaje.split("Frutas restantes:");
                if (partes.length > 1) {
                    String numStr = partes[1].split("\\n")[0].trim();
                    frutas = Integer.parseInt(numStr);
                }
            }

            // Actualizar modelo y vista
            controladorModelo.actualizarEstadoJuego(puntaje, tiempo, frutas);
            controladorVista.actualizarInformacionJuego(puntaje, tiempo, frutas);

        } catch (Exception e) {
            // Si falla la extracción, ignorar
        }
    }
}
