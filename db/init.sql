USE estoque_db;

CREATE TABLE IF NOT EXISTS users (
     id            INT AUTO_INCREMENT PRIMARY KEY,
     username      VARCHAR(50)  NOT NULL UNIQUE,
     psw           VARCHAR(255) NOT NULL,
     nameFirst     VARCHAR(100),
     sobrenome     VARCHAR(100),
     matricula     VARCHAR(50),
     cpf           VARCHAR(14),
     sexo          VARCHAR(10),
     dtaNascimento DATE,
     email         VARCHAR(150),
     telefone      VARCHAR(20),
     funcao        VARCHAR(100),
     cep           VARCHAR(10),
     endereco      VARCHAR(150),
     numero        VARCHAR(10),
     bairro        VARCHAR(100),
     cidade        VARCHAR(100),
     estado        VARCHAR(50),
     complemento   VARCHAR(150)

);

INSERT INTO users (
    username, psw, nameFirst, sobrenome, matricula, cpf,
    sexo, dtaNascimento, email, telefone, funcao,
    cep, endereco, numero, bairro, cidade, estado, complemento
) VALUES (
     'admin', '$2a$10$htQv/2ebpKfJiG0eYwbB/erAwhcsjkR/oStjPhwR.QQOyWqC3uUmK', 'Admin', 'Sistema', '0001', '000.000.000-00',
     'Masculino', '1990-01-01', 'admin@email.com', '71999999999', 'ADMIN',
     '40000-000', 'Rua Exemplo', '123', 'Centro', 'Salvador', 'BA', 'N/A'
);

CREATE TABLE IF NOT EXISTS produtos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras    VARCHAR(100) NOT NULL,
    nome_produto     VARCHAR(255) NOT NULL,
    fabricante       VARCHAR(255),
    marca            VARCHAR(255),
    data_fabricacao  DATE,
    data_vencimento  DATE,
    quantidade       BIGINT,
    valor            DECIMAL(10,2),
    total            DECIMAL(10,2),
    status           VARCHAR(100),
    prateleira       VARCHAR(100),
    qtd_minima       INT DEFAULT 0,

    CONSTRAINT codigo_validade UNIQUE (codigo_barras, data_vencimento)
);

CREATE TABLE IF NOT EXISTS historico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    nome_produto VARCHAR(255) NOT NULL,
    quantidade BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historico_produto
     FOREIGN KEY (produto_id) REFERENCES produtos(id)
         ON DELETE CASCADE
    );