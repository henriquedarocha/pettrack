CREATE TABLE gaiolas (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            codigo              VARCHAR(30)     NOT NULL,
            regiao_cd           VARCHAR(30)     NOT NULL,
            status              VARCHAR(20)     NOT NULL DEFAULT 'ABERTA',
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_gaiolas PRIMARY KEY (id),
            CONSTRAINT uq_gaiolas_codigo UNIQUE (codigo)
);

CREATE TABLE pedidos (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            numero_pedido       VARCHAR(30)     NOT NULL,
            cliente_nome        VARCHAR(150)    NOT NULL,
            cliente_cpf         VARCHAR(14)     NOT NULL,
            cliente_email       VARCHAR(100),
            cliente_telefone    VARCHAR(20),
            endereco_entrega    VARCHAR(250)    NOT NULL,
            cep_entrega         VARCHAR(10)     NOT NULL,
            cidade_entrega      VARCHAR(100)    NOT NULL,
            uf_entrega          VARCHAR(2)      NOT NULL,
            regiao_cd           VARCHAR(30)     NOT NULL,
            subregiao_filial    VARCHAR(50),
            status              VARCHAR(40)     NOT NULL DEFAULT 'PEDIDO_CRIADO',
            valor_total         NUMERIC(10, 2),
            peso_total_kg       NUMERIC(10, 3),
            observacoes         VARCHAR(500),
            gaiola_id           UUID,
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_pedidos PRIMARY KEY (id),
            CONSTRAINT uq_pedidos_numero UNIQUE (numero_pedido),
            CONSTRAINT fk_pedidos_gaiola FOREIGN KEY (gaiola_id) REFERENCES gaiolas(id)
);

CREATE TABLE itens_pedido (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            pedido_id           UUID            NOT NULL,
            produto_id          UUID            NOT NULL,
            item_estoque_id     UUID,
            quantidade          INTEGER         NOT NULL,
            preco_unitario      NUMERIC(10, 2)  NOT NULL,
            preco_total         NUMERIC(10, 2)  NOT NULL,
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_itens_pedido PRIMARY KEY (id),
            CONSTRAINT fk_itens_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
            CONSTRAINT fk_itens_pedido_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
            CONSTRAINT fk_itens_pedido_estoque FOREIGN KEY (item_estoque_id) REFERENCES itens_estoque(id)
);

CREATE INDEX idx_pedidos_status ON pedidos(status);
CREATE INDEX idx_pedidos_regiao_cd ON pedidos(regiao_cd);
CREATE INDEX idx_pedidos_cep ON pedidos(cep_entrega);
CREATE INDEX idx_itens_pedido_pedido ON itens_pedido(pedido_id);
CREATE INDEX idx_itens_pedido_produto ON itens_pedido(produto_id);

COMMENT ON TABLE gaiolas IS 'Gaiolas de separação por região no setor de e-commerce';
COMMENT ON TABLE pedidos IS 'Pedidos de clientes do e-commerce';
COMMENT ON TABLE itens_pedido IS 'Itens individuais de cada pedido';
COMMENT ON COLUMN pedidos.regiao_cd IS 'Região determinada pelo CEP — define qual gaiola o pedido vai';
COMMENT ON COLUMN pedidos.subregiao_filial IS 'Preenchida na filial para definir qual van fará a entrega';
COMMENT ON COLUMN itens_pedido.item_estoque_id IS 'Vinculado quando o operador faz a separação física na baia';

INSERT INTO gaiolas (id, codigo, regiao_cd, status, criado_em) VALUES
       (gen_random_uuid(), 'GAIOLA-NORTE', 'NORTE', 'ABERTA', NOW()),
       (gen_random_uuid(), 'GAIOLA-NORDESTE', 'NORDESTE', 'ABERTA', NOW()),
       (gen_random_uuid(), 'GAIOLA-CENTRO-OESTE', 'CENTRO_OESTE', 'ABERTA', NOW()),
       (gen_random_uuid(), 'GAIOLA-SUDESTE', 'SUDESTE', 'ABERTA', NOW()),
       (gen_random_uuid(), 'GAIOLA-SAO-PAULO', 'SAO_PAULO', 'ABERTA', NOW()),
       (gen_random_uuid(), 'GAIOLA-SUL', 'SUL', 'ABERTA', NOW());