package com.bailun.gogirl.bean;

import java.util.Map;

public class JsonResult {

	public static final boolean APP_REQ_CODE_ERROR = false;// 请求返回码-失败
	public static final boolean APP_REQ_CODE_SUCCESS = true;// 请求返回码-成功

	public static final String APP_DEFINE_ERR = "参数错误或者操作未成功";// 返回信息-失败
	public static final String APP_DEFINE_SUC = "操作成功";// 返回信息-成功
	public static final String APP_DEFINE_URL_ERR = "请求地址错误";// 返回信息-地址错误
	public static final String APP_DEFINE_SIGN_ERR = "参数签名验证未通过";// 返回信息-参数签名验证未通过
	public static final String APP_DEFINE_EXCEPTION = "操作未成功";// 返回信息-服务器发生异常
	public static final String APP_DEFINE_PARAM_ERR = "参数%s为空,请检查入参";// 失败
	public static final String APP_DEFINE_LOGIN_ERR = "用户未登录";
	public static final String APP_DEFINE_LOGIN_STATE_ERR = "登录状态异常";
	public static final String APP_DEFINE_CODE_NULL = "当前会话无验证码，请确认是否获取验证码";
	public static final String APP_DEFINE_CODE_ERR = "验证码不正确";
	public static final String APP_DEFINE_PHONE_ERR = "手机号码格式不正确";
	public static final String APP_DEFINE_PHONE_BIND_ERR = "号码已绑定";
	public static final String APP_DEFINE_LOGIN_TYPE_ERR = "用户登录类型type范围：1~127,请检查入参";
	public static final String APP_DEFINE_PHONE_PAY = "手机用户需要绑定微信后才能使用微信支付";
	public static final String APP_DEFINE_CUSTOMER_INFO_ERR = "用户信息异常，请联系客服";
	public static final String APP_DEFINE_ADDORDER_ERR = "获取订单失败";
	public static final String APP_DEFINE_LOGINING_ERR = "登录异常，请联系管理员";
	public static final String APP_DEFINE_ORDERID_NULL_ERR = "订单编号为空";
	public static final String APP_DEFINE_ORDERID_PAY_ERR = "订单支付异常";
	public static final String APP_DEFINE_AMOUNT_NULL_ERR = "充值金额为空";
	public static final String APP_DEFINE_AMOUNT_ERR = "充值金额为空";
	public static final String INVALID_CODE = "INVALID_CODE";
	public static final String PAY_TYPE_ERR = "不能修改为该支付类型:%s";
	public static final String TOKEN_NULL_CODE = "0001";
	public static final String TOKEN_NULL_MSG = "token为空";
	
	
	private Boolean success;
	private String message;
	private Object data;
	
	public JsonResult() {
		this.message = APP_DEFINE_ERR;
		this.success = APP_REQ_CODE_ERROR;
		
	}
	public JsonResult(boolean success){
		this.success = success;
	}
	public JsonResult(Boolean success, String message, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "JsonResult [success=" + success + ", message=" + message
				+ ", data=" + data + "]";
	}
	
	
}
