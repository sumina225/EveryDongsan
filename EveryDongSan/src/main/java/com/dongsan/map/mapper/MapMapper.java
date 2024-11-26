package com.dongsan.map.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface MapMapper {
	List<HomeEntity> searchBySchool(int schoolId);
	int searchSchoolId(String school);
	List<BoardEntity> searchBoardByhomeNo(int homeNo);
}
