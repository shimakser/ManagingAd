CREATE TABLE IF NOT EXISTS usr (
    id serial       primary key,
    username        varchar(32) not null,
    user_email      varchar(32) not null,
    password        varchar(60) not null,
    role_id         integer references roles(id),
    user_deleted    boolean not null
);