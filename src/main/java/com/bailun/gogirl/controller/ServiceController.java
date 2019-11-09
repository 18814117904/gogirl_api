package com.bailun.gogirl.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.service.RedirectService;

@Controller
//@RequestMapping("/gogirl-service")
public class ServiceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	String appname = "";
	String appname = "/gogirl-service";
	@Resource
	RedirectService redirectService;
	@ResponseBody
	@RequestMapping("/broadcast/*")
	public JsonResult broadcast(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/label/*")
	public JsonResult label(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/produce/*")
	public JsonResult produce(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/serve/*")
	public JsonResult serve(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/type/*")
	public JsonResult type(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	
	@ResponseBody
	@RequestMapping("/user/queryUserForCheck")
	public JsonResult user(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/sysUserController/*")
	public JsonResult sysUserController(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/image/*")
	public JsonResult image(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
	}
//	@ResponseBody//总后台系统不需要转发
//	@RequestMapping("/user/*")
//	public JsonResult user(HttpServletRequest request,HttpServletResponse response) {
//		System.out.println("转发请求:"+RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
//		return redirectService.redirect(request,null,RouteConfig.GOGIRLSERVICE+appname+request.getRequestURI().substring(7));
//	}
}
