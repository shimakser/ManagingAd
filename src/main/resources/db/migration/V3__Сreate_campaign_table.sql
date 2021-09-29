CREATE TABLE IF NOT EXISTS campaign
(
    id                    serial primary key,
    campaign_title        varchar(32) not null,
    campaign_description  text        not null,
    image                 varchar(32) not null,
    country               varchar(50) not null,
    language              varchar(50) not null,
    age                   varchar(32) not null,
    geolocation           varchar(50) not null,
    advertiser_id         integer references advertiser (id),
    campaign_deleted      boolean     not null,
    campaign_created_date timestamp   not null,
    campaign_deleted_date timestamp,
    campaign_delete_notes text
);

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (1, 'FirstCamp', 'First campaigns description', 'link_to_the_first_image', 'Belarus', 'Russian',
        '14+', 'geoloc_to_the_first_campaign', 1, false, '17.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date, campaign_deleted_date,
                     campaign_delete_notes)
VALUES (2, 'SecondCamp', 'Second campaigns description', 'link_to_the_second_image', 'Russia', 'Russian',
        '18+', 'geoloc_to_the_second_campaign', 1, true, '23.09.2021', '29.09.2021',
        'Note after deleting second campaign');

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (3, 'ThirdCamp', 'Third campaigns description', 'link_to_the_third_image', 'Belarus', 'Belarus',
        '16+', 'geoloc_to_the_third_campaign', 4, false, '04.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date, campaign_deleted_date,
                     campaign_delete_notes)
VALUES (4, 'FourthCamp', 'Fourth campaigns description', 'link_to_the_fourth_image', 'Poland', 'Polish',
        '18+', 'geoloc_to_the_fourth_campaign', 4, true, '11.09.2021', '19.09.2021', 'Note after deleting fourth campaign');

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date, campaign_deleted_date,
                     campaign_delete_notes)
VALUES (5, 'FifthCamp', 'Fifth campaigns description', 'link_to_the_fifth_image', 'Belarus', 'Russian',
        '16+', 'geoloc_to_the_fifth_campaign', 3, true, '01.09.2021', '07.09.2021',
        'Some text from first campaign note for searching by substring');

INSERT INTO campaign(id, campaign_title, campaign_description, image, country, language,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (6, 'SixthCamp', 'Sixth campaigns description', 'link_to_the_sixth_image', 'Russian', 'Russian',
        '16+', 'geoloc_to_the_sixth_campaign', 4, false, '02.09.2021');


