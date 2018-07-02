if exists(select 1 from form_state )
begin
end
else
begin
INSERT INTO `form_state` VALUES ('0', '未接手处理') ;
INSERT INTO `form_state` VALUES ('1', '处理中') ;
INSERT INTO `form_state` VALUES ('2', '事件升级中') ;
INSERT INTO `form_state` VALUES ('3', '已解决');
INSERT INTO `form_state` VALUES ('4', '待审核');
INSERT INTO `form_state` VALUES ('5', '待评价');
INSERT INTO `form_state` VALUES ('6', '已结案');
INSERT INTO `form_state` VALUES ('7', '退回');
INSERT INTO `form_state` VALUES ('8', '已删除');
end;



if exists(select 1 from form_demand )
begin
end
else
begin
INSERT INTO `form_demand` VALUES ('1', '', '咨询', null);
INSERT INTO `form_demand` VALUES ('2', '', '巡检', null);
INSERT INTO `form_demand` VALUES ('3', '', '网络', null);
INSERT INTO `form_demand` VALUES ('4', '', '服务器（非虚拟化）', null);
INSERT INTO `form_demand` VALUES ('5', '', '存储', null);
INSERT INTO `form_demand` VALUES ('6', '', '服务器虚拟化', null);
INSERT INTO `form_demand` VALUES ('7', '', '桌面虚拟化', null);
INSERT INTO `form_demand` VALUES ('8', '', '应用', null);
end;



if exists(select 1 from form_priority )
begin
end
else
begin
INSERT INTO `form_priority` (`id`, `name`) VALUES ('0', '一般');
INSERT INTO `form_priority` (`id`, `name`) VALUES ('1', '紧急');
INSERT INTO `form_priority` (`id`, `name`) VALUES ('2', '非常紧急');
end;


if exists(select 1 from form_company )
begin
end
else
begin
INSERT INTO `form_company` (`id`, `name`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES ('1', '本公司', '1', NULL, NULL, NULL);
end;


if exists(select 1 from form_client )
begin
end
else
begin
INSERT INTO `form_client` (`id`, `email`, `name`, `phone`, `is_principal`, `company_id`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES ('1', '', '负责人', '18111111111', '', '1', '1', NULL, NULL, NULL) ;
end;

