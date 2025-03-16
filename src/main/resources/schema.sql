DROP TABLE IF EXISTS Member;
DROP TABLE IF EXISTS Url_Mapping;
DROP TABLE IF EXISTS Url_Access_log;

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

create table Url_Access_log
(
    id bigint auto_increment not null,
    url_mapping_id bigint not null,
    accessed_at timestamp default current_timestamp,
    primary key (id),
    foreign key (url_mapping_id) references url_mapping(id)
);