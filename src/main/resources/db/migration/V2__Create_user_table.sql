CREATE TABLE IF NOT EXISTS usr (
    id serial primary key,
    username     varchar(32) not null,
    user_email    varchar(32) not null,
    password varchar(60) not null,
    role_id integer references roles(id)
);

INSERT INTO usr(id, username, user_email, password, role_id)
    VALUES (1, 'Шиянов Максим', 'shimakser@gmail.com', 'root', 1);