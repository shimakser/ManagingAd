CREATE TABLE IF NOT EXISTS campaign
(
    id                    serial primary key,
    campaign_title        varchar(32) not null,
    campaign_description  text        not null,
    image                 varchar(32) not null,
    countries             varchar(50) not null,
    languages             varchar(50) not null,
    age                   varchar(32) not null,
    geolocation           varchar(50) not null,
    advertiser_id         integer references advertiser (id),
    campaign_deleted      boolean     not null,
    campaign_created_date date        not null,
    campaign_deleted_date date
);

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (1, 'FirstCamp', 'First campaigns description', 'link_to_the_first_image', 'Belarus', 'Russian',
        '14+', 'geoloc_to_the_first_campaign', 1, false, '17.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (2, 'SecondCamp', 'Second campaigns description', 'link_to_the_second_image', 'Russia', 'Russian',
        '18+', 'geoloc_to_the_second_campaign', 1, false, '23.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (3, 'ThirdCamp', 'Third campaigns description', 'link_to_the_third_image', 'Belarus', 'Belarus',
        '16+', 'geoloc_to_the_third_campaign', 4, false, '04.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (4, 'FourthCamp', 'Fourth campaigns description', 'link_to_the_fourth_image', 'Poland', 'Polish',
        '18+', 'geoloc_to_the_fourth_campaign', 4, false, '11.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date, campaign_deleted_date)
VALUES (5, 'FifthCamp', 'Fifth campaigns description', 'link_to_the_fifth_image', 'Belarus', 'Russian',
        '16+', 'geoloc_to_the_fifth_campaign', 3, true, '01.09.2021', '07.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (6, 'SixthCamp', 'Sixth campaigns description', 'link_to_the_sixth_image', 'Russian', 'Russian',
        '16+', 'geoloc_to_the_sixth_campaign', 4, false, '02.09.2021');


