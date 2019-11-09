package com.bailun.gogirl.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.bailun.gogirl.config.RouteConfig;
import com.bailun.gogirl.config.WxConfig;
import com.bailun.gogirl.constant.WxUrlType;
import com.bailun.gogirl.util.qrcode.QRCodeUtils;

@Service
public class QRCodeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${gogirl.path.PICTURE}")
	String PICTURE;
	public String wxAuthorize(String url) {
		//		String member_id = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxabd5e4915643494a&redirect_uri=http%3A%2F%2F192.168.43.244%2Fauthorized_user_info&response_type=code&scope=snsapi_userinfo&state=fromtest#wechat_redirect";
        String QRCodeName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";//定义二维码文件名，采用uuid命名，存储格式为png
//      String imgPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"img/";
//        String imgPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String imgPath = PICTURE;
        if(imgPath.startsWith("file://")){//linux取文件路径
        	imgPath = imgPath.substring(7, imgPath.length());
        }else{//windows开发环境保存在classpath
        	imgPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"img/";
        }
        logger.info(imgPath);
        File file = new File(imgPath);
        if (!file.exists()) {
        	file.mkdirs();
        }
        
        QRCodeUtils handler = new QRCodeUtils();
        handler.encoderQRCode(url, imgPath+QRCodeName, "png");
        return RouteConfig.NGINX+"img/"+QRCodeName;
	}
}
