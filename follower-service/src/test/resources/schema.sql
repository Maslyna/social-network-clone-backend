DROP TABLE IF EXISTS t_user_connections CASCADE;
DROP TABLE IF EXISTS t_user_followers CASCADE;

CREATE TABLE t_user_connections
(
    user_id                  BIGINT  NOT NULL,
    is_enabled_notifications BOOLEAN NOT NULL,
    is_public_followers      BOOLEAN NOT NULL,
    is_public_subscriptions  BOOLEAN NOT NULL,
    CONSTRAINT pk_t_user_connections PRIMARY KEY (user_id)
);

CREATE TABLE t_user_followers
(
    follower_id BIGINT NOT NULL,
    user_id     BIGINT NOT NULL
);

ALTER TABLE t_user_followers
    ADD CONSTRAINT fk_tusefol_on_follower FOREIGN KEY (follower_id) REFERENCES t_user_connections (user_id);

ALTER TABLE t_user_followers
    ADD CONSTRAINT fk_tusefol_on_user FOREIGN KEY (user_id) REFERENCES t_user_connections (user_id);