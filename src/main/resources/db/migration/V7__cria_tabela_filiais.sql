CREATE TABLE filiais (
            id              UUID            NOT NULL DEFAULT gen_random_uuid(),
            nome            VARCHAR(150)    NOT NULL,
            regiao          VARCHAR(30)     NOT NULL,
            endereco        VARCHAR(250)    NOT NULL,
            cep             VARCHAR(10)     NOT NULL,
            cidade          VARCHAR(100)    NOT NULL,
            uf              VARCHAR(2)      NOT NULL,
            telefone        VARCHAR(20),
            email           VARCHAR(100),
            status          VARCHAR(20)     NOT NULL DEFAULT 'ATIVA',
            criado_em       TIMESTAMP       NOT NULL,
            atualizado_em   TIMESTAMP,
            criado_por      VARCHAR(100),
            atualizado_por  VARCHAR(100),

            CONSTRAINT pk_filiais PRIMARY KEY (id)
);

ALTER TABLE viagens
    ADD CONSTRAINT fk_viagens_filial
        FOREIGN KEY (filial_destino_id) REFERENCES filiais(id);

CREATE INDEX idx_filiais_regiao ON filiais(regiao);
CREATE INDEX idx_filiais_status ON filiais(status);

COMMENT ON TABLE filiais IS 'Filiais regionais do PetTrack para distribuição local';
COMMENT ON COLUMN filiais.regiao IS 'Macrorregião do CD que abastece esta filial';

INSERT INTO filiais (id, nome, regiao, endereco, cep, cidade, uf, status, criado_em) VALUES
            (gen_random_uuid(), 'Filial Norte', 'NORTE', 'Av. Eduardo Ribeiro, 520', '69010-001', 'Manaus', 'AM', 'ATIVA', NOW()),
            (gen_random_uuid(), 'Filial Nordeste', 'NORDESTE', 'Av. Getúlio Vargas, 300', '40010-020', 'Salvador', 'BA', 'ATIVA', NOW()),
            (gen_random_uuid(), 'Filial Centro-Oeste', 'CENTRO_OESTE', 'SCS Quadra 6, Bloco A', '70316-900', 'Brasília', 'DF', 'ATIVA', NOW()),
            (gen_random_uuid(), 'Filial Sudeste', 'SUDESTE', 'Av. Presidente Vargas, 1000', '20071-003', 'Rio de Janeiro', 'RJ', 'ATIVA', NOW()),
            (gen_random_uuid(), 'Filial São Paulo', 'SAO_PAULO', 'Av. Paulista, 1000', '01310-100', 'São Paulo', 'SP', 'ATIVA', NOW()),
            (gen_random_uuid(), 'Filial Sul', 'SUL', 'Rua XV de Novembro, 800', '80020-310', 'Curitiba', 'PR', 'ATIVA', NOW());