package com.dongsan.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface HomeMapper {
	void insertHome(HomeEntity homeEntity);
	int getHomeNo(HomeEntity homeEntity);
	int findHome(HomeEntity homeEntity);
	List<HomeEntity> findHomeByNum(int homeNo);
	
	int findSchoolNum(String school);
	String findSchool(int schoolId);
}
