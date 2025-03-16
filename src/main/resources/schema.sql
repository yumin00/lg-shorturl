DROP TABLE IF EXISTS Member;
DROP TABLE IF EXISTS Url_Mapping;

create table Member
(
    id integer not null,
    name varchar(255) not null,
    primary key (id)
);

create table Url_Mapping
(
    id bigint auto_increment not null,
    original_url varchar(255) not null,
    short_url varchar(8) not null unique,
    created_at timestamp default current_timestamp,
    primary key (id)
);

create INDEX idx_original_url ON Url_Mapping(original_url);
create UNIQUE INDEX idx_short_url ON Url_Mapping(short_url);
