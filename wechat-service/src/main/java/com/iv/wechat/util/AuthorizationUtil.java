package com.iv.wechat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iv.wechat.dto.WeixinOauth2Token;
import com.iv.wechat.entity.UserWechatEntity;

import net.sf.json.JSONObject;
@Component
public class AuthorizationUtil {
	
	private final static Logger log = LoggerFactory.getLogger(AuthorizationUtil.class); 
	
	@Value("${iv.wechat.open.redirectUri}")
	private String openRedirectUri;
	@Value("${iv.wechat.open.QrCode}")
	private String QrCode;	
	@Value("${iv.wechat.oauthTokenUrl}")
	private String oauthTokenUrl;
	@Value("${iv.wechat.open.appId}")
	private String openAppId;	
	@Autowired
	private WechatUtil wechatUtil;
	
	/**
	 * 获取微信登录二维码
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String getQrCodeUrl() throws UnsupportedEncodingException {		
		openRedirectUri = URLEncoder.encode(openRedirectUri,"utf-8");
		QrCode = QrCode.replace("REDIRECT_URI",openRedirectUri);
		QrCode = QrCode.replace("APPID", openAppId);
        return QrCode;		
	}

	
	/**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
	public WeixinOauth2Token getAccessToken(String appId, String appSecret,String code) {
        WeixinOauth2Token wat = null;
        // 拼接请求地址        					 
       /* oauthTokenUrl = oauthTokenUrl.replace("APPID", appId);
        oauthTokenUrl = oauthTokenUrl.replace("SECRET", appSecret);
        oauthTokenUrl = oauthTokenUrl.replace("CODE", code);*/
        String url = String.format(oauthTokenUrl,appId,appSecret,code);
        // 获取网页授权凭证
        JSONObject jsonObject = wechatUtil.httpsRequest(url, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
                //开放平台，获取token的同时返回unionid
                if(jsonObject.has("unionid")) {
                	wat.setUnionid(jsonObject.getString("unionid"));
                }
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wat;
    
	}

	
	/**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
	public UserWechatEntity getUserInfo(String url,String accessToken, String openId) {

    	UserWechatEntity snsUserInfo = null;
        // 拼接请求地址    	                     
    	String userInfoUrl = String.format(url, accessToken,openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = wechatUtil.httpsRequest(userInfoUrl, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new UserWechatEntity();
                // 用户的标识
                //snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex((byte) jsonObject.getInt("sex"));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
                //unionid
                if(jsonObject.containsKey("unionid")) {
                	snsUserInfo.setUnionid(jsonObject.getString("unionid"));
                }
                // 用户特权信息
                //snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return snsUserInfo;
    
	}

	public String refreshToken(String refresh_token) {
		// TODO Auto-generated method stub
		return null;
	}

}
