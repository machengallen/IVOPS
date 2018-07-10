package com.iv.wechat.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.iv.entity.dto.UserWechatEntityDto;
import com.iv.wechat.autoReply.BatchOpenId;
import com.iv.wechat.autoReply.BatchOpenIdList;
import com.iv.wechat.autoReply.OpenIdData;
import com.iv.wechat.autoReply.UserOpenIdList;
import com.iv.wechat.dao.TokenDaoImpl;
import com.iv.wechat.dao.UserWechatDaoImpl;
import com.iv.wechat.dto.UserWechatDto;
import com.iv.wechat.entity.UserWechatEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 容器初始化工具类
 * 
 * @author macheng
 *
 */
@Component
@DependsOn("hibernateUtil")
// @ConfigurationProperties(prefix = "iv.wechat")
public class InitBean implements InitializingBean {

	@Value("${iv.wechat.urlOpenIds}")
	private String urlOpenIds;
	@Value("${iv.wechat.nextOpenId}")
	private String nextOpenId;
	@Value("${iv.wechat.urlUserInfo}")
	private String urlUserInfo;
	@Value("${iv.wechat.urlUserInfoBatch}")
	private String urlUserInfoBatch;
	@Value("${iv.wechat.appId}")
	private String appId;
	

	@Autowired
	private WechatUtil wechatUtil;
	/*@Autowired
	private TokenDaoImpl tokenDao;*/
	@Autowired
	private UserWechatDaoImpl userWechatDao;
	

	private static Logger logger = LoggerFactory.getLogger(InitBean.class);

	/**
	 * 容器初始化时，首次获取公众号关注用户的信息
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("****************初始化微信公众号关注者信息****************");
		try {
			// 获取微信服务端数据
			JSONObject jsonObject = getToWechat(urlOpenIds, "NEXT_OPENID", nextOpenId, null);
			// 校验返回结果
			Object errcode = jsonObject.get("errcode");
			if (null != errcode && 0 != Integer.parseInt(errcode.toString())) {
				System.out.println("请求微信异常：" + jsonObject.toString());
				logger.error("请求微信异常：" + jsonObject.toString());
				jsonObject = getToWechat(urlOpenIds, "NEXT_OPENID", nextOpenId,
						wechatUtil.getTokenDirect().getAccessToken());
			}
			UserOpenIdList openIdList = null;
			// 数据封装
			if (null != jsonObject) {
				if (jsonObject.getInt("count") != 0) {
					openIdList = new UserOpenIdList();
					openIdList.setTotal(jsonObject.getInt("total"));
					openIdList.setCount(jsonObject.getInt("count"));
					JSONObject data = jsonObject.getJSONObject("data");
					JSONArray openid = data.getJSONArray("openid");
					openIdList.setData(new OpenIdData(JSONArray.toList(openid, String.class)));
					openIdList.setNext_openid(jsonObject.getString("next_openid"));

					// 获取关注用户的信息
					// getWechatUserInfoList(openIdList);
					batchWechatUserInfoList(openIdList);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("容器初始化：获取微信用户列表失败", e);
		}
	}

	/*
	 * @PostConstruct public void getWechatOpenIdList() {
	 * 
	 * 
	 * }
	 */

	/**
	 * 获取用户基本信息
	 * 
	 * @param openIdList
	 */
	@SuppressWarnings("unused")
	private void getWechatUserInfoList(UserOpenIdList openIdList) {

		try {
			UserWechatDto userWechatDto = new UserWechatDto();
			List<UserWechatEntityDto> userWechatEntities = new ArrayList<UserWechatEntityDto>(10);
			for (String openId : openIdList.getData().getOpenid()) {
				// 获取微信服务端数据
				JSONObject jsonObject = getToWechat(urlUserInfo, "OPENID", openId, null);
				// 用户数据封装
				UserWechatEntityDto UserWechatEntityDto = (UserWechatEntityDto) JSONObject.toBean(jsonObject,
						UserWechatEntityDto.class);
				UserWechatEntityDto.filterEmoji();
				userWechatEntities.add(UserWechatEntityDto);
			}
			userWechatDto.setUserWechatEntities(userWechatEntities);
			// 批量存储用户数据
			//serviceClient.saveUsersInfo(userWechatDto);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("获取微信用户详情失败", e);
		}
	}

