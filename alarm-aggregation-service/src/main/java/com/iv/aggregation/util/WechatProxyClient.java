package com.iv.aggregation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.iv.aggregation.api.constant.AlarmStatus;
import com.iv.aggregation.api.constant.NoticeModel;
import com.iv.aggregation.api.constant.NoticeType;
import com.iv.aggregation.dao.impl.AlarmMsgDaoImpl;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.entity.AlarmMsgEntity;
import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.aggregation.feign.clients.IGroupServiceClient;
import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.aggregation.feign.clients.IWechatServiceClient;
import com.iv.common.enumeration.SendType;
import com.iv.common.enumeration.Severity;
import com.iv.dto.TemplateMessageDto;
import com.iv.enter.dto.GroupQuery;
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
	private AlarmMsgDaoImpl alarmMsgDao;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private IEmailServiceClient emailServiceClient;
	@Autowired
	private IWechatServiceClient wechatServiceClient;
	@Autowired
	private IGroupServiceClient groupServiceClient;
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
			transferNotice(alarmLifeEntity, templateMessageDto, toEmails);
		}

		// 发送通知至邮件
		if (!CollectionUtils.isEmpty(toEmails)) {
			emailServiceClient.alarmToMail(toEmails.toArray(new String[toEmails.size()]), SendType.ALARMTRIGGER,
					alarmLifeEntity);
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
		AlarmStrategyDto dispatchStrategy = (AlarmStrategyDto)alarmStrategyClient.getStrategy(strategyQuery).getData();
		short groupId = dispatchStrategy.getGroupIds().get(index);
		// 调用组服务获取组信息
		GroupQuery groupQuery = new GroupQuery();
		groupQuery.setGroupId(groupId);
		groupQuery.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		GroupEntityDto groupDto = groupServiceClient.selectGroupInfo(groupQuery);
		
		switch (model) {
		case ROUND_ROBIN:// 轮询通知方式
			Integer targetUserId = TeamRoundRobin.getToUser(alarmLifeEntity.getAlarm().getTenantId(), groupId, groupDto.getUserId());
			LocalAuthDto localAuth = userServiceClient.selectLocalAuthById(targetUserId);
			// HireUser targetUser1 = hireUserDao.selectHireUserByUserId(hirer.getUserId());
			if (null == targetUserId) {
				break;
			}
			int firstNotice = 1;
			if (index + 1 == firstNotice) {
				// 首次通知，则写入当前处理人
				alarmLifeEntity.setHandlerCurrent(targetUserId);
			}
			
			// 判断用户是否已关注公众号
			if (wechatServiceClient.ifFocusWechat(targetUserId)) {// 微信推送
				LOGGER.info("推送人员：" + localAuth.toString());
				// 记录告警生命周期-推送人
				alarmLifeEntity.getToHandlers().add(targetUserId);
				// 调用微信管理服务，发送模板消息
				atm.setUserId(targetUserId);
				wechatServiceClient.SendWeChatInfo(atm);
				
			}
			// 记录邮件发送人
			toEmails.add(localAuth.getUserName());
			toUserIds.add(localAuth.getId());
			// 返回告警派送人
			String dispatcher = null;
			if (null == localAuth.getRealName()) {
				dispatcher = localAuth.getUserName();
			} else {
				dispatcher = localAuth.getRealName();
			}
			infoMap.put("dispatcher", dispatcher);
			infoMap.put("toUserIds", toUserIds);
			return infoMap;

		case BROADCAST:// 广播通知方式
			for (int userId : groupDto.getUserId()) {
				LocalAuthDto localAuthDto = userServiceClient.selectLocalAuthById(userId);
				
				//TODO 判断用户是否已关注公众号
				if (wechatServiceClient.ifFocusWechat(userId)) {
					LOGGER.info("推送人员：" + localAuthDto.toString());
					// 写入当前处理人
					// alarmLifeEntity.setHandlerCurrent(user);
					// 记录告警生命周期-推送人
					alarmLifeEntity.getToHandlers().add(userId);
					// 调用微信管理服务，发送模板消息
					atm.setUserId(userId);
					wechatServiceClient.SendWeChatInfo(atm);
				}
				// 记录邮件发送人
				toEmails.add(localAuthDto.getUserName());
				toUserIds.add(localAuthDto.getId());
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
		LocalAuthDto user = userServiceClient.selectLocalAuthById(atm.getUserId());
		LOGGER.info("转让人员：" + user.toString());
		// 记录被通知人
		alarmLifeEntity.getToHandlers().add(atm.getUserId());
		// 记录邮件发送人
		toEmails.add(user.getUserName());
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
		// 用于记录邮件发送人Id
		ArrayList<Integer> toUserIds = new ArrayList<Integer>(1);
		// 转换为微信模板消息体
		// 转换为微信模板消息体
		TemplateMessageDto templateMessageDto = DataConvert.AlarmTempConvert(alarmLifeEntity.getAlarm());
		templateMessageDto.setSendType(SendType.ALARMRECOVERY);
		templateMessageDto.setRedirect_uri(urlAlarmDetails + alarmLifeEntity.getId() + "$" +
				alarmLifeEntity.getAlarm().getTenantId());

		// 发送消息至微信服务器
		for (int targetUserId : alarmLifeEntity.getToHandlers()) {
			// 调用微信管理服务，发送模板消息
			if(wechatServiceClient.ifFocusWechat(targetUserId)) {
				templateMessageDto.setUserId(targetUserId);
				wechatServiceClient.SendWeChatInfo(templateMessageDto);
			}
			// 记录邮箱发送人
			toEmails.add(userServiceClient.selectLocalAuthById(targetUserId).getUserName());
			toUserIds.add(targetUserId);
		}

		// 调用邮件服务
		if (!CollectionUtils.isEmpty(toEmails)) {
			emailServiceClient.alarmToMail(toEmails.toArray(new String[toEmails.size()]), SendType.ALARMRECOVERY,
					alarmLifeEntity);
		}

		// 保存恢复信息
		if (!CollectionUtils.isEmpty(toUserIds)) {
			for (int userId : toUserIds) {
				AlarmMsgEntity AlarmMsgEntity = new AlarmMsgEntity();
				AlarmMsgEntity.setUserId(userId);
				AlarmMsgEntity.setConfirmed(false);
				AlarmMsgEntity.setMsgDate(System.currentTimeMillis());
				AlarmMsgEntity.setAlarmId(alarmLifeEntity.getId());
				AlarmMsgEntity.setType(NoticeType.RECOVERY);
				AlarmMsgEntity.setTitle(alarmLifeEntity.getAlarm().getTitle());
				AlarmMsgEntity.setHostName(alarmLifeEntity.getAlarm().getHostName());
				AlarmMsgEntity.setHostIp(alarmLifeEntity.getAlarm().getHostIp());
				AlarmMsgEntity.setTriDate(alarmLifeEntity.getRecDate());
				AlarmMsgEntity.setAlarmStatus(AlarmStatus.CLOSED);
				alarmMsgDao.save(AlarmMsgEntity, alarmLifeEntity.getAlarm().getTenantId());
			}
		}
	}

}
