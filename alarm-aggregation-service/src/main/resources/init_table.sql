CREATE TABLE IF NOT EXISTS `alarm_clean_strategy` (
  `id` int(11) NOT NULL,
  `cycle_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_log_info` (
  `id` varchar(255) NOT NULL,
  `claimant` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `delay_time` int(11) NOT NULL,
  `ops_type` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `transferee` varchar(255) DEFAULT NULL,
  `upgrade_time` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_source` (
  `alarm_id` varchar(255) NOT NULL,
  `agent_type` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `details` longtext,
  `event_data` date DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `event_time` time DEFAULT NULL,
  `host_ip` varchar(255) DEFAULT NULL,
  `host_name` varchar(255) DEFAULT NULL,
  `item_key` varchar(255) DEFAULT NULL,
  `monitor_ip` varchar(255) DEFAULT NULL,
  `severity` int(11) DEFAULT NULL,
  `tenant_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`alarm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_recovery` (
  `recovery_id` varchar(255) NOT NULL,
  `event_age` varchar(255) DEFAULT NULL,
  `event_recovery_id` varchar(255) DEFAULT NULL,
  `monitor_ip` varchar(255) DEFAULT NULL,
  `recovery_data` date DEFAULT NULL,
  `recovery_time` time DEFAULT NULL,
  `alarm_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`recovery_id`),
  KEY `FKp2thh4ebak1td38l3e7wb7x1p` (`alarm_id`),
  CONSTRAINT `FKp2thh4ebak1td38l3e7wb7x1p` FOREIGN KEY (`alarm_id`) REFERENCES `alarm_source` (`alarm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_life` (
`id` varchar(255) NOT NULL,
  `alarm_status` varchar(255) DEFAULT NULL,
  `handler_current` int(11) NOT NULL,
  `handler_last` int(11) NOT NULL,
  `host_alarm_num` int(11) NOT NULL,
  `item_type` varchar(255) DEFAULT NULL,
  `rec_date` bigint(20) NOT NULL,
  `res_date` bigint(20) NOT NULL,
  `tri_date` bigint(20) NOT NULL,
  `upgrade` tinyint(4) NOT NULL,
  `alarm_id` varchar(255) DEFAULT NULL,
  `recovery_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiefguai98bsbl3pu5csp95vtq` (`alarm_id`),
  KEY `FKtad43tehh7h83ebuovgmpnl8e` (`recovery_id`),
  CONSTRAINT `FKiefguai98bsbl3pu5csp95vtq` FOREIGN KEY (`alarm_id`) REFERENCES `alarm_source` (`alarm_id`),
  CONSTRAINT `FKtad43tehh7h83ebuovgmpnl8e` FOREIGN KEY (`recovery_id`) REFERENCES `alarm_recovery` (`recovery_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_life_entity_to_hire_user_ids` (
  `alarm_life_entity_id` varchar(255) NOT NULL,
  `to_hire_user_ids` int(11) DEFAULT NULL,
  KEY `FKe3dlccvht64lo1nw7u2k9kdos` (`alarm_life_entity_id`),
  CONSTRAINT `FKe3dlccvht64lo1nw7u2k9kdos` FOREIGN KEY (`alarm_life_entity_id`) REFERENCES `alarm_life` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_life_logs` (
  `alarm_life_entity_id` varchar(255) NOT NULL,
  `logs_id` varchar(255) NOT NULL,
  PRIMARY KEY (`alarm_life_entity_id`,`logs_id`),
  UNIQUE KEY `UK_gj2culr52pv4ua8m5d8etimgx` (`logs_id`),
  CONSTRAINT `FK2rx253yw9un9qhwcy35i94qrq` FOREIGN KEY (`alarm_life_entity_id`) REFERENCES `alarm_life` (`id`),
  CONSTRAINT `FK422axotg7jbo10sk7mvwewuub` FOREIGN KEY (`logs_id`) REFERENCES `alarm_log_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `alarm_proxy_cer_entity` (
  `token` varchar(255) NOT NULL,
  `tenant_id` varchar(255) NOT NULL,
  PRIMARY KEY (`token`),
  UNIQUE KEY `UK_ohaqfeguulrjidubta06lmxgi` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end