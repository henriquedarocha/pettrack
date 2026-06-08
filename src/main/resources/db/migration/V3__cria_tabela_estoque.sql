CREATE TABLE baias (

    id                      UUID                NOT NULL DEFAULT gen_random_uuid(),
    codigo                  VARCHAR(10)         NOT NULL,
    descricao               VARCHAR(100)        NOT NULL,
    categoria_produto       VARCHAR(50)         NOT NULL,
    tipo_armazenamento      VARCHAR(30)         NOT NULL,
    capacidade_kg           NUMERIC(10, 3)      NOT NULL,
    capacidade_unidades     INTEGER             NOT NULL,
    peso_atual_kg           NUMERIC(10, 3)      NOT NULL DEFAULT 0,
    unidades_atuais         INTEGER             NOT NULL DEFAULT 0,
    status                  VARCHAR(20)         NOT NULL DEFAULT 'DISPONIVEL',
    temperatura_minima      DOUBLE PRECISION,
    temperatura_maxima      DOUBLE PRECISION,
    criado_em               TIMESTAMP           NOT NULL,
    atualizado_em           TIMESTAMP,
    criado_por              VARCHAR(100),
    atualizado_por          VARCHAR(100),

    CONSTRAINT pk_baias PRIMARY KEY (id),
    CONSTRAINT uq_baias_codigo UNIQUE (codigo)

);

CREATE TABLE itens_estoque (

    id                      UUID                NOT NULL DEFAULT gen_random_uuid(),
    produto_id              UUID                NOT NULL,
    baia_id                 UUID                NOT NULL,
    numero_lote             VARCHAR(50)         NOT NULL,
    data_fabricacao         DATE,
    data_validade           DATE,
    quantidade              INTEGER             NOT NULL,
    numero_serie            VARCHAR(100),
    nota_fiscal_entrada     VARCHAR(50),
    status                  VARCHAR(20)         NOT NULL DEFAULT 'DISPONIVEL',
    criado_em               TIMESTAMP           NOT NULL,
    atualizado_em           TIMESTAMP,
    criado_por              VARCHAR(100),
    atualizado_por          VARCHAR(100),

    CONSTRAINT pk_itens_estoque PRIMARY KEY (id),
    CONSTRAINT fk_itens_estoque_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_itens_estoque_baia FOREIGN KEY (baia_id) REFERENCES baias(id)

);

CREATE INDEX idx_itens_estoque_produto ON itens_estoque(produto_id);
CREATE INDEX idx_itens_estoque_baia ON itens_estoque(baia_id);
CREATE INDEX idx_itens_estoque_status ON itens_estoque(status);
CREATE INDEX idx_itens_estoque_data_validade ON itens_estoque(data_validade);

COMMENT ON TABLE baias IS 'Baias físicas de armazenamento no CD';
COMMENT ON TABLE itens_estoque IS 'Itens físicos em estoque por lote e baia';
COMMENT ON COLUMN itens_estoque.numero_serie IS 'Usado para equipamentos veterinários com rastreio individual';
COMMENT ON COLUMN baias.temperatura_minima IS 'Temperatura mínima em ºC para baias refrigeradas';
COMMENT ON INDEX idx_itens_estoque_data_validade IS 'Otimiza a busca de itens próximos do vencimento pelo Scheduler';