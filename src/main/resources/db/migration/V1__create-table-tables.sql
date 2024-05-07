CREATE SEQUENCE public.client_id_seq INCREMENT BY 1 START
WITH
    1 MINVALUE 1 MAXVALUE 1000000000 CACHE 1;

CREATE TABLE public.cliente (
    id bigint NOT NULL DEFAULT nextval ('client_id_seq'),
    nome varchar(100) NOT NULL,
    tipo varchar(20) NOT NULL,
    ativo boolean NOT NULL,
    CONSTRAINT cliente_pk PRIMARY KEY (id)
);

CREATE SEQUENCE public.venda_id_seq INCREMENT BY 1 START
WITH
    1 MINVALUE 1 MAXVALUE 1000000000 CACHE 1;

CREATE TABLE public.venda (
    id bigint NOT NULL DEFAULT nextval ('venda_id_seq'),
    cliente_id bigint NOT NULL,
    tipo_cliente varchar(20) NOT NULL,
    valor numeric(38, 2) NOT NULL,
    data timestamp NOT NULL,
    ativo boolean NOT NULL,
    CONSTRAINT venda_pk PRIMARY KEY (id),
    constraint cliente_fk foreign key (cliente_id) references cliente (id)
);

CREATE SEQUENCE public.pato_id_seq INCREMENT BY 1 START
WITH
    1 MINVALUE 1 MAXVALUE 1000000000 CACHE 1;

CREATE TABLE public.pato (
    id bigint NOT NULL DEFAULT nextval ('pato_id_seq'),
    nome varchar(100) NOT NULL,
    tipo varchar(20) NOT NULL,
    mae_id bigint,
    venda_id bigint,
    valor numeric(38, 2) NOT NULL,
    status varchar(20) NOT NULL,
    CONSTRAINT pato_pk PRIMARY KEY (id),
    constraint mae_fk foreign key (mae_id) references pato (id),
    constraint venda_fk foreign key (venda_id) references venda (id)
);

CREATE OR REPLACE FUNCTION data_venda()
RETURNS TRIGGER AS
$$
BEGIN
    NEW.data := NOW();
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER venda_trigger BEFORE
INSERT
    ON public.venda FOR EACH ROW
EXECUTE FUNCTION data_venda ();