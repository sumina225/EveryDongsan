package com.dongsan.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface BoardMapper {
	int posts(BoardEntity board);
}
