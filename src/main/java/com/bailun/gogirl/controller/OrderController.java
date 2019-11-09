package com.bailun.gogirl.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bailun.gogirl.bean.ImageManage;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.service.RedirectService;
import com.bailun.gogirl.util.ImageUtil;

@Controller
//@RequestMapping("/gogirl-order")		//找到优惠券，且把优惠券设置为未使用；

public class OrderController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${gogirl.url.picturePath}")
	String picturePath;
//	String appname = "";
	String appname = "/gogirl-order";
	@Resource
	RedirectService redirectService;
	@ResponseBody
	@RequestMapping("/ordercomment/modifyOrderComment")
	public JsonResult ordercommentModifyOrderComment(@RequestParam(value = "formData", required = false) MultipartFile [] formData,HttpServletRequest request) {
		logger.info("test");
		//		if(formData.length == 0){
//            return new JsonResult(false,"files文件为空",null);
//        }
		List<ImageManage> list;
		try {
			list = ImageUtil.saveImage(picturePath, formData);
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonResult(false,e.getMessage(),null);
		}
		String urls = ImageUtil.imageManageListToString(list);
		Map<String, Object> map = new HashMap<>();
		map.put("picturePath", urls);
		System.out.println("转发请求:"+RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,map,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/ordercomment/*") 
	public JsonResult ordercomment(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/orderlabel/*")
	public JsonResult orderlabel(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/ordermanage/*")
	public JsonResult ordermanage(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
//		int orderUser = 0;
//		HttpSession session = request.getSession();
//		if(session.getAttribute("customer")!=null&&session.getAttribute("customer")instanceof Customer){
//			Customer customer = (Customer)session.getAttribute("customer");
//			orderUser = customer.getId();
//		}else{
//			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_STATE_ERR,null);
//		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("orderUser",String.valueOf(orderUser));
//		return redirectService.redirect(request,map,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
	}
	
	@ResponseBody
	@RequestMapping("/schedule/*")
	public JsonResult schedule(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
//		int scheduledUser = 0;
//		HttpSession session = request.getSession();
//		if(session.getAttribute("customer")!=null&&session.getAttribute("customer")instanceof Customer){
//			Customer customer = (Customer)session.getAttribute("customer");
//			scheduledUser = customer.getId();
//		}else{
//			return new JsonResult(false,JsonResult.APP_DEFINE_LOGIN_STATE_ERR,null);
//		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("scheduledUser",String.valueOf(scheduledUser));
//		return redirectService.redirect(request,map,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLORDER+appname+request.getRequestURI().substring(7));
	}
}
