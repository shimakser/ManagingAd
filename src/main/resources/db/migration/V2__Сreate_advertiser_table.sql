CREATE TABLE IF NOT EXISTS advertiser (
    id                     serial primary key,
    advertiser_title       varchar(32) not null,
    advertiser_description text not null,
    creator_id             integer references usr(id),
    advertiser_deleted     boolean not null
);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
    VALUES (1, 'firstAdvertiser', 'First advertisers description', 3, false);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
    VALUES (2, 'secondAdvertiser', 'Second advertisers description', 2, true);
