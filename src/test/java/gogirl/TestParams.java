package gogirl;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.util.HttpUtils;

public class TestParams {
//	@Test
//	public void testGet() {
//		String url= "http://172.16.4.212/redirect/bbb?id=1&openid=a&phone=a&nickname=a&password=a&sex=a&country=a&province=a&city=a&headimgurl=a&privilege=a&state=a";
//		String result = HttpUtils.doGet(url, HttpUtils.UTF8);
//		System.out.println(result);
//	}
//	@Test
//	public void testPost() {
//        //消息头
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        //参数
//        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String,Object>();
//        params.add("openid","oNzJP1Y9i7t7xDCEJJfu6ss43tHo");
//        params.add("aaaaa","aaaaaaa");
//        //请求
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        JsonResult result = restTemplate.postForObject("http://192.168.2.104:8082/gogirl-service/broadcast/queryBroadcast",httpEntity , JsonResult.class);
//        JsonResult result2 = restTemplate.postForObject("http://192.168.2.101:8089/gogirl_user/getCustomer",httpEntity , JsonResult.class);
//        System.out.println(result);
//	}
//	@Test
//	public void test() {
//		OrderManage orderManage = new OrderManage();
//		orderManage.setOrderUser(3);
//        //消息头
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        //参数
//        MultiValueMap<String, String> params= new LinkedMultiValueMap<String,String>();
//        params.add("orderUser","3");
//        //请求
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//        JsonResult result = restTemplate.postForObject("http://192.168.2.104:8084/ordermanage/queryOrderManageForPage?pageNum=1&pageSize=10",httpEntity , JsonResult.class);
//        System.out.println(result);
//	}
}
