CREATE TABLE IF NOT EXISTS `group_info` (
  `group_id` smallint(6) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `group_entity_user_ids` (
  `group_entity_group_id` smallint(6) NOT NULL,
  `user_ids` int(11) DEFAULT NULL,
  KEY `FKtfb2g48b54f2rlx6n1vtdjmm5` (`group_entity_group_id`),
  CONSTRAINT `FKtfb2g48b54f2rlx6n1vtdjmm5` FOREIGN KEY (`group_entity_group_id`) REFERENCES `group_info` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end