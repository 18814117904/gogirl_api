package com.bailun.gogirl.constant;

public class SmsConstant {
	// 短信应用SDK AppID
	public static final int appid = 1400159337; // 1400开头

	// 短信应用SDK AppKey
	public static final String appkey = "253e42aa6423cfd51ff1eb735cd12d99";

	// 短信模板ID，需要在短信应用中申请
	public static final int loginTemplateId = 223595; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
	// 短信模板ID，需要在短信应用中申请
	public static final int bindTemplateId = 223595; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
	
	// 签名
	public static final String smsSign = "gogirl美甲美睫"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`

	//验证码有效时间
	public static final String activeTime = "5";
}
