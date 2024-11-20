package com.dongsan.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface BoardMapper {
	int posts(BoardEntity board);
	List<BoardEntity> listAll();
}
