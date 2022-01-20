CREATE TABLE IF NOT EXISTS translations(
    id                      integer not null,
    entity_type             varchar not null,
    entity_name             varchar not null,
    translation_language    varchar not null,
    name_translation        varchar not null,
    PRIMARY KEY(id, entity_name)
);

INSERT INTO translations(id, entity_type, entity_name, translation_language, name_translation)
VALUES(1, 'Currency', 'USD', 'RU', 'Доллар');
INSERT INTO translations(id, entity_type, entity_name, translation_language, name_translation)
VALUES(2, 'Currency', 'BYN', 'RU', 'Беларуский рубль');
