CREATE TABLE transportadoras (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            nome                VARCHAR(150)    NOT NULL,
            cnpj                VARCHAR(20),
            tipo                VARCHAR(20)     NOT NULL,
            telefone            VARCHAR(20),
            email_contato       VARCHAR(100),
            ativa               BOOLEAN         NOT NULL DEFAULT TRUE,
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_transportadoras PRIMARY KEY (id)
);

CREATE TABLE transportadora_regioes (
            transportadora_id   UUID            NOT NULL,
            regiao              VARCHAR(30)     NOT NULL,

            CONSTRAINT pk_transportadora_regioes PRIMARY KEY (transportadora_id, regiao),
            CONSTRAINT fk_transportadora_regioes FOREIGN KEY (transportadora_id)
            REFERENCES transportadoras(id)
);

CREATE TABLE veiculos (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            placa               VARCHAR(20)     NOT NULL,
            tipo                VARCHAR(30)     NOT NULL,
            transportadora_id   UUID            NOT NULL,
            capacidade_kg       NUMERIC(10, 3)  NOT NULL,
            capacidade_pallets  INTEGER,
            disponivel          BOOLEAN         NOT NULL DEFAULT TRUE,
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_veiculos PRIMARY KEY (id),
            CONSTRAINT uq_veiculos_placa UNIQUE (placa),
            CONSTRAINT fk_veiculos_transportadora FOREIGN KEY (transportadora_id)
            REFERENCES transportadoras(id)
);

CREATE TABLE pallets (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            codigo_pallet       VARCHAR(30)     NOT NULL,
            regiao_destino      VARCHAR(30)     NOT NULL,
            transportadora_id   UUID,
            peso_maximo_kg      NUMERIC(10, 3)  NOT NULL,
            peso_atual_kg       NUMERIC(10, 3)  NOT NULL DEFAULT 0,
            status              VARCHAR(30)     NOT NULL DEFAULT 'EM_MONTAGEM',
            validado_por_id     UUID,
            observacoes         VARCHAR(300),
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_pallets PRIMARY KEY (id),
            CONSTRAINT uq_pallets_codigo UNIQUE (codigo_pallet),
            CONSTRAINT fk_pallets_transportadora FOREIGN KEY (transportadora_id)
            REFERENCES transportadoras(id),
            CONSTRAINT fk_pallets_validado_por FOREIGN KEY (validado_por_id)
            REFERENCES usuarios(id)
);

CREATE TABLE pallet_gaiolas (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            pallet_id           UUID            NOT NULL,
            gaiola_id           UUID            NOT NULL,

            CONSTRAINT pk_pallet_gaiolas PRIMARY KEY (id),
            CONSTRAINT fk_pallet_gaiolas_pallet FOREIGN KEY (pallet_id) REFERENCES pallets(id),
            CONSTRAINT fk_pallet_gaiolas_gaiola FOREIGN KEY (gaiola_id) REFERENCES gaiolas(id)
);

CREATE TABLE viagens (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            codigo_viagem       VARCHAR(30)     NOT NULL,
            veiculo_id          UUID            NOT NULL,
            filial_destino_id   UUID            NOT NULL,
            regiao_cd           VARCHAR(30)     NOT NULL,
            status              VARCHAR(30)     NOT NULL DEFAULT 'AGUARDANDO_CARREGAMENTO',
            data_saida          TIMESTAMP,
            data_chegada        TIMESTAMP,
            observacoes         VARCHAR(300),
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_viagens PRIMARY KEY (id),
            CONSTRAINT uq_viagens_codigo UNIQUE (codigo_viagem),
            CONSTRAINT fk_viagens_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);

CREATE TABLE viagem_pallets (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            viagem_id           UUID            NOT NULL,
            pallet_id           UUID            NOT NULL,

            CONSTRAINT pk_viagem_pallets PRIMARY KEY (id),
            CONSTRAINT fk_viagem_pallets_viagem FOREIGN KEY (viagem_id) REFERENCES viagens(id),
            CONSTRAINT fk_viagem_pallets_pallet FOREIGN KEY (pallet_id) REFERENCES pallets(id)
);

CREATE INDEX idx_pallets_status ON pallets(status);
CREATE INDEX idx_pallets_regiao ON pallets(regiao_destino);
CREATE INDEX idx_viagens_status ON viagens(status);
CREATE INDEX idx_viagens_regiao ON viagens(regiao_cd);

COMMENT ON TABLE transportadoras IS 'Transportadoras próprias e terceirizadas';
COMMENT ON TABLE veiculos IS 'Veículos das transportadoras';
COMMENT ON TABLE pallets IS 'Pallets montados no setor de transporte';
COMMENT ON TABLE pallet_gaiolas IS 'Gaiolas que compõem cada pallet';
COMMENT ON TABLE viagens IS 'Viagens dos caminhões do CD para as filiais';
COMMENT ON TABLE viagem_pallets IS 'Pallets carregados em cada viagem';