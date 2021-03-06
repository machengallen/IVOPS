package com.iv.wechat.service;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.Constants;
import com.iv.common.util.spring.SpringContextUtil;
import com.iv.dto.TemplateFormMessageDto;
import com.iv.dto.TemplateMessageDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.enumeration.LoginType;
import com.iv.external.service.IAuthenticationServiceClient;
import com.iv.external.service.UserServiceClient;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.outer.dto.UserOauthDto;
import com.iv.wechat.autoReply.EventSubMessage;
import com.iv.wechat.autoReply.MessageResponse;
import com.iv.wechat.dao.MessageDaoImpl;
import com.iv.wechat.dao.UserWechatDaoImpl;
import com.iv.wechat.dto.ErrorMsg;
import com.iv.wechat.dto.QrcodeActionInfo;
import com.iv.wechat.dto.QrcodeRequest;
import com.iv.wechat.dto.QrcodeScene;
import com.iv.wechat.dto.QrcodeTicket;
import com.iv.wechat.dto.TemplateMessage;
import com.iv.wechat.dto.WeixinOauth2Token;
import com.iv.wechat.entity.UserWechatEntity;
import com.iv.wechat.robot.TulingApiProcess;
import com.iv.wechat.util.AuthorizationUtil;
import com.iv.wechat.util.InitBean;
import com.iv.wechat.util.MessageUtil;
import com.iv.wechat.util.WechatUtil;

import net.sf.json.JSONObject;

