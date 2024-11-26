package com.dongsan.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
	List<Map<String,Object>> getBoardCount();
	
	List<Map<String,Object>> searchKeyword(String keyword);
}
