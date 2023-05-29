--changeset author:geno
create table if not exists weather_conditions (
    id serial primary key,
    ip_request_id int null,
    temperature_celsius real null,
    temperature_fahrenheit real null,
    wind_kph real null,
    wind_mph real null,
    condition varchar(150) null,
    condition_code int null,
    humidity int null,
    created_on timestamp not null,
    CONSTRAINT fk_customer FOREIGN KEY(ip_request_id) REFERENCES ip_requests(id)
);