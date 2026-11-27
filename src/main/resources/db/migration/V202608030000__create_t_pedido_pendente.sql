-- Must precede V202608030001, which adds possui_form_distancia.
-- Existing environments may already contain this table.
CREATE TABLE IF NOT EXISTS paef.t_pedido_pendente (
    id int4 NOT NULL,
    data_pedido timestamp NOT NULL,
    dm_estado varchar(255) NOT NULL,
    process_type varchar(255),
    process_type_name varchar(255) NOT NULL,
    task_cod varchar(255) NOT NULL,
    task_name varchar(255),
    user_registo varchar(255) NOT NULL,
    "xml" bytea,
    id_alvara varchar(255),
    id_entidade varchar(255),
    possui_form_out_catalog bool DEFAULT false,
    fase_atual varchar(255),
    id_pedido varchar,
    obs varchar(4000),
    CONSTRAINT t_pedido_pendente_pkey PRIMARY KEY (id),
    CONSTRAINT t_pedido_pendente_fk FOREIGN KEY (id_entidade)
        REFERENCES paef.paef_t_entidade(id),
    CONSTRAINT t_pedido_pendente_fk_1 FOREIGN KEY (id_alvara)
        REFERENCES paef.paef_t_alvara(id),
    CONSTRAINT t_pedido_pendente_t_pedido_fk FOREIGN KEY (id_pedido)
        REFERENCES paef.t_pedido(id)
);
