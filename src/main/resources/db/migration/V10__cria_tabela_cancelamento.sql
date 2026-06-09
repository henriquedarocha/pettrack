CREATE TABLE solicitacoes_cancelamento (
            id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
            pedido_id               UUID            NOT NULL,
            motivo                  VARCHAR(40)     NOT NULL,
            descricao               VARCHAR(500),
            status_pedido_momento   VARCHAR(40)     NOT NULL,
            status                  VARCHAR(30)     NOT NULL DEFAULT 'SOLICITADO',
            bloqueado_motivo        VARCHAR(300),
            data_resolucao          TIMESTAMP,
            requer_retorno_cd       BOOLEAN         NOT NULL DEFAULT FALSE,
            criado_em               TIMESTAMP       NOT NULL,
            atualizado_em           TIMESTAMP,
            criado_por              VARCHAR(100),
            atualizado_por          VARCHAR(100),

            CONSTRAINT pk_solicitacoes_cancelamento PRIMARY KEY (id),
            CONSTRAINT fk_cancelamento_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);

CREATE INDEX idx_cancelamento_pedido ON solicitacoes_cancelamento(pedido_id);
CREATE INDEX idx_cancelamento_status ON solicitacoes_cancelamento(status);

COMMENT ON TABLE solicitacoes_cancelamento IS 'Solicitações de cancelamento de pedidos';
COMMENT ON COLUMN solicitacoes_cancelamento.status_pedido_momento IS 'Status do pedido no momento da solicitação';
COMMENT ON COLUMN solicitacoes_cancelamento.requer_retorno_cd IS 'TRUE quando o produto precisa voltar ao CD antes de ir ao estoque';