package com.bailun.gogirl.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bailun.gogirl.bean.JsApiTicket;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.service.JsApiTicketService;
import com.bailun.gogirl.util.SignatureUtil;

@Controller
@RequestMapping("/js")
public class JsSdkController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	WxConfig config;
	@Resource
	JsApiTicketService jsApiTicketService;
	
	
	@ResponseBody
	@RequestMapping(value = "js_sdk_sign")
	public JsonResult js_sdk_sign (
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name="url",required=false) String url
			){
	        String origin = request.getHeader("Origin");
//	        if(request.getHeader("Origin").contains("gogirl.cn")) {//部分网站允许跨域
//		        response.setHeader("Access-Control-Allow-Origin", origin);
//		        response.setHeader("Access-Control-Allow-Credentials", "true");	       
//	        }
	        response.setHeader("Access-Control-Allow-Origin", origin);
	        response.setHeader("Access-Control-Allow-Credentials", "true");	       
		try {
			//获取jsapi_ticket
			JsApiTicket jsApiTicket = jsApiTicketService.getJsApiTicket();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);//时间搓
			String nonceStr = UUID.randomUUID().toString().replaceAll("-",	"");//随机字符串
			//获取签名
			url =new String(Base64.getDecoder().decode(url.getBytes()));
			logger.info("url:"+url);
//            decrypt = new String(cipher.doFinal(base));
//            
//            
//            Base64Coder decoder = new Base64Coder();
//			Base64.decodeBase64(url); 
//			new String(base64.decode(str1));  
			String signature = SignatureUtil.getSignature(jsApiTicket.getJs_api_ticket(), nonceStr, timestamp, url);
			System.out.println("签名为："+signature);
			if(signature==null){
				return new JsonResult(false,"签名失败，请检查入参。",null);
			}
			String appId = config.getAppId();
			Map<String, Object> map = new HashMap<>();
			map.put("timestamp",timestamp );
			map.put("nonceStr",nonceStr );
			map.put("appId", appId);
			map.put("signature", signature);
			map.put("jsapi_ticket", jsApiTicket.getJs_api_ticket());
			return new JsonResult(true,"",map);
		} catch (Exception e) {
			return new JsonResult(false,e.getMessage(),null);
		}
	}
}
