CREATE TABLE auth_roles (
    auth_id BIGINT NOT NULL,
    roles VARCHAR(255) NOT NULL,
    CONSTRAINT fk_auth FOREIGN KEY(auth_id) REFERENCES auths(id)
);