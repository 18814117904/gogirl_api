package com.bailun.gogirl.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.Customer;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.service.SmsService;
import com.bailun.gogirl.service.myhttp.MyHttpPost;
import com.bailun.gogirl.util.CheckUtil;
import com.bailun.gogirl.util.ParseUtil;
@Controller
//@RequestMapping("/gogirl")
public class PhoneLoginController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	SmsService smsService;
//	@Resource
//	private RestTemplate restTemplate;
	@Resource
	private MyHttpPost myHttpPost;
	
	@Value("${spring.jackson.date-format}")
	String dateformat;
	@ResponseBody
	@RequestMapping("/getLoginCode")
	public JsonResult getLoginCode(HttpServletRequest request,@RequestParam String phone,HttpServletResponse response) {
		if(!CheckUtil.isPhone(phone)){
			logger.info(JsonResult.APP_DEFINE_PHONE_ERR);
			return new JsonResult(false,JsonResult.APP_DEFINE_PHONE_ERR,null);
		}
		String code=getNewCode();
		Boolean result = smsService.sendLoginSmsCode(phone, code);
		if(result){
			//验证码保存到session
			HttpSession session = request.getSession();
			System.out.println(session.hashCode());
			session.setAttribute("loginCode"+phone, code);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, null);
		}else{
			return new JsonResult(false, "发送验证码失败，请重试", null);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getBindCode")
	public JsonResult getBindCode(HttpServletRequest request,@RequestParam String phone,HttpServletResponse response) {
		if(!CheckUtil.isPhone(phone)){
			logger.info(JsonResult.APP_DEFINE_PHONE_ERR);
			return new JsonResult(false,JsonResult.APP_DEFINE_PHONE_ERR,null);
		}
		String code=getNewCode();
		Boolean result = smsService.sendBindSmsCode(phone, code);
		if(result){
			//验证码保存到session
			HttpSession session = request.getSession();
			session.setAttribute("bindCode"+phone, code);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, null);
		}else{
			return new JsonResult(false, "发送验证码失败，请重试", null);
		}
	}
	@ResponseBody
	@RequestMapping("/phoneLogin")
	public JsonResult phoneLogin(HttpServletRequest request,@RequestParam String phone,@RequestParam String code,HttpServletResponse response) {
        HttpSession session = request.getSession();
		System.out.println(session.hashCode());
		Object session_code =  session.getAttribute("loginCode"+phone);
		if(session_code==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_CODE_NULL , null);
		}
		if(!session_code.equals(code)){
			return new JsonResult(false,JsonResult.APP_DEFINE_CODE_ERR , null);
		}
		//清除验证码
//		session.removeAttribute("loginCode"+phone);
		//查询用户信息
//		JsonResult jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone, null, JsonResult.class);
		JsonResult jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone, null);
		Map<String, Object> map = new HashMap<>();
		//判断数据库是否查询的到该用户信息，若无，则存入该用户信息。
		if(jsonResult==null||jsonResult.getData()==null||((Map<String, Object>)jsonResult.getData()).get("user")==null){//***数据库存在用户
			Customer customer = new Customer();
			customer.setPhone(phone);
//			restTemplate.postForEntity(RouteConfig.GOGIRLUSER+"gogirl_user/insertCustomer",customer , String.class);
			myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/insertCustomer",ParseUtil.paramsToMap(customer));
//			jsonResult = restTemplate.postForObject(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone, null, JsonResult.class);
			jsonResult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone, null);
		}
		Map<?, ?> user =  (Map<?, ?>) ((Map<String, Object>)jsonResult.getData()).get("user");
		Customer customer = new Customer();
		customer = new Customer();
		try {
			customer.setId(user.get("id")==null?0:(Integer) user.get("id"));
			customer.setOpenid(user.get("openid")==null?"":(String) user.get("openid"));
			customer.setPhone(user.get("phone")==null?"":(String) user.get("phone"));
			customer.setNickname(user.get("nickname")==null?"":(String) user.get("nickname"));
			customer.setPassword(user.get("password")==null?"":(String) user.get("password"));
			customer.setSex(user.get("sex")==null?"":(String) user.get("sex"));
			customer.setCountry(user.get("country")==null?"":(String) user.get("country"));
			customer.setProvince(user.get("province")==null?"":(String) user.get("province"));
			customer.setCity(user.get("city")==null?"":(String) user.get("city"));
			customer.setHeadimgurl(user.get("headimgurl")==null?"":(String) user.get("headimgurl"));
			customer.setPrivilege( user.get("privilege")==null?"":user.get("privilege").toString());
			customer.setState(user.get("state")==null?"":(String) user.get("state"));
			if(user.get("registerTime")!=null&&user.get("registerTime") instanceof String){
				String time = (String) user.get("registerTime");
				SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
				customer.setRegisterTime(sdf.parse(time));
			}
			if(user.get("updateTime")!=null&&user.get("updateTime") instanceof String){
				String time = (String) user.get("updateTime");
				SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
				customer.setUpdateTime(sdf.parse(time));
			}
	//		customer.setUpdateTime(user.get("updateTime")==null?null:(Date) user.get("updateTime"));
			
		} catch (ParseException e) {
			return new JsonResult(false, "类型转换异常："+e.getMessage(), map);
		} catch (Exception e) {
			return new JsonResult(false, "异常："+e.getMessage(), map);
		}
		map.put("customer", customer);
		//保存session登陆状态
		session.setAttribute("customer",customer);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@ResponseBody
	@RequestMapping("/phoneBind")
	public JsonResult phoneBind(HttpServletRequest request,@RequestParam String phone,@RequestParam String code,HttpServletResponse response) {
        //检察登陆状态，拿到用户
		HttpSession session = request.getSession();
		
		Object customerObject = session.getAttribute("customer");
		Customer customer = null;
		if(customerObject!=null){
			customer = (Customer) customerObject;
		}else{
			return new JsonResult(false, "用户未登录", null);
		}
		
		Object session_code =  session.getAttribute("bindCode"+phone);
		if(session_code==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_CODE_NULL , null);
		}
		if(!session_code.equals(code)){
			return new JsonResult(false,JsonResult.APP_DEFINE_CODE_ERR , null);
		}
		//清除验证码
