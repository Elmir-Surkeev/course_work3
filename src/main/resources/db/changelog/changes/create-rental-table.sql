CREATE TABLE if not exists `rentals`
(
    `id`                 bigint NOT NULL AUTO_INCREMENT,
    `rental_date`        DATETIME   NOT NULL,
    `return_date`        DATETIME   NOT NULL,
    `actual_return_date` DATETIME,
    `car_id`             bigint NOT NULL,
    `user_id`            bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `user_fk_idx` (`user_id`),
    KEY `car_fk_idx` (`car_id`),
    CONSTRAINT `car_fk` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
    CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
