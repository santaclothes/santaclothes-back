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
    token        uuid PRIMARY KEY,
    name         varchar(255),
    account_type varchar(50)
);


CREATE TABLE IF NOT EXISTS USER_TOKEN
(
    id            bigserial PRIMARY KEY,
    user_token    uuid,
    access_token  uuid unique,
    refresh_token uuid,
    expired_at    timestamp default now() + interval '1 week',
    created_at    timestamp default now(),
    FOREIGN KEY (user_token)
        REFERENCES "USER" (token)
);
