DROP TABLE IF EXISTS t_accounts CASCADE;
DROP TABLE IF EXISTS t_tokens CASCADE;

CREATE TABLE t_accounts
(
    account_id            BIGINT  NOT NULL,
    email                 VARCHAR(255),
    password              VARCHAR(255),
    role                  VARCHAR(20),
    is_account_non_locked BOOLEAN NOT NULL,
    is_enabled            BOOLEAN NOT NULL,
    CONSTRAINT pk_t_accounts PRIMARY KEY (account_id)
);

CREATE TABLE t_tokens
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    jwt        VARCHAR(255)                            NOT NULL,
    account_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_t_tokens PRIMARY KEY (id)
);

ALTER TABLE t_tokens
    ADD CONSTRAINT uc_t_tokens_jwt UNIQUE (jwt);

ALTER TABLE t_tokens
    ADD CONSTRAINT FK_T_TOKENS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES t_accounts (account_id);