//		session.removeAttribute("loginCode"+phone);
		
		//绑定用户手机
		Map<String, Object> mapp = new HashMap<>();
		mapp.put("id", String.valueOf(customer.getId()));
		mapp.put("phone", phone);
		JsonResult jsonre = null;
		//TODO 相同号码测处理,绑定号码的限制
		if(customer.getPhone()==null||!customer.getPhone().equals(phone)){
			customer.setPhone(phone);
			jsonre = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",mapp);
		}else{
			return new JsonResult(false, JsonResult.APP_DEFINE_PHONE_BIND_ERR, null);
		}
		if(jsonre!=null&&jsonre.getSuccess()){
			//充值session
			JsonResult userresult = null;
//			userresult = redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone);
			userresult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone,null);
			logger.info("更新后用户信息："+userresult);
			if(userresult!=null&&userresult.getSuccess()){
				Map<?,?> map = (Map<?,?>) ((Map<String, Object>)userresult.getData()).get("user");
				customer = new Customer();
				customer.setId((Integer) map.get("id"));
				customer.setOpenid(String.valueOf( map.get("openid")));
				customer.setPhone(String.valueOf( map.get("phone")));
				String nickname =  String.valueOf(map.get("nickname"));
				customer.setNickname(nickname);
				customer.setPassword(String.valueOf( map.get("password")));
				customer.setSex(String.valueOf(map.get("sex")));
				customer.setCountry(String.valueOf( map.get("country")));
				customer.setProvince(String.valueOf( map.get("province")));
				customer.setCity(String.valueOf( map.get("city")));
				customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
				customer.setPrivilege( map.get("privilege").toString());
				customer.setState(String.valueOf( map.get("state")));
				customer.setRealName(map.get("realName")==null?"":String.valueOf( map.get("realName")));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
				} catch (ParseException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
				session.setAttribute("customer",customer);
			}
		}else{
			return jsonre;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("customer", customer);
		//保存session登陆状态
		session.setAttribute("customer",customer);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@ResponseBody
	@RequestMapping("/activeBindInfo")
	public JsonResult activeBindInfo(HttpServletRequest request,@RequestParam String phone,@RequestParam String code,String realName,HttpServletResponse response) {
        //检察登陆状态，拿到用户
		HttpSession session = request.getSession();
		
		Object customerObject = session.getAttribute("customer");
		Customer customer = null;
		if(customerObject!=null){
			customer = (Customer) customerObject;
		}else{
			return new JsonResult(false, "用户未登录", null);
		}
		
		if(phone!=null&&!phone.isEmpty()&&customer.getPhone()!=null&&phone.equals(customer.getPhone())){
		}else{
			Object session_code =  session.getAttribute("bindCode"+phone);
			if(session_code==null){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_NULL , null);
			}
			if(!session_code.equals(code)){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_ERR , null);
			}
		}
		
		//清除验证码
