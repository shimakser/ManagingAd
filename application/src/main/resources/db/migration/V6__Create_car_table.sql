CREATE TABLE IF NOT EXISTS car
(
    car_id    serial primary key,
    car_name varchar(32) not null,
    car_creation_date  timestamp   not null,
    car_count integer     not null
);

INSERT INTO car(car_id, car_name, car_creation_date, car_count)
VALUES (1, 'Audi', '2021-11-08', 2);
INSERT INTO car(car_id, car_name, car_creation_date, car_count)
VALUES (2, 'BMW', '2021-10-09', 3);
INSERT INTO car(car_id, car_name, car_creation_date, car_count)
VALUES (3, 'Volkswagen', '2021-09-10', 5);