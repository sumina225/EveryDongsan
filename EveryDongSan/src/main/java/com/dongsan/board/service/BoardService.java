package com.dongsan.board.service;

import java.util.List;

import com.dongsan.board.model.Board;

public interface BoardService {
	boolean posts(Board board);
	List<Board> listAll();
	boolean deleteBoard(int articleNo,String username);
	boolean updateBoard(int articleNo, String username,Board board);
	List<Board> myList(String username);
	List<Board> searchBySchool(String school);
	List<Board> search(String content);
}
