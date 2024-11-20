package com.dongsan.board.service;

import java.util.List;

import com.dongsan.board.model.Board;

public interface BoardService {
	boolean posts(Board board);
	List<Board> listAll();
}
