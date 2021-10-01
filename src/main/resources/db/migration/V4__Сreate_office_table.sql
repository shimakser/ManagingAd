CREATE TABLE IF NOT EXISTS office
(
    id                    serial primary key,
    office_title          varchar(32) not null,
    office_address        varchar(32) not null
);