DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `name` varchar(100) NOT NULL,
                            `age` int(11) NOT NULL,
                            `salary` bigint(20) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `employee` VALUES ('1', 'ffzs', '20', '110000'), ('2', 'dz', '30', '2000000');
COMMIT;
