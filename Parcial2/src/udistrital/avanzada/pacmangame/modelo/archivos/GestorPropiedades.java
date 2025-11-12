package udistrital.avanzada.pacmangame.modelo.archivos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Gestor para cargar y leer archivos de propiedades (.properties).
 * Cumple con SRP al encargarse solo de operaciones con archivos properties.
 * Proporciona métodos para leer propiedades de diferentes tipos.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class GestorPropiedades {
    
    private Properties propiedades;
    private String rutaArchivo;
    
    /**
     * Constructor por defecto.
     * Inicializa el objeto Properties vacío.
     */
    public GestorPropiedades() {
        this.propiedades = new Properties();
        this.rutaArchivo = null;
    }
    
    /**
     * Constructor que carga inmediatamente un archivo.
     * 
     * @param rutaArchivo Ruta del archivo properties a cargar
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public GestorPropiedades(String rutaArchivo) throws IOException {
        this();
        cargarArchivo(rutaArchivo);
    }
    
    /**
     * Carga un archivo de propiedades desde disco.
     * 
     * @param rutaArchivo Ruta del archivo properties
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public void cargarArchivo(String rutaArchivo) throws IOException {
        this.rutaArchivo = rutaArchivo;
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            propiedades.load(fis);
        }
    }
    
    /**
     * Guarda las propiedades actuales en un archivo.
     * 
     * @param rutaArchivo Ruta donde guardar el archivo
     * @param comentario Comentario para el archivo
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void guardarArchivo(String rutaArchivo, String comentario) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            propiedades.store(fos, comentario);
        }
    }
    
    /**
     * Obtiene una propiedad como String.
     * 
     * @param clave Clave de la propiedad
     * @return Valor de la propiedad o null si no existe
     */
    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave);
    }
    
    /**
     * Obtiene una propiedad como String con valor por defecto.
     * 
     * @param clave Clave de la propiedad
     * @param valorDefecto Valor por defecto si no existe
     * @return Valor de la propiedad o el valor por defecto
     */
    public String obtenerPropiedad(String clave, String valorDefecto) {
        return propiedades.getProperty(clave, valorDefecto);
    }
    
    /**
     * Obtiene una propiedad como entero.
     * 
     * @param clave Clave de la propiedad
     * @return Valor entero de la propiedad
     * @throws NumberFormatException Si el valor no es un número válido
     */
    public int obtenerPropiedadInt(String clave) {
        String valor = obtenerPropiedad(clave);
        if (valor == null) {
            throw new NumberFormatException("Propiedad no encontrada: " + clave);
        }
        return Integer.parseInt(valor.trim());
    }
    
    /**
     * Obtiene una propiedad como entero con valor por defecto.
     * 
     * @param clave Clave de la propiedad
     * @param valorDefecto Valor por defecto
     * @return Valor entero de la propiedad o el valor por defecto
     */
    public int obtenerPropiedadInt(String clave, int valorDefecto) {
        try {
            return obtenerPropiedadInt(clave);
        } catch (NumberFormatException e) {
            return valorDefecto;
        }
    }
    
    /**
     * Obtiene una propiedad como long.
     * 
     * @param clave Clave de la propiedad
     * @return Valor long de la propiedad
     * @throws NumberFormatException Si el valor no es un número válido
     */
    public long obtenerPropiedadLong(String clave) {
        String valor = obtenerPropiedad(clave);
        if (valor == null) {
            throw new NumberFormatException("Propiedad no encontrada: " + clave);
        }
        return Long.parseLong(valor.trim());
    }
    
    /**
     * Obtiene una propiedad como long con valor por defecto.
     * 
     * @param clave Clave de la propiedad
     * @param valorDefecto Valor por defecto
     * @return Valor long de la propiedad o el valor por defecto
     */
    public long obtenerPropiedadLong(String clave, long valorDefecto) {
        try {
            return obtenerPropiedadLong(clave);
        } catch (NumberFormatException e) {
            return valorDefecto;
        }
    }
    
    /**
     * Obtiene una propiedad como double.
     * 
     * @param clave Clave de la propiedad
     * @return Valor double de la propiedad
     * @throws NumberFormatException Si el valor no es un número válido
     */
    public double obtenerPropiedadDouble(String clave) {
        String valor = obtenerPropiedad(clave);
        if (valor == null) {
            throw new NumberFormatException("Propiedad no encontrada: " + clave);
        }
        return Double.parseDouble(valor.trim());
    }
    
    /**
     * Obtiene una propiedad como double con valor por defecto.
     * 
     * @param clave Clave de la propiedad
     * @param valorDefecto Valor por defecto
     * @return Valor double de la propiedad o el valor por defecto
     */
    public double obtenerPropiedadDouble(String clave, double valorDefecto) {
        try {
            return obtenerPropiedadDouble(clave);
        } catch (NumberFormatException e) {
            return valorDefecto;
        }
    }
    
    /**
     * Obtiene una propiedad como boolean.
     * 
     * @param clave Clave de la propiedad
     * @return true si el valor es "true", false en caso contrario
     */
    public boolean obtenerPropiedadBoolean(String clave) {
        String valor = obtenerPropiedad(clave);
        return valor != null && valor.trim().equalsIgnoreCase("true");
    }
    
    /**
     * Obtiene una propiedad como boolean con valor por defecto.
     * 
     * @param clave Clave de la propiedad
     * @param valorDefecto Valor por defecto
     * @return Valor boolean de la propiedad o el valor por defecto
     */
    public boolean obtenerPropiedadBoolean(String clave, boolean valorDefecto) {
        String valor = obtenerPropiedad(clave);
        if (valor == null) {
            return valorDefecto;
        }
        return valor.trim().equalsIgnoreCase("true");
    }
    
    /**
     * Establece el valor de una propiedad.
     * 
     * @param clave Clave de la propiedad
     * @param valor Valor a establecer
     */
    public void establecerPropiedad(String clave, String valor) {
        propiedades.setProperty(clave, valor);
    }
    
    /**
     * Verifica si existe una propiedad.
     * 
     * @param clave Clave de la propiedad
     * @return true si la propiedad existe
     */
    public boolean contienePropiedad(String clave) {
        return propiedades.containsKey(clave);
    }
    
    /**
     * Elimina una propiedad.
     * 
     * @param clave Clave de la propiedad a eliminar
     * @return El valor anterior o null si no existía
     */
    public Object eliminarPropiedad(String clave) {
        return propiedades.remove(clave);
    }
    
    /**
     * Limpia todas las propiedades.
     */
    public void limpiar() {
        propiedades.clear();
    }
    
    /**
     * Obtiene el número de propiedades cargadas.
     * 
     * @return Cantidad de propiedades
     */
    public int obtenerCantidadPropiedades() {
        return propiedades.size();
    }
    
    /**
     * Obtiene la ruta del archivo cargado actualmente.
     * 
     * @return Ruta del archivo o null si no se ha cargado ninguno
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    
    @Override
    public String toString() {
        return "GestorPropiedades{" +
                "rutaArchivo='" + rutaArchivo + '\'' +
                ", propiedades=" + propiedades.size() +
                '}';
    }
}