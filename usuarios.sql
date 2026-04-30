CREATE DATABASE IF NOT EXISTS control_turnos
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE control_turnos;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS bitacora;
DROP TABLE IF EXISTS marcajes;
DROP TABLE IF EXISTS turnos;
DROP TABLE IF EXISTS solicitudes;
DROP TABLE IF EXISTS asignacion_turnos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS motivos_inactividad;
DROP TABLE IF EXISTS turnos_catalogo;
DROP TABLE IF EXISTS roles;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE roles (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_roles_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE turnos_catalogo (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  hora_inicio TIME NOT NULL,
  hora_fin TIME NOT NULL,
  horas INT NOT NULL DEFAULT 8,
  PRIMARY KEY (id),
  UNIQUE KEY uk_turnos_catalogo_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE motivos_inactividad (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  dpi VARCHAR(20) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  usuario VARCHAR(50) NOT NULL,
  area VARCHAR(100) NOT NULL,
  puesto VARCHAR(100) DEFAULT NULL,
  turno_actual_id INT DEFAULT NULL,
  correo VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  estado ENUM('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
  motivo_inactivo_id INT DEFAULT NULL,
  rol_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuarios_dpi (dpi),
  UNIQUE KEY uk_usuarios_usuario (usuario),
  KEY idx_usuarios_turno (turno_actual_id),
  KEY idx_usuarios_rol (rol_id),
  KEY idx_usuarios_motivo (motivo_inactivo_id),
  CONSTRAINT fk_usuarios_turno FOREIGN KEY (turno_actual_id) REFERENCES turnos_catalogo(id),
  CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles(id),
  CONSTRAINT fk_usuarios_motivo FOREIGN KEY (motivo_inactivo_id) REFERENCES motivos_inactividad(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE asignacion_turnos (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  turno_id INT NOT NULL,
  estado VARCHAR(30) NOT NULL DEFAULT 'Activo',
  PRIMARY KEY (id),
  UNIQUE KEY uk_asignacion_turno (usuario_id, fecha_inicio, fecha_fin),
  KEY idx_asignacion_turno (turno_id),
  CONSTRAINT fk_asignacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT fk_asignacion_turno FOREIGN KEY (turno_id) REFERENCES turnos_catalogo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE solicitudes (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  tipo VARCHAR(100) NOT NULL,
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  motivo TEXT NOT NULL,
  estado VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_solicitudes_usuario (usuario_id),
  CONSTRAINT fk_solicitudes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE turnos (
  id INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  fecha_inicio DATE NOT NULL,
  TurnoInicial VARCHAR(50) NOT NULL,
  NuevoTurno VARCHAR(50) NOT NULL,
  Nuevafecha DATE NOT NULL,
  Motivo TEXT NOT NULL,
  estado VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_turnos_usuario (id_usuario),
  CONSTRAINT fk_turnos_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE marcajes (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  fecha DATE NOT NULL,
  hora_entrada TIME DEFAULT NULL,
  entrada_tarde TINYINT(1) NOT NULL DEFAULT 0,
  hora_descanso1 TIME DEFAULT NULL,
  hora_descanso2 TIME DEFAULT NULL,
  hora_salida TIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_marcaje_dia (usuario_id, fecha),
  CONSTRAINT fk_marcajes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bitacora (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT DEFAULT NULL,
  modulo VARCHAR(80) NOT NULL,
  tipo_operacion VARCHAR(80) NOT NULL,
  descripcion TEXT NOT NULL,
  fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_bitacora_usuario (usuario_id),
  CONSTRAINT fk_bitacora_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO roles (id, nombre) VALUES
(1, 'AdminRRHH'),
(2, 'AdminArea'),
(3, 'Empleado');

INSERT INTO turnos_catalogo (id, nombre, hora_inicio, hora_fin, horas) VALUES
(1, 'Matutino', '08:00:00', '16:00:00', 8),
(2, 'Vespertino', '14:00:00', '22:00:00', 8),
(3, 'Nocturno', '22:00:00', '06:00:00', 8),
(4, 'Diurno', '08:00:00', '16:00:00', 8);

INSERT INTO motivos_inactividad (id, nombre) VALUES
(1, 'Vacaciones'),
(2, 'Permiso Personal'),
(3, 'Citas al IGSS'),
(4, 'Licencia de Cumpleanos'),
(5, 'Suspension Laboral'),
(6, 'Otros');

INSERT INTO usuarios
(id, dpi, nombre, usuario, area, puesto, turno_actual_id, correo, password, estado, motivo_inactivo_id, rol_id)
VALUES
(1, '0000000000001', 'Administrador RRHH', 'admin', 'RRHH', 'Administrador', 1, 'admin@empresa.com', '1234', 'Activo', NULL, 1),
(2, '0000000000002', 'Administrador Area', 'area', 'Operaciones', 'Administrador de area', 1, 'area@empresa.com', '1234', 'Activo', NULL, 2),
(3, '0000000000003', 'Empleado Demo', 'empleado', 'Operaciones', 'Digitador', 1, 'empleado@empresa.com', '1234', 'Activo', NULL, 3);

COMMIT;
