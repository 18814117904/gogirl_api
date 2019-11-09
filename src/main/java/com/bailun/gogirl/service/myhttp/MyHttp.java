package com.bailun.gogirl.service.myhttp;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.JsonResult;
@Component
public abstract class MyHttp {
	@Resource
	public RestTemplate restTemplate;

	/*请求转发方法*/
	public abstract JsonResult httpRequest(HttpServletRequest req,String target_url,Map<String, Object> map);
	/*请求转发方法*/
	public abstract JsonResult httpRequest(String url,Map<String, Object> map);
//	/*请求转发方法*/
//	public abstract ResponseEntity<JsonResult> httpRequestEntity(String url,Map<String, Object> map);
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
