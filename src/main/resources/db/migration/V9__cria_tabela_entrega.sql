CREATE TABLE entregas_finais (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            pedido_id           UUID            NOT NULL,
            filial_id           UUID            NOT NULL,
            veiculo_id          UUID,
            subregiao           VARCHAR(50)     NOT NULL,
            status              VARCHAR(30)     NOT NULL DEFAULT 'AGUARDANDO_DESPACHO',
            data_despacho       TIMESTAMP,
            data_entrega        TIMESTAMP,
            observacoes         VARCHAR(300),
            criado_em           TIMESTAMP       NOT NULL,
            atualizado_em       TIMESTAMP,
            criado_por          VARCHAR(100),
            atualizado_por      VARCHAR(100),

            CONSTRAINT pk_entregas_finais PRIMARY KEY (id),
            CONSTRAINT fk_entregas_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
            CONSTRAINT fk_entregas_filial FOREIGN KEY (filial_id) REFERENCES filiais(id),
            CONSTRAINT fk_entregas_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);

CREATE INDEX idx_entregas_status ON entregas_finais(status);
CREATE INDEX idx_entregas_subregiao ON entregas_finais(subregiao);
CREATE INDEX idx_entregas_pedido ON entregas_finais(pedido_id);

COMMENT ON TABLE entregas_finais IS 'Entregas ao cliente final feitas pelas filiais';
COMMENT ON COLUMN entregas_finais.subregiao IS 'Subregião determinada na filial para definir a van correta';