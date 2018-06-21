CREATE TABLE IF NOT EXISTS `sub_tenant_permission` (
  `group_permission_id` int(11) NOT NULL,
  PRIMARY KEY (`group_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_tenant_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) NOT NULL,
  `create_date` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_tenant_role_group_ids` (
  `sub_tenant_role_id` int(11) NOT NULL,
  `group_ids` smallint(6) DEFAULT NULL,
  KEY `FK3lniaqv3o4icaspt20bk2liyb` (`sub_tenant_role_id`),
  CONSTRAINT `FK3lniaqv3o4icaspt20bk2liyb` FOREIGN KEY (`sub_tenant_role_id`) REFERENCES `sub_tenant_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_tenant_role_sub_tenant_permissions` (
  `sub_tenant_role_id` int(11) NOT NULL,
  `sub_tenant_permissions_group_permission_id` int(11) NOT NULL,
  KEY `FKk2pus7xav4cou1ry2iu37x85a` (`sub_tenant_permissions_group_permission_id`),
  KEY `FK75ocsi7s4in8hoor7xihmecbc` (`sub_tenant_role_id`),
  CONSTRAINT `FK75ocsi7s4in8hoor7xihmecbc` FOREIGN KEY (`sub_tenant_role_id`) REFERENCES `sub_tenant_role` (`id`),
  CONSTRAINT `FKk2pus7xav4cou1ry2iu37x85a` FOREIGN KEY (`sub_tenant_permissions_group_permission_id`) REFERENCES `sub_tenant_permission` (`group_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_tenant_user_role` (
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_tenant_user_role_sub_tenant_roles` (
  `sub_tenant_user_role_user_id` int(11) NOT NULL,
  `sub_tenant_roles_id` int(11) NOT NULL,
  KEY `FKj065lunuwgsy6npklxl4s4v9n` (`sub_tenant_roles_id`),
  KEY `FK3yiyb95q36fc63di14in1dcx3` (`sub_tenant_user_role_user_id`),
  CONSTRAINT `FK3yiyb95q36fc63di14in1dcx3` FOREIGN KEY (`sub_tenant_user_role_user_id`) REFERENCES `sub_tenant_user_role` (`user_id`),
  CONSTRAINT `FKj065lunuwgsy6npklxl4s4v9n` FOREIGN KEY (`sub_tenant_roles_id`) REFERENCES `sub_tenant_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

end