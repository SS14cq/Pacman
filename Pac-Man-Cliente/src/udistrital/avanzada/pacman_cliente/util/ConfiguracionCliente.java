package udistrital.avanzada.pacman_cliente.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase que gestiona la configuración del cliente desde archivo properties.
 * NO tiene dependencias de otros paquetes.
 * 
 * PRINCIPIO: Single Responsibility
 * - Solo lee y proporciona acceso a la configuración
 * 
 * @author Steban
 * @version 1.0
 */
public class ConfiguracionCliente {
    
    private Properties properties;
    
    /**
     * Constructor. Inicializa el objeto Properties.
     */
    public ConfiguracionCliente() {
        this.properties = new Properties();
    }
    
    /**
     * Carga la configuración desde un archivo.
     * 
     * @param archivo Archivo properties a cargar
     * @throws IOException Si hay error al leer el archivo
     */
    public void cargarDesdeArchivo(File archivo) throws IOException {
        if (archivo == null || !archivo.exists()) {
            throw new IOException("El archivo no existe: " + 
                (archivo != null ? archivo.getAbsolutePath() : "null"));
        }
        
        if (!archivo.canRead()) {
            throw new IOException("No se puede leer el archivo: " + 
                archivo.getAbsolutePath());
        }
        
        try (FileInputStream fis = new FileInputStream(archivo)) {
            properties.load(fis);
        }
    }
    
    /**
     * Obtiene la IP del servidor.
     * 
     * @return IP del servidor
     * @throws IllegalStateException Si no está configurada
     */
    public String getServidorIP() {
        String ip = properties.getProperty("servidor.ip");
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalStateException("servidor.ip no está configurada");
        }
        return ip.trim();
    }
    
    /**
     * Obtiene el puerto del servidor.
     * 
     * @return Puerto del servidor
     * @throws IllegalStateException Si no está configurado
     * @throws NumberFormatException Si el puerto no es válido
     */
    public int getServidorPuerto() {
        String puertoStr = properties.getProperty("servidor.puerto");
        if (puertoStr == null || puertoStr.trim().isEmpty()) {
            throw new IllegalStateException("servidor.puerto no está configurado");
        }
        
        try {
            int puerto = Integer.parseInt(puertoStr.trim());
            if (puerto < 1 || puerto > 65535) {
                throw new IllegalArgumentException("Puerto fuera de rango: " + puerto);
            }
            return puerto;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Puerto inválido: " + puertoStr);
        }
    }
    
    /**
     * Verifica si la configuración está cargada.
     * 
     * @return true si está cargada y válida
     */
    public boolean isConfigurada() {
        try {
            getServidorIP();
            getServidorPuerto();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtiene un resumen de la configuración.
     * 
     * @return String con la configuración
     */
    public String obtenerResumen() {
        if (!isConfigurada()) {
            return "Configuración no válida";
        }
        
        return String.format("Servidor: %s:%d", 
            getServidorIP(), 
            getServidorPuerto()
        );
    }
}