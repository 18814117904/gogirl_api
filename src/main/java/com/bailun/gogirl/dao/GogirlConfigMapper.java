package com.bailun.gogirl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bailun.gogirl.bean.GogirlConfig;
@Mapper
public interface GogirlConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GogirlConfig record);

    int insertSelective(GogirlConfig record);
    
    List<GogirlConfig> selectByType(String type);
    GogirlConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GogirlConfig record);

    int updateByPrimaryKey(GogirlConfig record);
}