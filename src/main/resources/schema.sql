CREATE TABLE if not exists member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name     varchar(255) NOT NULL UNIQUE,
    group_id BIGINT
);

CREATE TABLE if not exists member_group
(
    id   BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);
