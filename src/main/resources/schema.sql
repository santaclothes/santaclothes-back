CREATE TABLE IF NOT EXISTS "USER"
(
    token        varchar(64) PRIMARY KEY,
    name         varchar(255),
    account_type varchar(50)
);

CREATE TABLE IF NOT EXISTS ANALYSIS_REQUEST
(
    id         integer auto_increment PRIMARY KEY,
    user_token varchar(64),
    status     varchar(10),
    created_at timestamp default now()
);

CREATE TABLE IF NOT EXISTS CLOTH
(
    cloth_id            integer auto_increment PRIMARY KEY,
    name                varchar(255),
    color               varchar(20),
    analysis_request_id integer,
    type                varchar(20),
    user_token          varchar(64),
    created_at          timestamp default now()
);

CREATE TABLE IF NOT EXISTS CARE_LABEL
(
    id           integer auto_increment PRIMARY KEY,
    water_type   varchar(50),
    bleach_type  varchar(50),
    dry_type     varchar(50),
    dry_cleaning varchar(50),
    ironing_type varchar(50),
    cloth_id     bigint
);

CREATE TABLE IF NOT EXISTS IMAGE
(
    image_id       integer auto_increment PRIMARY KEY,
    file_name      varchar(100) unique,
    file_url       varchar(255),
    file_path      varchar(255),
    thumbnail_path varchar(255),
    type           varchar(50),
    user_token     varchar(64),
    cloth_id       integer,
    care_label_id  integer
);

CREATE TABLE IF NOT EXISTS AUTHORIZATION_TOKEN
(
    id            integer auto_increment PRIMARY KEY,
    user_token    varchar(64),
    device_token  varchar(255),
    access_token  uuid unique,
    refresh_token uuid,
    expired_at    timestamp default DATEADD('DAY', 7, NOW()),
    created_at    timestamp default now()
);

CREATE TABLE IF NOT EXISTS NOTICE
(
    id      integer auto_increment PRIMARY KEY,
    title   varchar(50),
    hint    varchar(50),
    content varchar(255)
);

CREATE TABLE IF NOT EXISTS NOTIFICATION
(
    id                  integer auto_increment PRIMARY KEY,
    user_token          varchar(64),
    analysis_request_id integer,
    category            varchar(20),
    new                 boolean,
    created_at          timestamp default now()
);
