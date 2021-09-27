CREATE TABLE IF NOT EXISTS campaign
(
    id                   serial primary key,
    campaign_title       varchar(32) not null,
    campaign_description text        not null,
    image                varchar(32) not null,
    countries            varchar(50) not null,
    languages            varchar(50) not null,
    age                  varchar(32) not null,
    geolocation          varchar(50) not null,
    advertiser_id        integer references advertiser(id),
    campaign_deleted     boolean     not null,
    campaign_created_date date not null,
    campaign_deleted_date date
);

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (1, 'firstCampaign', 'First campaigns description', 'link_to_the_first_image', 'Belarus', 'Russian',
        '14+', 'geoloc_to_the_first_campaign', 1, false, '17.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date)
VALUES (2, 'secondCampaign', 'Second campaigns description', 'link_to_the_second_image', 'Belarus', 'Russian',
        '18+', 'geoloc_to_the_second_campaign', 1, false, '23.09.2021');

INSERT INTO campaign(id, campaign_title, campaign_description, image, countries, languages,
                     age, geolocation, advertiser_id, campaign_deleted, campaign_created_date, campaign_deleted_date)
VALUES (3, 'thirdCampaign', 'Third campaigns description', 'link_to_the_third_image', 'Belarus', 'Russian',
        '18+', 'geoloc_to_the_third_campaign', 2, true, '17.09.2021', '27.09.2021');