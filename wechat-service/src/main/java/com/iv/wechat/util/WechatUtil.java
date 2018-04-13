package com.iv.wechat.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iv.wechat.dao.TokenDaoImpl;
import com.iv.wechat.dto.AccessToken;

import net.sf.json.JSONObject;

@Component
// @ConfigurationProperties(prefix = "iv.wechat")
public class WechatUtil {
	@Value("${iv.wechat.appId}")
	private String appId;
	@Value("${iv.wechat.secret}")
	private String secret;
	@Value("${iv.wechat.urlAccesstoken}")
	private String urlAccesstoken;
	// 日志工具实例化
	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);
	// 数据库工具实例化
	@Autowired
	private TokenDaoImpl tokenDao;

	/**
	 * 进行超时校验，并返回微信访问凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public AccessToken getToken() throws Exception {

		// 获取accessToken,判断时间有没有过期
		Long timeout = tokenDao.getTimeOut(Constants.ACCESS_TIMEOUT_KEY);
		timeout = timeout == null ? 0 : timeout;
		long now = System.currentTimeMillis();
		AccessToken token = new AccessToken();
		if (now > timeout) {
			// 超时从新获取accessToken
			token = getTokenFromWechat(appId, secret);
			tokenDao.saveAccessToken(Constants.ACCESS_TOKEN_KEY, token.getAccessToken());
			tokenDao.saveAccessToken(Constants.ACCESS_TIMEOUT_KEY,
					String.valueOf(now + (token.getExpiresIn() - 100) * 1000));
		} else {
			token.setAccessToken(tokenDao.getAccessToken(Constants.ACCESS_TOKEN_KEY));
		}
		return token;

	}

	/**
	 * 不进行超时校验，直接获取新的微信凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public AccessToken getTokenDirect() throws Exception {
		
		AccessToken token = getTokenFromWechat(appId, secret);
		tokenDao.saveAccessToken(Constants.ACCESS_TOKEN_KEY, token.getAccessToken());
		tokenDao.saveAccessToken(Constants.ACCESS_TIMEOUT_KEY,
				String.valueOf(System.currentTimeMillis() + (token.getExpiresIn() - 100) * 1000));
		return token;
	}

	/**
	 * 从微信服务端获取token
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public AccessToken getTokenFromWechat(String appid, String appsecret) {
		AccessToken accessToken = null;

		if (null == appid) {
			appid = this.appId;
		}
		if (null == appsecret) {
			appsecret = this.secret;
		}
		String requestUrl = urlAccesstoken.replace("APPID", appid).replace("APPSECRET", appsecret);
		String json = httpRequest(requestUrl, "GET", null);
		JSONObject jsonObject = JSONObject.fromObject(json);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setAccessToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				// 获取token失败
				System.out.println("获取token失败 errcode:{} errmsg:{}");
			}
		}
		return accessToken;
	}

	/**
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			// jsonObject = JSONObject.fromObject(buffer.toString());
			return buffer.toString();
		} catch (ConnectException ce) {
			logger.error("微信服务器连接超时", ce);
			System.out.println("Weixin server connection timed out.");
		} catch (Exception e) {
			logger.error("error", e);
			System.out.println("error.");
		}
		// return jsonObject;
		return null;
	}

	/**
	 * 发送模板消息至微信
	 * 
	 * @param uri
	 * @param temp
	 * @throws Exception
	 */
	@Deprecated
	public static void sendToWechat(String uri, Object temp) throws Exception {

		try {
			// 创建请求
			URL url = new URL(uri);
			JSONObject json = JSONObject.fromObject(temp);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Charset", "utf-8");
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(json.toString());
			out.flush();
			out.close();
			// 读取响应数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sbf = new StringBuffer();
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sbf.append(lines);
			}
			System.out.println(sbf);
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("模板消息发送失败", e);
		}

	}

	/**
	 * Http post方式请求封装
	 * 
	 * @param uri
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public JSONObject httpPost(String uri, String token, Object param) throws Exception {

		// String token = getToken().getAccessToken();
		URL url = null;
		if(null != token) {
			url = new URL(uri.replace("ACCESS_TOKEN", token));
		}else {
			url = new URL(uri);
		}
		JSONObject json = JSONObject.fromObject(param);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Charset", "utf-8");
		connection.connect();
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write(json.toString());
		out.flush();
		out.close();
		// 读取响应数据
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String str = null;
		StringBuffer buffer = new StringBuffer();
		while ((str = reader.readLine()) != null) {
			str = new String(str.getBytes(), "utf-8");
			buffer.append(str);
		}
		reader.close();
		connection.disconnect();
		JSONObject jsonObject = JSONObject.fromObject(buffer.toString());

		// 校验返回结果
		Object errcode = jsonObject.get("errcode");
		if (null == errcode || 0 == Integer.parseInt(errcode.toString())) {
			return jsonObject;
		} else {
			System.out.println("请求微信异常：" + jsonObject.toString());
			logger.error("请求微信异常：" + jsonObject.toString());
			return null;
		}

	}
	
	/**
     * 发送https请求
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
        	logger.error("连接超时：{}", ce);
        } catch (Exception e) {
        	logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

}
