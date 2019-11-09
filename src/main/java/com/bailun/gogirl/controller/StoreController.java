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
//@RequestMapping("/gogirl-store")
public class StoreController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	String appname = "";
	String appname = "/gogirl-store";
	@Resource
	RedirectService redirectService;
	@ResponseBody
	@RequestMapping("/classes/*")
	public JsonResult classes(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/classestechnician/*")
	public JsonResult classestechnician(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/shop/*")
	public JsonResult shop(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/technician/*")
	public JsonResult technician(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/user/*")
	public JsonResult user(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/activity/*")
	public JsonResult activity(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/activitySummary/*")
	public JsonResult activitySummary(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
	@ResponseBody
	@RequestMapping("/activityCustomer/*")
	public JsonResult activityCustomer(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("转发请求:"+RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
		return redirectService.redirect(request,null,RouteConfig.GOGIRLSTORE+appname+request.getRequestURI().substring(7));
	}
}
