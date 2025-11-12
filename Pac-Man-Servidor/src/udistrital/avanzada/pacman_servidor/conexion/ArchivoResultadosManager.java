package udistrital.avanzada.pacman_servidor.conexion;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.pacman_servidor.modelo.RegistroJuego;

/**
 * Gestiona el archivo de acceso aleatorio para almacenar resultados de juegos.
 * Implementa operaciones de lectura, escritura y búsqueda en el archivo.
 * 
 * @author Steban
 * @version 1.0
 */
public class ArchivoResultadosManager {
    
    private String rutaArchivo;
    
    /**
     * Constructor que recibe la ruta del archivo.
     * 
     * @param rutaArchivo Ruta donde se guardará el archivo
     */
    public ArchivoResultadosManager(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        verificarYCrearArchivo();
    }
    
    /**
     * Verifica si el archivo existe, si no lo crea.
     */
    private void verificarYCrearArchivo() {
        File archivo = new File(rutaArchivo);
        
        // Crear directorio padre si no existe
        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            directorioPadre.mkdirs();
        }
        
        // Crear archivo si no existe
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear archivo de resultados: " + e.getMessage());
            }
        }
    }
    
    /**
     * Guarda un registro al final del archivo.
     * 
     * @param registro Registro a guardar
     * @throws IOException Si hay error de escritura
     */
    public void guardarResultado(RegistroJuego registro) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
            // Posicionarse al final del archivo
            raf.seek(raf.length());
            
            // Escribir el registro
            escribirRegistro(raf, registro);
        }
    }
    
    /**
     * Escribe un registro en la posición actual del archivo.
     * 
     * @param raf RandomAccessFile donde escribir
     * @param registro Registro a escribir
     * @throws IOException Si hay error de escritura
     */
    private void escribirRegistro(RandomAccessFile raf, RegistroJuego registro) throws IOException {
        // Escribir nombre (siempre 50 caracteres)
        String nombre = registro.getNombreJugador();
        
        // Rellenar con espacios si es necesario
        StringBuilder nombreFijo = new StringBuilder(nombre);
        while (nombreFijo.length() < RegistroJuego.LONGITUD_NOMBRE) {
            nombreFijo.append(" ");
        }
        
        // Escribir cada carácter (2 bytes por char)
        for (int i = 0; i < RegistroJuego.LONGITUD_NOMBRE; i++) {
            raf.writeChar(nombreFijo.charAt(i));
        }
        
        // Escribir puntaje (4 bytes)
        raf.writeInt(registro.getPuntaje());
        
        // Escribir tiempo (8 bytes)
        raf.writeLong(registro.getTiempoSegundos());
    }
    
    /**
     * Lee un registro desde la posición actual del archivo.
     * 
     * @param raf RandomAccessFile desde donde leer
     * @return Registro leído
     * @throws IOException Si hay error de lectura
     */
    private RegistroJuego leerRegistro(RandomAccessFile raf) throws IOException {
        // Leer nombre (50 caracteres)
        StringBuilder nombre = new StringBuilder();
        for (int i = 0; i < RegistroJuego.LONGITUD_NOMBRE; i++) {
            nombre.append(raf.readChar());
        }
        
        // Leer puntaje
        int puntaje = raf.readInt();
        
        // Leer tiempo
        long tiempo = raf.readLong();
        
        return new RegistroJuego(nombre.toString().trim(), puntaje, tiempo);
    }
    
    /**
     * Lee todos los registros del archivo.
     * 
     * @return Lista con todos los registros
     * @throws IOException Si hay error de lectura
     */
    public List<RegistroJuego> leerTodosLosRegistros() throws IOException {
        List<RegistroJuego> registros = new ArrayList<>();
        
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "r")) {
            long longitudArchivo = raf.length();
            
            // Calcular número de registros
            int numeroRegistros = (int) (longitudArchivo / RegistroJuego.TAMANIO_REGISTRO);
            
            // Leer cada registro
            for (int i = 0; i < numeroRegistros; i++) {
                raf.seek(i * RegistroJuego.TAMANIO_REGISTRO);
                RegistroJuego registro = leerRegistro(raf);
                registros.add(registro);
            }
        }
        
        return registros;
    }
    
    /**
     * Lee un registro específico por su posición.
     * 
     * @param indice Índice del registro (base 0)
     * @return Registro en esa posición
     * @throws IOException Si hay error de lectura o índice inválido
     */
    public RegistroJuego leerRegistroPorIndice(int indice) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "r")) {
            long longitudArchivo = raf.length();
            int numeroRegistros = (int) (longitudArchivo / RegistroJuego.TAMANIO_REGISTRO);
            
            if (indice < 0 || indice >= numeroRegistros) {
                throw new IOException("Índice fuera de rango: " + indice);
            }
            
            // Posicionarse en el registro
            raf.seek(indice * RegistroJuego.TAMANIO_REGISTRO);
            
            return leerRegistro(raf);
        }
    }
    
    /**
     * Obtiene el mejor jugador (mayor puntaje, menor tiempo en caso de empate).
     * 
     * @return Registro del mejor jugador o null si no hay registros
     * @throws IOException Si hay error de lectura
     */
    public RegistroJuego obtenerMejorJugador() throws IOException {
        List<RegistroJuego> registros = leerTodosLosRegistros();
        
        if (registros.isEmpty()) {
            return null;
        }
        
        RegistroJuego mejor = registros.get(0);
        
        for (RegistroJuego registro : registros) {
            // Comparar por puntaje primero
            if (registro.getPuntaje() > mejor.getPuntaje()) {
                mejor = registro;
            } 
            // Si tienen el mismo puntaje, comparar por tiempo (menor es mejor)
            else if (registro.getPuntaje() == mejor.getPuntaje() && 
                     registro.getTiempoSegundos() < mejor.getTiempoSegundos()) {
                mejor = registro;
            }
        }
        
        return mejor;
    }
    
    /**
     * Obtiene el número total de registros en el archivo.
     * 
     * @return Cantidad de registros
     * @throws IOException Si hay error de lectura
     */
    public int obtenerCantidadRegistros() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "r")) {
            long longitudArchivo = raf.length();
            return (int) (longitudArchivo / RegistroJuego.TAMANIO_REGISTRO);
        }
    }
    
    /**
     * Verifica si el archivo está vacío.
     * 
     * @return true si está vacío, false en caso contrario
     * @throws IOException Si hay error de lectura
     */
    public boolean estaVacio() throws IOException {
        return obtenerCantidadRegistros() == 0;
    }
    
    /**
     * Borra todos los registros del archivo (reinicia el archivo).
     * 
     * @throws IOException Si hay error al borrar
     */
    public void borrarTodosLosRegistros() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
            raf.setLength(0);
        }
    }
    
    /**
     * Obtiene estadísticas del archivo.
     * 
     * @return String con estadísticas
     * @throws IOException Si hay error de lectura
     */
    public String obtenerEstadisticas() throws IOException {
        List<RegistroJuego> registros = leerTodosLosRegistros();
        
        if (registros.isEmpty()) {
            return "No hay registros disponibles.";
        }
        
        int totalJugadores = registros.size();
        int puntajeTotal = 0;
        long tiempoTotal = 0;
        
        for (RegistroJuego registro : registros) {
            puntajeTotal += registro.getPuntaje();
            tiempoTotal += registro.getTiempoSegundos();
        }
        
        double puntajePromedio = (double) puntajeTotal / totalJugadores;
        double tiempoPromedio = (double) tiempoTotal / totalJugadores;
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== Estadísticas del Archivo ===\n");
        stats.append("Total de jugadores: ").append(totalJugadores).append("\n");
        stats.append("Puntaje promedio: ").append(String.format("%.2f", puntajePromedio)).append("\n");
        stats.append("Tiempo promedio: ").append(String.format("%.2f", tiempoPromedio)).append(" segundos\n");
        
        return stats.toString();
    }
}