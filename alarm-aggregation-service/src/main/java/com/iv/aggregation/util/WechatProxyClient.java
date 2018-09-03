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
import com.iv.aggregation.binding.BinderConfiguration;
import com.iv.aggregation.entity.AlarmLifeEntity;
import com.iv.aggregation.feign.clients.IAlarmStrategyClient;
import com.iv.aggregation.feign.clients.IEmailServiceClient;
import com.iv.aggregation.feign.clients.IGroupServiceClient;
import com.iv.aggregation.feign.clients.IUserServiceClient;
import com.iv.aggregation.feign.clients.IWechatServiceClient;
import com.iv.common.enumeration.NoticeType;
import com.iv.common.enumeration.SendType;
import com.iv.common.enumeration.Severity;
import com.iv.common.response.ResponseDto;
import com.iv.dto.AlarmInfoTemplate;
import com.iv.dto.AlarmLifeEntityDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.strategy.api.dto.AlarmStrategyDto;
import com.iv.strategy.api.dto.NoticeStrategyDto;
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
	private BinderConfiguration msgSender;
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
		templateMessageDto.setRedirect_uri(
				urlAlarmDetails + alarmLifeEntity.getId() + "$" + alarmLifeEntity.getAlarm().getTenantId());
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 发送通知至微信服务器
		if (null == toUser) {
			// 非转让事件
			infoMap = strategyNotice(alarmLifeEntity, templateMessageDto, index, model, toEmails);
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
			alarmLifeEntityDto.setUpgrade(alarmLifeEntity.getUpgrade());
			alarmInfoTemplate.setAlarmLifeEntity(alarmLifeEntityDto);
			ResponseDto responseDto = emailServiceClient.alarmToMail(alarmInfoTemplate);
			if (null != responseDto && 0 != responseDto.getErrcode()) {
				LOGGER.error("告警邮件通知失败：" + responseDto.getErrmsg());
			}
		}
		return infoMap;
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
	private Map<String, Object> strategyNotice(AlarmLifeEntity alarmLifeEntity, TemplateMessageDto atm, Integer index,
			NoticeModel model, ArrayList<String> toEmails) throws Exception {
		ResponseDto responseDto = null;
		ArrayList<Integer> toUserIds = new ArrayList<Integer>(1);// 记录被通知人
		Map<String, Object> infoMap = new HashMap<String, Object>();
		StrategyQueryDto strategyQuery = new StrategyQueryDto();// 调用告警策略服务
		strategyQuery.setSeverity(Severity.valueOf(alarmLifeEntity.getAlarm().getSeverity().name()));
		strategyQuery.setItemType(alarmLifeEntity.getItemType());
		strategyQuery.setTenantId(alarmLifeEntity.getAlarm().getTenantId());
		AlarmStrategyDto dispatchStrategy = alarmStrategyClient.getStrategy(strategyQuery);
		if (null == dispatchStrategy) {
			return infoMap;
		}
		short groupId = dispatchStrategy.getGroupIds().get(index);
		// 调用组服务获取组信息
		GroupEntityDto groupDto = groupServiceClient.selectGroupInfo(alarmLifeEntity.getAlarm().getTenantId(), groupId);
		if (null == groupDto) {
			return infoMap;
		}

		switch (model) {
		case ROUND_ROBIN:// 轮询通知方式
			Integer targetUserId = TeamRoundRobin.getToUser(alarmLifeEntity.getAlarm().getTenantId(), groupId,
					groupDto.getUserId());
			if (null == targetUserId) {
				break;
			}
			int firstNotice = 1;
			if (index + 1 == firstNotice) {
				alarmLifeEntity.setHandlerCurrent(targetUserId);// 首次通知，则写入当前处理人
			}
			alarmLifeEntity.getToHireUserIds().add(targetUserId);// 记录告警生命周期-推送人
			NoticeStrategyDto noticeStrategy = alarmStrategyClient.selectNoticeStrategy(targetUserId);// 获取通知策略
			if (noticeStrategy == null) {
				noticeStrategy = new NoticeStrategyDto();
				noticeStrategy.setEmailNotice(Boolean.TRUE);
				noticeStrategy.setWechatNotice(Boolean.TRUE);
			}
			if (noticeStrategy.isWechatNotice()) {
				atm.setUserIds(Arrays.asList(targetUserId));// 调用微信管理服务，发送模板消息
				responseDto = wechatServiceClient.SendWeChatInfo(atm);
				if (null != responseDto && 0 != responseDto.getErrcode()) {
					LOGGER.error("微信告警失败：" + responseDto.getErrmsg());
				}
			}

			LocalAuthDto localAuth = userServiceClient.selectLocalAuthById(targetUserId);
			LOGGER.info("推送人员：" + localAuth.getRealName());
			if (null != localAuth) {
				if (noticeStrategy.isEmailNotice()) { // 记录邮件发送人
					toEmails.add(localAuth.getEmail());
				}
				String dispatcher = null;// 返回告警派送人
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
			List<Integer> userIds = new ArrayList<Integer>();// 记录微信通知人
			UsersQueryDto usersQuery = new UsersQueryDto();
			usersQuery.setUserIds(groupDto.getUserId());
			List<LocalAuthDto> authDtos = userServiceClient.selectUserInfos(usersQuery);
			if (null != authDtos) {
				for (LocalAuthDto user : authDtos) {
					LOGGER.info("推送人员：" + user.getRealName());
					// 写入当前处理人
					// alarmLifeEntity.setHandlerCurrent(user);
					// 记录告警生命周期-推送人
					alarmLifeEntity.getToHireUserIds().add(user.getId());
					toUserIds.add(user.getId());
					NoticeStrategyDto noticeStrategy1 = alarmStrategyClient.selectNoticeStrategy(user.getId());
					// 记录邮件发送人
					if (noticeStrategy1.isEmailNotice()) {
						toEmails.add(user.getEmail());
					}
					if (noticeStrategy1.isWechatNotice()) {
						userIds.add(user.getId());
					}
				}
			}
			// 调用微信管理服务，发送模板消息
			if (!userIds.isEmpty()) {
				atm.setUserIds(userIds);
				responseDto = wechatServiceClient.SendWeChatInfo(atm);
				if (null != responseDto && 0 != responseDto.getErrcode()) {
					LOGGER.error("微信告警失败：" + responseDto.getErrmsg());
				}
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
	private void transferNotice(AlarmLifeEntity alarmLifeEntity, TemplateMessageDto atm, ArrayList<String> toEmails)
			throws Exception {
		int userId = atm.getUserIds().get(0);
		NoticeStrategyDto noticeStrategy = alarmStrategyClient.selectNoticeStrategy(userId);
		if (noticeStrategy == null) {
			noticeStrategy = new NoticeStrategyDto();
			noticeStrategy.setEmailNotice(Boolean.TRUE);
			noticeStrategy.setWechatNotice(Boolean.TRUE);
		}
		if (noticeStrategy.isWechatNotice()) {
			// 调用微信管理服务，发送模板消息
			ResponseDto responseDto = wechatServiceClient.SendWeChatInfo(atm);
			if (null != responseDto && 0 != responseDto.getErrcode()) {
				LOGGER.error("微信告警失败：" + responseDto.getErrmsg());
			}
		}
		if (noticeStrategy.isEmailNotice()) {
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			// 记录邮件发送人
			if (null != user) {
				toEmails.add(user.getEmail());
			}
		}
		alarmLifeEntity.getToHireUserIds().add(userId);// 记录被通知人
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
		ArrayList<String> toEmails = new ArrayList<String>(1);// 用于记录邮件发送人
		// 转换为微信模板消息体
		TemplateMessageDto templateMessageDto = DataConvert.AlarmTempConvert(alarmLifeEntity.getAlarm());
		templateMessageDto.setSendType(SendType.ALARMRECOVERY);
		templateMessageDto.setRedirect_uri(
				urlAlarmDetails + alarmLifeEntity.getId() + "$" + alarmLifeEntity.getAlarm().getTenantId());

		List<Integer> emailUserIds = new ArrayList<>();
		List<Integer> wechatUserIds = new ArrayList<>();
		for (Integer userId : alarmLifeEntity.getToHireUserIds()) {
			NoticeStrategyDto noticeStrategy = alarmStrategyClient.selectNoticeStrategy(userId);
			if (noticeStrategy == null) {
				noticeStrategy = new NoticeStrategyDto();
				noticeStrategy.setEmailNotice(Boolean.TRUE);
				noticeStrategy.setWechatNotice(Boolean.TRUE);
			}
			if (noticeStrategy.isEmailNotice()) {
				emailUserIds.add(userId);
			}
			if (noticeStrategy.isWechatNotice()) {
				wechatUserIds.add(userId);
			}
		}
		// 发送邮件通知
		UsersQueryDto usersQuery = new UsersQueryDto();
		usersQuery.setUserIds(emailUserIds);
		List<LocalAuthDto> authDtos = userServiceClient.selectUserInfos(usersQuery);
		if (null != authDtos) {
			for (LocalAuthDto user : authDtos) {
				// 记录邮箱发送人
				toEmails.add(user.getEmail());
			}
		}
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

		// 发送微信通知
		templateMessageDto.setUserIds(wechatUserIds);
		wechatServiceClient.SendWeChatInfo(templateMessageDto);
		// 发送web消息
		msgSender.alarmMsgSend(alarmLifeEntity, new ArrayList<>(alarmLifeEntity.getToHireUserIds()),
				NoticeType.RECOVERY);
	}

}
