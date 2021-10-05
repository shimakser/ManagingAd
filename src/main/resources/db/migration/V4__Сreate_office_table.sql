CREATE TABLE IF NOT EXISTS office
(
    id                 serial primary key,
    office_title       varchar(32) not null,
    office_address     varchar(32) not null,
    office_description text
);

INSERT INTO office(id, office_title, office_address, office_description)
VALUES (1, 'office_title', 'office_address', '{"1":"first_description","2":"second_description"}');
INSERT INTO office(id, office_title, office_address)
VALUES (2, 'new_title', 'new_address');
INSERT INTO office(id, office_title, office_address, office_description)
VALUES (3, 'some_title', 'some_address', '{"1":"first_description"}');
