# create databases
CREATE DATABASE IF NOT EXISTS `spring_batch`;
CREATE DATABASE IF NOT EXISTS `migracao_dados`;

# use database migracao_dados
USE migracao_dados;

# create client pessoa
DROP TABLE IF EXISTS pessoa;
CREATE TABLE pessoa(id INT, nome VARCHAR(500),email VARCHAR(500),data_nascimento DATETIME,idade INT, PRIMARY KEY(id));
DROP TABLE IF EXISTS dados_bancarios;
CREATE TABLE dados_bancarios(id INT, pessoa_id INT,agencia INT, conta INT, banco INT , PRIMARY KEY(id));


# insert clients
