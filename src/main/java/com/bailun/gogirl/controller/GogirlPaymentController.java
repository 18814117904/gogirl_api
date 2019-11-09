package com.bailun.gogirl.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bailun.gogirl.bean.Customer;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.constant.Constant;
import com.bailun.gogirl.constant.WxConsts.PayType;
import com.bailun.gogirl.service.RedirectService;
import com.bailun.gogirl.service.myhttp.MyHttpPost;
@Controller
@RequestMapping("/gogirl_payment")
public class GogirlPaymentController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	RedirectService redirectService;
	@Autowired
    public MyHttpPost myHttpPost;
	@Resource
	WxConfig wxConfig;
	@Resource
	RouteConfig routeConfig;
	@ResponseBody
	@RequestMapping("/unifiedorderGetJsAPIConfig")
	public JsonResult unifiedorderGetJsAPIConfig(int attach,Double amount,String orderId,HttpServletRequest request,HttpServletResponse response,Integer couponRelevanceId) {
		logger.debug("调用unifiedorderGetJsAPIConfig");

		HttpSession session = request.getSession();
		Object o = session.getAttribute("customer");
		
		if(o!=null&&o instanceof Customer){
			Customer customer = (Customer)o;
			logger.info("用户："+customer);
			if(customer.getOpenid()!=null&&!customer.getOpenid().isEmpty()){
				int intamount = 0;
				Map<String, Object> map = new HashMap<>();
				map.put("openId", customer.getOpenid());
				map.put("amount", intamount);
				double doubleAmount = 0.0;
				if(attach==Constant.ATTACH_CHARGE_BALANCE){//生成充值的orderid
					logger.info("用户id:"+customer.getId());
					String chargeOrderId = getChargeOrderId(customer.getId());//时间+用户id+4位随机数生成订单id
					logger.info("生成订单id:"+chargeOrderId);
					map.put("orderId", chargeOrderId);
					if(amount==null){
						return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "金额"),null);
					}
					intamount=(int) Math.ceil(amount*100);
					map.put("amount", intamount);
				}else{
					map.put("orderId", orderId);
					/*amount转换  */
					/*根据订单id查询订单金额*/
					String orderUrl = RouteConfig.GOGIRLORDER + "gogirl-order/ordermanage/queryOrderForDetail";
					Map<String, Object> orderMap = new HashMap<>();
					orderMap.put("id",String.valueOf(orderId));
					logger.info("查询订单:"+orderUrl);
					JsonResult orderResult = myHttpPost.httpRequest(orderUrl, orderMap);
					logger.info(orderResult.toString());
					Map<String, Object> map1 = (Map<String, Object>) orderResult.getData();
					logger.info("map:"+map1);
					doubleAmount = Double.parseDouble(map1.get("totalPrice").toString());
//					double changePrice = Double.parseDouble(map1.get("changePrice").toString());
//					doubleAmount = new BigDecimal(doubleAmount).add(new BigDecimal(changePrice)).doubleValue();//改价后价格,再去扣款
					int price =(int)(doubleAmount*100);
					logger.info("price:"+price);

//					if(amount==null){
//						return new JsonResult(false,JsonResult.APP_DEFINE_AMOUNT_NULL_ERR,null);
//					}
					try {
						//计算折扣
						String url = RouteConfig.GOGIRLUSER + "gogirl_user/count";
						Map<String, Object> parmMap = new HashMap<>();
						parmMap.put("customerId", String.valueOf(customer.getId()));
						parmMap.put("couponRelevanceId", couponRelevanceId==null?null:String.valueOf(couponRelevanceId));
						parmMap.put("price", String.valueOf(doubleAmount));
						logger.info("请求:"+url);
						JsonResult jsonResult = myHttpPost.httpRequest(url, parmMap);
						logger.info(jsonResult.toString());
						if(jsonResult.getSuccess()){
							Map<String, Object> mapDiscount = (Map<String, Object>) jsonResult.getData();
							double discountPrice = (double)mapDiscount.get("discountPrice");
							changeOrderStateSuc(Integer.parseInt(orderId),couponRelevanceId,discountPrice);//修改订单状态
							doubleAmount = new BigDecimal(doubleAmount).subtract(new BigDecimal(discountPrice)).doubleValue();
							logger.info("计算折扣:"+discountPrice);
							logger.info("计算折扣回来:"+doubleAmount);
						}else{
							// TODO 扣除用户余额异常，需不需要做特殊处理
							logger.error("扣除用户余额异常：" + jsonResult.getMessage());
							return new JsonResult(false,jsonResult.getMessage(),null);
						}
						intamount = (int) Math.floor(doubleAmount*100);//换算为分,向下取整
					} catch (Exception e) {
						return new JsonResult(false,JsonResult.APP_DEFINE_AMOUNT_ERR,null);
					}
					/*amount转换  */
					map.put("amount", intamount);
				}
//				//测试专用充值以及支付都把金额设为0.01
//				if(wxConfig.getAppId().equals("wx29745c13ddde71aa")){//如果是测试公众号
//					map.put("amount", String.valueOf(1));
//				}
//				if(wxConfig.getAppId().equals("wxabd5e4915643494a")){//如果是开发公众号
//					map.put("amount", String.valueOf(1));
//				}
//				//测试专用充值以及支付都把金额设为0.01
				System.out.println("转发wechat请求:"+RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
				JsonResult result = redirectService.redirect(request,map,RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
				
//				if(result.getSuccess()==true){//支付成功后发送一张优惠券给用户
//					Map<String, Object> mapParm = new HashMap<>();
//					mapParm.put("openid", customer.getOpenid());
//					mapParm.put("orderAmount", doubleAmount);
//					redirectService.myHttpPost.httpRequest(routeConfig.GOGIRLUSER+"gogirl_user/sendTicketAfterOrder", mapParm);
//				}
				
				return result;
			}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
				return new JsonResult(false,JsonResult.APP_DEFINE_PHONE_PAY,null);
			}else{
				return new JsonResult(false,JsonResult.APP_DEFINE_CUSTOMER_INFO_ERR,null);
			}
		}else{
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
	}

	private String getChargeOrderId(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append(new Date().getTime());
		sb.append(id);
		sb.append((int)(Math.random()*10000));
		return sb.toString();
	}
	@ResponseBody
	@RequestMapping("/*")
	public JsonResult redirect1(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用redirect1");
		HttpSession session = request.getSession();
		Object o = session.getAttribute("customer");
		if(o!=null&&o instanceof Customer){
			Customer customer = (Customer)o;
			logger.info("用户："+customer);
			if(customer.getOpenid()!=null&&!customer.getOpenid().isEmpty()){
				Map<String, Object> map = new HashMap<>();
				map.put("openId", customer.getOpenid());
				System.out.println("转发wechat请求:"+RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
				return redirectService.redirect(request,map,RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
			}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
				return new JsonResult(false,JsonResult.APP_DEFINE_PHONE_PAY,null);
			}else{
				return new JsonResult(false,JsonResult.APP_DEFINE_CUSTOMER_INFO_ERR,null);
			}
		}else{
			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_ERR,null);
		}
	}
	@ResponseBody
	@RequestMapping("/wechatPayNotify")
	public String wechatPayNotify(String return_code,String return_msg,HttpServletRequest request,HttpServletResponse response) {
		logger.info("调用redirect2,return_code:"+return_code+"；return_msg:"+return_msg);
		String xmlParm = "<xml></xml>";
		try {
	        xmlParm = getXmlPram(request);
	        logger.info("notice_msg:" + xmlParm);
		} catch (Exception e) {
			logger.info("接收xml数据出错");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("xmlParm", xmlParm);
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
		JsonResult result = redirectService.redirect(request,map,RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
		logger.info(result.toString());
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}
	private String getXmlPram(HttpServletRequest request) throws IOException {
		InputStream is = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }
        return sb.toString();
	}
	@ResponseBody
	@RequestMapping("/wechatRefundNotify")
	public Object wechatRefundNotify(HttpServletRequest request,HttpServletResponse response) {
		logger.info("调用redirect2");
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/*/*")
	public JsonResult redirect2(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("调用redirect2");
		System.out.println("转发wechat请求:"+RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLPAYMENT+request.getRequestURI().substring(7));
	}
	@RequestMapping("/jspay")
	public String jspay(HttpServletRequest req) {
		logger.info("返回支付页面");
		return "jspay";
	}

	@ResponseBody
	@RequestMapping("/setPayType")
	private JsonResult setPayType(HttpServletRequest request,int orderId,int payType,Integer couponRelevanceId) {
		// 修改订单状态
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
		
		if(payType==Constant.ATTACH_CONSUME_POS){
			logger.info("用户发起pos支付");
		}else if(payType==Constant.ATTACH_CONSUME_CASH){
			logger.info("用户发起现金支付");
		}else if(payType==Constant.ATTACH_CONSUME_MEITUAN){
			logger.info("用户发起美团/大众点评支付");
		}else{
			logger.info("用户发起"+payType+"支付,不能设置该支付类型.");
			return new JsonResult(false,String.format(JsonResult.PAY_TYPE_ERR, payType),null);
		}
		
		
		/*根据订单id查询订单金额*/
		String orderUrl = RouteConfig.GOGIRLORDER + "gogirl-order/ordermanage/queryOrderForDetail";
		Map<String, Object> orderMap1 = new HashMap<>();
		orderMap1.put("id",String.valueOf(orderId));
		logger.info("查询订单:"+orderUrl);
		JsonResult orderResult = myHttpPost.httpRequest(orderUrl, orderMap1);
		logger.info(orderResult.toString());
		Map<String, Object> map1 = (Map<String, Object>) orderResult.getData();
		logger.info("map:"+map1);
		double doubleAmount = Double.parseDouble(map1.get("totalPrice").toString());
//		double changePrice = Double.parseDouble(map1.get("changePrice").toString());
//		doubleAmount = new BigDecimal(doubleAmount).add(new BigDecimal(changePrice)).doubleValue();//改价后价格,再去扣款
		int price =(int)(doubleAmount*100);
		logger.info("price:"+price);

//		if(amount==null){
//			return new JsonResult(false,JsonResult.APP_DEFINE_AMOUNT_NULL_ERR,null);
//		}
		try {
			//计算折扣
			customer =(Customer) object;
			String url = RouteConfig.GOGIRLUSER + "gogirl_user/count";
			Map<String, Object> parmMap = new HashMap<>();
			parmMap.put("customerId", String.valueOf(customer.getId()));
			parmMap.put("couponRelevanceId", couponRelevanceId==null?null:String.valueOf(couponRelevanceId));
			parmMap.put("price", String.valueOf(doubleAmount));
			logger.info("请求:"+url);
			JsonResult jsonResult = myHttpPost.httpRequest(url, parmMap);
			logger.info(jsonResult.toString());
			if(jsonResult.getSuccess()){
				Map<String, Object> mapDiscount = (Map<String, Object>) jsonResult.getData();
				doubleAmount = (double)mapDiscount.get("discountPrice");
			}else{
				// TODO 扣除用户余额异常，需不需要做特殊处理
				logger.error("扣除用户余额异常：" + jsonResult.getMessage());
				return new JsonResult(false,jsonResult.getMessage(),null);
			}
		} catch (Exception e) {
			return new JsonResult(false,JsonResult.APP_DEFINE_AMOUNT_ERR,null);
		}
		/*amount转换  */
		
		
		try {
			String url = RouteConfig.GOGIRLORDER
					+ "gogirl-order/ordermanage/modifyOrderPayMentType"; // 参数status=4
																	// id:订单id
			Map<String, Object> orderMap = new HashMap<>();
			orderMap.put("status", String.valueOf(2));
			orderMap.put("id", String.valueOf(orderId));
			orderMap.put("paymentType", String.valueOf(payType));
			orderMap.put("couponRelevanceId", String.valueOf(couponRelevanceId));
			orderMap.put("discountPrice", String.valueOf(doubleAmount));
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

	private JsonResult changeOrderStateSuc(int orderId,Integer couponRelevanceId,double discountPrice) {
		// 修改订单状态
		try {
			String url = RouteConfig.GOGIRLORDER
					+ "gogirl-order/ordermanage/modifyOrderStatusFinash"; // 参数status=4
																	// id:订单id
			Map<String, Object> orderMap = new HashMap<>();
//			orderMap.put("status", String.valueOf(3));
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

}
