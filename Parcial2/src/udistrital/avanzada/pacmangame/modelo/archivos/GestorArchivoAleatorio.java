package udistrital.avanzada.pacmangame.modelo.archivos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.pacmangame.modelo.Constantes;
import udistrital.avanzada.pacmangame.modelo.entidades.RegistroJuego;

/**
 * Gestor para manejar el archivo de acceso aleatorio de registros de juego.
 * Cumple con SRP al encargarse solo de persistencia en archivo aleatorio.
 * 
 * @author [Tu nombre]
 * @version 1.0
 */
public class GestorArchivoAleatorio {
    
    private String rutaArchivo;
    
    /**
     * Constructor del gestor de archivo aleatorio.
     * 
     * @param rutaArchivo Ruta del archivo de acceso aleatorio
     */
    public GestorArchivoAleatorio(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    /**
     * Escribe un registro al final del archivo.
     * 
     * @param registro Registro a escribir
     * @throws IOException Si ocurre un error al escribir
     */
    public void escribirRegistro(RegistroJuego registro) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
            // Posicionarse al final del archivo
            raf.seek(raf.length());
            
            // Escribir nombre (50 caracteres fijos)
            escribirCadenaFija(raf, registro.getNombreJugador(), Constantes.TAMANIO_NOMBRE);
            
            // Escribir puntaje (4 bytes - int)
            raf.writeInt(registro.getPuntaje());
            
            // Escribir tiempo (8 bytes - long)
            raf.writeLong(registro.getTiempoJuego());
            
            // Escribir estado activo (4 bytes - int como boolean)
            raf.writeInt(registro.isActivo() ? 1 : 0);
        }
    }
    
    /**
     * Lee todos los registros activos del archivo.
     * 
     * @return Lista de registros activos
     * @throws IOException Si ocurre un error al leer
     */
    public List<RegistroJuego> leerTodosLosRegistros() throws IOException {
        List<RegistroJuego> registros = new ArrayList<>();
        
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "r")) {
            long longitudArchivo = raf.length();
            long posicion = 0;
            
            while (posicion < longitudArchivo) {
                raf.seek(posicion);
                
                // Leer nombre
                String nombre = leerCadenaFija(raf, Constantes.TAMANIO_NOMBRE);
                
                // Leer puntaje
                int puntaje = raf.readInt();
                
                // Leer tiempo
                long tiempo = raf.readLong();
                
                // Leer estado activo
                int activoInt = raf.readInt();
                boolean activo = activoInt == 1;
                
                if (activo) {
                    RegistroJuego registro = new RegistroJuego(nombre, puntaje, tiempo);
                    registro.setActivo(activo);
                    registros.add(registro);
                }
                
                posicion += Constantes.TAMANIO_REGISTRO;
            }
        }
        
        return registros;
    }
    
    /**
     * Encuentra el mejor jugador (mayor puntaje y menor tiempo).
     * 
     * @return Registro del mejor jugador o null si no hay registros
     * @throws IOException Si ocurre un error al leer
     */
    public RegistroJuego encontrarMejorJugador() throws IOException {
        List<RegistroJuego> registros = leerTodosLosRegistros();
        
        if (registros.isEmpty()) {
            return null;
        }
        
        RegistroJuego mejor = registros.get(0);
        
        for (RegistroJuego registro : registros) {
            // Comparar por puntaje primero
            if (registro.getPuntaje() > mejor.getPuntaje()) {
                mejor = registro;
            } else if (registro.getPuntaje() == mejor.getPuntaje()) {
                // Si tienen el mismo puntaje, elegir el de menor tiempo
                if (registro.getTiempoJuego() < mejor.getTiempoJuego()) {
                    mejor = registro;
                }
            }
        }
        
        return mejor;
    }
    
    /**
     * Escribe una cadena con tamaño fijo al archivo.
     * 
     * @param raf Archivo de acceso aleatorio
     * @param cadena Cadena a escribir
     * @param tamanio Tamaño fijo en caracteres
     * @throws IOException Si ocurre un error al escribir
     */
    private void escribirCadenaFija(RandomAccessFile raf, String cadena, int tamanio) throws IOException {
        StringBuilder sb = new StringBuilder(cadena);
        sb.setLength(tamanio);
        raf.writeChars(sb.toString());
    }
    
    /**
     * Lee una cadena con tamaño fijo del archivo.
     * 
     * @param raf Archivo de acceso aleatorio
     * @param tamanio Tamaño en caracteres a leer
     * @return Cadena leída y limpiada
     * @throws IOException Si ocurre un error al leer
     */
    private String leerCadenaFija(RandomAccessFile raf, int tamanio) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanio; i++) {
            sb.append(raf.readChar());
        }
        return sb.toString().trim();
    }
    
    /**
     * Verifica si el archivo existe y crea uno vacío si no existe.
     * 
     * @throws IOException Si ocurre un error al crear el archivo
     */
    public void inicializarArchivo() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
            // Solo crear el archivo si no existe
        }
    }
}