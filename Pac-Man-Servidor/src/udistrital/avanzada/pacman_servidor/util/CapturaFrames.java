package udistrital.avanzada.pacman_servidor.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Utilidad para capturar y comprimir frames de un JPanel.
 * Convierte el contenido visual a imágenes JPEG comprimidas.
 * 
 * @author Steban
 * @version 1.0
 */
public class CapturaFrames {
    
    private float calidadCompresion;
    
    /**
     * Constructor que establece la calidad de compresión.
     * 
     * @param calidad Calidad JPEG (0.0 - 1.0)
     */
    public CapturaFrames(float calidad) {
        this.calidadCompresion = calidad;
    }
    
    /**
     * Captura el contenido visual de un panel.
     * 
     * @param panel Panel a capturar
     * @return BufferedImage con el contenido
     */
    public BufferedImage capturarFrame(JPanel panel) {
        int ancho = panel.getWidth();
        int alto = panel.getHeight();
        
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        
        // Renderizar el panel en la imagen
        panel.paint(g2d);
        g2d.dispose();
        
        return imagen;
    }
    
    /**
     * Comprime una imagen a formato JPEG.
     * 
     * @param imagen Imagen a comprimir
     * @return Array de bytes con la imagen comprimida
     * @throws IOException Si hay error en la compresión
     */
    public byte[] comprimirJPEG(BufferedImage imagen) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Obtener escritor JPEG
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IOException("No hay escritor JPEG disponible");
        }
        
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        
        // Configurar parámetros de compresión
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(calidadCompresion);
        }
        
        // Escribir imagen
        writer.write(null, new IIOImage(imagen, null, null), param);
        
        // Limpiar
        ios.close();
        writer.dispose();
        
        return baos.toByteArray();
    }
    
    /**
     * Redimensiona una imagen.
     * 
     * @param original Imagen original
     * @param nuevoAncho Nuevo ancho
     * @param nuevoAlto Nuevo alto
     * @return Imagen redimensionada
     */
    public BufferedImage redimensionar(BufferedImage original, int nuevoAncho, int nuevoAlto) {
        Image tmp = original.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_FAST);
        BufferedImage redimensionada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2d = redimensionada.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return redimensionada;
    }
}