/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  genar
 * Created: 20 jun 2026
 */
CREATE DATABASE IF NOT EXISTS pedidos_db;
USE pedidos_db;
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS categoria;
CREATE TABLE categoria (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL UNIQUE,
descripcion VARCHAR(255),
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE producto (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
precio DECIMAL(10,2) NOT NULL,
descripcion VARCHAR(255),
stock INT NOT NULL,
imagen VARCHAR(255),
disponible BOOLEAN NOT NULL DEFAULT TRUE,
categoria_id BIGINT NOT NULL,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_producto_categoria
FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);
CREATE TABLE usuario (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
apellido VARCHAR(100) NOT NULL,
mail VARCHAR(150) NOT NULL UNIQUE,
celular VARCHAR(50),
contrasenia VARCHAR(100),
rol VARCHAR(30) NOT NULL,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE pedido (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
usuario_id BIGINT NOT NULL,
fecha DATE NOT NULL,
estado VARCHAR(30) NOT NULL,
total DECIMAL(10,2) NOT NULL DEFAULT 0,
forma_pago VARCHAR(30) NOT NULL,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_pedido_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
CREATE TABLE detalle_pedido (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
pedido_id BIGINT NOT NULL,
producto_id BIGINT NOT NULL,
cantidad INT NOT NULL,
subtotal DECIMAL(10,2) NOT NULL,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_detalle_pedido
FOREIGN KEY (pedido_id) REFERENCES pedido(id),
CONSTRAINT fk_detalle_producto
FOREIGN KEY (producto_id) REFERENCES producto(id)
);
INSERT INTO categoria (nombre, descripcion) VALUES
('Hamburguesas', 'Hamburguesas completas'),
('Pizzas', 'Pizzas grandes y chicas'),
('Bebidas', 'Bebidas sin alcohol');
INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id) VALUES
('Hamburguesa simple', 3500, 'Hamburguesa con queso', 20, 'hamburguesa.jpg', true, 1),
('Pizza muzzarella', 6000, 'Pizza clásica de muzzarella', 15, 'pizza.jpg', true, 2),
('Coca Cola', 1800, 'Gaseosa 500ml', 40, 'coca.jpg', true, 3);
INSERT INTO usuario (nombre, apellido, mail, celular, contrasenia, rol) VALUES
('Juan', 'Pérez', 'juan@mail.com', '1111111111', '1234', 'USUARIO'),
('Admin', 'Sistema', 'admin@mail.com', '2222222222', 'admin', 'ADMIN');

