CREATE TABLE IF NOT EXISTS usr
(
    id                 serial primary key,
    username           varchar      not null,
    password           varchar      not null
);

INSERT INTO usr(id, username, password)
VALUES(1, 'user1', '$2a$12$qBiqmJ20I160f3/5ibf9JexU78H.StVa6sB7iykRcqil0KuQSPQ3a');
