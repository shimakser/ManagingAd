CREATE TABLE IF NOT EXISTS advertiser
(
    id                     serial primary key,
    advertiser_title       varchar(32) not null,
    advertiser_description text        not null,
    creator_id             integer references usr (id),
    advertiser_deleted     boolean     not null
);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
VALUES (1, 'firstAdvertiser', 'First advertisers description', 3, false);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
VALUES (2, 'secondAdvertiser', 'Second advertisers description', 2, true);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
VALUES (3, 'thirdAdvertiser', 'Third advertisers description', 1, false);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
VALUES (4, 'fourthAdvertiser', 'Fourth advertisers description', 2, false);

INSERT INTO advertiser(id, advertiser_title, advertiser_description, creator_id, advertiser_deleted)
VALUES (5, 'fifthAdvertiser', 'Fifth advertisers description', 3, true);
