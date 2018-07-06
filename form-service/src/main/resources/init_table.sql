CREATE TABLE IF NOT EXISTS `form_audit` (
  `id` varchar(255) NOT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL,
  `pass` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_audit_person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `form_id` varchar(255) DEFAULT NULL,
  `group_id` smallint(6) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_change_logs` (
  `id` varchar(255) NOT NULL,
  `change_content` varchar(255) DEFAULT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `form_state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) DEFAULT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_principal` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_date` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `form_client` VALUES ('1', '1', '1', null, '', '\0', '负责人', '18111111111', null, null);

CREATE TABLE IF NOT EXISTS `form_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_date` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `form_company` VALUES ('1', '1', null, '本公司', null, null);

CREATE TABLE IF NOT EXISTS `form_demand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(64) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5dpfctena7fqq2jtnq5c22tif` (`parent_id`),
  CONSTRAINT `FK5dpfctena7fqq2jtnq5c22tif` FOREIGN KEY (`parent_id`) REFERENCES `form_demand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

INSERT INTO `form_demand` VALUES ('1', '', '咨询', null);

INSERT INTO `form_demand` VALUES ('2', '', '巡检', null);

INSERT INTO `form_demand` VALUES ('3', '', '网络', null);

INSERT INTO `form_demand` VALUES ('4', '', '服务器（非虚拟化）', null);

INSERT INTO `form_demand` VALUES ('5', '', '存储', null);

INSERT INTO `form_demand` VALUES ('6', '', '服务器虚拟化', null);

INSERT INTO `form_demand` VALUES ('7', '', '桌面虚拟化', null);

INSERT INTO `form_demand` VALUES ('8', '', '应用', null);

CREATE TABLE IF NOT EXISTS `form_evaluate` (
  `id` varchar(255) NOT NULL,
  `client_opinion` varchar(1000) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `evaluate_level` int(11) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `operate_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_info` (
  `id` varchar(255) NOT NULL,
  `applicant_id` int(11) DEFAULT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `del_flag` tinyint(4) DEFAULT NULL,
  `demand_content` varchar(1000) DEFAULT NULL,
  `demand_type_code` int(11) DEFAULT NULL,
  `form_apply_time` bigint(20) DEFAULT NULL,
  `form_expect_end_time` bigint(20) DEFAULT NULL,
  `form_owner_id` int(11) DEFAULT NULL,
  `form_owner_phone` varchar(255) DEFAULT NULL,
  `form_real_end_time` bigint(20) DEFAULT NULL,
  `form_state` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `handler_id` int(11) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `relation_form_id` varchar(255) DEFAULT NULL,
  `source_type` int(11) DEFAULT NULL,
  `unit_code` int(11) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_date` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_marks` (
  `id` varchar(255) NOT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_operate_logs` (
  `id` varchar(255) NOT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `demand_type_code` int(11) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `handler_id` int(11) DEFAULT NULL,
  `operate_details` varchar(1000) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `workload` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_priority` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `form_priority` VALUES ('0', '一般');

INSERT INTO `form_priority` VALUES ('1', '紧急');

INSERT INTO `form_priority` VALUES ('2', '非常紧急');

CREATE TABLE IF NOT EXISTS `form_state` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `form_state` VALUES ('0', '未接手处理');

INSERT INTO `form_state` VALUES ('1', '处理中');

INSERT INTO `form_state` VALUES ('2', '事件升级中');

INSERT INTO `form_state` VALUES ('3', '已解决');

INSERT INTO `form_state` VALUES ('4', '待审核');

INSERT INTO `form_state` VALUES ('5', '待评价');

INSERT INTO `form_state` VALUES ('6', '已结案');

INSERT INTO `form_state` VALUES ('7', '退回');

INSERT INTO `form_state` VALUES ('8', '已删除');

CREATE TABLE IF NOT EXISTS `form_upgrade_logs` (
  `id` varchar(255) NOT NULL,
  `create_by` int(11) DEFAULT NULL,
  `create_date` bigint(20) DEFAULT NULL,
  `form_id` varchar(255) DEFAULT NULL,
  `handler_id` int(11) DEFAULT NULL,
  `upgrade_reason` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `form_user` (
  `id` int(11) NOT NULL,
  `cur_tenant_id` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `head_img_url` varchar(255) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `sequence_next_hi_value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `r_form_file` (
  `form_id` varchar(255) NOT NULL,
  `file_id` int(11) NOT NULL,
  PRIMARY KEY (`form_id`,`file_id`),
  KEY `FKixb33h0m26rh0ix2m1l9mcuat` (`file_id`),
  CONSTRAINT `FKmeeaoejgbhhfqh584fotq17un` FOREIGN KEY (`form_id`) REFERENCES `form_info` (`id`),
  CONSTRAINT `FKixb33h0m26rh0ix2m1l9mcuat` FOREIGN KEY (`file_id`) REFERENCES `form_file` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
