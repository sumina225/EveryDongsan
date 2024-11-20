package com.dongsan.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface HomeMapper {
	void insertHome(HomeEntity homeEntity);
	int getHomeNo(HomeEntity homeEntity);
	
	int findSchoolNum(String school);
}
