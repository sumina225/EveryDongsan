package com.dongsan.openai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface AIMapper {
	int findSchoolId(String school);
	
	List<HomeEntity> listAll(int schoolId); 
}
