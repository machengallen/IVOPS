CREATE TABLE IF NOT EXISTS `single_task_life` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `crea_date` bigint(20) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `exec_num` int(11) NOT NULL,
  `mod_date` bigint(20) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `single_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `script_id` int(11) NOT NULL,
  `script_src` int(11) DEFAULT NULL,
  `task_description` varchar(255) DEFAULT NULL,
  `task_name` varchar(255) DEFAULT NULL,
  `timeout` int(11) NOT NULL,
  `task_life_id` int(11) DEFAULT NULL,
  `system_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2asnil5j2ueokaf5kg9ic3k77` (`task_life_id`),
  CONSTRAINT `FK2asnil5j2ueokaf5kg9ic3k77` FOREIGN KEY (`task_life_id`) REFERENCES `single_task_life` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `single_task_entity_script_args` (
  `single_task_entity_id` int(11) NOT NULL,
  `script_args` varchar(255) DEFAULT NULL,
  KEY `FK83ct3v8gkn2ey0x023s6gx7if` (`single_task_entity_id`),
  CONSTRAINT `FK83ct3v8gkn2ey0x023s6gx7if` FOREIGN KEY (`single_task_entity_id`) REFERENCES `single_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `immediate_target` (
  `id` varchar(255) NOT NULL,
  `account` varchar(255) DEFAULT NULL,
  `host_ip` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `result` varchar(255) DEFAULT NULL,
  `success` bit(1) NOT NULL,
  `task_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrdori89y0wkgx0inj2dscfoyo` (`task_id`),
  CONSTRAINT `FKrdori89y0wkgx0inj2dscfoyo` FOREIGN KEY (`task_id`) REFERENCES `single_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `single_task_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `cron_exp` varchar(255) DEFAULT NULL,
  `single_task_id` int(11) DEFAULT NULL,
  `crea_date` bigint(20) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `mod_date` bigint(20) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb2m3pmtlwqpoy9ettuq7x2km2` (`single_task_id`),
  CONSTRAINT `FKb2m3pmtlwqpoy9ettuq7x2km2` FOREIGN KEY (`single_task_id`) REFERENCES `single_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `schedule_target` (
  `id` varchar(255) NOT NULL,
  `account` varchar(255) DEFAULT NULL,
  `host_ip` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `result` varchar(255) DEFAULT NULL,
  `success` bit(1) NOT NULL,
  `task_schedule_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjcrst87f55aemhubwq2jt8gps` (`task_schedule_id`),
  CONSTRAINT `FKjcrst87f55aemhubwq2jt8gps` FOREIGN KEY (`task_schedule_id`) REFERENCES `single_task_schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end