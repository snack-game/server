CREATE TABLE if not exists member
(
    id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name  varchar(255) NOT NULL,
    group_name varchar(255) NOT NULL
);

