package com.bailun.gogirl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bailun.gogirl.bean.Customer;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.constant.Constant;
import com.bailun.gogirl.service.RedirectService;
import com.bailun.gogirl.service.WechatService;
import com.bailun.gogirl.service.myhttp.MyHttpPost;

@Controller
@RequestMapping("/gogirl_user")
public class GoGirlUserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	RedirectService redirectService;
	@Resource
	MyHttpPost myHttpPost;
	@Resource
	WechatService wechatService;
	@Resource
	RouteConfig routeConfig;
	@ResponseBody
	@RequestMapping("/getUserInfo")
	public JsonResult getUserInfo(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用getUserInfo");
		//入参校验无
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		return redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPrimaryKey/"+customer.getId());
//		Map<String, Object> map = new HashMap<>();
//		map.put("user",customer );
//		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	@ResponseBody
	@RequestMapping("/getBalance")
	public JsonResult getBalance(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用getUserInfo");
		Map<String, Object> map = new HashMap<>();
		//入参校验无
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
//		map.put("user",customer);
		//请求用户卡信息
		Map<String, Object> parm = new HashMap<>();
		parm.put("customerId", customer.getId());
		JsonResult cardResult = redirectService.redirect(request,parm,RouteConfig.GOGIRLUSER+"gogirl_user/getBalance");
		if(cardResult.getSuccess()){
			Map<String, Object> balance = (Map<String, Object>) ((Map<String, Object>)cardResult.getData()).get("balance");
			balance.put("balance", ((int)balance.get("balance"))/100.0);
			map.put("balance",balance);
			map.put("discountConfig",((Map<String, Object>)cardResult.getData()).get("discountConfig"));
		}else{
			return cardResult;
		}
		
		
		
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	@ResponseBody
	@RequestMapping("/getUserInfoWithCard")
	public JsonResult getUserInfoWithCard(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用getUserInfo");
		Map<String, Object> map = new HashMap<>();
		//入参校验无
		HttpSession session = request.getSession();
		logger.info("session:"+session.hashCode());
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		map.put("user",customer);
		//请求用户卡信息
		Map<String, Object> parm = new HashMap<>();
		parm.put("customerId", customer.getId());
		JsonResult cardResult = redirectService.redirect(request,parm,RouteConfig.GOGIRLUSER+"gogirl_user/getBalance");
		if(cardResult.getSuccess()){
			Map<String, Object> balance = (Map<String, Object>) ((Map<String, Object>)cardResult.getData()).get("balance");
			balance.put("balance", ((int)balance.get("balance"))/100.0);
			map.put("balance",balance);
			map.put("discountConfig",((Map<String, Object>)cardResult.getData()).get("discountConfig"));
		}else{
			return cardResult;
		}
		
		
		
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	
	@ResponseBody
	@RequestMapping("/deleteCustomer")
	public JsonResult deleteCustomer(HttpServletRequest request,HttpServletResponse response,@RequestParam(required=false) String id) {
		logger.debug("调用deleteCustomer");
		if(id==null||id.isEmpty()){
			JsonResult result = new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"id"),null);
			return result;
		}

		JsonResult responseEntity = redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/deleteCustomerByPrimaryKey/"+id);
		System.out.println(responseEntity.toString());
		return responseEntity;
	}
	@ResponseBody
	@RequestMapping("/consumeBalance")
	public JsonResult consumeBalance(HttpServletRequest request,HttpServletResponse response,int orderId,Integer couponRelevanceId) {
		logger.debug("调用consumeBalance");
		if(orderId==0){
			return new JsonResult(false,JsonResult.APP_DEFINE_ORDERID_NULL_ERR,null);
		}
		
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		
		/*根据订单id查询订单金额*/
		String orderUrl = RouteConfig.GOGIRLORDER + "gogirl-order/ordermanage/queryOrderForDetail";
		Map<String, Object> orderMap = new HashMap<>();
		orderMap.put("id",String.valueOf(orderId));
		logger.info("查询订单:"+orderUrl);
		JsonResult orderResult = myHttpPost.httpRequest(orderUrl, orderMap);
		logger.info(orderResult.toString());
		Map<String, Object> map1 = (Map<String, Object>) orderResult.getData();
		logger.info("map:"+map1);
		double doubleAmount = Double.parseDouble(map1.get("totalPrice").toString());
		int departmentId = Integer.parseInt(map1.get("departmentId").toString());
//		double changePrice = Double.parseDouble(map1.get("changePrice").toString());
//		doubleAmount = new BigDecimal(doubleAmount).add(new BigDecimal(changePrice)).doubleValue();//改价后价格,再去扣款
		int price =(int)(doubleAmount*100);
		logger.info("price:"+price);
		// 扣除用户余额
		try {
			String url = RouteConfig.GOGIRLUSER + "gogirl_user/consumeBalanceOpenid";
			Map<String, Object> parmMap = new HashMap<>();
			parmMap.put("openid", String.valueOf(customer.getOpenid()));
			parmMap.put("amount", String.valueOf(price));
			parmMap.put("departmentId", String.valueOf(departmentId));
			parmMap.put("orderId", String.valueOf(orderId));
			parmMap.put("couponRelevanceId",couponRelevanceId==null?null:String.valueOf(couponRelevanceId));
			logger.info("请求:"+url);
			logger.info(orderMap.toString());
			JsonResult jsonResult = myHttpPost.httpRequest(url, parmMap);
			logger.info(jsonResult.toString());
			
			if(jsonResult.getSuccess()){
				Map<String, Object> mapDiscount = (Map<String, Object>) jsonResult.getData();
				JsonResult r=changeOrderStateSuc(orderId,couponRelevanceId,(double)mapDiscount.get("discountPrice"));//修改订单状态
//				if(r.getSuccess()==true){//支付成功后发送一张优惠券给用户
//					Map<String, Object> mapParm = new HashMap<>();
//					mapParm.put("openid", customer.getOpenid());
//					mapParm.put("orderAmount", doubleAmount);
//					redirectService.myHttpPost.httpRequest(routeConfig.GOGIRLUSER+"gogirl_user/sendTicketAfterOrder", mapParm);
//				}
				return  r;
			}else{
				// TODO 扣除用户余额异常，需不需要做特殊处理
				logger.error("扣除用户余额异常：" + jsonResult.getMessage());
				return new JsonResult(false,jsonResult.getMessage(),null);
			}
		} catch (Exception e) {
			// TODO 扣除用户余额异常，需不需要做特殊处理
			logger.error("扣除用户余额异常：" + e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		
	}
	
	
	
	private JsonResult changeOrderStateSuc(int orderId,Integer couponRelevanceId,double discountPrice) {
		// 修改订单状态
		try {
			String url = RouteConfig.GOGIRLORDER
					+ "gogirl-order/ordermanage/modifyOrderStatusFinash"; // 参数status=4
																	// id:订单id
			Map<String, Object> orderMap = new HashMap<>();
			orderMap.put("status", String.valueOf(3));
			orderMap.put("id", String.valueOf(orderId));
			orderMap.put("paymentType", String.valueOf(Constant.ATTACH_CONSUME_BALANCE));
			if(couponRelevanceId!=null){
				orderMap.put("couponRelevanceId", String.valueOf(couponRelevanceId));
			}
			orderMap.put("discountPrice", String.valueOf(discountPrice));
			logger.info("请求:"+url);
			logger.info(orderMap.toString());
			JsonResult jsonResult = myHttpPost.httpRequest(url, orderMap);
			logger.info(jsonResult.toString());
			return jsonResult;
		} catch (Exception e) {
			// TODO 修改订单状态异常，需不需要做特殊处理
			logger.error("修改订单状态异常：" + e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		
	}

	
	
	
	@ResponseBody
	@RequestMapping("/*")
	public JsonResult redirect1(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用redirect1");
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		Map<String, Object> parmMap = new HashMap<>();
		parmMap.put("id",customer.getId());
		parmMap.put("customerId",customer.getId());
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
		return redirectService.redirect(request,parmMap,RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/updateCustomerSelective")
	public JsonResult updateCustomerSelective(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		logger.debug("调用redirect1");
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		Map<String, Object> parmMap = new HashMap<>();
		parmMap.put("id",customer.getId());
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
		JsonResult jsonre =redirectService.redirect(request,parmMap,RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
		if(jsonre.getSuccess()){
			//充值session
			JsonResult userresult = null;
			String phone = request.getParameter("phone");
			if(phone!=null&&!phone.isEmpty()){
				userresult = redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPhone/"+phone);
			}else{
				userresult = redirectService.redirect(request,parmMap,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPrimaryKey/"+customer.getId());
			}
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
				customer.setBirthday(map.get("birthday")==null?null:sdf.parse(String.valueOf( map.get("birthday"))));
				customer.setRemark(map.get("remark")==null?"":String.valueOf( map.get("remark")));
				session.setAttribute("customer",customer);
			}
		}
		return jsonre;
	}
	@ResponseBody
	@RequestMapping("/*/*")
	public JsonResult redirect2(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用redirect2");
		HttpSession session = request.getSession();
		Object object = session.getAttribute("customer");
		Customer customer ;
		if(object==null){
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
		customer =(Customer) object;
		Map<String, Object> parmMap = new HashMap<>();
		parmMap.put("id",customer.getId());
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/getCustomerById")
	public JsonResult getCustomerById(HttpServletRequest request,HttpServletResponse response,String target_id) {
		if(target_id==null||target_id.isEmpty()){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"target_id"),null);
		}
		return redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/getCustomerByPrimaryKey/"+target_id);
	}
	@ResponseBody
	@RequestMapping("/subscribeOpenBigFoka")
	public JsonResult subscribeOpenBigFoka(HttpServletRequest request,HttpServletResponse response) {
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
				JSONObject jo = (JSONObject) jr.getData();
				if(jo.get("subscribe").equals(1)){
					return redirectService.redirect(request,null,RouteConfig.GOGIRLUSER+"gogirl_user/openBigFoka?customerId="+customer.getId());
				}else{
					return new JsonResult(false,"先关注公众号'GOGIRL美甲美睫沙龙'后,才能打开五福",null);
				}
				
			}else{
				return jr;
			}
		}else{
			return new JsonResult(false,"登录信息异常,请联系管理员",null);
		}
	}
	
	
}
