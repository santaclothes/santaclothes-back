CREATE TABLE IF NOT EXISTS "USER"
(
    token        varchar(64) PRIMARY KEY,
    name         varchar(255),
    account_type varchar(50)
);

CREATE TABLE IF NOT EXISTS IMAGE
(
    image_id       integer auto_increment PRIMARY KEY,
    file_path      varchar(255),
    thumbnail_path varchar(255),
    image_type     varchar(50),
    image_status   varchar(10),
    user_token     varchar(64),
    FOREIGN KEY (user_token)
        REFERENCES "USER" (token)
);

CREATE TABLE IF NOT EXISTS CARE_LABEL
(
    id           integer auto_increment PRIMARY KEY,
    icon_url     varchar(255),
    water_type   varchar(50),
    bleach_type  varchar(50),
    dry_type     varchar(50),
    dry_cleaning varchar(50),
    ironing_type varchar(50),
    image        bigint,
    FOREIGN KEY (image)
        REFERENCES IMAGE (image_id)
);

CREATE TABLE IF NOT EXISTS CLOTH
(
    id        integer auto_increment PRIMARY KEY,
    name      varchar(255),
    color     varchar(20),
    type      varchar(20),
    careLabel bigint,
    image     bigint,
    FOREIGN KEY (careLabel)
        REFERENCES CARE_LABEL (id),
    FOREIGN KEY (image)
        REFERENCES IMAGE (image_id)
);

CREATE TABLE IF NOT EXISTS AUTHORIZATION_TOKEN
(
    id            integer auto_increment PRIMARY KEY,
    user_token    varchar(64),
    device_token  varchar(64),
    access_token  uuid unique,
    refresh_token uuid,
    expired_at    timestamp default DATEADD('DAY', 7, NOW()),
    created_at    timestamp default now(),
    FOREIGN KEY (user_token)
        REFERENCES "USER" (token)
);

CREATE TABLE IF NOT EXISTS NOTICE
(
    id      integer auto_increment PRIMARY KEY,
    title   varchar(50),
    hint    varchar(50),
    content varchar(255)
);

CREATE TABLE IF NOT EXISTS CARE_LABEL_IMAGE_REF
(
    image_id      integer,
    care_label_id integer,

    FOREIGN KEY (image_id)
        REFERENCES IMAGE (image_id),
    FOREIGN KEY (care_label_id)
        REFERENCES CARE_LABEL (id)
);

CREATE TABLE IF NOT EXISTS ANALYSIS_REQUEST
(
    id         integer auto_increment PRIMARY KEY,
    user_token varchar(64),
    cloth      integer,
    status     varchar(10),
    created_at timestamp default now(),
    FOREIGN KEY (user_token)
        REFERENCES "USER" (token),
    FOREIGN KEY (cloth)
        REFERENCES CLOTH (id)
)
