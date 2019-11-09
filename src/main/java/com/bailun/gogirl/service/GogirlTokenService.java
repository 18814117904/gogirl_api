package com.bailun.gogirl.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.bailun.gogirl.bean.GogirlToken;
import com.bailun.gogirl.dao.GogirlTokenMapper;



@Service
public class GogirlTokenService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	GogirlTokenMapper gogirlTokenMapper;
	//getCostomerIdByToken
    public Integer getCostomerIdByToken(String token){
    	GogirlToken gogirlToken = gogirlTokenMapper.selectByToken(token);
    	if(gogirlToken!=null){
    		GogirlToken updateToken = new GogirlToken();
    		updateToken.setCustomerId(gogirlToken.getCustomerId());
    		updateToken.setUpdateTime(new Date());
    		gogirlTokenMapper.updateByPrimaryKeySelective(updateToken);
    		return gogirlToken.getCustomerId();
    	}else{
    		return null;
    	}
    }
    public GogirlToken getTokenByToken(String token){
    	return gogirlTokenMapper.selectByToken(token);
    	
    }
//	//根据用户id查询token
//    public GogirlToken selectTokenByCustomerId(Integer customerId){
//    	GogirlToken gogirlToken = gogirlTokenMapper.selectByPrimaryKey(customerId);
//    	if(gogirlToken!=null){
//    		GogirlToken updateToken = new GogirlToken();
//    		updateToken.setCustomerId(gogirlToken.getCustomerId());
//    		updateToken.setUpdateTime(new Date());
//    		gogirlTokenMapper.updateByPrimaryKeySelective(updateToken);
//    	}
//    	return gogirlToken;
//    }
//    //根据token查询token
//    public int selectByToken(String token){
//    	return gogirlTokenMapper.selectByToken(token);
//    }
    //更新token
    public int updateByPrimaryKeySelective(GogirlToken record){
    	record.setUpdateTime(new Date());
    	return gogirlTokenMapper.updateByPrimaryKeySelective(record);
    }
    //插入token
    public String createToken(GogirlToken record){
    	Date date = new Date();
    	record.setCreateTime(date);
    	record.setUpdateTime(date);
    	//生成token
    	SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
    	String dateString = sdf.format(date);
    	if(record.getCustomerId()!=null){
    		dateString = record.getCustomerId()+dateString;
    	}else{
    		dateString = "2"+(int)(Math.random()*1000)+dateString;
    	}
    	String token = new BigInteger(dateString, 10).toString(16);
    	record.setToken(token);
    	gogirlTokenMapper.insertSelective(record);
    	return token;
    }
  //定时清理过期的token
    @Scheduled(cron = "0 0 0/2 * * *")
    public void refreshAccessToken(){
//    	return gogirlTokenMapper.deleteByPrimaryKey(customerId);
    	
    }

}
