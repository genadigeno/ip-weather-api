--changeset author:geno
create table if not exists ip_requests (
    id serial primary key,
    ip_address varchar(50) not null ,
    created_on timestamp not null
);