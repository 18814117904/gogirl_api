package com.bailun.gogirl.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bailun.gogirl.bean.Customer;
import com.bailun.gogirl.bean.LoginInfo;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.service.LoginInfoService;
import com.bailun.gogirl.service.QRCodeService;
import com.bailun.gogirl.service.WechatService;
import com.bailun.gogirl.util.CheckUtil;

@Controller
public class WechatAuthorization {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	WechatService wechatService;
	@Resource
	LoginInfoService loginInfoService;
	@Resource
	QRCodeService qrCodeService;
	
	//type=1=公众号菜单登录
	//type=2=普通二维码登录
	//type=3=手机登陆后，扫码绑定微信
	//type=127=其他登录
	@ResponseBody
	@RequestMapping("/getQRCode")
	public JsonResult getQRCode(
			@RequestParam(required = false) String redirect_uri,
			@RequestParam(required = false) String type){
		logger.info("调用getQRCode，获取登录验证码");
		if(type==null||type.isEmpty()){
			type = "127";
		}
		//判断type是否为数值，且大小在1——127之间
		if(!CheckUtil.isNumeric(type)){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_TYPE_ERR,null);
		}
		int typeInt = Integer.parseInt(type);
		if(typeInt>127||typeInt<1){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_TYPE_ERR,null);
		}
		if(redirect_uri==null||redirect_uri.isEmpty()){
			type = type+"|"+RouteConfig.NGINX+"redirect/suc";
		}else{
			type = type+"|"+redirect_uri;
		}
		redirect_uri=RouteConfig.NGINX+"authorized";
		String url = wechatService.getAUrl(redirect_uri,type);
		String imgUrl = qrCodeService.wxAuthorize(url);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", url);
		map.put("imgUrl", imgUrl);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}	
	
	@ResponseBody
	@RequestMapping("/getBindQRCode")
	public JsonResult getBindQRCode(
			@RequestParam(required = false) String redirect_uri){
		logger.info("调用getBindQRCode，获取绑定微信验证码");
		String type = "2";
		if(redirect_uri==null){
			type = type+"|"+RouteConfig.NGINX+"redirect/login_success";
		}
		type = type+"|"+redirect_uri;

		redirect_uri=RouteConfig.NGINX+"authorized_bind";
		String url = wechatService.getAUrl(redirect_uri,type);
		String imgUrl = qrCodeService.wxAuthorize(url);
//		String imgUrl = qrCodeService.wxAuthorize(redirect_uri, type);
//		System.out.println(imgUrl);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", url);
		map.put("imgUrl", imgUrl);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	
	@ResponseBody
	@RequestMapping("/authorized")
	public JsonResult Authorization(
			@RequestParam(name="code",required = false) String code,
			@RequestParam(name="state",required = false) String state,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("调用authorized，授权用户信息到程序");
		response.setCharacterEncoding("UTF-8"); 
		if(code==null||code.isEmpty()){
			logger.info("微信登录，授权用户信息时,code为空");
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"code"),null);
		}
		if(state==null||state.isEmpty()){
			logger.info("微信登录，授权用户信息时,state为空");
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"state"),null);
		}
		
		Customer customer = null;
		try {
			logger.info(code+"获取用户信息");
			customer = wechatService.getCustomerBycode(code,0);
		} catch (Exception e) {
			logger.info("登录异常:"+e.getMessage());
			if(e.getMessage().equals(JsonResult.INVALID_CODE)){
				return new JsonResult(false,JsonResult.INVALID_CODE,null);
			}else{
				throw e;
			}
		}
		if(customer==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGINING_ERR,null);
		}
		
		/*设置用户session*/
		HttpSession session = request.getSession();
		logger.info("session:"+session.hashCode());
		session.setAttribute("customer", customer);
		
		/*记录登陆信息*/
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setId(customer.getId()==null?0:customer.getId());
		loginInfo.setType(new Byte(state));
		loginInfo.setTime(new Date());
		loginInfo.setRedirectUri(request.getRequestURL().toString());
		loginInfoService.insertSelective(loginInfo);
		/*记录登陆信息*/
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customer);
	}
	
	@RequestMapping("/authorized_bind")
	public JsonResult AuthorizationBind(
			@RequestParam(name="code",required = false) String code,
			@RequestParam(name="state",required = false) String state,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("调用AuthorizationBind，绑定微信");
		response.setCharacterEncoding("UTF-8"); 
		if(code==null||code.isEmpty()){
			logger.info("手机用户绑定微信，授权用户信息时,code为空");
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"code"),null);
		}
		if(state==null||state.isEmpty()){
			logger.info("手机用户绑定微信，授权用户信息时,state为空");
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"state"),null);
		}
		
		//检查手机登录状态
		/*设置用户session*/
		HttpSession session = request.getSession();
		Object o = session.getAttribute("customer");
		if(o==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		Customer phoneCustomer = (Customer) o;//已经登录的手机用户
		Customer customer = null;
		try {
			customer = wechatService.getCustomerBycode(code,0);
		} catch (Exception e) {
			if(e.getMessage().equals("123")){
				return new JsonResult(false,"123",null);
			}
		}
		if(customer==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGINING_ERR,null);
		}
		if(phoneCustomer.getId()!=customer.getId()){
			customer.setPhone(phoneCustomer.getPhone());
			wechatService.mergeRemoveCustomer(phoneCustomer.getId(),customer);
		}
		//记录最终用户到session
		session.setAttribute("customer", customer);
		
		/*记录登陆信息*/
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setId(customer.getId());
		loginInfo.setType(new Byte(state));
		loginInfo.setTime(new Date());
		loginInfo.setRedirectUri(request.getRequestURL().toString());
		loginInfoService.insert(loginInfo);
		/*记录登陆信息*/
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customer);
	}

	@ResponseBody
	@RequestMapping("/getSubscribeInfo")
	public JsonResult getSubscribeInfo(HttpServletRequest request){
		/*设置用户session*/
		HttpSession session = request.getSession();
		Object o = session.getAttribute("customer");
		if(o==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		Customer customer = (Customer) o;//已经登录的手机用户
		String openId = customer.getOpenid();
		if(openId!=null){
			JsonResult jr = wechatService.getUserInfoByAccessToken2(openId);
			if(jr.getSuccess()){
				
				
				return jr;
			}else{
				return jr;
			}
		}else{
			return new JsonResult(false,"登录信息异常,请联系管理员",null);
		}
	}
}
