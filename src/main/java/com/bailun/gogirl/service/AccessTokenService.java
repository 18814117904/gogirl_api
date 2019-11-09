package com.bailun.gogirl.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bailun.gogirl.bean.AccessToken;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.service.myhttp.MyHttpPost;


@Configuration          //证明这个类是一个配置文件
@EnableScheduling       //打开quartz定时器总开关

@Service
public class AccessTokenService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	WxConfig config;
	@Resource
	AccessToken accessToken;
	@Resource
	RedirectService redirectService;
	@Resource
	MyHttpPost myHttpPost;
	public String getAccess_Token() {
		System.out.println("获取accessToken");
		JsonResult cardResult = myHttpPost.httpRequest(RouteConfig.GOGIRLMP+"gogirl_mp/wx/getAccessToken", null);
		if(cardResult.getSuccess()){
			String accessToken = (String)cardResult.getData();
			return accessToken;
		}else{
			return null;
		}

//		if (accessToken == null || accessToken.getAccess_token() == null) {
//			System.out.println("从微信服务器获取");
//			AccessToken accessToken = getAccessToken(config.getAppId(), config.getSecret());
//			this.accessToken = accessToken;
//			return accessToken;
//		} else {
//			System.out.println("从上下文获取");
//			return accessToken;
//		}
	}

	/**
	 * 获取accessToken
	 * 
	 * @param appID微信公众号凭证
	 * @param appScret微信公众号凭证秘钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appID, String appScret) {
		AccessToken token = new AccessToken();
		// 访问微信服务器
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ appID + "&secret=" + appScret;
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl
					.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);

			String message = new String(b, "UTF-8");

			JSONObject json = JSONObject.fromObject(message);
			System.out.println(json.toString());
			token.setAccess_token(json.getString("access_token"));
			token.setExpires_in(new Integer(json.getString("expires_in")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}
//  @Scheduled(cron = "0 1/2 * * * *")//TODO 关闭自动获取accesstoken
////  @Scheduled(cron = "0 0/2 * * * *")//TODO 关闭自动获取accesstoken
//    public void refreshAccessToken(){
//        //获取当前时间
//        LocalDateTime localDateTime =LocalDateTime.now();
//        System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		//刷新accessToken
//        AccessToken accessToken = getAccessToken(config.getAppId(), config.getSecret());
//		this.accessToken = accessToken;
//        System.out.println("刷新accessToken为："+accessToken.getAccess_token());
//    }
	// public AccessToken getAccessToken(){
	// System.out.println("从接口中获取");
	// Jedis jedis = RedisUtil.getJedis();
	// AccessToken token = new AccessToken();
	// String url = UrlType.ACCESS_TOKEN_URL.replace("APPID",
	// config.getAppId()).replace("APPSECRET", config.getSecret());
	// JSONObject json = WeiXinUtil.doGetstr(url);
	// if(json!=null){
	// token.setAccess_token(json.getString("access_token"));
	// token.setExpires_in(json.getInt("expires_in"));
	// jedis.set("access_token", json.getString("access_token"));
	// jedis.expire("access_token", 60*60*2);
	// }
	// RedisUtil.returnResource(jedis);
	// return token;
	// }
}
