-- ============================================
-- Script de creación de base de datos
-- Proyecto Pac-Man Cliente-Servidor
-- ============================================

-- Eliminar base de datos si existe
DROP DATABASE IF EXISTS pacman_db;

-- Crear base de datos
CREATE DATABASE pacman_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE pacman_db;

-- ============================================
-- Tabla: usuarios
-- Almacena los jugadores registrados
-- ============================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- Insertar usuarios de prueba
-- (Estos también se cargarán desde properties)
-- ============================================
INSERT INTO usuarios (nombre, password) VALUES
('jugador1', 'pass1'),
('jugador2', 'pass2'),
('jugador3', 'pass3'),
('admin', 'admin123');

-- ============================================
-- Verificar creación
-- ============================================
SELECT * FROM usuarios;
-- ```
-- 
-- Crea también `data/README_BD.txt`:
-- ```
-- ============================================
-- CONFIGURACIÓN DE BASE DE DATOS - PAC-MAN
-- ============================================
-- 
-- 1. REQUISITOS:
--    - MySQL Server 8.0 o superior
--    - Puerto: 3306 (por defecto)
-- 
-- 2. INSTALACIÓN:
--    
--    a) Ejecutar el script:
--       mysql -u root -p < script_bd.sql
--    
--    b) O desde MySQL Workbench:
--       - Abrir script_bd.sql
--       - Ejecutar (Ctrl + Shift + Enter)
-- 
-- 3. CREDENCIALES:
--    
--    Usuario BD: root
--    Password BD: (tu password de MySQL)
--    Base de Datos: pacman_db
--    Puerto: 3306
-- 
-- 4. USUARIOS DE PRUEBA:
--    
--    nombre: jugador1 | password: pass1
--    nombre: jugador2 | password: pass2
--    nombre: jugador3 | password: pass3
--    nombre: admin    | password: admin123
-- 
-- 5. VERIFICACIÓN:
--    
--    mysql -u root -p
--    USE pacman_db;
--    SELECT * FROM usuarios;
-- 
-- ============================================