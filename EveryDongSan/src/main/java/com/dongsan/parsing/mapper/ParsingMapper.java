package com.dongsan.parsing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface ParsingMapper {
	void insertHomeData(List<HomeEntity> list);
}
