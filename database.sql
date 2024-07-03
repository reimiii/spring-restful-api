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