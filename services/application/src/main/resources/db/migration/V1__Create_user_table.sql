CREATE TABLE IF NOT EXISTS usr (
    id serial       primary key,
    username        varchar(32) not null,
    user_email      varchar(32) not null,
    password        varchar(60) not null,
    user_role       varchar(32),
    user_deleted    boolean not null
);

INSERT INTO usr(id, username, user_email, password, user_role, user_deleted)
VALUES(1, 'admin', 'admin@gmail.com', '$2a$12$KL87I7JdvLGHO25G.iPSWO2HEBCF3bYP3JwIOLuUwKi0ifKbIkFQS', 'ADMIN', false);
INSERT INTO usr(id, username, user_email, password, user_role, user_deleted)
VALUES(2, 'user1', 'firstusr@gmail.com', '$2a$12$qBiqmJ20I160f3/5ibf9JexU78H.StVa6sB7iykRcqil0KuQSPQ3a', 'USER', true);
INSERT INTO usr(id, username, user_email, password, user_role, user_deleted)
VALUES(3, 'user2', 'secondusr@gmail.com', '$2a$12$IfsGY8b9QlH3hiRs9WD1G.25145TMKEjyhB.VTtkn5i5.YuwbY.Da', 'USER', false);