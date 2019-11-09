package com.bailun.gogirl.service.myhttp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bailun.gogirl.bean.JsonResult;
@Component
public class MyHttpGet extends MyHttp {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public JsonResult httpRequest(String url, Map<String, Object> map) {
		url = addGetParams(url,map);
//        JsonResult response = restTemplate.getForObject(url,JsonResult.class);
        ResponseEntity<JsonResult> entity = null;
        try {
            entity = restTemplate.getForEntity(url,JsonResult.class);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,"访问"+url+"异常，异常信息："+e.getMessage(),null);
		}
//        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(url,JsonResult.class);
		if(entity==null){
			return new JsonResult(false,"get1转发服务返回空",null);
		}else
        if(entity.getStatusCodeValue()==200){
    		return entity.getBody();
        }else{
        	return new JsonResult(false,entity.toString(),null);
        }
//		return response;
	}
	@Override
	public JsonResult httpRequest(HttpServletRequest req,String target_url,Map<String, Object> map
			) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		//添加req中的参数到params
		Enumeration<String> enumeration= req.getParameterNames();
		while(enumeration.hasMoreElements()){
			String key = enumeration.nextElement();
			Object o = req.getParameter(key);
			params.put(key, String.valueOf(o));
		}
		//添加map中的参数到params
        if(map!=null){
        	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        	while(it.hasNext()){
        		Entry<String, Object> entry = it.next();
        		params.put(entry.getKey(), String.valueOf(entry.getValue()));
        	}
        }
        
        target_url = addGetParams(target_url,params);
        ResponseEntity<JsonResult> entity = null;
        try {
            entity = restTemplate.getForEntity(target_url,JsonResult.class);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,"访问"+target_url+"异常，异常信息："+e.getMessage(),null);
		}
//        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(target_url,JsonResult.class);
		if(entity==null){
			return new JsonResult(false,"get2转发服务返回空",null);
		}else
        if(entity.getStatusCodeValue()==200){
    		return entity.getBody();
        }else{
        	return new JsonResult(false,entity.toString(),null);
        }
	}
	//params转为put参数
	public String addGetParams(String url,Map<String, Object> map) {
		StringBuffer sb = new StringBuffer(url);
		sb.append("?");
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
    	while(it.hasNext()){
    		Entry<String, Object> entry = it.next();
    		sb.append(entry.getKey());
    		sb.append("=");
    		sb.append(String.valueOf(entry.getValue()));
    		sb.append("&");
    	}
		if(sb.charAt(sb.length()-1)=='&'){
			sb.deleteCharAt(sb.length()-1);
		}
		if(sb.charAt(sb.length()-1)=='?'){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
//	@Override
//	public ResponseEntity<JsonResult> httpRequestEntity(String url, Map<String, Object> map) {
//		url = addGetParams(url,map);
//		ResponseEntity<JsonResult> response = restTemplate.getForEntity(url,JsonResult.class);
//		return response;
//	}
}
