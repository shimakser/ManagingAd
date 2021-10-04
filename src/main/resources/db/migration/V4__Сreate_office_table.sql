CREATE TABLE IF NOT EXISTS office
(
    id             serial primary key,
    office_title   varchar(32) not null,
    office_address varchar(32) not null
);

INSERT INTO office(id, office_title, office_address)
VALUES (1, 'office_title', 'office_address');
INSERT INTO office(id, office_title, office_address)
VALUES (2, 'new_title', 'new_address');
INSERT INTO office(id, office_title, office_address)
VALUES (3, 'some_title', 'some_address');
