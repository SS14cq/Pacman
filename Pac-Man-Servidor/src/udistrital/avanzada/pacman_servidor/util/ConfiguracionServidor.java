package udistrital.avanzada.pacman_servidor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Gestiona la carga y acceso a la configuración del servidor.
 * Lee el archivo properties y proporciona métodos para obtener valores.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class ConfiguracionServidor {
    
    private Properties properties;
    
    public ConfiguracionServidor() {
        this.properties = new Properties();
    }
    
    /**
     * Carga la configuración desde un archivo.
     * 
     * @param archivo Archivo properties a cargar
     * @throws IOException Si hay error al leer el archivo
     */
    public void cargarDesdeArchivo(File archivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivo)) {
            properties.load(fis);
        }
    }
    
    /**
     * Obtiene el puerto del servidor.
     * 
     * @return Puerto configurado
     */
    public int getPuerto() {
        return Integer.parseInt(properties.getProperty("servidor.puerto", "8080"));
    }
    
    /**
     * Obtiene la URL de conexión a la base de datos.
     * 
     * @return URL de conexión
     */
    public String getDBUrl() {
        return properties.getProperty("db.url");
    }
    
    /**
     * Obtiene el usuario de la base de datos.
     * 
     * @return Usuario de BD
     */
    public String getDBUsuario() {
        return properties.getProperty("db.usuario");
    }
    
    /**
     * Obtiene la contraseña de la base de datos.
     * 
     * @return Password de BD
     */
    public String getDBPassword() {
        return properties.getProperty("db.password");
    }
    
    /**
     * Obtiene el driver JDBC de MySQL.
     * 
     * @return Nombre completo de la clase del driver
     */
    public String getDBDriver() {
        return properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }
    
    /**
     * Obtiene el mapa de usuarios precargados.
     * Parsea el formato: "usuario1:pass1,usuario2:pass2"
     * 
     * @return Map con usuario como key y password como value
     */
    public Map<String, String> getUsuariosPrecargados() {
        Map<String, String> usuarios = new HashMap<>();
        String usuariosStr = properties.getProperty("usuarios.precargados", "");
        
        if (!usuariosStr.isEmpty()) {
            String[] parejas = usuariosStr.split(",");
            for (String pareja : parejas) {
                String[] partes = pareja.trim().split(":");
                if (partes.length == 2) {
                    usuarios.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        
        return usuarios;
    }
    
    /**
     * Obtiene el ancho de la ventana de juego.
     * 
     * @return Ancho en píxeles
     */
    public int getAnchoVentana() {
        return Integer.parseInt(properties.getProperty("juego.ventana.ancho", "800"));
    }
    
    /**
     * Obtiene el alto de la ventana de juego.
     * 
     * @return Alto en píxeles
     */
    public int getAltoVentana() {
        return Integer.parseInt(properties.getProperty("juego.ventana.alto", "600"));
    }
    
    /**
     * Obtiene la ruta del archivo de resultados.
     * 
     * @return Ruta del archivo
     */
    public String getRutaArchivoResultados() {
        return properties.getProperty("archivo.resultados", "data/resultados.dat");
    }
    
    /**
     * Obtiene los FPS para el streaming de video.
     * 
     * @return Frames por segundo
     */
    public int getStreamingFPS() {
        return Integer.parseInt(properties.getProperty("streaming.fps", "30"));
    }
    
    /**
     * Obtiene la calidad de compresión JPEG (0.0 - 1.0).
     * 
     * @return Calidad de compresión
     */
    public float getCalidadJPEG() {
        return Float.parseFloat(properties.getProperty("streaming.calidad.jpeg", "0.5"));
    }
}