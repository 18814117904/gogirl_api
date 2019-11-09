package com.bailun.gogirl.dao;

import org.apache.ibatis.annotations.Mapper;

import com.bailun.gogirl.bean.LoginInfo;
import com.bailun.gogirl.bean.LoginInfoKey;
@Mapper
public interface LoginInfoMapper {
    int deleteByPrimaryKey(LoginInfoKey key);

    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);

    LoginInfo selectByPrimaryKey(LoginInfoKey key);

    int updateByPrimaryKeySelective(LoginInfo record);

    int updateByPrimaryKey(LoginInfo record);
}