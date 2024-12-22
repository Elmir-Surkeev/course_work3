CREATE TABLE IF NOT EXISTS `users`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL UNIQUE,
    `first_name` VARCHAR(255) DEFAULT NULL,
    `last_name`  VARCHAR(255) DEFAULT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `role`       VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
