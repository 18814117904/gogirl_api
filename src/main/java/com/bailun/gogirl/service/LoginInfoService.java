package com.bailun.gogirl.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bailun.gogirl.bean.LoginInfo;
import com.bailun.gogirl.dao.LoginInfoMapper;

@Service
public class LoginInfoService {
	@Resource
	LoginInfoMapper loginInfoMapper;
	
    public int insert(LoginInfo record){
    	return loginInfoMapper.insert(record);
    }

    public int insertSelective(LoginInfo record){
    	return loginInfoMapper.insertSelective(record);
    }
	
}
