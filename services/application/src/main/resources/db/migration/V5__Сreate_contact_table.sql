CREATE TABLE IF NOT EXISTS contact
(
    id serial                  primary key,
    contact_phone_number        varchar(32) not null,
    contact_email              varchar(32) not null,
    contact_site               varchar(60) not null,
    office_id                  integer references office(id)
);

INSERT INTO contact(id, contact_phone_number, contact_email, contact_site, office_id)
VALUES(1, '+375123456789', 'contact@gmail.com', 'contact/site.by', 1);
INSERT INTO contact(id, contact_phone_number, contact_email, contact_site, office_id)
VALUES(2, '+375123456789', 'new@gmail.com', 'new/site.by', 2);
INSERT INTO contact(id, contact_phone_number, contact_email, contact_site, office_id)
VALUES(3, '+375123456789', 'some@gmail.com', 'some/site.by', 2);