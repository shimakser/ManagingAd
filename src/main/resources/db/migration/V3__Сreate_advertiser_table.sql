CREATE TABLE IF NOT EXISTS advertiser (
    id                     serial primary key,
    advertiser_title       varchar(32) not null,
    advertiser_description text not null,
    creator_id             integer references usr(id),
    advertiser_deleted     boolean not null
);
