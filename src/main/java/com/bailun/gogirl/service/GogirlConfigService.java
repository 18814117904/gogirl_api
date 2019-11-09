package com.bailun.gogirl.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bailun.gogirl.bean.AccessToken;
import com.bailun.gogirl.bean.GogirlConfig;
import com.bailun.gogirl.bean.JsonResult;
import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.dao.GogirlConfigMapper;
import com.bailun.gogirl.service.myhttp.MyHttpPost;

@Service
public class GogirlConfigService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	GogirlConfigMapper gogirlConfigMapper;

	public List<GogirlConfig> selectByType(String type) {
		return gogirlConfigMapper.selectByType(type);
	}
}
