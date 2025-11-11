package udistrital.avanzada.pacman_servidor.util;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import udistrital.avanzada.pacman_servidor.modelo.TipoMensaje;

/**
 * Hilo que captura y envía frames de video continuamente.
 * Controla FPS y compresión para el streaming.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class HiloEnvioFrames extends Thread {
    
    private DataOutputStream salida;
    private JPanel panel;
    private CapturaFrames captura;
    private volatile boolean activo;
    private int fps;
    private int delayMilisegundos;
    
    /**
     * Constructor del hilo de streaming.
     * 
     * @param salida Stream de salida hacia el cliente
     * @param panel Panel a capturar
     * @param fps Frames por segundo objetivo
     * @param calidad Calidad de compresión JPEG (0.0 - 1.0)
     */
    public HiloEnvioFrames(DataOutputStream salida, JPanel panel, int fps, float calidad) {
        this.salida = salida;
        this.panel = panel;
        this.fps = fps;
        this.delayMilisegundos = 1000 / fps;
        this.captura = new CapturaFrames(calidad);
        this.activo = true;
        
        // Daemon thread para no bloquear cierre
        setDaemon(true);
    }
    
    @Override
    public void run() {
        while (activo) {
            long tiempoInicio = System.currentTimeMillis();
            
            try {
                // Capturar frame
                BufferedImage frame = captura.capturarFrame(panel);
                
                // Comprimir
                byte[] frameComprimido = captura.comprimirJPEG(frame);
                
                // Enviar por socket (synchronized para evitar corrupción)
                synchronized (salida) {
                    salida.writeUTF(TipoMensaje.FRAME_VIDEO.getCodigo());
                    salida.writeInt(frameComprimido.length);
                    salida.write(frameComprimido);
                    salida.flush();
                }
                
            } catch (IOException e) {
                if (activo) {
                    System.err.println("Error enviando frame: " + e.getMessage());
                }
                activo = false;
                break;
            }
            
            // Control de FPS
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
            long tiempoEspera = delayMilisegundos - tiempoTranscurrido;
            
            if (tiempoEspera > 0) {
                try {
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException e) {
                    activo = false;
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    /**
     * Detiene el hilo de streaming.
     */
    public void detener() {
        activo = false;
        interrupt();
    }
}