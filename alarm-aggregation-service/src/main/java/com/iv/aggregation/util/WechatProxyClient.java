package com.iv.aggregation.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.iv.aggregation.api.constant.NoticeModel;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.aggregation.feign.clients.IGroupServiceClient;
import com.iv.aggregation.feign.clients.IMessageServiceClient;
import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.aggregation.feign.clients.IWechatServiceClient;
import com.iv.common.enumeration.AlarmStatus;
import com.iv.common.enumeration.NoticeType;
import com.iv.common.enumeration.SendType;
import com.iv.common.enumeration.Severity;
import com.iv.common.response.ResponseDto;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.AlarmLifeEntityDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.StrategyQueryDto;

/**
 * 微信管理服务客户端
 * 
 * @author macheng 2018年4月8日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Component
public class WechatProxyClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(WechatProxyClient.class);
	@Autowired
	private IAlarmStrategyClient alarmStrategyClient;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private IEmailServiceClient emailServiceClient;
	@Autowired
	private IWechatServiceClient wechatServiceClient;
	@Autowired
	private IGroupServiceClient groupServiceClient;
	@Autowired
	private IMessageServiceClient messageServiceClient;
	@Value("${iv.wechat.urlAlarmDetails}")
	private String urlAlarmDetails;

	/**
	 * 消息通知入口
	 * 
	 * @param ami
	 * @param alarmSourceEntity
	 * @param itemId
	 * @throws Exception
	 */
	public Map<String, Object> alarmNotice(AlarmLifeEntity alarmLifeEntity, Integer index, NoticeModel model,
			Integer toUser) throws Exception {
		// 用于记录邮件发送人
		ArrayList<String> toEmails = new ArrayList<String>(1);
		// 转换为微信模板消息体
		TemplateMessageDto templateMessageDto = DataConvert.AlarmTempConvert(alarmLifeEntity.getAlarm());
		templateMessageDto.setSendType(SendType.ALARMTRIGGER);
		// 设置基本信息
		// 判断通知类型，选择模板id
		// atm.setTemplate_id(templateAlarm);
		templateMessageDto.setRedirect_uri(urlAlarmDetails + alarmLifeEntity.getId() + "$" +
		alarmLifeEntity.getAlarm().getTenantId());
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 记录告警派送人/组
		String dispatcher = null;
		// 发送通知至微信服务器
		if (null == toUser) {
			// 非转让事件
			infoMap = strategyNotice(alarmLifeEntity, templateMessageDto, index, model, toEmails);
			dispatcher = (String) infoMap.get("dispatcher");
		} else {
			// 转让事件
			// atm.setTouser(toUser);
			templateMessageDto.setUserIds(Arrays.asList(toUser));
			transferNotice(alarmLifeEntity, templateMessageDto, toEmails);
		}

		// 发送通知至邮件
		if (!CollectionUtils.isEmpty(toEmails)) {
			AlarmInfoTemplate alarmInfoTemplate = new AlarmInfoTemplate();
			alarmInfoTemplate.setToEmails(toEmails.toArray(new String[toEmails.size()]));
			alarmInfoTemplate.setEmailType(SendType.ALARMTRIGGER);
			AlarmLifeEntityDto alarmLifeEntityDto = new AlarmLifeEntityDto();
			alarmLifeEntityDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
			alarmLifeEntityDto.setContent(alarmLifeEntity.getAlarm().getContent());
			alarmLifeEntityDto.setCurrentHandlerId(alarmLifeEntity.getHandlerCurrent());
			alarmLifeEntityDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
			alarmLifeEntityDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
			alarmLifeEntityDto.setId(alarmLifeEntity.getId());
			alarmLifeEntityDto.setSeverity(alarmLifeEntity.getAlarm().getSeverity());
			alarmLifeEntityDto.setTime(new Date(alarmLifeEntity.getTriDate()).toLocaleString());
			alarmLifeEntityDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
			alarmLifeEntityDto.setUpgrade(alarmLifeEntityDto.getUpgrade());
			alarmInfoTemplate.setAlarmLifeEntity(alarmLifeEntityDto);
			emailServiceClient.alarmToMail(alarmInfoTemplate);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dispatcher", dispatcher);
		map.put("toUserIds", infoMap.get("toUserIds"));
		return map;
	}

	/**
	 * 调用分派策略通知
	 * 
	 * @param alarmLifeEntity
	 * @param atm
	 * @param index
	 * @param toEmails
	 * @throws Exception
	 */
	private Map<String,Object> strategyNotice(AlarmLifeEntity alarmLifeEntity, TemplateMessageDto atm, Integer index,
			NoticeModel model, ArrayList<String> toEmails) throws Exception {
		ArrayList<Integer> toUserIds = new ArrayList<Integer>(1);
		Map<String,Object> infoMap = new HashMap<String,Object>();
		// 调用告警策略服务
		StrategyQueryDto strategyQuery = new StrategyQueryDto();
		strategyQuery.setSeverity(Severity.valueOf(alarmLifeEntity.getAlarm().getSeverity().name()));
		strategyQuery.setItemType(alarmLifeEntity.getItemType());
		strategyQuery.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		ResponseDto responseDto = alarmStrategyClient.getStrategy(strategyQuery);
		AlarmStrategyDto dispatchStrategy = null;
		if(null != responseDto) {
			dispatchStrategy = (AlarmStrategyDto)responseDto.getData();
		} else {
			return infoMap;
		}
		//AlarmStrategyDto dispatchStrategy = (AlarmStrategyDto)alarmStrategyClient.getStrategy(strategyQuery).getData();
		short groupId = dispatchStrategy.getGroupIds().get(index);
		// 调用组服务获取组信息
		GroupQuery groupQuery = new GroupQuery();
		groupQuery.setGroupId(groupId);
		groupQuery.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		GroupEntityDto groupDto = groupServiceClient.selectGroupInfo(groupQuery);
		if(null == groupDto) {
			return infoMap;
		}
		
		switch (model) {
		case ROUND_ROBIN:// 轮询通知方式
			Integer targetUserId = TeamRoundRobin.getToUser(alarmLifeEntity.getAlarm().getTenantId(), groupId, groupDto.getUserId());
			// HireUser targetUser1 = hireUserDao.selectHireUserByUserId(hirer.getUserId());
			if (null == targetUserId) {
				break;
			}
			int firstNotice = 1;
			if (index + 1 == firstNotice) {
				// 首次通知，则写入当前处理人
				alarmLifeEntity.setHandlerCurrent(targetUserId);
			}
			
			//LOGGER.info("推送人员：" + localAuth.toString());
			// 记录告警生命周期-推送人
			alarmLifeEntity.getToHireUserIds().add(targetUserId);
			// 调用微信管理服务，发送模板消息
			atm.setUserIds(Arrays.asList(targetUserId));
			wechatServiceClient.SendWeChatInfo(atm);
				
			// 记录邮件发送人
			LocalAuthDto localAuth = userServiceClient.selectLocalAuthById(targetUserId);
			if(null != localAuth) {
				toEmails.add(localAuth.getUserName());
				// 返回告警派送人
				String dispatcher = null;
				if (null == localAuth.getRealName()) {
					dispatcher = localAuth.getUserName();
				} else {
					dispatcher = localAuth.getRealName();
				}
				infoMap.put("dispatcher", dispatcher);
			}
			
			toUserIds.add(targetUserId);
			infoMap.put("toUserIds", toUserIds);
			return infoMap;

		case BROADCAST:// 广播通知方式
			List<Integer> userIds = new ArrayList<Integer>();
			UsersQueryDto usersQuery = new UsersQueryDto();
			usersQuery.setUserIds(groupDto.getUserId());
			List<LocalAuthDto> authDtos = userServiceClient.selectUserInfos(usersQuery);
			if(null != authDtos) {
				for (LocalAuthDto user : authDtos) {
					
					LOGGER.info("推送人员：" + user.toString());
					// 写入当前处理人
					// alarmLifeEntity.setHandlerCurrent(user);
					// 记录告警生命周期-推送人
					alarmLifeEntity.getToHireUserIds().add(user.getId());
					userIds.add(user.getId());
					// 记录邮件发送人
					toEmails.add(user.getUserName());
					toUserIds.add(user.getId());
				}
			}
			// 调用微信管理服务，发送模板消息
			atm.setUserIds(userIds);
			if(!userIds.isEmpty()) {
				wechatServiceClient.SendWeChatInfo(atm);
			}
			infoMap.put("dispatcher", groupDto.getGroupName());
			infoMap.put("toUserIds", toUserIds);
			return infoMap;

		default:
			break;
		}
		return null;

	}

	/**
	 * 转让事件通知
	 * 
	 * @param alarmLifeEntity
	 * @param atm
	 * @param toEmails
	 * @throws Exception
	 */
	private void transferNotice(AlarmLifeEntity alarmLifeEntity, TemplateMessageDto atm, ArrayList<String> toEmails) throws Exception {
		// 调用微信管理服务，发送模板消息
		wechatServiceClient.SendWeChatInfo(atm);
		LocalAuthDto user = userServiceClient.selectLocalAuthById(atm.getUserIds().get(0));
		// 记录邮件发送人
		if(null != user) {
			toEmails.add(user.getUserName());
		}
		//LOGGER.info("转让人员：" + user.toString());
		// 记录被通知人
		alarmLifeEntity.getToHireUserIds().add(atm.getUserIds().get(0));
	}

	/**
	 * 构建告警恢复模板消息
	 * 
	 * @param ami
	 * @param alarmRecoveryEntity
	 * @param itemId
	 * @throws Exception
	 */
	public void alarmRecovery(AlarmLifeEntity alarmLifeEntity) throws Exception {

		// 用于记录邮件发送人
		ArrayList<String> toEmails = new ArrayList<String>(1);
		// 转换为微信模板消息体
		// 转换为微信模板消息体
		TemplateMessageDto templateMessageDto = DataConvert.AlarmTempConvert(alarmLifeEntity.getAlarm());
		templateMessageDto.setSendType(SendType.ALARMRECOVERY);
		templateMessageDto.setRedirect_uri(urlAlarmDetails + alarmLifeEntity.getId() + "$" +
				alarmLifeEntity.getAlarm().getTenantId());

		UsersQueryDto usersQuery = new UsersQueryDto();
		usersQuery.setUserIds(new ArrayList<>(alarmLifeEntity.getToHireUserIds()));
		List<LocalAuthDto> authDtos = userServiceClient.selectUserInfos(usersQuery);
		if(null != authDtos) {
			for (LocalAuthDto user : authDtos) {
				// 记录邮箱发送人
				toEmails.add(user.getUserName());
			}
		}
		// 发送消息至微信服务器
		templateMessageDto.setUserIds(new ArrayList<>(alarmLifeEntity.getToHireUserIds()));
		wechatServiceClient.SendWeChatInfo(templateMessageDto);

		// 调用邮件服务
		if (!CollectionUtils.isEmpty(toEmails)) {
			AlarmInfoTemplate alarmInfoTemplate = new AlarmInfoTemplate();
			alarmInfoTemplate.setToEmails(toEmails.toArray(new String[toEmails.size()]));
			alarmInfoTemplate.setEmailType(SendType.ALARMRECOVERY);
			AlarmLifeEntityDto alarmLifeEntityDto = new AlarmLifeEntityDto();
			alarmLifeEntityDto.setAlarmStatus(alarmLifeEntity.getAlarmStatus());
			alarmLifeEntityDto.setContent(alarmLifeEntity.getAlarm().getContent());
			alarmLifeEntityDto.setCurrentHandlerId(alarmLifeEntity.getHandlerCurrent());
			alarmLifeEntityDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
			alarmLifeEntityDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
			alarmLifeEntityDto.setId(alarmLifeEntity.getId());
			alarmLifeEntityDto.setSeverity(alarmLifeEntity.getAlarm().getSeverity());
			alarmLifeEntityDto.setTime(new Date(alarmLifeEntity.getRecDate()).toLocaleString());
			alarmLifeEntityDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
			alarmLifeEntityDto.setUpgrade(alarmLifeEntityDto.getUpgrade());
			alarmInfoTemplate.setAlarmLifeEntity(alarmLifeEntityDto);
			emailServiceClient.alarmToMail(alarmInfoTemplate);
		}

		// 保存恢复信息
		AlarmMsgDto msgDto = new AlarmMsgDto();
		msgDto.setUserIds(new ArrayList<>(alarmLifeEntity.getToHireUserIds()));
		msgDto.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		msgDto.setMsgDate(System.currentTimeMillis());
		msgDto.setAlarmId(alarmLifeEntity.getId());
		msgDto.setType(NoticeType.RECOVERY);
		msgDto.setTitle(alarmLifeEntity.getAlarm().getTitle());
		msgDto.setHostName(alarmLifeEntity.getAlarm().getHostName());
		msgDto.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
		msgDto.setTriDate(alarmLifeEntity.getTriDate());
		msgDto.setAlarmStatus(AlarmStatus.CLOSED);
		// 调用用户消息服务
		messageServiceClient.produceAlarmMsg(msgDto);
	}

}
