--changeset author:geno
create table if not exists geolocations (
    id serial primary key,
    ip_request_id int null,
    country varchar(150) null,
    city varchar(150) null,
    latitude varchar(50) null,
    longitude varchar(50) null,
    created_on timestamp not null,
    CONSTRAINT fk_customer FOREIGN KEY(ip_request_id) REFERENCES ip_requests(id)
);