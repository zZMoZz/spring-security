CREATE TABLE `customer` (
    `id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(45) NOT NULL,
    `pwd` varchar(200) NOT NULL,
    `role` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT  INTO `customer` (`email`, `pwd`, `role`) VALUES ('happy@example.com', '{noop}EazyBytes@12345', 'read');
INSERT  INTO `customer` (`email`, `pwd`, `role`) VALUES ('admin@example.com', '{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m', 'admin');