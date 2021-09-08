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
VALUES(2, 'user1', 'firstusr@gmail.com', '$2a$12$5I2WjVkjq2F5yts2aA8RSe34cMmkGSczOtb7DVWQGIjIDJG6z063q', 'USER', true);
INSERT INTO usr(id, username, user_email, password, user_role, user_deleted)
VALUES(3, 'user2', 'secondusr@gmail.com', '$2a$12$7ksh4VWfbAxA5H.fGskMF.EaArEwd08.StgFfK/LmYlYJ60vrxltS', 'USER', false);