CREATE TABLE IF NOT EXISTS `alarm_strategy` (
  `id` varchar(255) NOT NULL,
  `delay_time` int(11) NOT NULL,
  `item_type` varchar(255) DEFAULT NULL,
  `notice_model` varchar(255) NOT NULL,
  `severity` int(11) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `upgrade_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `strategy_log_info` (
  `id` varchar(255) NOT NULL,
  `date` datetime DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `ops_strategy` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_strategy_entity_group_ids` (
  `alarm_strategy_entity_id` varchar(255) NOT NULL,
  `group_ids` smallint(6) DEFAULT NULL,
  KEY `FKogq6crda7b5725dxkvtw554bp` (`alarm_strategy_entity_id`),
  CONSTRAINT `FKogq6crda7b5725dxkvtw554bp` FOREIGN KEY (`alarm_strategy_entity_id`) REFERENCES `alarm_strategy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_strategy_logs` (
  `alarm_strategy_entity_id` varchar(255) NOT NULL,
  `logs_id` varchar(255) NOT NULL,
  PRIMARY KEY (`alarm_strategy_entity_id`,`logs_id`),
  UNIQUE KEY `UK_qq61ci2nm3iy47yvk3ytm50s1` (`logs_id`),
  CONSTRAINT `FKabrsmdsbape0d1maoqfr47sr0` FOREIGN KEY (`logs_id`) REFERENCES `strategy_log_info` (`id`),
  CONSTRAINT `FKk4ygbwgeymj48ou33v1ccl759` FOREIGN KEY (`alarm_strategy_entity_id`) REFERENCES `alarm_strategy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `notice_strategy` (
  `user_id` int(11) NOT NULL,
  `email_notice` bit(1) NOT NULL,
  `wechat_notice` bit(1) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end