package com.bailun.gogirl.service;

import com.bailun.gogirl.constant.SmsConstant;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class SmsService {
	int appid = SmsConstant.appid; // 1400开头
	String appkey = SmsConstant.appkey;

	public Boolean sendLoginSmsCode(String phoneNumber,String code) {
		String smsSign = SmsConstant.smsSign; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
			int templateId = SmsConstant.loginTemplateId; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
			String activeTime = SmsConstant.activeTime;

		    String[] params = {code,activeTime};
		    return sendSmsCode(phoneNumber, params, templateId, smsSign);
	}	
	public Boolean sendBindSmsCode(String phoneNumber,String code) {
		String smsSign = SmsConstant.smsSign; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
			int templateId = SmsConstant.bindTemplateId; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
			String activeTime = SmsConstant.activeTime;

		    String[] params = {code,activeTime};
		    return sendSmsCode(phoneNumber, params, templateId, smsSign);
	}	
	private Boolean sendSmsCode(String phoneNumber,String[] params,int templateId,String smsSign) {
		try {
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    String msg = "【gogirl美甲美睫】"+params[0]+"为您的绑定手机的验证码，请于"+params[1]+"分钟内填写。如非本人操作，请忽略本短信。";
		    SmsSingleSenderResult result = ssender.send(0, "86", phoneNumber,msg, "", "");
//		    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		    System.out.println(result);
		} catch (HTTPException e) {
		    // HTTP响应码错误
		    e.printStackTrace();
		    return false;
		} catch (JSONException e) {
		    // json解析错误
		    e.printStackTrace();
		    return false;
		} catch (IOException e) {
		    // 网络IO错误
		    e.printStackTrace();
		    return false;
		}
		return true;
	}
}
