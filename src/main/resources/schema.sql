
-- #TABLE todos
CREATE TABLE IF NOT EXISTS `todos`
(
    `id`            BIGINT NOT NULL AUTO_INCREMENT,
    `title`         VARCHAR(255),
    `description`   LONGTEXT,
    `completed`     BOOLEAN,
    `last_updated`  DATETIME NULL,
    PRIMARY KEY (id)
);