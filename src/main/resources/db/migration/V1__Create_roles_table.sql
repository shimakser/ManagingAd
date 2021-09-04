CREATE TABLE IF NOT EXISTS roles (
    id serial primary key,
    role     varchar(32) not null
);

INSERT INTO roles(id, role) VALUES (0, 'USER');
INSERT INTO roles(id, role) VALUES (1, 'ADMIN');