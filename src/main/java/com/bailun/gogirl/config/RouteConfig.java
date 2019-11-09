package com.bailun.gogirl.config;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bailun.gogirl.bean.GogirlConfig;
import com.bailun.gogirl.service.GogirlConfigService;


@Component
@ConfigurationProperties(prefix = "gogirl.url")
public class RouteConfig {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static String NGINX;
	public static String GOGIRLMP;
	public static String GOGIRLUSER;
	public static String GOGIRLSTORE;
	public static String GOGIRLSERVICE;
	public static String GOGIRLTECHNICIAN;
	public static String GOGIRLORDER;
	public static String GOGIRLPAYMENT;
	
	@Resource
	GogirlConfigService gogirlConfigService;
	@PostConstruct
	public void init(){
		List<GogirlConfig> list = gogirlConfigService.selectByType("gogirl");
		Map<String, String> map  = new HashMap<String, String>();
		for(int i = 0 ;i<list.size();i++){
			GogirlConfig item = list.get(i);
			map.put(item.getName(), item.getValue());
		}
		NGINX=map.get("nginx_url");
		GOGIRLMP=map.get("mp_url");
		GOGIRLUSER=map.get("user_url");
		GOGIRLSTORE=map.get("store_url");
		GOGIRLSERVICE=map.get("service_url");
		GOGIRLTECHNICIAN=map.get("technician_url");
		GOGIRLORDER=map.get("order_url");
		GOGIRLPAYMENT=map.get("payment_url");		
		logger.info("成功加载gogirl_config表配置:"+list.toString());
	}
	
	
	//错误页面
	public static String ERROR_PAGE;

	@Bean
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
	}

	public static String getGOGIRLMP() {
		return GOGIRLMP;
	}

	public void setGOGIRLMP(String gOGIRLMP) {
		GOGIRLMP = gOGIRLMP;
	}

	public static String getGOGIRLUSER() {
		return GOGIRLUSER;
	}

	public void setGOGIRLUSER(String gOGIRLUSER) {
		GOGIRLUSER = gOGIRLUSER;
	}


	public static String getGOGIRLSTORE() {
		return GOGIRLSTORE;
	}

	public void setGOGIRLSTORE(String gOGIRLSTORE) {
		GOGIRLSTORE = gOGIRLSTORE;
	}

	public static String getGOGIRLSERVICE() {
		return GOGIRLSERVICE;
	}

	public void setGOGIRLSERVICE(String gOGIRLSERVICE) {
		GOGIRLSERVICE = gOGIRLSERVICE;
	}

	public static String getGOGIRLTECHNICIAN() {
		return GOGIRLTECHNICIAN;
	}

	public void setGOGIRLTECHNICIAN(String gOGIRLTECHNICIAN) {
		GOGIRLTECHNICIAN = gOGIRLTECHNICIAN;
	}

	public static String getGOGIRLORDER() {
		return GOGIRLORDER;
	}

	public void setGOGIRLORDER(String gOGIRLORDER) {
		GOGIRLORDER = gOGIRLORDER;
	}

	public static String getERROR_PAGE() {
		return ERROR_PAGE;
	}

	public void setERROR_PAGE(String eRROR_PAGE) {
		ERROR_PAGE = eRROR_PAGE;
	}

	public static String getNGINX() {
		return NGINX;
	}

	public void setNGINX(String nGINX) {
		NGINX = nGINX;
	}

	public static String getGOGIRLPAYMENT() {
		return GOGIRLPAYMENT;
	}

	public void setGOGIRLPAYMENT(String gOGIRLPAYMENT) {
		GOGIRLPAYMENT = gOGIRLPAYMENT;
	}
	
}
