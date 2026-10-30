CREATE TABLE if not exists paef.paef_t_notificacao_mensagem (
    id VARCHAR(128) PRIMARY KEY,
    id_notificacao VARCHAR(128) NOT NULL,
    email VARCHAR(254) NOT NULL,
    mensagem TEXT NOT NULL,
    data_registro TIMESTAMP NOT NULL,
    data_envio TIMESTAMP,
    CONSTRAINT fk_notificacao_mensagem_notificacao
        FOREIGN KEY (id_notificacao) REFERENCES paef.t_notificacao (id)
);

CREATE INDEX idx_notificacao_mensagem_historico
    ON paef.paef_t_notificacao_mensagem (id_notificacao, data_registro, id);
