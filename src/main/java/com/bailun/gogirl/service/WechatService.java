package com.bailun.gogirl.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.Customer;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.constant.WxUrlType;
import com.bailun.gogirl.service.myhttp.MyHttpPost;
import com.bailun.gogirl.util.ParseUtil;

@Service
public class WechatService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	MyHttpPost myHttpPost ;
	@Autowired
	WxConfig wxConfig;
	@Autowired
	AccessTokenService accessTokenService;
	//根据code获得openid和accesstoken
	public JsonResult getOpenidByCode(String code,int type) {
		String url = null;
		if(type == 0){
			url = WxUrlType.OAuth2_ACCESS_TOKEN_URL.replaceAll("APPID", wxConfig.getAppId()).replaceAll("SECRET", wxConfig.getSecret()).replaceAll("CODE", code);
		}else if(type == 1){
			url = WxUrlType.OAuth2_ACCESS_TOKEN_URL.replaceAll("APPID", wxConfig.getAppId1()).replaceAll("SECRET", wxConfig.getSecret1()).replaceAll("CODE", code);
		}else if(type == 1){
			url = WxUrlType.OAuth2_ACCESS_TOKEN_URL.replaceAll("APPID", wxConfig.getAppId2()).replaceAll("SECRET", wxConfig.getSecret2()).replaceAll("CODE", code);
		}else{
			return new JsonResult(false,"type不对",null);
		}
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}

	public JsonResult getUserInfoByAccessToken(String access_token) {
		String url = WxUrlType.OAuth2_USER_INFO_URL.replaceAll("APPID", wxConfig.getAppId()).replaceAll("ACCESS_TOKEN", access_token);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}
	public JsonResult getUserInfoByAccessToken2(String openid) {
		String token = accessTokenService.getAccess_Token();
		String url = WxUrlType.OAuth2_USER_INFO_URL2.replaceAll("OPENID", openid).replaceAll("ACCESS_TOKEN", token);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}
	
	
	//从微信服务器获得用户信息
	public Customer getCustomerBycode(String code,int type) throws Exception {
		//获得openid和accesstoken
		JsonResult responseResult = getOpenidByCode(code,type);
		if(!responseResult.getSuccess()){
			return null;
		}
		Map<String, Object> accesstoken_map = (Map<String, Object>)responseResult.getData();
		if(accesstoken_map.containsKey("errcode")&&(accesstoken_map.get("errcode").equals(40029)||accesstoken_map.get("errcode").equals(40163))){
			logger.info("40029");
			throw new Exception(JsonResult.INVALID_CODE);
		}
		String access_token = (String) accesstoken_map.get("access_token");
		String openid = (String) accesstoken_map.get("openid");
		
//		if(openid.equals("o7112v-gTwp2OnTun_iBfiRz9_do")){
//			JsonResult  customerResult = getUserInfoByAccessToken2(openid);
//			if(customerResult.getSuccess()){
//				return null;
//			}
//			return null;
//		}
		
		//openid查询数据库信息
//		JsonResult jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByOpenid/"+openid, null,JsonResult.class );
		JsonResult jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByOpenid/"+openid, null);
		Customer customer = null;
		//判断数据库是否查询的到该用户信
		if(jsonResult==null||!jsonResult.getSuccess()){
			return null;
		}
		if(jsonResult.getData()!=null&&((Map<String, Object>)jsonResult.getData()).get("user")!=null){//***数据库存在用户
			Map<?,?> map = (Map<?,?>) ((Map<String, Object>)jsonResult.getData()).get("user");
//			Customer c = (Customer) ((Map<String, Object>)jsonResult.getData()).get("user");
			if(map!=null&&map.get("updateTime")!=null){
				String updateTime =  (String) map.get("updateTime");
					try {
						if(greaterThan30Day(updateTime)&&map.get("id")instanceof Integer){//当前时间距离该用户上次登陆是否大于30天
							customer = updateInfoFromWechat((Integer)map.get("id"),access_token);//更新数据
						}else{//使用数据库查询得到的数据
							customer = new Customer();
							customer.setId((Integer) map.get("id"));
							customer.setOpenid(String.valueOf( map.get("openid")));
							customer.setPhone(map.get("phone")==null?null:String.valueOf( map.get("phone")));
							String nickname =  filterEmoji(String.valueOf(map.get("nickname")));
							customer.setNickname(nickname);
							customer.setPassword(map.get("password")==null?null:String.valueOf( map.get("password")));
							customer.setSex(map.get("sex")==null?null:String.valueOf(map.get("sex")));
							customer.setCountry(String.valueOf( map.get("country")));
							customer.setProvince(String.valueOf( map.get("province")));
							customer.setCity(String.valueOf( map.get("city")));
							customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
							customer.setPrivilege( map.get("privilege").toString());
							customer.setState(map.get("state")==null?null:String.valueOf( map.get("state")));
							customer.setRealName(map.get("realName")==null?"":String.valueOf( map.get("realName")));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
							customer.setRemark(map.get("remark")==null?"":map.get("remark").toString());
							
	//			customer.setRegisterTime(new Date());//更新不需要这个字段
							customer.setUpdateTime(new Date());
							return customer;
						}
					} catch (Exception e) {
						return null;//TODO id不为int时候的解决方法
					}
			}else{
				customer = updateInfoFromWechat((Integer)map.get("id"),access_token);//更新数据
			}
		}else{
			customer = insertInfoFromWechat(access_token);//添加用户
		}
		return customer;
	}
	
	private Customer updateInfoFromWechat(Integer integer, String access_token){
		//通过accesstoken，从微信服务器获得用户信息
		JsonResult response2=getUserInfoByAccessToken(access_token);
		//用户信息存入数据库
		Customer customer;
		if(response2.getSuccess()==true){
			Map<String, Object> map = ((Map<String, Object>)response2.getData());
			map.put("id",integer);//从数据库中获取
			//Todo 设置id
			map.put("sex",map.get("sex").equals(1)?"男":"女");
//			map.put("state","1");//状态固定设为1正常
			
			customer = new Customer();
			customer.setId((Integer) map.get("id"));
			customer.setOpenid((String) map.get("openid"));
			customer.setPhone((String) map.get("phone"));
			String nickname =  filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword((String) map.get("password"));
			customer.setSex((String) map.get("sex"));
			customer.setCountry((String) map.get("country"));
			customer.setProvince((String) map.get("province"));
			customer.setCity((String) map.get("city"));
			customer.setHeadimgurl((String) map.get("headimgurl"));
			customer.setPrivilege( map.get("privilege").toString());
			customer.setState((String) map.get("state"));
//			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			customer.setSource(3);
			/*更新数据库客户客户信息*/
//			restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective", customer,JsonResult.class );
			myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",ParseUtil.paramsToMap(customer));
			/*更新数据库客户客户信息*/
		}else{
			logger.info("JsonResult response2=getUserInfoByAccessToken(access_token);==false");
			return null;
		}
		return customer;
	}
	private Customer insertInfoFromWechat(String access_token){
		//通过accesstoken，从微信服务器获得用户信息
		JsonResult response2=getUserInfoByAccessToken(access_token);
		//用户信息存入数据库
		logger.info("getUserInfoByAccessToken:"+response2);
		Customer customer;
		if(response2.getSuccess()==true){
			Map<String, Object> map = ((Map<String, Object>)response2.getData());
//			map.put("id",1);//自增
			//Todo 设置id
			map.put("sex",map.get("sex").equals(1)?"男":"女");
			map.put("state","1");//状态固定设为1正常
			
			customer = new Customer();
//			customer.setId((Integer) map.get("id"));//自增
			if(map.get("unionid")!=null){
				logger.info("unionid:"+map.get("unionid"));
				customer.setUnionid((String) map.get("unionid"));
			}
			customer.setOpenid((String) map.get("openid"));
			customer.setPhone((String) map.get("phone"));
			String nickname =  filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword((String) map.get("password"));
			customer.setSex((String) map.get("sex"));
			customer.setCountry((String) map.get("country"));
			customer.setProvince((String) map.get("province"));
			customer.setCity((String) map.get("city"));
			customer.setHeadimgurl((String) map.get("headimgurl"));
			customer.setPrivilege( map.get("privilege").toString());
			customer.setState((String) map.get("state"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			customer.setSource(3);
			/*更新数据库客户客户信息*/
			System.out.println("刚生成的date："+sdf.format(new Date()));
			System.out.println("转键值对后的map："+ParseUtil.paramsToMap(customer));
//			JsonResult jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/insertCustomer",customer , JsonResult.class);
			JsonResult jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/insertCustomer",ParseUtil.paramsToMap(customer));
			/*更新数据库客户客户信息*/
			if(jsonResult!=null&&jsonResult.getSuccess()){//插入返回了用户id，把用户id设置到customer中
				Map<String, Object> map2 = ((Map<String, Object>)jsonResult.getData());
				Object idObject = map2.get("id");
				if(idObject instanceof Integer){
					customer.setId((Integer)idObject);
				}
			}
		}else{
			return null;
		}
		return customer;
	}

	public JsonResult mergeRemoveCustomer(Integer id, Customer customer) {
//		ResponseEntity<String> result1 = restTemplate.postForEntity(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",customer , String.class);
		JsonResult result1 = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",ParseUtil.paramsToMap(customer));
		System.out.println(result1);
//		JsonResult jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/mergeRemoveCustomer/"+id,null , JsonResult.class);
		JsonResult jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/mergeRemoveCustomer/"+id,null);
		return jsonResult;
	}
	public boolean  greaterThan30Day(String updateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(updateTime);
			Long test = new Date().getTime()-d.getTime();
			Long day30 = new Long("2592000000");//30天的毫秒数
			if(test.compareTo(day30)>0){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			System.out.println("*******************************************************");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getAUrl(String redirect_uri,String state) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
			state = URLEncoder.encode(state, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = WxUrlType.OAUTH2_AUTHORIZE_URL.replaceAll("APPID", wxConfig.getAppId())
			.replaceAll("REDIRECT_URI", redirect_uri).replaceAll("SCOPE", "snsapi_userinfo").replaceAll("STATE", state);
		return url;
	}

/**
  * 将emoji表情替换成空串
  *  
  * @param source
  * @return 过滤后的字符串
  */
 public static String filterEmoji(String source) {
  if (source != null && source.length() > 0) {
   return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
  } else {
   return source;
  }
 }


}
