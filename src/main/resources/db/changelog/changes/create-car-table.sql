CREATE TABLE IF NOT EXISTS `cars`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `model`     VARCHAR(255) NOT NULL DEFAULT 'UNKNOWN_MODEL',
    `brand`     VARCHAR(255) NOT NULL DEFAULT 'UNKNOWN_BRAND',
    `type`      VARCHAR(255) DEFAULT NULL,
    `inventory` INT          NOT NULL DEFAULT '0',
    `daily_fee` DECIMAL      NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

