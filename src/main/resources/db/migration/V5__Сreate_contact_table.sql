CREATE TABLE IF NOT EXISTS contact
(
    id serial                  primary key,
    contact_phoneNumber        varchar(32) not null,
    contact_email              varchar(32) not null,
    contact_site               varchar(60) not null,
    office_id                  integer references office(id)
);