	/**
	 * 批量获取用户基本信息
	 * 
	 * @param openIdList
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	private void batchWechatUserInfoList(UserOpenIdList openIdList) {

		try {
			List<String> openids = openIdList.getData().getOpenid();
			String uri = urlUserInfoBatch;
			List<UserWechatEntityDto> userWechatEntities = new ArrayList<UserWechatEntityDto>(10);

			// 超过100条，则分批获取
			while (openids.size() > 100) {
				List<String> list = openids.subList(0, 100);
				openids = new ArrayList<String>(openids.subList(100, openids.size()));

				BatchOpenIdList batchOpenIdList = new BatchOpenIdList(new ArrayList<BatchOpenId>());
				for (String openid : list) {
					batchOpenIdList.getUser_list().add(new BatchOpenId(openid, "zh-CN"));
				}
				// 批量获取用户信息
				JSONObject jsonObject = wechatUtil.httpPost(uri, wechatUtil.getToken().getAccessToken(),
						batchOpenIdList);
				if (null == jsonObject) {
					System.out.println("****************更新 wechat token****************");
					jsonObject = wechatUtil.httpPost(uri, wechatUtil.getTokenDirect().getAccessToken(),
							batchOpenIdList);
				}
				List<UserWechatEntityDto> userList = JSONArray.toList(jsonObject.getJSONArray("user_info_list"),
						UserWechatEntityDto.class);
				userWechatEntities.addAll(userList);
			}

			// 100条以内，一次获取
			BatchOpenIdList batchOpenIdList = new BatchOpenIdList(new ArrayList<BatchOpenId>());
			for (String openid : openids) {
				batchOpenIdList.getUser_list().add(new BatchOpenId(openid, "zh-CN"));
			}
			JSONObject jsonObject = wechatUtil.httpPost(uri, wechatUtil.getToken().getAccessToken(), batchOpenIdList);
			if (null == jsonObject) {
				System.out.println("****************更新 wechat token****************");
				jsonObject = wechatUtil.httpPost(uri, wechatUtil.getTokenDirect().getAccessToken(), batchOpenIdList);
			}
			List<UserWechatEntityDto> userList = JSONArray.toList(jsonObject.getJSONArray("user_info_list"),
					UserWechatEntityDto.class);
			userWechatEntities.addAll(userList);
			List<UserWechatEntity> userWechatEntitys = new ArrayList<UserWechatEntity>();
			// 统一过滤emoji
			for (UserWechatEntityDto UserWechatEntityDto : userWechatEntities) {
				UserWechatEntityDto.filterEmoji();
				UserWechatEntity userWechatEntity = new UserWechatEntity(); 				
				BeanCopier copy=BeanCopier.create(UserWechatEntityDto.class, UserWechatEntity.class, false);
				copy.copy(UserWechatEntityDto, userWechatEntity, null);
				Map<String, String> map = new HashMap<String, String>();
				map.put(appId, UserWechatEntityDto.getOpenid());
				userWechatEntity.setPlatformSigns(map);
				userWechatEntitys.add(userWechatEntity);
			}
			userWechatDao.saveOrUpdateUserWechats(userWechatEntitys);
			/*UserWechatDto wechatDto = new UserWechatDto();
			wechatDto.setUserWechatEntities(userWechatEntities);*/
			// 批量存储用户数据
			//serviceClient.saveUsersInfo(wechatDto);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("批量获取微信用户详情失败", e);
		}
	}

	/**
	 * urlUserInfo, "OPENID", fromUserName, null
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public JSONObject getToWechat(String param1, String param2, String param3, String token) throws Exception {

		// 读取配置文件
		String url = param1;
		// 获取微信token
		if (null == token) {
			token = wechatUtil.getToken().getAccessToken();
		}
		// 创建连接
		URL u = new URL(url.replace("ACCESS_TOKEN", token).replace(param2, param3));
		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		connection.connect();
		// 读取数据
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer buffer = new StringBuffer();
		String str = null;
		while ((str = reader.readLine()) != null) {
			buffer.append(str);
		}
		reader.close();
		connection.disconnect();

		JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
		return jsonObject;
	}

}