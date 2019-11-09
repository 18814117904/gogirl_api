package com.bailun.gogirl.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.service.myhttp.MyHttpGet;
import com.bailun.gogirl.service.myhttp.MyHttpPost;
import com.thoughtworks.xstream.XStream;

@Component
public class RedirectService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    public MyHttpGet myHttpGet;
	@Autowired
    public MyHttpPost myHttpPost;
	@Resource
	RestTemplate restTemplate;
	
	//转发
	public JsonResult redirect(HttpServletRequest req,Map<String, Object> map,String target_url){
		JsonResult response = null;
		if(req.getMethod()!=null&&req.getMethod().equals("GET")){//转发get请求
			response = myHttpGet.httpRequest(req, target_url,map);	
		}else if(req.getMethod()!=null&&req.getMethod().equals("POST")){//转发post请求
			response = myHttpPost.httpRequest(req, target_url,map);
		}else{
			System.out.println("不是get也不是set请求");
		}
        return response;//直接返回结果集
	}
//	public <T> Object redirectXml(String url, HttpServletRequest request,Class<T> responseType) {
//
//
//		
//		Map<String, String> map = null;
//		try {
//			map = getCallbackParams(request);
//		} catch (Exception e) {
//			logger.info("接收xml参数错误："+e.getMessage());
//		}
//		//获取所有的消息头
//		HttpHeaders headers = new HttpHeaders();
//        Enumeration<String> headerNames2 = request.getHeaderNames();
//        while(headerNames2.hasMoreElements()){
//            String nextElement = headerNames2.nextElement();
////            System.out.println(nextElement+":"+req.getHeader(nextElement));
//            headers.add(nextElement, request.getHeader(nextElement));
//        }
//        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
////        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);APPLICATION_PROBLEM_JSON_UTF8_VALUE
//        
//        //获取所有的参数
//        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
//        Enumeration<String> parameterNames2 = request.getParameterNames();
//        logger.info("查看post转发入参name:"+parameterNames2.toString());
//        while(parameterNames2.hasMoreElements()){
//            String nextElement = parameterNames2.nextElement();
////            System.out.println(nextElement+":"+req.getParameter(nextElement));
//            params.add(nextElement, request.getParameter(nextElement));
//        }
//        logger.info("查看post转发入参1"+params.toString());
//        if(map!=null){
//        	Iterator<Entry<String, String>> it = map.entrySet().iterator();
//        	while(it.hasNext()){
//        		Entry<String, String> entry = it.next();
//        		params.add(entry.getKey(), entry.getValue());
//        	}
//        }
//        logger.info("查看post转发入参2"+params.toString());
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
//        logger.info("转发支付成功通知到支付系统"+params.toString());
//        ResponseEntity<T> entity = restTemplate.postForEntity(url, httpEntity, responseType);
//        logger.info("支付系统返回的支付回调结果："+entity);
//        if(entity==null){
//			return null;
//		}else
//        if(entity.getStatusCodeValue()==200){
//        	logger.info(String.valueOf(entity.getBody()));
//    		return entity.getBody();
//        }else{
//        	return null;
//        }
//	}
//    /**
//     * 获取请求参数
//     * @Title: getCallbackParams
//     * @Description: TODO
//     * @param @param request
//     * @param @return
//     * @param @throws Exception    
//     * @return Map<String,String>    
//     * @throws
//     */
//    public Map<String, String> getCallbackParams(HttpServletRequest request)
//            throws Exception {
//        InputStream inStream = request.getInputStream();
//        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while ((len = inStream.read(buffer)) != -1) {
//            outSteam.write(buffer, 0, len);
//        }
//        System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
//        outSteam.close();
//        inStream.close();
//        String result = new String(outSteam.toByteArray(), "utf-8");
//        return (Map<String, String>) new XStream().fromXML(result);//TODO 这里需要确认数据类型，然后转为map
//    }
//    
    
}
