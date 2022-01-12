CREATE TABLE IF NOT EXISTS office
(
    id                 serial primary key,
    office_title       varchar(32)      not null,
    office_address     varchar(32)      not null,
    office_price       double precision not null,
    office_description json
);

INSERT INTO office(id, office_title, office_address, office_price, office_description)
VALUES (1, 'office_title', 'office_address', 50.5, '{"1": "first_description","2": "second_description"}');
INSERT INTO office(id, office_title, office_address, office_price)
VALUES (2, 'new_title', 'new_address', 25.7);
INSERT INTO office(id, office_title, office_address, office_price, office_description)
VALUES (3, 'some_title', 'some_address', 38.2, '{"1": "first_description"}');
