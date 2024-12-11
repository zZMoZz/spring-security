CREATE TABLE `account` (
    `id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(45) NOT NULL,
    `pwd` varchar(200) NOT NULL,
    `role` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT  INTO `account` (`email`, `pwd`, `role`) VALUES ('happy@example.com', '{noop}EazyBytes@12345', 'read');
INSERT  INTO `account` (`email`, `pwd`, `role`) VALUES ('admin@example.com', '{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m', 'admin');

CREATE TABLE `authorities` (
    `id` int NOT NULL AUTO_INCREMENT,
    `customer_id` int NOT NULL,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `customer_id` (`customer_id`),
    CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `account` (`id`)
);

INSERT INTO `authorities` (`customer_id`, `name`) VALUES (1, 'ROLE_USER');
INSERT INTO `authorities` (`customer_id`, `name`) VALUES (2, 'ROLE_ADMIN');