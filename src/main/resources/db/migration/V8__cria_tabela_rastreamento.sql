CREATE TABLE historico_rastreamento (
            id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
            pedido_id           UUID            NOT NULL,
            status_anterior     VARCHAR(40),
            status_novo         VARCHAR(40)     NOT NULL,
            usuario_id          UUID,
            localizacao         VARCHAR(150),
            observacao          VARCHAR(500),
            registrado_em       TIMESTAMP       NOT NULL DEFAULT NOW(),

            CONSTRAINT pk_historico_rastreamento PRIMARY KEY (id),
            CONSTRAINT fk_rastreamento_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
            CONSTRAINT fk_rastreamento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_rastreamento_pedido ON historico_rastreamento(pedido_id);
CREATE INDEX idx_rastreamento_registrado_em ON historico_rastreamento(registrado_em);

COMMENT ON TABLE historico_rastreamento IS 'Histórico completo de mudanças de status dos pedidos';
COMMENT ON COLUMN historico_rastreamento.localizacao IS 'Onde o pedido estava quando o status mudou';