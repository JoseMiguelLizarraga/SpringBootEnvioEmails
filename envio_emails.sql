
drop database if exists envio_emails;
create database envio_emails default character set utf8 default collate utf8_general_ci;
use envio_emails;   

-- =========================================================>>>>

create table parametros_login(
	id int primary key not null auto_increment,
	duracion_token_milisegundos int,
	mensaje_error_autenticacion varchar(100),
	mensaje_token_expirado varchar(100),
	mensaje_usuario_deshabilitado varchar(100),
	mensaje_error_interno varchar(100)
);

-- =========================================================>>>>

create table usuario(
	id int primary key not null auto_increment,
	username varchar(40),
	password varchar(120),
	largo_password int,
	nombre varchar(40),
	apellido_paterno varchar(40),
	apellido_materno varchar(40),
	rut varchar(40),
	telefono varchar(40),
	activo boolean,
	visible boolean  
);

create table cargo(
	id int primary key not null auto_increment,
	authority varchar(40),
	descripcion varchar(40),
	activo boolean,
	visible boolean
);

create table usuario_cargo(
	id int primary key not null auto_increment,
	usuario_id int,
	cargo_id int,
	foreign key(usuario_id) references usuario(id),
	foreign key(cargo_id) references cargo(id)    
);

-- =========================================================>>>>

create table contacto_persona(
	id int primary key not null auto_increment,
	email varchar(100),
	nombre_completo varchar(100),
	ultima_fecha_envio_correo date,
	ultima_fecha_respuesta date,
	ha_respondido boolean,
	existe boolean
);

create table envio_masivo_correo(
	id int primary key not null auto_increment,
	descripcion varchar(100), 
	email_origen_envio varchar(100),
	asunto_correo varchar(100),
	contenido_correo varchar(10000),
	fecha_envio date,
	usuario_id int,
	estado varchar(20),                                -- sin enviar     en cola         finalizado 
	foreign key(usuario_id) references usuario(id)
);


create table envio_correo_contacto_persona(
	id int primary key not null auto_increment,
	estado varchar(20),                                -- sin enviar            enviado 
	envio_masivo_correo_id int,
	contacto_persona_id int, 
	foreign key(envio_masivo_correo_id) references envio_masivo_correo(id),
	foreign key(contacto_persona_id) references contacto_persona(id)
);


create table maestro_envio_email(
	id int primary key not null auto_increment,
	email_envio varchar(100),
	password_email_envio varchar(100),
	host_email_envio varchar(100),
	puerto_email_envio varchar(20)
);

-- ===================================================================================================================>>>>>>

-- 3.600.000  equivale a una hora. Ej: multiplicar 3.600.000 * 24(horas) = 86400000 

INSERT INTO parametros_login(id, duracion_token_milisegundos, mensaje_error_autenticacion, mensaje_token_expirado, mensaje_usuario_deshabilitado, mensaje_error_interno) 
VALUES (
	1, 
	86400000, 
	'Error de autenticación. Username o password incorrecto',
	'Se termino el tiempo limite de uso del usuario',
	'Usuario deshabilitado',
	'Error interno'
);

-- ===================================================================================================================>>>>>>

-- Inserto los cargos

insert into cargo(id, authority, descripcion, activo, visible) values(1, 'ROLE_SUPER_ADMIN', 'Super Administrador IT', 1, 0);

-- ===================================================================================================================>>>>>>
-- Creo un usuario Administrador y su(s) cargo(s)

-- username:  admin
-- clave:  	  123456789

INSERT INTO usuario (id, username, password, largo_password, nombre, apellido_paterno, apellido_materno, rut, telefono, activo, visible) VALUES
(1, 'admin', '$2a$11$oTiVF3rePm8YqI//.8N4Bup6P3B3509KbN6imVrfvTKHUgy2VZgJG', 4, 'Juan', 'Perez', 'Perez', '12.222.222-2', NULL, 1, 0);

INSERT INTO usuario_cargo(id, usuario_id, cargo_id) VALUES 
(1, (select id from usuario where username = 'admin'), (select id from cargo where descripcion = 'Super Administrador IT'));

-- ===================================================================================================================>>>>>>

INSERT INTO maestro_envio_email (email_envio, password_email_envio, host_email_envio, puerto_email_envio) 
VALUES ('tucorreo@gmail.com', 'tuclave', 'smtp.gmail.com', '465');






/*
update envio_correo_contacto_persona set estado = 'sin enviar'
*/






















