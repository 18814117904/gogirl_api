package com.bailun.gogirl.service.myhttp;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bailun.gogirl.bean.JsonResult;
@Component
public class MyHttpPost extends MyHttp {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public JsonResult httpRequest(String url, Map<String, Object> map) {
        //消息头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //参数
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
        if(map!=null){
        	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        	while(it.hasNext()){
        		Entry<String, Object> entry = it.next();
        		params.add(entry.getKey(), entry.getValue());
        	}
        }
        //请求
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        try {
            ResponseEntity<JsonResult> entity = null;
            try {
                entity = restTemplate.postForEntity(url, httpEntity, JsonResult.class);
    		} catch (Exception e) {
    			logger.info(e.getMessage());
    			return new JsonResult(false,"访问"+url+"异常，异常信息："+e.getMessage(),null);
    		}
//            ResponseEntity<JsonResult> entity = restTemplate.postForEntity(url, httpEntity, JsonResult.class);
    		if(entity==null){
    			return new JsonResult(false,"post1转发服务返回空",null);
    		}else
            if(entity.getStatusCodeValue()==200){
        		return entity.getBody();
            }else{
            	return new JsonResult(false,entity.toString(),null);
            }
		} catch (Exception e) {
			return new JsonResult(false,e.getMessage(),null);
		}
//        JsonResult response = restTemplate.postForObject(url, httpEntity, JsonResult.class);
//		return response;
	}
	@Override
	public JsonResult httpRequest(HttpServletRequest req,String target_url,Map<String, Object> map
			) {
        //获取所有的消息头
		HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames2 = req.getHeaderNames();
        while(headerNames2.hasMoreElements()){
            String nextElement = headerNames2.nextElement();
//            System.out.println(nextElement+":"+req.getHeader(nextElement));
            headers.add(nextElement, req.getHeader(nextElement));
        }
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
//        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);APPLICATION_PROBLEM_JSON_UTF8_VALUE
        
        //获取所有的参数
        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
        Enumeration<String> parameterNames2 = req.getParameterNames();
        while(parameterNames2.hasMoreElements()){
            String nextElement = parameterNames2.nextElement();
//            System.out.println(nextElement+":"+req.getParameter(nextElement));
            params.add(nextElement, req.getParameter(nextElement));
        }
        
        if(map!=null){
        	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        	while(it.hasNext()){
        		Entry<String, Object> entry = it.next();
//        		logger.info(entry.getKey()+":"+entry.getValue());
        		params.add(entry.getKey(), entry.getValue());
        	}
        }
        
        //转发请求
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        ResponseEntity<JsonResult> entity = null;
        try {
            entity = restTemplate.postForEntity(target_url, httpEntity, JsonResult.class);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,"访问"+target_url+"异常，异常信息："+e.getMessage(),null);
		}
//        ResponseEntity<JsonResult> entity = restTemplate.postForEntity(target_url, httpEntity, JsonResult.class);
		if(entity==null){
			return new JsonResult(false,"post2转发服务返回空",null);
		}else
        if(entity.getStatusCodeValue()==200){
    		return entity.getBody();
        }else{
        	return new JsonResult(false,entity.toString(),null);
        }
//        JsonResult response = restTemplate.postForObject(target_url, httpEntity, JsonResult.class);
//		return response;
	}
//	@Override
//	public ResponseEntity<JsonResult> httpRequestEntity(String url,
//			Map<String, Object> map) {
//        //消息头
//		HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
//        //参数
//        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
//        if(map!=null){
//        	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
//        	while(it.hasNext()){
//        		Entry<String, Object> entry = it.next();
//        		params.add(entry.getKey(), entry.getValue());
//        	}
//        }
//        //请求
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
//        ResponseEntity<JsonResult> response = restTemplate.postForEntity(url, httpEntity, JsonResult.class);
//		return response;
//	}


}
