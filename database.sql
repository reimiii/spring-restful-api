create database restful_api;

use restful_api;

create table users
(
    username         varchar(100) not null,
    password         varchar(100) not null,
    name             varchar(100) not null,
    token            varchar(100),
    token_expired_at bigint,
    primary key (username),
    unique (token)
) engine innodb;

select *
from users;

desc users;

create table contacts
(
    id         varchar(100) not null,
    username   varchar(100) not null,
    first_name varchar(100) not null,
    last_name  varchar(100),
    phone      varchar(100),
    email      varchar(100),
    primary key (id),
    foreign key fk_users_contacts (username) references users (username)
) engine innodb;

select *
from contacts;