//		session.removeAttribute("loginCode"+phone);
		
		//绑定用户手机
		Map<String, Object> mapp = new HashMap<>();
		mapp.put("id", String.valueOf(customer.getId()));
			mapp.put("phone", phone);
			customer.setPhone(phone);
		if(realName!=null&&!realName.isEmpty()){
			mapp.put("realName",realName);
		}
		JsonResult jsonre = null;
		//TODO 相同号码测处理,绑定号码的限制
//		if(customer.getPhone()==null||!customer.getPhone().equals(phone)){
			jsonre = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",mapp);
//		}else{
//			return new JsonResult(false, JsonResult.APP_DEFINE_PHONE_BIND_ERR, null);
//		}
		if(jsonre!=null&&jsonre.getSuccess()){
			//充值session
			JsonResult userresult = null;
//			userresult = redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone);
			userresult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone,null);
			logger.info("更新后用户信息："+userresult);
			if(userresult!=null&&userresult.getSuccess()){
				Map<?,?> map = (Map<?,?>) ((Map<String, Object>)userresult.getData()).get("user");
				customer = new Customer();
				customer.setId((Integer) map.get("id"));
				customer.setOpenid(String.valueOf( map.get("openid")));
				customer.setPhone(String.valueOf( map.get("phone")));
				String nickname =  String.valueOf(map.get("nickname"));
				customer.setNickname(nickname);
				customer.setPassword(String.valueOf( map.get("password")));
				customer.setSex(String.valueOf(map.get("sex")));
				customer.setCountry(String.valueOf( map.get("country")));
				customer.setProvince(String.valueOf( map.get("province")));
				customer.setCity(String.valueOf( map.get("city")));
				customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
				customer.setPrivilege( map.get("privilege").toString());
				customer.setState(String.valueOf( map.get("state")));
				customer.setRealName(map.get("realName")==null?"":String.valueOf( map.get("realName")));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
				} catch (ParseException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
				session.setAttribute("customer",customer);
			}
		}else{
			return jsonre;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("customer", customer);
		//保存session登陆状态
		session.setAttribute("customer",customer);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@ResponseBody
	@RequestMapping("/activeFokaBindInfo")
	public JsonResult activeFokaBindInfo(HttpServletRequest request,@RequestParam String phone,@RequestParam String code,String realName,HttpServletResponse response) {
        //检察登陆状态，拿到用户
		HttpSession session = request.getSession();
		
		Object customerObject = session.getAttribute("customer");
		Customer customer = null;
		if(customerObject!=null){
			customer = (Customer) customerObject;
		}else{
			return new JsonResult(false, "用户未登录", null);
		}
		
		if(phone!=null&&!phone.isEmpty()&&customer.getPhone()!=null&&phone.equals(customer.getPhone())){
		}else{
			Object session_code =  session.getAttribute("bindCode"+phone);
			if(session_code==null){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_NULL , null);
			}
			if(!session_code.equals(code)){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_ERR , null);
			}
		}
		
		//清除验证码
//		session.removeAttribute("loginCode"+phone);
		
		//绑定用户手机
		Map<String, Object> mapp = new HashMap<>();
		mapp.put("id", String.valueOf(customer.getId()));
			mapp.put("phone", phone);
			customer.setPhone(phone);
		if(realName!=null&&!realName.isEmpty()){
			mapp.put("realName",realName);
		}
		JsonResult jsonre = null;
		//TODO 相同号码测处理,绑定号码的限制
//		if(customer.getPhone()==null||!customer.getPhone().equals(phone)){
			jsonre = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",mapp);
