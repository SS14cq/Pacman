package udistrital.avanzada.pacman_servidor.util;

import java.io.IOException;
import java.util.List;
import udistrital.avanzada.pacman_servidor.conexion.ArchivoResultadosManager;
import udistrital.avanzada.pacman_servidor.modelo.RegistroJuego;

/**
 * Clase de prueba para verificar el funcionamiento del archivo de acceso aleatorio.
 * NO es parte del proyecto final, solo para testing.
 */
public class PruebaArchivoResultados {
    
    public static void main(String[] args) {
        try {
            ArchivoResultadosManager manager = new ArchivoResultadosManager("data/test_resultados.dat");
            
            // Limpiar archivo de prueba
            manager.borrarTodosLosRegistros();
            
            // Insertar registros de prueba
            System.out.println("=== Insertando registros ===");
            manager.guardarResultado(new RegistroJuego("Jugador1", 1500, 120));
            manager.guardarResultado(new RegistroJuego("Jugador2", 2300, 95));
            manager.guardarResultado(new RegistroJuego("Jugador3", 800, 180));
            manager.guardarResultado(new RegistroJuego("Jugador4", 2300, 85)); // Mismo puntaje, menor tiempo
            
            // Leer todos los registros
            System.out.println("\n=== Todos los registros ===");
            List<RegistroJuego> registros = manager.leerTodosLosRegistros();
            for (int i = 0; i < registros.size(); i++) {
                System.out.println("Registro " + i + ": " + registros.get(i));
            }
            
            // Obtener mejor jugador
            System.out.println("\n=== Mejor jugador ===");
            RegistroJuego mejor = (RegistroJuego) manager.obtenerMejorJugador();
            System.out.println(mejor);
            
            // Estadísticas
            System.out.println("\n" + manager.obtenerEstadisticas());
            
            // Leer registro específico
            System.out.println("\n=== Registro en posición 2 ===");
            RegistroJuego registro2 = (RegistroJuego) manager.leerRegistroPorIndice(2);
            System.out.println(registro2);
            
            System.out.println("\n✓ Prueba completada exitosamente");
            
        } catch (IOException e) {
            System.err.println("Error en prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
//```
//
//**Ejecución esperada:**
//```
//=== Insertando registros ===
//
//=== Todos los registros ===
//Registro 0: Jugador: Jugador1 | Puntaje: 1500 | Tiempo: 120 segundos
//Registro 1: Jugador: Jugador2 | Puntaje: 2300 | Tiempo: 95 segundos
//Registro 2: Jugador: Jugador3 | Puntaje: 800 | Tiempo: 180 segundos
//Registro 3: Jugador: Jugador4 | Puntaje: 2300 | Tiempo: 85 segundos
//
//=== Mejor jugador ===
//Jugador: Jugador4 | Puntaje: 2300 | Tiempo: 85 segundos
//
//=== Estadísticas del Archivo ===
//Total de jugadores: 4
//Puntaje promedio: 1725.00
//Tiempo promedio: 120.00 segundos
//
//=== Registro en posición 2 ===
//Jugador: Jugador3 | Puntaje: 800 | Tiempo: 180 segundos
//
//✓ Prueba completada exitosamente
//```
//
//---
//
//## Verificación del Tamaño de Registro
//
//**Cálculo manual:**
//```
//Nombre:   50 caracteres × 2 bytes/char = 100 bytes
//Puntaje:  int                          =   4 bytes
//Tiempo:   long                         =   8 bytes
//                                TOTAL   = 112 bytes