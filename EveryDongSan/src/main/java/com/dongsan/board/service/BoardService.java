package com.dongsan.board.service;

import java.util.List;

import com.dongsan.board.model.Board;

public interface BoardService {
	boolean posts(Board board);
	List<Board> listAll(int page, int size);
	boolean deleteBoard(int articleNo,String username);
	boolean updateBoard(int articleNo, String username,Board board);
	List<Board> myList(String username, int page, int size);
	List<Board> searchBySchool(String school, int page, int size);
	List<Board> search(String content, int page, int size);
}
