--
-- table structure for table `app_info`
--
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `app`    VARCHAR(255) NOT NULL,
  `host`   VARCHAR(255) NOT NULL,
  `type`   INT(11)      NOT NULL,
  `deploy` VARCHAR(255) NOT NULL,
  `status` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`app`, `host`, `type`)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `name_info`
--

DROP TABLE IF EXISTS `name_info`;
CREATE TABLE `name_info` (
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `app`  VARCHAR(255) NOT NULL,
  `tid`  INT(11),
  PRIMARY KEY (`name`, `type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `service_info`
--

DROP TABLE IF EXISTS `service_info`;
CREATE TABLE `service_info` (
  `iface`  VARCHAR(255) NOT NULL,
  `method` VARCHAR(255) NOT NULL,
  `sid`    VARCHAR(255) NOT NULL,
  PRIMARY KEY (`iface`, `method`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `monitor_template`
--

DROP TABLE IF EXISTS `monitor_template`;
CREATE TABLE `monitor_template` (
  `id`        INT(11)      NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `window`    INT(11)      NOT NULL,
  `threshold` DOUBLE       NOT NULL,
  `cost`      VARCHAR(255) NOT NULL,
  `preset`    INT(11)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Data for table `monitor_template`
--
INSERT INTO `monitor_template` VALUES (1, 'api报警预置模板', 1, 0.1, '3500', 1);
INSERT INTO `monitor_template` VALUES (2, '第三方报警预置模板', 1, 0.1, '3000', 1);
INSERT INTO `monitor_template` VALUES (3, 'middleware报警预置模板', 1, 0.1, '1000', 1);
