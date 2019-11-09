package com.bailun.gogirl.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "wechat")
public class WxConfig {
	public String appId;
	public String secret;
	public String appId1;
	public String secret1;
	public String appId2;
	public String secret2;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAppId1() {
		return appId1;
	}
	public void setAppId1(String appId1) {
		this.appId1 = appId1;
	}
	public String getSecret1() {
		return secret1;
	}
	public void setSecret1(String secret1) {
		this.secret1 = secret1;
	}
	public String getAppId2() {
		return appId2;
	}
	public void setAppId2(String appId2) {
		this.appId2 = appId2;
	}
	public String getSecret2() {
		return secret2;
	}
	public void setSecret2(String secret2) {
		this.secret2 = secret2;
	}

	
}
