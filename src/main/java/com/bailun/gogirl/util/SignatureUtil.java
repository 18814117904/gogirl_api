package com.bailun.gogirl.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class SignatureUtil {
//	String jsapi_ticket;
//	String noncestr;
//	String timestamp;
//	String url;
	public static String getSignature(String jsapi_ticket,String noncestr,String timestamp,String url){
		if(jsapi_ticket==null||noncestr==null||timestamp==null||url==null){
			return null;
		}


        Map<String, String> ret = new HashMap<String, String>();
        String string1;
        String signature = "";
		 
        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + noncestr +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);
 
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8")); //对string1 字符串进行SHA-1加密处理
            signature = byteToHex(crypt.digest());  //对加密后字符串转成16进制
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

		return signature;
	}
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
	
}
