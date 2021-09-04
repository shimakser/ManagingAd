CREATE TABLE IF NOT EXISTS campaign (
    id                     serial primary key,
    campaign_title         varchar(32) not null,
    campaign_description   text        not null,
    image                  varchar(32) not null,
    countries              varchar(50) not null,
    languages              varchar(50) not null,
    age                    varchar(32) not null,
    geolocation            varchar(50) not null,
    advertiser_id          integer references advertiser(id)
);