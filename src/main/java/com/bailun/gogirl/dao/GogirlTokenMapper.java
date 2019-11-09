package com.bailun.gogirl.dao;

import org.apache.ibatis.annotations.Mapper;

import com.bailun.gogirl.bean.GogirlToken;

@Mapper
public interface GogirlTokenMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insertSelective(GogirlToken record);

    GogirlToken selectByCustomerId(Integer customerId);

    int updateByPrimaryKeySelective(GogirlToken record);

	GogirlToken selectByToken(String token);

//	int selectByToken(String token);
}