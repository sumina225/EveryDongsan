package com.dongsan.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface BoardMapper {
	int posts(BoardEntity board);
	List<BoardEntity> listAll();
	List<BoardEntity> myList(String username);
	List<BoardEntity> searchBySchoolId(int schoolId);
	List<BoardEntity> search(String content);
	
	void deleteBoard(int articleNo);
	void updateBoard(BoardEntity boardEntity);
	
	
	String findMemberIdByBoard(int articleNo);
	int findHomeNoByBoard(int articleNo);
	
	int findBoard(BoardEntity board);
	
	
}