@Service
public class WeChatService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatService.class);
	@Value("${iv.wechat.open.redirectUri}")
	private String openRedirectUri;
	@Value("${iv.wechat.open.loginQrCode}")
	private String loginQrCode;	
	@Value("${iv.wechat.open.appId}")
	private String openAppId;
	@Value("${iv.wechat.open.secret}")
	private String openSecret;	
	@Value("${iv.wechat.open.userInfo}")
	private String openUserInfo;	
	@Value("${iv.wechat.urlQrcodeCreate}")
	private String urlQrcodeCreate;
	@Value("${iv.wechat.urlQrcodeGet}")
	private String urlQrcodeGet;
	@Value("${iv.wechat.urlUserInfo}")
	private String urlUserInfo;
	@Value("${iv.wechat.focusTips}")
	private String focusTips;
	@Value("${iv.wechat.urlNodeBinding}")
	private String urlNodeBinding;
	@Value("${iv.wechat.templateAlarm}")
	private String templateAlarm;
	@Value("${iv.wechat.templateRecovery}")
	private String templateRecovery;
	@Value("${iv.wechat.urlAlarmDetails}")
	private String urlAlarmDetails;
	@Value("${iv.wechat.appId}")
	private String appId;	
	@Value("${iv.wechat.secret}")
	private String secret;	
	@Value("${iv.wechat.urlTemplateMsg}")
	private String urlTemplateMsg;
	@Value("${iv.wechat.templateForm}")
	private String templateForm;
	@Autowired
	private UserServiceClient userService;
	@Autowired
	private UserWechatDaoImpl userWechatDao;	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	@Autowired
	private InitBean initBean;
	@Autowired
	private WechatUtil wechatUtil;
	@Autowired
	private MessageDaoImpl messageDao;
	@Autowired
    protected AuthenticationManager authenticationManager;
	@Autowired
	private IAuthenticationServiceClient authenticationServiceClient;
	
	
	public String getWechatLoginCode() throws UnsupportedEncodingException {
		String loginRedirectUri = URLEncoder.encode(openRedirectUri,"utf-8");
		String qrCode = String.format(loginQrCode, openAppId,loginRedirectUri);
		LOGGER.info(qrCode);
		System.out.println(qrCode);
		LOGGER.error(qrCode);
		return qrCode;	
	}
	
	public ResponseDto wxLoginCallBack(String code, HttpServletRequest request) {
		ResponseDto dto = new ResponseDto();
		// 获取网页授权access_token
		WeixinOauth2Token weixinOauth2Token = authorizationUtil.getAccessToken(openAppId, openSecret, code);
        //用户标识	            
        String unionid = weixinOauth2Token.getUnionid();
        //根据unionid、登录方式，查询用户绑定情况
        UserOauthDto userOauthDto = userService.bindInfo(unionid, LoginType.WECHAT);
        if(null == userOauthDto) {
        	//无绑定用户，将微信信息存入数据库，并跳转到绑定注册页面
        	UserWechatEntity userWechatEntity = authorizationUtil.getUserInfo(openUserInfo, weixinOauth2Token.getAccessToken(), weixinOauth2Token.getOpenId());       	
        	Map<String,String> platformSign = userWechatEntity.getPlatformSigns();
        	if(null == platformSign) {
        		platformSign = new HashMap<String,String>();
        	}
        	platformSign.put(openAppId,weixinOauth2Token.getOpenId());       	
        	userWechatEntity.setPlatformSigns(platformSign);
        	userWechatDao.saveOrUpdateUserWechat(userWechatEntity);
        	dto.setData(userWechatEntity);
        	dto.setErrorMsg(ErrorMsg.WECHAT_UNBOUNDED);
        	return dto;
        }        
        //有绑定信息，查询绑定用户，自动登录
        LocalAuthDto localAuthDto = userService.selectLocalAuthById(userOauthDto.getUserId());
        //用户自动登录
        dto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
        System.out.println(localAuthDto.getUserName());
        System.out.println(localAuthDto.getPassWord());
        dto.setData(authenticationServiceClient.token(Constants.OAUTH2_CLIENT_BASIC, "password", localAuthDto.getUserName() + Constants.THREE_PARTY_LOGIN, localAuthDto.getPassWord()));
    		   
        return dto;
	}
	
	
	/**
	 * 微信公众号带参二维码生成：关注公众号
	 * @return
	 * @throws Exception 
	 */
	public QrcodeTicket qrcodeCreate(int userId) throws Exception {
		QrcodeScene scene = new QrcodeScene(userId);
		QrcodeActionInfo actionInfo = new QrcodeActionInfo(scene);
		QrcodeRequest qrcodeRequest = new QrcodeRequest(600, "QR_SCENE", actionInfo);
		// 从微信服务端获取二维码
		WechatUtil wechatUtil = SpringContextUtil.getBean(WechatUtil.class);
		JSONObject jsonObject = wechatUtil.httpPost(urlQrcodeCreate, wechatUtil.getToken().getAccessToken(),
				qrcodeRequest);
		if (null == jsonObject) {
			// token异常，重新获取
			System.out.println("****************更新 wechat token****************");
			jsonObject = wechatUtil.httpPost(urlQrcodeCreate, wechatUtil.getTokenDirect().getAccessToken(),
					qrcodeRequest);
		}

		QrcodeTicket qrcodeTicket = new QrcodeTicket();
		qrcodeTicket.setExpire_seconds(jsonObject.getInt("expire_seconds"));
		StringBuffer ticketUrl = new StringBuffer();
		ticketUrl.append(urlQrcodeGet);
		ticketUrl.append(jsonObject.getString("ticket"));
		qrcodeTicket.setUrl(ticketUrl.toString());

		return qrcodeTicket;
	}
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		LOGGER.info("***************微信事件推送******************");
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			// 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 从HashMap中取出消息中的字段；
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			LOGGER.info(fromUserName);
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 创建时间
			Long createTime = Long.parseLong(requestMap.get("CreateTime"));
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content = requestMap.get("Content");
			// 从HashMap中取出消息中的字段；

			LOGGER.info("fromUserName is:" + fromUserName + " toUserName is:" + toUserName + " msgType is:" + msgType);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 微信聊天机器人测试
				if (content != null) {
					respContent = TulingApiProcess.getTulingResult(content);
					if (respContent == "" || null == respContent) {
						MessageResponse.getTextMessage(fromUserName, toUserName, "服务号暂时无法回复，请稍后再试！");
					}
					// return FormatXmlProcess.formatXmlAnswer(toUserName,
					// fromUserName, respContent);
					return MessageResponse.getTextMessage(fromUserName, toUserName, respContent);
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送

				String eventType = requestMap.get("Event");// 事件类型
				String eventKey = null;
				// String ticket = null;
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
					System.out.println("订阅：");
					LOGGER.error("订阅：");
					// 获取关注用户的信息并存储
					JSONObject jsonObject = initBean.getToWechat(urlUserInfo, "OPENID", fromUserName, null);
					// 校验返回结果
					Object errcode = jsonObject.get("errcode");
					if (null != errcode && 0 != Integer.parseInt(errcode.toString())) {
						System.out.println("请求微信异常：" + jsonObject.toString());
						LOGGER.error("请求微信异常：" + jsonObject.toString());
						jsonObject = initBean.getToWechat(urlUserInfo, "OPENID", fromUserName,
								wechatUtil.getTokenDirect().getAccessToken());
					}
					UserWechatEntity userWechatEntity = (UserWechatEntity) JSONObject.toBean(jsonObject,
							UserWechatEntity.class);
					Map<String, String> map = userWechatEntity.getPlatformSigns();
					if(null == map) {
						map = new HashMap<>();
						map.put(appId, jsonObject.getString("openid"));
					}else {
						map.put(appId, jsonObject.getString("openid"));
					}
					userWechatEntity.setPlatformSigns(map);
					userWechatEntity.filterEmoji();					
					userWechatDao.saveOrUpdateUserWechat(userWechatEntity);
					// 公众号关注提示
					respContent = this.focusTips;
					// 判断是否为带参二维码场景
					if (requestMap.containsKey("EventKey")) {
						eventKey = requestMap.get("EventKey");
						// ticket = requestMap.get("Ticket");
					}

					if (!StringUtils.isEmpty(eventKey)) {
						// 带参二维码扫描事件，进行微信账号绑定
						eventKey = eventKey.split("_")[1];
						if (!StringUtils.isEmpty(eventKey)) {
							//boolean result = LOCAL_USER_DAO.updateOpenId(Integer.parseInt(eventKey), userWechatEntity);
							//公众号未绑定其他账号
							/*UserOauthDto userOauthDto = userService.bindInfo(userWechatEntity.getUnionid(), LoginType.WECHAT);
							//账号未绑定其他微信
							UserOauthDto userOauthDto1 = userService.selectUserWechatUnionid(Integer.parseInt(eventKey), LoginType.WECHAT);*/
							if (true) {
								//userService.focusBindWechat(Integer.parseInt(eventKey), userWechatEntity.getUnionid());
								/*UserOauthDto UserOauthDto = new UserOauthDto();
								UserOauthDto.setLoginType(LoginType.WECHAT);
								UserOauthDto.setUnionid(userWechatEntity.getUnionid());
								UserOauthDto.setUserId(Integer.parseInt(eventKey));*/
								
								// 感知前端，页面跳转
								ResponseDto responseDto = new ResponseDto();
								responseDto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
								SpringContextUtil.getBean(WechatUtil.class).httpPost(urlNodeBinding, null, responseDto);
								// 完善微信账户信息
								/*LocalAuth localAuth = LOCAL_USER_DAO.selectUserAuthById(Integer.parseInt(eventKey));
								userWechatEntity.setRemark(localAuth.getRealName());
								userWechatEntity.setTel(localAuth.getTel());
								userDao.saveUserInfo(userWechatEntity);*/
							} else {
								// 感知前端，错误提示
								ResponseDto responseDto = new ResponseDto();
								responseDto.setErrorMsg(ErrorMsg.WECHAT_BINDING_ILLEGAL);
								SpringContextUtil.getBean(WechatUtil.class).httpPost(urlNodeBinding, null, responseDto);
							}
						}
					}

					respMessage = MessageResponse.getTextMessage(fromUserName, toUserName, respContent);
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅
					// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					// 删除事件消息
					messageDao.deleteSubMessage(fromUserName);
					// 微信解绑,并删除微信用户信息,删除微信公众号信息
					UserWechatEntity userWechatEntity = userWechatDao.selectUserWechatByUnionid(fromUserName);
					userWechatEntity.getPlatformSigns().remove(appId);
					userWechatDao.saveOrUpdateUserWechat(userWechatEntity);
					//LOCAL_USER_DAO.unboundWechat(userDao.selectUserWechatById(fromUserName));
					//userDao.deleteUserInfoById(fromUserName);
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
					eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					LOGGER.info("eventKey is:" + eventKey);
					return MenuClickService.getClickResponse(eventKey, fromUserName, toUserName);
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
					eventKey = requestMap.get("EventKey");
					// 已关注用户的微信账号绑定
					//UserWechatEntity wechatEntity = userDao.selectUserWechatById(fromUserName);
					if (!StringUtils.isEmpty(eventKey)) {
						//boolean result = LOCAL_USER_DAO.updateOpenId(Integer.parseInt(eventKey), wechatEntity);
						if (true) {
							// 感知前端，页面跳转
							/*ResponseDto responseDto = new ResponseDto();
							responseDto.setErrorMsg(com.iv.common.response.ErrorMsg.OK);
							SpringContextUtil.getBean(WechatUtil.class).httpPost(urlNodeBinding, null, responseDto);
							// 完善微信账户信息
*/							/*LocalAuth localAuth = LOCAL_USER_DAO.selectUserAuthById(Integer.parseInt(eventKey));
							wechatEntity.setRemark(localAuth.getRealName());
							wechatEntity.setTel(localAuth.getTel());
							userDao.saveUserInfo(wechatEntity);*/
						} else {
							// 感知前端，错误提示
							/*ResponseDto responseDto = new ResponseDto();
							responseDto.setErrorMsg(ErrorMsg.WECHAT_BINDING_ILLEGAL);
							SpringContextUtil.getBean(WechatUtil.class).httpPost(urlNodeBinding, null, responseDto);*/
						}
					}
				}

				// 记录事件信息
				EventSubMessage esm = new EventSubMessage();
				esm.setToUserName(toUserName);
				esm.setFromUserName(fromUserName);
				esm.setCreateTime(createTime);
				esm.setMsgType(msgType);
				esm.setEvent(eventType);
				esm.setEventKey(eventKey);
				// esm.setTicket(ticket);
				messageDao.saveEventSubMessage(esm);

				return respMessage;
			}

			// 开启微信声音识别测试
			else if (msgType.equals("voice")) {
				String recvMessage = requestMap.get("Recognition");
				// respContent = "收到的语音解析结果："+recvMessage;
				if (recvMessage != null) {
					respContent = TulingApiProcess.getTulingResult(recvMessage);
				} else {
					respContent = "您说的太模糊了，能不能重新说下呢？";
				}
				return MessageResponse.getTextMessage(fromUserName, toUserName, respContent);
			}
			// 拍照功能
			else if (msgType.equals("pic_sysphoto")) {

			} else {
				return MessageResponse.getTextMessage(fromUserName, toUserName, "返回为空");
			}

		} catch (Exception e) {
			LOGGER.error("微信消息处理失败", e);
		}

		return respMessage;
	}
	
	public UserWechatEntityDto selectUserWechatByUnionid(String unionid) {
		UserWechatEntity userWechatEntity = userWechatDao.selectUserWechatByUnionid(unionid);
		UserWechatEntityDto userWechatEntityDto = new UserWechatEntityDto();
		BeanCopier copy=BeanCopier.create(UserWechatEntity.class, UserWechatEntityDto.class, false);
		copy.copy(userWechatEntity, userWechatEntityDto, null);
		return userWechatEntityDto;
	}
	
	/**
	 * 发送微信模板消息
	 * @throws Exception 
	 */
	public void SendWeChatInfo(TemplateMessageDto templateMessageDto) throws Exception{
		TemplateMessage templateMessage = new TemplateMessage();
		templateMessage.setData(templateMessageDto.getData());
		if(templateMessageDto.getSendType().toString().toUpperCase().equals("ALARMTRIGGER")) {
			templateMessage.setTemplate_id(templateAlarm);
		}
		if(templateMessageDto.getSendType().toString().toUpperCase().equals("ALARMRECOVERY")) {
			templateMessage.setTemplate_id(templateRecovery);
		}
		String encodeUrl = URLEncoder.encode(templateMessageDto.getRedirect_uri(), "UTF-8");
		String url = String.format(urlAlarmDetails, appId,encodeUrl);
		templateMessage.setUrl(url);	
		UsersQueryDto UsersQueryDto = new UsersQueryDto(templateMessageDto.getUserIds(),LoginType.WECHAT);
		Set<String> unionids = userService.selectUsersWechatUnionid(UsersQueryDto);
		//有绑定账号的用户
		if(!CollectionUtils.isEmpty(unionids)) {
			List<UserWechatEntity> userWechatEntitys = userWechatDao.selectUserWechatsByUnionids(unionids);
			String token = wechatUtil.getToken().getAccessToken();
			String uri = urlTemplateMsg;
			for (UserWechatEntity userWechatEntity : userWechatEntitys) {
				String openId = userWechatEntity.getPlatformSigns().get(appId);	
				System.out.println(openId);
				//已关注微信公众号
				if(!StringUtils.isEmpty(openId)) {
					templateMessage.setTouser(openId);		
					JSONObject jsonObject = wechatUtil.httpPost(uri, token, templateMessage);
					if (null == jsonObject) {
						// token异常，重新获取
						System.out.println("****************更新 wechat token****************");
						jsonObject = wechatUtil.httpPost(uri, wechatUtil.getTokenDirect().getAccessToken(), templateMessage);
					}
				}else {
					LOGGER.info("****************" + userWechatEntity.getNickname() + "未关注微信公众号****************");
				}
				
			}	
		}else {
			LOGGER.info("****************推送账号均为绑定微信****************");
		}
					
		
	}
	
	/**
	 * 根据userId判断是否已关注微信公众号
	 * @param userId
	 * @return
	 */
	public boolean ifFocusWechat(int userId) {
		boolean ifFocus = false;
		UserOauthDto userOauthDto = userService.selectUserWechatUnionid(userId,LoginType.WECHAT);
		if(null == userOauthDto) {
			return ifFocus;
		}
		UserWechatEntity userWechatEntity = userWechatDao.selectUserWechatByUnionid(userOauthDto.getUnionid());
		if(!StringUtils.isEmpty(userWechatEntity.getPlatformSigns().get(appId))) {
			ifFocus = true;
		}
		return ifFocus;
	}
	
	/**
	 * 发送工单微信模板消息
	 * @param templateFormMessageDto
	 * @return
	 * @throws Exception 
	 */
	public void SendFormWeChatInfo(TemplateFormMessageDto templateFormMessageDto) throws Exception {

		TemplateMessage templateMessage = new TemplateMessage();
		templateMessage.setData(templateFormMessageDto.getData());
		templateMessage.setTemplate_id(templateForm);		
		String encodeUrl = URLEncoder.encode(templateFormMessageDto.getRedirect_uri(), "UTF-8");
		String url = String.format(urlAlarmDetails, appId,encodeUrl);
		templateMessage.setUrl(url);	
		UsersQueryDto UsersQueryDto = new UsersQueryDto(templateFormMessageDto.getUserIds(),LoginType.WECHAT);
		Set<String> unionids = userService.selectUsersWechatUnionid(UsersQueryDto);
		if(!CollectionUtils.isEmpty(unionids)) {
			List<UserWechatEntity> userWechatEntitys = userWechatDao.selectUserWechatsByUnionids(unionids);
			String token = wechatUtil.getToken().getAccessToken();
			String uri = urlTemplateMsg;
			for (UserWechatEntity userWechatEntity : userWechatEntitys) {
				String openId = userWechatEntity.getPlatformSigns().get(appId);	
				if(!StringUtils.isEmpty(openId)) {
					templateMessage.setTouser(openId);		
					JSONObject jsonObject = wechatUtil.httpPost(uri, token, templateMessage);
					if (null == jsonObject) {
						// token异常，重新获取
						System.out.println("****************更新 wechat token****************");
						jsonObject = wechatUtil.httpPost(uri, wechatUtil.getTokenDirect().getAccessToken(), templateMessage);
					}
				}else {
					LOGGER.info("****************" + userWechatEntity.getNickname() + "未关注微信公众号****************");
				}
				
			}	
		}else {
			LOGGER.info("****************推送账号均为绑定微信****************");
		}
					
		
	
	}
	
	/**
	 * 获取用户微信unioid
	 * @param code
	 * @return
	 */
	public String getUnionid(String code) {
		WeixinOauth2Token weixinOauth2Token = authorizationUtil.getAccessToken(appId, secret, code); 
		if(null != weixinOauth2Token) {
			return weixinOauth2Token.getUnionid();
		}
        return null;
	}
}
