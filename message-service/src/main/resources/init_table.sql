CREATE TABLE IF NOT EXISTS `alarm_msg_entity` (
  `id` varchar(255) NOT NULL,
  `alarm_id` varchar(255) DEFAULT NULL,
  `alarm_status` varchar(255) DEFAULT NULL,
  `confirmed` bit(1) NOT NULL,
  `host_ip` varchar(255) DEFAULT NULL,
  `host_name` varchar(255) DEFAULT NULL,
  `msg_date` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `tri_date` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXc13vi6pqtu8fujbgrhiqoovuo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `apply_flow_msg_entity` (
  `id` varchar(255) NOT NULL,
  `approve_time` bigint(20) NOT NULL,
  `approved` bit(1) NOT NULL,
  `confirmed` bit(1) NOT NULL,
  `enterprise` varchar(255) DEFAULT NULL,
  `msg_date` bigint(20) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXk2twbfdaymptxx3ypayocd8ti` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `approve_flow_msg_entity` (
  `id` varchar(255) NOT NULL,
  `applicant` varchar(255) DEFAULT NULL,
  `apply_time` bigint(20) NOT NULL,
  `confirmed` bit(1) NOT NULL,
  `enterprise` varchar(255) DEFAULT NULL,
  `msg_date` bigint(20) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXca4826yf50klgt9n5bw6gqy1` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end