//		}else{
//			return new JsonResult(false, JsonResult.APP_DEFINE_PHONE_BIND_ERR, null);
//		}
		if(jsonre!=null&&jsonre.getSuccess()){
			//充值session
			JsonResult userresult = null;
//			userresult = redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone);
			userresult = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone,null);
			logger.info("更新后用户信息："+userresult);
			if(userresult!=null&&userresult.getSuccess()){
				Map<?,?> map = (Map<?,?>) ((Map<String, Object>)userresult.getData()).get("user");
				customer = new Customer();
				customer.setId((Integer) map.get("id"));
				customer.setOpenid(String.valueOf( map.get("openid")));
				customer.setPhone(String.valueOf( map.get("phone")));
				String nickname =  String.valueOf(map.get("nickname"));
				customer.setNickname(nickname);
				customer.setPassword(String.valueOf( map.get("password")));
				customer.setSex(String.valueOf(map.get("sex")));
				customer.setCountry(String.valueOf( map.get("country")));
				customer.setProvince(String.valueOf( map.get("province")));
				customer.setCity(String.valueOf( map.get("city")));
				customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
				customer.setPrivilege( map.get("privilege").toString());
				customer.setState(String.valueOf( map.get("state")));
				customer.setRealName(map.get("realName")==null?"":String.valueOf( map.get("realName")));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
				} catch (ParseException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
				session.setAttribute("customer",customer);
			}
		}else{
			return jsonre;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("customer", customer);
		//保存session登陆状态
		session.setAttribute("customer",customer);
		/*增加开福机会*/
		Map<String, Object> jtmap = new HashMap<>();
		jtmap.put("customerId", customer.getId());
		myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/setJiaTe?customerId="+customer.getId(),null);
		/*增加开福机会*/
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	/**
	 * 注册送优惠券接口
	 * @param request
	 * @param phone
	 * @param code
	 * @param realName
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/registerSendCoupon")//String code,String phone,String realName,String sex,String birthday 
	public JsonResult registerSendCoupon(HttpServletRequest request,String code,String phone,String realName,String sex,String birthday ,HttpServletResponse response) {
//		String phone = c.getPhone();
//		String realName = c.getRealName();
//		String sex  = c.getSex();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String birthday = sdf.format(c.getBirthday());
		
        //检察登陆状态，拿到用户
		HttpSession session = request.getSession();
		if(phone==null){
			return new JsonResult(false,"请输入手机号",null);
		}
		Object customerObject = session.getAttribute("customer");
		Customer customer = null;
		if(customerObject!=null){
			customer = (Customer) customerObject;
		}else{
			return new JsonResult(false, "用户未登录", null);
		}
//		Customer customer = new Customer();
//		customer.setId(1805);
		if(phone!=null&&!phone.isEmpty()&&customer.getPhone()!=null&&phone.equals(customer.getPhone())){
		}else{
			Object session_code =  session.getAttribute("bindCode"+phone);
			if(session_code==null){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_NULL , null);
			}
			if(!session_code.equals(code)){
				return new JsonResult(false,JsonResult.APP_DEFINE_CODE_ERR , null);
			}
		}
		//清除验证码
//		session.removeAttribute("loginCode"+phone);
		
		//绑定用户手机
		Map<String, Object> mapp = new HashMap<>();
		mapp.put("id", String.valueOf(customer.getId()));
		mapp.put("phone", phone);
		mapp.put("realName", realName);
		mapp.put("sex", sex);
		mapp.put("birthday", birthday);
		
		JsonResult jsonre = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/updateCustomerSelective",mapp);
		if(jsonre!=null&&jsonre.getSuccess()){
			//TODO 发优惠券接口
			Map<String, Object> couponParam = new HashMap<>();
			couponParam.put("customerId", String.valueOf(customer.getId()));
			couponParam.put("couponId", "63");
			JsonResult couponj = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/registerSendCoupon",couponParam);
			
			
			
			//查询新的用户信息设置到session
			JsonResult j = myHttpPost.httpRequest(RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPrimaryKey/"+customer.getId(),null);
			if(j.getSuccess()){
				Map<?,?> map = (Map<?,?>) ((Map<String, Object>)j.getData()).get("user");
				customer = new Customer();
				customer.setId((Integer) map.get("id"));
				customer.setOpenid(String.valueOf( map.get("openid")));
				customer.setPhone(String.valueOf( map.get("phone")));
				String nickname =  String.valueOf(map.get("nickname"));
				customer.setNickname(nickname);
				customer.setPassword(String.valueOf( map.get("password")));
				customer.setSex(String.valueOf(map.get("sex")));
				customer.setCountry(String.valueOf( map.get("country")));
				customer.setProvince(String.valueOf( map.get("province")));
				customer.setCity(String.valueOf( map.get("city")));
				customer.setHeadimgurl(String.valueOf( map.get("headimgurl")));
				customer.setPrivilege( map.get("privilege").toString());
				customer.setState(String.valueOf( map.get("state")));
				customer.setRealName(map.get("realName")==null?"":String.valueOf( map.get("realName")));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
				} catch (ParseException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
			//保存session登陆状态http://192.168.1.2/gogirl/registerSendCoupon?id=22&realName=姓名&sex=男&birthday=2019-07-07
			session.setAttribute("customer",customer);
			}else{
				return jsonre;
			}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, customer);
	}	
	public String getNewCode(){
		return String.valueOf((int)((Math.random()*9+1)*100000));
	}
	

}
