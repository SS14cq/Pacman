package udistrital.avanzada.pacman_cliente.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Decodifica frames de video JPEG comprimidos.
 * Convierte bytes en imágenes BufferedImage para visualización.
 * 
 * PRINCIPIO: Single Responsibility
 * - Solo maneja decodificación de imágenes
 * - No conoce sockets ni vista
 * 
 * @author Steban
 * @version 1.0
 */
public class DecodificadorFrames {
    
    /**
     * Decodifica un array de bytes JPEG a BufferedImage.
     * 
     * @param bytesJPEG Array de bytes con imagen JPEG
     * @return BufferedImage decodificada
     * @throws IOException Si hay error al decodificar
     */
    public BufferedImage decodificarJPEG(byte[] bytesJPEG) throws IOException {
        if (bytesJPEG == null || bytesJPEG.length == 0) {
            throw new IOException("Array de bytes vacío o nulo");
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytesJPEG);
        BufferedImage imagen = ImageIO.read(bais);
        bais.close();
        
        if (imagen == null) {
            throw new IOException("No se pudo decodificar la imagen JPEG");
        }
        
        return imagen;
    }
    
    /**
     * Verifica si un array de bytes es una imagen JPEG válida.
     * 
     * @param bytes Array de bytes a verificar
     * @return true si es JPEG válido
     */
    public boolean esJPEGValido(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            return false;
        }
        
        // Verificar signature JPEG (0xFF 0xD8)
        return (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8;
    }
}
