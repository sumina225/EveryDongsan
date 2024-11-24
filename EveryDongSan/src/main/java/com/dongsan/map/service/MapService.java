package com.dongsan.map.service;

import java.util.List;

import com.dongsan.board.model.Board;
import com.dongsan.home.model.Home;

public interface MapService {
	List<Home> searchByschool(String school);
	List<Board> searchBoardByhomeNo(int homeNo);
}
