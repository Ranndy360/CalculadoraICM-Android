CREATE DATABASE APPIMC

USE APPIMC

CREATE TABLE Usuario(ID_User INT IDENTITY(1,1) NOT NULL PRIMARY KEY, Nombre_User VARCHAR(100) NOT NULL, Email VARCHAR(100) NOT NULL, Pass VARCHAR(100) NOT NULL);

CREATE TABLE Estado(ID_Estado INT IDENTITY(1,1) NOT NULL PRIMARY KEY, Nombre_Estado varchar(100), Descripcion VARCHAR(200))

CREATE TABLE Perfil (ID_Perfil INT IDENTITY(1,1) NOT NULL PRIMARY KEY, ID_User INT NOT NULL FOREIGN KEY(ID_User) REFERENCES Usuario(ID_User), Nombre VARCHAR (100) DEFAULT 'ANONIMO', Apellido VARCHAR(100) DEFAULT 'ANONIMO', Sexo CHAR(1), Fecha_Birth VARCHAR(50) NOT NULL);

CREATE TABLE Registro (ID_Registro INT IDENTITY(1,1) NOT NULL PRIMARY KEY, ID_Perfil INT NOT NULL FOREIGN KEY(ID_Perfil) REFERENCES Perfil(ID_Perfil), Fecha VARCHAR(50), Altura FLOAT DEFAULT 1.72, Peso FLOAT DEFAULT 70, IMC FLOAT DEFAULT 19, ID_Estado INT NOT NULL FOREIGN KEY(ID_Estado) REFERENCES Estado(ID_Estado));

INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Peso Insuficiente','Indice IMC es inferior a 18.5');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Normopeso',' Indice IMC entre 18.5 a 24.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Sobrepeso Grado I',' Indice IMC entre 25 a 26.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Sobrepeso Grado II (Preobesidad)',' Indice IMC entre 27 a 29.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Obesidad de Tipo I',' Indice IMC entre 30 a 34.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Obesidad de Tipo II',' Indice IMC entre 35 a 39.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Obesidad de Tipo III (Morbida)',' Indice IMC entre 40 a 49.9');
INSERT INTO Estado (Nombre_Estado, Descripcion) VALUES ('Obesidad de Tipo IV (Extrema)',' Indice IMC mayor a 50');

SELECT * FROM Estado;