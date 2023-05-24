CREATE SCHEMA quarkussoical;

CREATE TABLE usuario(
id int auto_increment not null primary key,
name varchar(100) not null,
age int not null
);

CREATE TABLE post(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
post_text TEXT not null,
data_hora date,
usuario_id INT NOT NULL,
FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE seguidores(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
usuario_id int not null references usuario(id),
seguidor_id int not null references usuario(id)
);