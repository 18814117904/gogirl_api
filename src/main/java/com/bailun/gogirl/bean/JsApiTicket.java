package com.bailun.gogirl.bean;

import org.springframework.stereotype.Component;

@Component
public class JsApiTicket {
	
	private static String js_api_ticket;//获取到的凭证
	
	private static int expires_in;//凭证有效时间



	public String getJs_api_ticket() {
		return js_api_ticket;
	}

	public void setJs_api_ticket(String js_api_ticket) {
		this.js_api_ticket = js_api_ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "JsApiTicket [js_api_ticket=" + js_api_ticket + ", expires_in="
				+ expires_in + "]";
	}
	
	
}	
