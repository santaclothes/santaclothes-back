CREATE TABLE IF NOT EXISTS CLOTH
(
    id    bigserial PRIMARY KEY,
    name  varchar(255),
    color varchar(20),
    type  varchar(20)
);

CREATE TABLE IF NOT EXISTS CARE_LABEL
(
    id           bigserial PRIMARY KEY,
    water_type   varchar(50),
    bleach_type  varchar(50),
    dry_type     varchar(50),
    dry_cleaning varchar(50),
    ironing_type varchar(50),
    cloth        bigint,
    FOREIGN KEY (cloth)
        REFERENCES CLOTH (id)
);

CREATE TABLE IF NOT EXISTS "USER"
(
    token        varchar(64) PRIMARY KEY,
    name         varchar(255),
    account_type varchar(50)
);


CREATE TABLE IF NOT EXISTS USER_TOKEN
(
    id            bigserial PRIMARY KEY,
    user_token    varchar(64),
    access_token  uuid unique,
    refresh_token uuid,
    expired_at    timestamp default now() + interval '1 week',
    created_at    timestamp default now(),
    FOREIGN KEY (user_token)
        REFERENCES "USER" (token)
);

CREATE TABLE IF NOT EXISTS IMAGE
(
    id             bigserial PRIMARY KEY,
    file_path      varchar(255),
    thumbnail_path varchar(255),
    image_type     varchar(50),
    cloth          bigint,
    care_label     bigint,
    FOREIGN KEY (cloth)
        REFERENCES cloth (id),
    FOREIGN KEY (care_label)
        REFERENCES CARE_LABEL (id)
)
